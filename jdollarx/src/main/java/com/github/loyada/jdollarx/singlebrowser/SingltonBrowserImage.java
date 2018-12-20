package com.github.loyada.jdollarx.singlebrowser;

import com.github.loyada.jdollarx.Images;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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

  private static InBrowser browser() {
    return new InBrowser(InBrowserSinglton.driver);
  }

}