.. java:import:: org.openqa.selenium Dimension

.. java:import:: org.openqa.selenium JavascriptExecutor

.. java:import:: org.openqa.selenium OutputType

.. java:import:: org.openqa.selenium Point

.. java:import:: org.openqa.selenium TakesScreenshot

.. java:import:: org.openqa.selenium WebElement

.. java:import:: javax.imageio ImageIO

.. java:import:: java.awt.image BufferedImage

.. java:import:: java.io ByteArrayInputStream

.. java:import:: java.io File

.. java:import:: java.io IOException

.. java:import:: java.io InputStream

.. java:import:: java.util Base64

.. java:import:: java.util.stream IntStream

Images
======

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public class Images

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

