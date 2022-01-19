package com.github.loyada.jdollarx;

import com.google.common.collect.ImmutableList;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.String.format;
import static java.util.stream.IntStream.range;
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
   * Save the source of an HTML img element to file
   * @param browser - browser
   * @param imgEl -  HTML img element to capture
   * @param outputFile - output file
   */
  public static void captureImgSrcToFile(InBrowser browser, Path imgEl, File outputFile) {
    BufferedImage elementImage = captureHTMLImgSource(browser, imgEl);
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
   * Verify that an image downloaded from an HTML img src attribute, is pixel-perfect
   * @param browser - browser
   * @param el - HTML img element to capture and verify
   * @param expectedImageInput reference image file
   * @throws IOException - file could not be read
   */
  public static void assertHTMLImgSoureIsEqualToExpected(InBrowser browser, Path el, InputStream expectedImageInput) throws IOException {
    assertInternal(captureHTMLImgSource(browser, el), expectedImageInput);
  }

  /**
   * Verify that the element's image is pixel-perfect
   * @param browser - browser
   * @param el - element to capture and verify
   * @param expectedImageInput reference image file
   * @throws IOException - file could not be read
   */
  public static void assertImageIsEqualToExpected(InBrowser browser, Path el, InputStream expectedImageInput) throws IOException {
    assertInternal(captureImage(browser, el), expectedImageInput);

  }

  /**
   * Verify that the element's image is pixel-perfect
   * @param browser - browser
   * @param el - canvas to capture and verify
   * @param expectedImageInput reference image file
   * @throws IOException - file could not be read
   */
  public static void assertCanvasImageIsEqualToExpected(InBrowser browser, Path el, InputStream expectedImageInput) throws IOException {
    assertInternal(captureCanvas(browser, el), expectedImageInput);
  }

  private static void assertInternal(BufferedImage elementImage, InputStream expectedImageInput) throws IOException {
    BufferedImage expectedImage =  ImageIO.read(expectedImageInput);
    ImageComparator.verifyImagesAreEqual(elementImage, expectedImage);
  }

  /**
   * create and return an image that highlights the different pixels between the captured image and the reference image
   * @param browser - browser
   * @param el - element to capture and verify
   * @param expectedImageInput reference image file
   * @return an image that highlights the different pixels. If the images are equal, returns an empty optional.
   * @throws IOException - file could not be read
   * @throws AssertionError - images are not the same size
   */
  public static Optional<BufferedImage> getErrorsImage(InBrowser browser, Path el, InputStream expectedImageInput) throws IOException {
    BufferedImage elementImage = captureImage(browser, el);
    BufferedImage expectedImage =  ImageIO.read(expectedImageInput);
    return ImageComparator.getErrorImage(elementImage, expectedImage);
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

  /**
   * Verify the picture is "similar" to the reference image.
   * Ignores minor differences between the pixels.
   * @param browser - browser
   * @param el - element to capture and validate
   * @param expectedImageInput - reference image
   * @param filterImageInput - image that filters interesting areas
   * @param maxBadPixelsRatio - a positive number. For example: If it's 100, then
   *                           1% of the pixels can have major differences compared to
   *                          the reference.
   * @throws IOException - image file could not be read
   */
  public static void assertImageIsSimilarToExpectedWithFilter(
          InBrowser browser,
          Path el,
          InputStream filterImageInput,
          InputStream expectedImageInput,
          int maxBadPixelsRatio) throws IOException {
    BufferedImage elementImage = captureImage(browser, el);
    BufferedImage expectedImage =  ImageIO.read(expectedImageInput);
    BufferedImage filterImage =  ImageIO.read(filterImageInput);
    ImageComparator.verifyImagesAreSimilarFilteringInterestingAreas(
            filterImage,
            expectedImage,
            elementImage,
            maxBadPixelsRatio
    );
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

  static void showImage(BufferedImage image) {
    Icon icon = new ImageIcon(image);
    JLabel label = new JLabel(icon);

    final JFrame f = new JFrame("ImageIconExample");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.getContentPane().add(label);
    f.pack();
    SwingUtilities.invokeLater(() -> {
      f.setLocationRelativeTo(null);
      f.setVisible(true);
    });
  }

  private static Point getVisiblePageOffset(InBrowser browser){
    JavascriptExecutor js = (JavascriptExecutor) browser.getDriver();
    @SuppressWarnings("unchecked")
    Map<String, Long> pointResult = (Map<String, Long>)js.executeScript("return {'y': window.pageYOffset, 'x': window.pageXOffset};");
    return new Point(pointResult.get("x").intValue(), pointResult.get("y").intValue());
  }

  private static BufferedImage captureHTMLImgSource(InBrowser browser, Path el) {
    String src = browser.find(el).getAttribute("src");
    URL url;
    try {
      url = new URL(src);
      return ImageIO.read(url);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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

    Point fullImgOffset = getVisiblePageOffset(browser);
    if (fullImg.getWidth() +  fullImgOffset.getX() < elementLocation.getX() + elementDimensions.getWidth() ||
        fullImgOffset.getX() > elementLocation.getX() ||
         fullImg.getHeight()  + fullImgOffset.getY() < elementLocation.getY() + elementDimensions.getHeight() ||
          fullImgOffset.getY() > elementLocation.getY()) {
      throw new IllegalArgumentException(format("The element '%s' is partially outside the visible area in the browser." +
          "You might need to resize the window to a larger size, or scroll to the location of the element", el));
    }
    return fullImg.getSubimage(
        elementLocation.getX() - fullImgOffset.getX(),
        elementLocation.getY() - fullImgOffset.getY(),
           elementDimensions.getWidth(), elementDimensions.getHeight());
  }

  /**
   * Internal utility class for images
   */
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
              throw new AssertionError(format("images have significant differences. \n" +
                      "found %d significant differences in %d pixels",
                      threshold, y*img1.getWidth()+x));
          }
        }
      }

    public static void verifyImagesAreSimilarFilteringInterestingAreas(
            BufferedImage filterImage,
            BufferedImage refImage,
            BufferedImage img,
            int maxBadPixelsRatio) {
      assertThat("width", refImage.getWidth(), equalTo(img.getWidth()));
      assertThat("height", refImage.getHeight(), equalTo(img.getHeight()));
      int totalPixels = 0;
      int countOfErrors = 0;
      for (int y=0; y<refImage.getHeight(); y++) {
        for (int x = 0; x < refImage.getWidth(); x++) {
          if ((filterImage.getRGB(x, y) & 0xffffff) == 0)
            continue;

          totalPixels += 1;
          if (pixelValueIsSignificantlyDifferent(refImage.getRGB(x, y) & 0xffffff, img.getRGB(x, y) & 0xffffff)) {
            countOfErrors++;
          }
        }
      }
      int threshold = totalPixels / maxBadPixelsRatio;

      if (countOfErrors > threshold)
            throw new AssertionError(format("images have significant differences. \n" +
                            "found %d significant differences. Allowed %d",
                    countOfErrors, threshold));

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
            Images.logger.info(format("Found correct shift: %d, %d", xShift, yShift));
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
            Images.logger.info(format("Found correct negative shift: %d, %d", xShift, yShift));
            return;
          } catch (AssertionError e) {
 //           Images.logger.info(String.format("failed with negative shift: %d, %d", xShift, yShift));
          }
        }
      }

      throw new AssertionError("could not find any shift");
    }


    public static Optional<BufferedImage> getErrorImage(BufferedImage img1, BufferedImage img2) {
      assertThat("width", img1.getWidth(), equalTo(img2.getWidth()));
      assertThat("height", img1.getHeight(), equalTo(img2.getHeight()));
      BufferedImage errImage = new BufferedImage(img1.getWidth(), img1.getHeight(),BufferedImage.TYPE_INT_RGB);
      AtomicReference<Boolean> foundDiff = new AtomicReference<>(false);
      range(0, img1.getHeight()).forEach(y ->
          range(0, img1.getWidth()).forEach(x -> {
                if (img1.getRGB(x, y) == img2.getRGB(x, y)) {
                  Color oldColor = new Color(img1.getRGB(x, y), img1.isAlphaPremultiplied());
                  Color newColor = new Color(oldColor.getRed() / 4, oldColor.getGreen() / 4, oldColor.getBlue() / 4);
                  errImage.setRGB(x, y, newColor.getRGB());
                } else {
                  foundDiff.set(true);
                  errImage.setRGB(x, y, 0xff0000);
                }
              }
          ));
      return foundDiff.get() ? Optional.of(errImage) : Optional.empty();
    }

    public static void verifyImagesAreEqual(BufferedImage img1, BufferedImage img2) {
      assertThat("width", img1.getWidth(), equalTo(img2.getWidth()));
      assertThat("height", img1.getHeight(), equalTo(img2.getHeight()));
      range(0, img1.getHeight()).forEach(y ->
              range(0, img1.getWidth()).forEach(x -> {
                        if (img1.getRGB(x, y) != img2.getRGB(x, y))
                          throw new AssertionError(format("found a different pixel at %d, %d",x ,y));
                      }
              ));
    }

    public static void verifyImagesAreEqualWithFilterImage(BufferedImage filterImg, BufferedImage img1, BufferedImage img2) {
      assertThat("width", img1.getWidth(), equalTo(img2.getWidth()));
      assertThat("height", img1.getHeight(), equalTo(img2.getHeight()));
      range(0, img1.getHeight()).forEach(y ->
              range(0, img1.getWidth()).forEach(x -> {
                        if ((filterImg.getRGB(x, y) & 0xffffff) != 0 && (img1.getRGB(x, y) != img2.getRGB(x, y)))
                          throw new AssertionError(format("found a different pixel at %d, %d",x ,y));
                      }
              ));
    }

    private static boolean pixelValueIsSignificantlyDifferent(int rgb1, int rgb2) {
      if (rgb1==rgb2 || (rgb1 & 0xfefefe) == (rgb2 & 0xfefefe))
        return false;

      Color c1 = new Color(rgb1, false);
      YUV yuv1 = YUV.fromRGB(c1.getRed(), c1.getGreen(), c1.getBlue());
      Color c2 = new Color(rgb2, false);
      YUV yuv2 = YUV.fromRGB(c2.getRed(), c2.getGreen(), c2.getBlue());
      return yuv1.isSignificantlyDifferentFrom(yuv2);
    }
  }



  private static class YUV {
    private final float y,u,v;

    private YUV(float y, float u, float v) {
      this.y = y;
      this.u = u;
      this.v = v;
    }

    // normalyze y,u,v to have a range of ~1
    public static YUV fromRGB(int r, int g, int b) {
        float rf = (float)r / 255;
        float gf = (float)g / 255;
        float bf = (float)b / 255;

        return new YUV(
                (float)(0.299 * rf + 0.587 * gf + 0.114 * bf),
                (float)(-0.14713 * rf - 0.28886 * gf + 0.436 * bf),
                (float)(0.615 * rf - 0.51499 * gf - 0.10001 * bf));
    }

    public boolean isSignificantlyDifferentFrom(YUV other) {
        float ydiff = abs(y - other.y);
        float udiff = abs(u - other.u);
        float vdiff = abs(v - other.v);
        return (ydiff > 0.1 || udiff > 0.25 || vdiff > 0.25);
    }
  }


  public static class Obscure implements AutoCloseable {
    private final Map<WebElement, String> styleByElement = new HashMap<>();
    final boolean strict;
    final JavascriptExecutor js;
    final List<Path> obscuredElements = new ArrayList<>();


    public Obscure(InBrowser browser, Path element) {
      this(browser, List.of(element), false);
    }

    public Obscure(InBrowser browser, List<Path> elements) {
      this(browser, elements, false);
    }

    public Obscure(InBrowser browser, List<Path> elements, boolean strict) {
      this.strict = strict;
      js = (JavascriptExecutor) browser.getDriver();

      elements.forEach(el -> {
        final WebElement webEl;
        try {
           webEl = browser.find(el);
        } catch (NoSuchElementException e) {
          if (strict) {
            throw e;
          }
          else return;
        }

        obscuredElements.add(el);
        String oldStyle = webEl.getAttribute("style");
        styleByElement.put(webEl, oldStyle);

        js.executeScript("arguments[0].setAttribute('style', arguments[1] + ' display: none;');",
            webEl, Optional.ofNullable(oldStyle).orElse(""));
      });
    }

    public List<Path> getObscuredElements() {
      return ImmutableList.copyOf(obscuredElements);
    }

    @Override
    public void close() throws Exception {
      styleByElement.keySet().forEach(webEl -> {
        String script = format("arguments[0].setAttribute('style', '%s');", styleByElement.get(webEl));
        js.executeScript(script, webEl);
      });
    }
  }
}
