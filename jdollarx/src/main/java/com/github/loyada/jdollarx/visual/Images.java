package com.github.loyada.jdollarx.visual;

import com.github.loyada.jdollarx.BasicPath;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.find;
import static java.lang.String.format;


public class Images {
  static Logger logger = Logger.getLogger(Images.class.getName());

  /**
   * Save image to file, scaling the image to the size in the browser
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
   * Save image to file
   * @param browser - browser
   * @param el - Path element to capture
   * @param outputFile - output file
   */
  public static void captureToFileNoScaling(InBrowser browser, Path el, File outputFile) {
    BufferedImage elementImage = captureImageNoScaling(browser, el);
    try {
      ImageIO.write(elementImage, "png", outputFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

   /** Save screenshot to file, scaling the image to the size in the browser
   * @param browser - browser
   * @param outputFile - output file
   */
  public static void captureToFile(InBrowser browser, File outputFile) {
    BufferedImage elementImage = captureImage(browser);
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
    showImage(elementImage, el.toString());
  }

  /**
   * Display image of an HTML5 canvas element in a separate window. Does not work as an evaluation within the debugger.
   * @param browser - browser
   * @param el - the element to capture and display
   */
  public static void showCanvas(InBrowser browser, Path el) {
    BufferedImage elementImage = captureCanvas(browser, el);
    showImage(elementImage, el.toString());
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
   * @param el - element to capture and verify
   * @param expectedImageInput reference image file
   * @throws IOException - file could not be read
   */
  public static void assertImageIsEqualToExpectedNoScaling(InBrowser browser, Path el, InputStream expectedImageInput) throws IOException {
    assertInternal(captureImageNoScaling(browser, el), expectedImageInput);
  }

  /**
   * Verify that the element's image is pixel-perfect
   * @param img - an image to be validated
   * @param expectedImageInput reference image file
   * @throws IOException - file could not be read
   */
  public static void assertImageIsEqualToExpected(BufferedImage img, InputStream expectedImageInput) throws IOException {
    assertInternal(img, expectedImageInput);

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
   * create and return an image that highlights the different pixels between the captured image and the reference image
   * @param browser - browser
   * @param el - element to capture and verify
   * @param expectedImageInput reference image file
   * @return an image that highlights the different pixels. If the images are equal, returns an empty optional.
   * @throws IOException - file could not be read
   * @throws AssertionError - images are not the same size
   */
  public static Optional<BufferedImage> getFuzzyErrorsImage(InBrowser browser, Path el, InputStream expectedImageInput) throws IOException {
    BufferedImage elementImage = captureImage(browser, el);
    BufferedImage expectedImage =  ImageIO.read(expectedImageInput);
    return ImageComparator.getFuzzyErrorImage(elementImage, expectedImage);
  }

  /**
   * create and return an image that highlights the different pixels between the captured image and the reference image
   * @param expectedImageInput reference image file
   * @return an image that highlights the different pixels. If the images are equal, returns an empty optional.
   * @throws IOException - file could not be read
   * @throws AssertionError - images are not the same size
   */
  public static Optional<BufferedImage> getFuzzyErrorsImage(InputStream actualImageInput, InputStream expectedImageInput) throws IOException {
    BufferedImage expectedImage =  ImageIO.read(expectedImageInput);
    BufferedImage actualImage =  ImageIO.read(actualImageInput);
    return ImageComparator.getFuzzyErrorImage(actualImage, expectedImage);
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
  public static Optional<BufferedImage> getFuzzyErrorImage(InBrowser browser, Path el, InputStream expectedImageInput) throws IOException {
    BufferedImage elementImage = captureImage(browser, el);
    BufferedImage expectedImage =  ImageIO.read(expectedImageInput);
    return ImageComparator.getFuzzyErrorImage(elementImage, expectedImage);
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
    ImageComparator.verifyImagesAreEqualWithShift(elementImage, expectedImage, maxShift);
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
   * @param expectedImageInput - reference image
   * @param maxBadPixelsRatio - a positive number. For example: If it's 100, then
   *                           1% of the pixels can have major differences compared to
   *                          the reference.
   * @throws IOException - image file could not be read
   */
  public static void assertScreenIsSimilarToExpected(InBrowser browser, InputStream expectedImageInput, int maxBadPixelsRatio) throws IOException {
    BufferedImage elementImage = captureImage(browser);
    BufferedImage expectedImage =  ImageIO.read(expectedImageInput);

    ImageComparator.verifyImagesAreSimilar(elementImage, expectedImage, maxBadPixelsRatio);
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
   * @param maxShift - max shift allowed
   * @throws IOException - image file could not be read
   */
  public static void assertImageIsSimilarToExpectedWithShift(InBrowser browser,
                                                             Path el,
                                                             InputStream expectedImageInput,
                                                             int maxBadPixelsRatio,
                                                             int maxShift) throws IOException {
    BufferedImage elementImage = captureImage(browser, el);
    BufferedImage expectedImage =  ImageIO.read(expectedImageInput);

    ImageComparator.verifyImagesAreSimilarWithShift(elementImage, expectedImage, maxBadPixelsRatio, maxShift);
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

  public static void assertImageIsSimilarToExpectedWithFilter(
          BufferedImage elementImage,
          InputStream filterImageInput,
          InputStream expectedImageInput,
          int maxBadPixelsRatio) throws IOException {
    BufferedImage expectedImage =  ImageIO.read(expectedImageInput);
    BufferedImage filterImage =  ImageIO.read(filterImageInput);
    ImageComparator.verifyImagesAreSimilarFilteringInterestingAreas(
            filterImage,
            expectedImage,
            elementImage,
            maxBadPixelsRatio
    );
  }


  /**
   * Capture a canvas DOM element to an image as a png.
   * The reason for the special treatment for canvas is that capturing it as a
   * raster image is much more efficient than a generic element.
   *
   * @param browser - the browser
   * @param canvas - a Path to the canvas element
   * @return a raster image
   */
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

  /**
   * Display an image on a JFrame
   * @param image - the image
   * @param header - display title
   */
  public static void showImage(BufferedImage image, String header) {
    Icon icon = new ImageIcon(image);
    JLabel label = new JLabel(icon);

    final JFrame f = new JFrame(header);
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

  
  public static BufferedImage resize(BufferedImage img, long newW, long newH) {
        Image tmp = img.getScaledInstance((int)newW, (int)newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage((int)newW, (int)newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
   }

  public static Map<String, Long> getVisibleDimensions(InBrowser browser, Path path) {
    JavascriptExecutor js = (JavascriptExecutor) browser.getDriver();
    WebElement el = find(path);
    return castToMap(js.executeScript("return  { 'height':  arguments[0].clientHeight, 'width':   arguments[0].clientWidth};", el));
  }

  public static Map<String, Long> getBrowserInnerDimensions(InBrowser browser) {
    JavascriptExecutor js = (JavascriptExecutor) browser.getDriver();
    return castToMap(js.executeScript("return  { 'height':  window.innerHeight, 'width':   window.innerWidth};"));
  }

  @SuppressWarnings("unchecked")
  private static <T> Map<String, T> castToMap(Object o) {
    return (Map<String, T>)o;
  }

  public static BufferedImage captureImage(InBrowser browser, Path el) {
    WebElement webEl = browser.find(el);
    File screenshot = ((TakesScreenshot) browser.getDriver()).getScreenshotAs(OutputType.FILE);
    final BufferedImage fullImgUnscaled;
    try {
            fullImgUnscaled = ImageIO.read(screenshot);
    } catch (IOException e) {
            throw new RuntimeException(e);
    }
    Map<String, Long> fullVisibleSize =getBrowserInnerDimensions(browser);
    BufferedImage fullImg = resize(fullImgUnscaled, fullVisibleSize.get("width") , fullVisibleSize.get("height"));
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

  public static BufferedImage captureImage(InBrowser browser) {
    File screenshot = ((TakesScreenshot) browser.getDriver()).getScreenshotAs(OutputType.FILE);
    final BufferedImage fullImgUnscaled;
    try {
      fullImgUnscaled = ImageIO.read(screenshot);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Map<String, Long> fullVisibleSize =getBrowserInnerDimensions(browser);
    return resize(fullImgUnscaled, fullVisibleSize.get("width") , fullVisibleSize.get("height"));
  }


  public static BufferedImage captureImageNoScaling(InBrowser browser, Path el) {
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

    Dimension fullSize = browser.find(BasicPath.html).getSize();
    if (fullImg.getWidth()!= fullSize.getWidth() || fullImg.getHeight() != fullSize.getHeight()) {
      logger.warning("Looks like the screen resolution is scaled. Visual testing may not work correctly.");
    }

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




}
