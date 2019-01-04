package com.github.loyada.jdollarx;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;


public class Images {
  static Logger logger = Logger.getLogger(Images.class.getName());


  /**
   * Save image to file
   * @param browser - browser
   * @param el - Path element to capture
   * @param outputFile - output file
   */
  public static void captureToFile(InBrowser browser, Path el, File outputFile) {
    BufferedImage elementImage = captureImage(browser, el);
    try {
      ImageIO.write(elementImage, "png", outputFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Save an HTML5 canvas to file. Optimized for canvas. Will fail if the element is not a canvas.
   * @param browser - browser
   * @param el - Path element to capture
   * @param outputFile - output file
   */
  public static void captureCanvasToFile(InBrowser browser, Path el, File outputFile) {
    BufferedImage elementImage = captureCanvas(browser, el);
    try {
      ImageIO.write(elementImage, "png", outputFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Display image of an element in a separate window. Does not work as an evaluation within the debugger.
   * @param browser - browser
   * @param el - the element to capture and display
   */
  public static void show(InBrowser browser, Path el) {
    BufferedImage elementImage = captureImage(browser, el);
    showImage(elementImage);
  }

  /**
   * Display image of an HTML5 canvas element in a separate window. Does not work as an evaluation within the debugger.
   * @param browser - browser
   * @param el - the element to capture and display
   */
  public static void showCanvas(InBrowser browser, Path el) {
    BufferedImage elementImage = captureCanvas(browser, el);
    showImage(elementImage);
  }

  /**
   * Verify that the element's image is pixel-perfect
   * @param browser - browser
   * @param el - element to capture and verify
   * @param expectedImageInput reference image file
   * @throws IOException - file could not be read
   */
  public static void assertImageIsEqualToExpected(InBrowser browser, Path el, InputStream expectedImageInput) throws IOException {
    BufferedImage elementImage = captureImage(browser, el);
    BufferedImage expectedImage =  ImageIO.read(expectedImageInput);
    ImageComparator.verifyImagesAreEqual(elementImage, expectedImage);
  }

  /**
   * Verify that the element's image is pixel-perfect, but allowing some crop/shift
   * @param browser - browser
   * @param el - element to capture and verify
   * @param expectedImageInput reference image file
   * @param maxShift maximum pixels the images are shifted/cropped compared to each other (both on x and y axis)
   * @throws IOException - file could not be read
   */
  public static void assertImageIsEqualToExpectedWithShiftAndCrop(InBrowser browser, Path el, InputStream expectedImageInput, int maxShift) throws IOException {
    BufferedImage elementImage = captureImage(browser, el);
    BufferedImage expectedImage =  ImageIO.read(expectedImageInput);
    ImageComparator.verifyImagesAreShifted(elementImage, expectedImage, maxShift);
  }

  /**
   * Verify the picture is "similar" to the reference image.
   * Ignores minor differences between the pixels.
   * @param browser - browser
   * @param el - element to capture and validate
   * @param expectedImageInput - reference image
   * @param maxBadPixelsRatio - a positive number. For example: If it's 100, then
   *                           1% of the pixels can have major differences compared to
   *                          the reference.
   * @throws IOException - image file could not be read
   */
  public static void assertImageIsSimilarToExpected(InBrowser browser, Path el, InputStream expectedImageInput, int maxBadPixelsRatio) throws IOException {
    BufferedImage elementImage = captureImage(browser, el);
    BufferedImage expectedImage =  ImageIO.read(expectedImageInput);

    ImageComparator.verifyImagesAreSimilar(elementImage, expectedImage, maxBadPixelsRatio);
  }

  public static BufferedImage captureCanvas(InBrowser browser, Path canvas) {
    final int startOfDataInDataURL = "data:image/png:base64,".length();
    JavascriptExecutor js = (JavascriptExecutor) browser.getDriver();
    WebElement canvasEl = browser.find(canvas);

    String imageEncondedData = (String)js.executeScript("return arguments[0].toDataURL('image/png').substring(arguments[1]);", canvasEl, startOfDataInDataURL);

    byte[]  decodedImage = Base64.getDecoder().decode(imageEncondedData);
    ByteArrayInputStream bis = new ByteArrayInputStream(decodedImage);
    try {
      BufferedImage image = ImageIO.read(bis);
      bis.close();
      return image;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }




  private static void showImage(BufferedImage image) {
    Icon icon = new ImageIcon(image);
    JLabel label = new JLabel(icon);

    final JFrame f = new JFrame("ImageIconExample");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.getContentPane().add(label);
    f.pack();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        f.setLocationRelativeTo(null);
        f.setVisible(true);
      }
    });
  }

  private static BufferedImage captureImage(InBrowser browser, Path el) {
    WebElement webEl = browser.find(el);

    File screenshot = ((TakesScreenshot) browser.getDriver()).getScreenshotAs(OutputType.FILE);
    final BufferedImage fullImg;
    try {
      fullImg = ImageIO.read(screenshot);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Point elementLocation = webEl.getLocation();
    Dimension elementDimensions = webEl.getSize();
    return fullImg.getSubimage(
        elementLocation.getX(), elementLocation.getY(),
        elementDimensions.getWidth(), elementDimensions.getHeight());
  }

  public final static class ImageComparator {
      private ImageComparator(){}

      public static void verifyImagesAreSimilar(BufferedImage img1, BufferedImage img2, int maxBadPixelsRatio) {
        assertThat("width", img1.getWidth(), equalTo(img2.getWidth()));
        assertThat("height", img1.getHeight(), equalTo(img2.getHeight()));
        int threshold = img1.getWidth() * img2.getHeight() / maxBadPixelsRatio;
        int countOfErrors = 0;
        for (int y=0; y<img1.getHeight(); y++) {
          for (int x=0; x<img1.getWidth(); x++) {
            if (pixelValueIsSignificantlyDifferent(img1.getRGB(x, y), img2.getRGB(x, y))) {
              countOfErrors++;
            }
            if (countOfErrors > threshold)
              throw new AssertionError(String.format("images have significant differences"));

          }
        }
      }

    public static void verifyImagesAreShifted(BufferedImage img1, BufferedImage img2, int maxShift) {
      assertThat("width", abs(img1.getWidth() - img2.getWidth()), lessThan(maxShift +1));
      assertThat("height", abs(img1.getHeight() - img2.getHeight()), lessThan(maxShift+1));
      for (int yShift=0; yShift<=maxShift; yShift++) {
        for (int xShift=0; xShift<=maxShift; xShift++) {
          BufferedImage croppedImage1 = img1.getSubimage(xShift, yShift,
                  min(img1.getWidth(), img2.getWidth()) - xShift,
                  min(img1.getHeight(), img2.getHeight()) - yShift);
          BufferedImage croppedImage2 = img2.getSubimage(0, 0,
                  min(img1.getWidth(), img2.getWidth()) - xShift,
                  min(img1.getHeight(), img2.getHeight()) - yShift);
          try {
            verifyImagesAreEqual(croppedImage1, croppedImage2);
            Images.logger.info(String.format("Found correct shift: %d, %d", xShift, yShift));
            return;
          } catch (AssertionError e) {
      //      Images.logger.info(String.format("failed with shift: %d, %d", xShift, yShift));
          }
        }

      }

      for (int yShift=0; yShift<=maxShift; yShift++) {
        for (int xShift=0; xShift<=maxShift; xShift++) {
          BufferedImage croppedImage1 = img1.getSubimage(0, 0,
                  min(img1.getWidth(), img2.getWidth()) - xShift,
                  min(img1.getHeight(), img2.getHeight()) - yShift);
          BufferedImage croppedImage2 = img2.getSubimage(xShift, yShift,
                  min(img1.getWidth(), img2.getWidth()) - xShift,
                  min(img1.getHeight(), img2.getHeight()) - yShift);
          try {
            verifyImagesAreEqual(croppedImage1, croppedImage2);
            Images.logger.info(String.format("Found correct negative shift: %d, %d", xShift, yShift));
            return;
          } catch (AssertionError e) {
 //           Images.logger.info(String.format("failed with negative shift: %d, %d", xShift, yShift));
          }
        }
      }

      throw new AssertionError("could not find any shift");
    }

    public static void verifyImagesAreEqual(BufferedImage img1, BufferedImage img2) {
      assertThat("width", img1.getWidth(), equalTo(img2.getWidth()));
      assertThat("height", img1.getHeight(), equalTo(img2.getHeight()));
      IntStream.range(0, img1.getHeight()).forEach(y ->
              IntStream.range(0, img1.getWidth()).forEach(x -> {
                        if (img1.getRGB(x, y) != img2.getRGB(x, y))
                          throw new AssertionError(String.format("found a different pixel at %d, %d",x ,y));
                      }
              ));
    }

    private static boolean pixelValueIsSignificantlyDifferent(int rgb1, int rgb2) {
      if (rgb1==rgb2)
        return false;
      Color c1 = new Color(rgb1, false);
      float[] hsb1 = Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), null);
      Color c2 = new Color(rgb2, false);
      float[] hsb2 = Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), null);
      float bDiff = abs(2*(hsb2[2] - hsb1[2])/(hsb2[2] + hsb1[2] + 1));
      float hDiff = abs(2*(hsb2[0] - hsb1[0])/(hsb2[0] + hsb1[0] + 1));
      float sDiff = abs(2*(hsb2[1] - hsb1[1])/(hsb2[1] + hsb1[1] + 1));
      return  (bDiff>0.05 || hDiff>0.3 || sDiff>0.2);
    }
  }
}
