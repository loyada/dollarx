.. java:import:: com.google.common.collect ImmutableList

.. java:import:: com.google.common.collect Lists

.. java:import:: org.openqa.selenium Dimension

.. java:import:: org.openqa.selenium JavascriptExecutor

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.openqa.selenium OutputType

.. java:import:: org.openqa.selenium Point

.. java:import:: org.openqa.selenium TakesScreenshot

.. java:import:: org.openqa.selenium WebElement

.. java:import:: javax.imageio ImageIO

.. java:import:: javax.swing Icon

.. java:import:: javax.swing ImageIcon

.. java:import:: javax.swing JFrame

.. java:import:: javax.swing JLabel

.. java:import:: javax.swing SwingUtilities

.. java:import:: java.awt Color

.. java:import:: java.awt.image BufferedImage

.. java:import:: java.io ByteArrayInputStream

.. java:import:: java.io File

.. java:import:: java.io IOException

.. java:import:: java.io InputStream

.. java:import:: java.util ArrayList

.. java:import:: java.util Arrays

.. java:import:: java.util Base64

.. java:import:: java.util HashMap

.. java:import:: java.util List

.. java:import:: java.util Map

.. java:import:: java.util Optional

.. java:import:: java.util.concurrent.atomic AtomicReference

.. java:import:: java.util.logging Logger

Images
======

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public class Images

Fields
------
logger
^^^^^^

.. java:field:: static Logger logger
   :outertype: Images

Methods
-------
assertImageIsEqualToExpected
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static void assertImageIsEqualToExpected(InBrowser browser, Path el, InputStream expectedImageInput) throws IOException
   :outertype: Images

   Verify that the element's image is pixel-perfect

   :param browser: - browser
   :param el: - element to capture and verify
   :param expectedImageInput: reference image file
   :throws IOException: - file could not be read

assertImageIsEqualToExpectedWithShiftAndCrop
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static void assertImageIsEqualToExpectedWithShiftAndCrop(InBrowser browser, Path el, InputStream expectedImageInput, int maxShift) throws IOException
   :outertype: Images

   Verify that the element's image is pixel-perfect, but allowing some crop/shift

   :param browser: - browser
   :param el: - element to capture and verify
   :param expectedImageInput: reference image file
   :param maxShift: maximum pixels the images are shifted/cropped compared to each other (both on x and y axis)
   :throws IOException: - file could not be read

assertImageIsSimilarToExpected
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static void assertImageIsSimilarToExpected(InBrowser browser, Path el, InputStream expectedImageInput, int maxBadPixelsRatio) throws IOException
   :outertype: Images

   Verify the picture is "similar" to the reference image. Ignores minor differences between the pixels.

   :param browser: - browser
   :param el: - element to capture and validate
   :param expectedImageInput: - reference image
   :param maxBadPixelsRatio: - a positive number. For example: If it's 100, then 1% of the pixels can have major differences compared to the reference.
   :throws IOException: - image file could not be read

captureCanvas
^^^^^^^^^^^^^

.. java:method:: public static BufferedImage captureCanvas(InBrowser browser, Path canvas)
   :outertype: Images

captureCanvasToFile
^^^^^^^^^^^^^^^^^^^

.. java:method:: public static void captureCanvasToFile(InBrowser browser, Path el, File outputFile)
   :outertype: Images

   Save an HTML5 canvas to file. Optimized for canvas. Will fail if the element is not a canvas.

   :param browser: - browser
   :param el: - Path element to capture
   :param outputFile: - output file

captureToFile
^^^^^^^^^^^^^

.. java:method:: public static void captureToFile(InBrowser browser, Path el, File outputFile)
   :outertype: Images

   Save image to file

   :param browser: - browser
   :param el: - Path element to capture
   :param outputFile: - output file

getErrorsImage
^^^^^^^^^^^^^^

.. java:method:: public static Optional<BufferedImage> getErrorsImage(InBrowser browser, Path el, InputStream expectedImageInput) throws IOException
   :outertype: Images

   create and return an image that highlights the different pixels between the captured image and the reference image

   :param browser: - browser
   :param el: - element to capture and verify
   :param expectedImageInput: reference image file
   :throws IOException: - file could not be read
   :throws AssertionError: - images are not the same size
   :return: an image that highlights the different pixels. If the images are equal, returns an empty optional.

show
^^^^

.. java:method:: public static void show(InBrowser browser, Path el)
   :outertype: Images

   Display image of an element in a separate window. Does not work as an evaluation within the debugger.

   :param browser: - browser
   :param el: - the element to capture and display

showCanvas
^^^^^^^^^^

.. java:method:: public static void showCanvas(InBrowser browser, Path el)
   :outertype: Images

   Display image of an HTML5 canvas element in a separate window. Does not work as an evaluation within the debugger.

   :param browser: - browser
   :param el: - the element to capture and display

showImage
^^^^^^^^^

.. java:method:: static void showImage(BufferedImage image)
   :outertype: Images

