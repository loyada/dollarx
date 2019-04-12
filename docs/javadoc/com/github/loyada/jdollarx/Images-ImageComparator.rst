.. java:import:: com.google.common.collect ImmutableList

.. java:import:: org.openqa.selenium Dimension

.. java:import:: org.openqa.selenium JavascriptExecutor

.. java:import:: org.openqa.selenium NoSuchElementException

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

.. java:import:: java.net URL

.. java:import:: java.util ArrayList

.. java:import:: java.util Arrays

.. java:import:: java.util Base64

.. java:import:: java.util HashMap

.. java:import:: java.util List

.. java:import:: java.util Map

.. java:import:: java.util Optional

.. java:import:: java.util.concurrent.atomic AtomicReference

.. java:import:: java.util.logging Logger

Images.ImageComparator
======================

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public static final class ImageComparator
   :outertype: Images

   Internal utility class for images

Methods
-------
getErrorImage
^^^^^^^^^^^^^

.. java:method:: public static Optional<BufferedImage> getErrorImage(BufferedImage img1, BufferedImage img2)
   :outertype: Images.ImageComparator

verifyImagesAreEqual
^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static void verifyImagesAreEqual(BufferedImage img1, BufferedImage img2)
   :outertype: Images.ImageComparator

verifyImagesAreShifted
^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static void verifyImagesAreShifted(BufferedImage img1, BufferedImage img2, int maxShift)
   :outertype: Images.ImageComparator

verifyImagesAreSimilar
^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static void verifyImagesAreSimilar(BufferedImage img1, BufferedImage img2, int maxBadPixelsRatio)
   :outertype: Images.ImageComparator

