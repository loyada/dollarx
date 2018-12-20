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
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;



public class Images {

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

    assertThat("width", elementImage.getWidth(), equalTo(expectedImage.getWidth()));
    assertThat("height", elementImage.getHeight(), equalTo(expectedImage.getHeight()));
    IntStream.range(0, elementImage.getHeight()).forEach(y ->
        IntStream.range(0, elementImage.getWidth()).forEach(x -> {
              if (elementImage.getRGB(x, y) != expectedImage.getRGB(x, y))
                throw new AssertionError(String.format("found a different pixel at %d, %d",x ,y));
            }
        ));
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

    assertThat("width", elementImage.getWidth(), equalTo(expectedImage.getWidth()));
    assertThat("height", elementImage.getHeight(), equalTo(expectedImage.getHeight()));
    int threshold = elementImage.getWidth() * elementImage.getHeight() / maxBadPixelsRatio;
    int countOfErrors = 0;
    for (int y=0; y<elementImage.getHeight(); y++) {
      for (int x=0; x<elementImage.getWidth(); x++) {
        if (!pixelValueIsClose(elementImage.getRGB(x, y), expectedImage.getRGB(x, y))) {
          countOfErrors++;
        }
        if (countOfErrors > threshold)
          throw new AssertionError(String.format("images have significant differences"));

      }
    }
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

  private static boolean pixelValueIsClose(int rgb1, int rgb2) {
    Color c1 = new Color(rgb1, false);
    float[] hsb1 = Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), null);
    Color c2 = new Color(rgb2, false);
    float[] hsb2 = Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), null);
    float bDiff = Math.abs(2*(hsb2[2] - hsb1[2])/(hsb2[2] + hsb1[2]));
    float hDiff = Math.abs(2*(hsb2[0] - hsb1[0])/(hsb2[0] + hsb1[0]));
    float sDiff = Math.abs(2*(hsb2[1] - hsb1[1])/(hsb2[1] + hsb1[1]));
    return  (bDiff>0.05 || hDiff>0.3 || sDiff>0.2);
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
}
