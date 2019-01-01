.. java:import:: com.github.loyada.jdollarx Images

.. java:import:: com.github.loyada.jdollarx InBrowser

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: java.io File

.. java:import:: java.io IOException

.. java:import:: java.io InputStream

SingltonBrowserImage
====================

.. java:package:: com.github.loyada.jdollarx.singlebrowser
   :noindex:

.. java:type:: public class SingltonBrowserImage

   Capturing and asserting the image (screenshot) of a Path element. It supports both canvas and a standard element image. It supports both accurate and fuzzy assertion. It provides utility functions to display an element in a separate window.

Constructors
------------
SingltonBrowserImage
^^^^^^^^^^^^^^^^^^^^

.. java:constructor:: public SingltonBrowserImage(Path el)
   :outertype: SingltonBrowserImage

   :param el: The element the represents the image we are interested in

Methods
-------
assertImageIsEqualToExpected
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void assertImageIsEqualToExpected(InputStream expectedImageInput) throws IOException
   :outertype: SingltonBrowserImage

   Verify that the element's image is pixel-perfect

   :param expectedImageInput: - reference png image
   :throws IOException: - file could not be read

assertImageIsEqualToExpectedWithShiftAndCrop
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void assertImageIsEqualToExpectedWithShiftAndCrop(InputStream expectedImageInput, int maxShift) throws IOException
   :outertype: SingltonBrowserImage

   Verify that the element's image is pixel-perfect, but allowing one to be a cropped/shifted version of the other.

   :param expectedImageInput: - reference png image
   :param maxShift: maximum pixels the images are shifted/cropped compared to each other
   :throws IOException: - file could not be read

assertImageIsSimilarToExpected
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void assertImageIsSimilarToExpected(InputStream expectedImageInput, int maxBadPixelsRatio) throws IOException
   :outertype: SingltonBrowserImage

   Verify the picture is "similar" to the reference image. Ignores minor differences between the pixels. Does not identify offsets and rotation. It uses a VERY simplistic approach (no wavelets or any other transform).

   :param expectedImageInput: - reference png image
   :param maxBadPixelsRatio: - a positive number. For example: If it's 100, then 1% of the pixels can have major differences compared to the reference.
   :throws IOException: - file could not be read

captureCanvasToFile
^^^^^^^^^^^^^^^^^^^

.. java:method:: public void captureCanvasToFile(File outputFile)
   :outertype: SingltonBrowserImage

   Capture the image of an HTML5 canvas as a png, and save it to the given file. If the element given is not a canvas, this will fail. Note that it is more optimized - it downloads only the section of the canvas as an image.

   :param outputFile: - output file

captureToFile
^^^^^^^^^^^^^

.. java:method:: public void captureToFile(File outputFile)
   :outertype: SingltonBrowserImage

   Capture the image of an element as a png, and save it to the given file

   :param outputFile: - output file

show
^^^^

.. java:method:: public void show()
   :outertype: SingltonBrowserImage

   Display the element image in a separate window. This is useful for troubleshooting/development. Note that this will not work well if you do it inside a debugger evaluation.

showCanvas
^^^^^^^^^^

.. java:method:: public void showCanvas()
   :outertype: SingltonBrowserImage

   Similar to show(), but optimized for an HTML5 canvas element

