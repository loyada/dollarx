package com.github.loyada.jdollarx.singlebrowser;

import com.github.loyada.jdollarx.visual.Images;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Capturing and asserting the image (screenshot) of a Path element.
 * It supports both canvas and a standard element image.
 * It supports both accurate and fuzzy assertion.
 * It provides utility functions to display an element in a separate window.
 */
public class SingltonBrowserImage {

  private final Path el;

  /**
   *
   * @param el The element the represents the image we are interested in
   */
  public SingltonBrowserImage(Path el) {
    this.el = el;
  }

  /**
   * Capture the image of an element as a png, and save it to the given file
   * @param outputFile - output file
   */
  public void captureToFile(File outputFile) {
    Images.captureToFile(browser(), el, outputFile);
  }


  /**
   * Capture the source of an img element as a png, and save it to the given file
   * @param outputFile - output file
   */
  public void captureImgSourceToFile(File outputFile) {
    Images.captureImgSrcToFile(browser(), el, outputFile);
  }

  /**
   * Capture the image of an HTML5 canvas as a png, and save it to the given file.
   * If the element given is not a canvas, this will fail.
   * Note that it is more optimized - it downloads only the section of the canvas as an image.
   * @param outputFile - output file
   */
  public void captureCanvasToFile(File outputFile) {
    Images.captureCanvasToFile(browser(), el, outputFile);
  }

  /**
   * Display the element image in a separate window.
   * This is useful for troubleshooting/development.
   * Note that this will not work well if you do it inside a debugger evaluation.
   */
  public void show() {
    Images.show(browser(), el);
  }


  /**
   * Similar to show(), but optimized for an HTML5 canvas element
   */
  public void showCanvas() {
    Images.showCanvas(browser(), el);
  }

  /**
   * Verify that the element's image is pixel-perfect
   * @param expectedImageInput - reference png image
   * @throws IOException - file could not be read
   */
  public void assertImageIsEqualToExpected(InputStream expectedImageInput) throws IOException {
    Images.assertImageIsEqualToExpected(browser(), el, expectedImageInput);
  }

  /**
   * Verify that the canvas image is pixel-perfect
   * @param expectedImageInput - reference png image
   * @throws IOException - file could not be read
   */
  public void assertCanvasImageIsEqualToExpected(InputStream expectedImageInput) throws IOException {
    Images.assertCanvasImageIsEqualToExpected(browser(), el, expectedImageInput);
  }


  /**
   * Verify that the HTML img source is pixel-perfect
   * @param expectedImageInput - reference png image
   * @throws IOException - file could not be read
   */
  public void assertImgSourceIsEqualToExpected(InputStream expectedImageInput) throws IOException {
    Images.assertHTMLImgSoureIsEqualToExpected(browser(), el, expectedImageInput);
  }

  /**
   * compare captured image to a reference image and return an image that highlights the differences.
   * Both images are expected to have the same dimensions, otherwise it throws in AssertionError.
   * @param expectedImageInput - reference png image
   * @return an image that highlights the different pixels. If the images are equal, returns an empty optional.
   * @throws IOException - file could not be read
   * @throws AssertionError - images are not the same size   */
  public Optional<BufferedImage> getErrorImage(InputStream expectedImageInput) throws IOException {
    return Images.getErrorsImage(browser(), el, expectedImageInput);
  }


  /**
   * Verify that the element's image is pixel-perfect, but allowing one
   * to be a cropped/shifted version of the other.
   * @param expectedImageInput - reference png image
   * @param maxShift maximum pixels the images are shifted/cropped compared to each other
   * @throws IOException - file could not be read
   */
  public void assertImageIsEqualToExpectedWithShiftAndCrop(InputStream expectedImageInput, int maxShift) throws  IOException {
    Images.assertImageIsEqualToExpectedWithShiftAndCrop(
            browser(), el, expectedImageInput, maxShift);
  }


  /**
   * Verify the picture is "similar" to the reference image.
   * Ignores minor differences between the pixels.
   * Does not identify offsets and rotation. It uses a VERY simplistic approach (no wavelets or any other transform).
   *
   * @param expectedImageInput - reference png image
   * @param maxBadPixelsRatio - a positive number. For example: If it's 100, then
   *                           1% of the pixels can have major differences compared to
   *                          the reference.
   * @throws IOException - file could not be read
   */
  public void assertImageIsSimilarToExpected(InputStream expectedImageInput, int maxBadPixelsRatio) throws IOException {
      Images.assertImageIsSimilarToExpected(browser(), el, expectedImageInput, maxBadPixelsRatio);
  }

  /**
   * Verify the picture is "similar" to the reference image.
   * Ignores minor differences between the pixels.
   * Does not identify offsets and rotation. It uses a VERY simplistic approach (no wavelets or any other transform).
   *
   * @param expectedImageInput - reference png image
   * @param maxBadPixelsRatio - a positive number. For example: If it's 100, then
   *                           1% of the pixels can have major differences compared to
   *                          the reference.
   * @param maxShift - max shift allowed
   * @throws IOException - file could not be read
   */
  public void assertImageIsSimilarToExpectedWithShift(InputStream expectedImageInput, int maxBadPixelsRatio, int maxShift) throws IOException {
    Images.assertImageIsSimilarToExpectedWithShift(browser(), el, expectedImageInput, maxBadPixelsRatio, maxShift);
  }

  /**
   * Verify the picture is "similar" to the reference image.
   * Ignores minor differences between the pixels.
   * Does not identify offsets and rotation. It uses a VERY simplistic approach (no wavelets or any other transform).
   *
   * @param filterImageInput - filter image
   * @param expectedImageInput - reference png image
   * @param maxBadPixelsRatio - a positive number. For example: If it's 100, then
   *                           1% of the pixels can have major differences compared to
   *                          the reference.
   * @throws IOException - file could not be read
   */
  public void assertImageIsSimilarToExpectedWithFilter(
          InputStream filterImageInput,
          InputStream expectedImageInput,
          int maxBadPixelsRatio
  ) throws IOException {
    Images.assertImageIsSimilarToExpectedWithFilter(
            browser(),
            el,
            filterImageInput,
            expectedImageInput,
            maxBadPixelsRatio);
  }


  private static InBrowser browser() {
    return new InBrowser(InBrowserSinglton.driver);
  }

}