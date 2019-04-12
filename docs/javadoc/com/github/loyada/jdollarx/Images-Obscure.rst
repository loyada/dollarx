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

Images.Obscure
==============

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public static class Obscure implements AutoCloseable
   :outertype: Images

Fields
------
js
^^

.. java:field:: final JavascriptExecutor js
   :outertype: Images.Obscure

obscuredElements
^^^^^^^^^^^^^^^^

.. java:field:: final List<Path> obscuredElements
   :outertype: Images.Obscure

strict
^^^^^^

.. java:field:: final boolean strict
   :outertype: Images.Obscure

Constructors
------------
Obscure
^^^^^^^

.. java:constructor:: public Obscure(InBrowser browser, Path element)
   :outertype: Images.Obscure

Obscure
^^^^^^^

.. java:constructor:: public Obscure(InBrowser browser, List<Path> elements)
   :outertype: Images.Obscure

Obscure
^^^^^^^

.. java:constructor:: public Obscure(InBrowser browser, List<Path> elements, boolean strict)
   :outertype: Images.Obscure

Methods
-------
close
^^^^^

.. java:method:: @Override public void close() throws Exception
   :outertype: Images.Obscure

getObscuredElements
^^^^^^^^^^^^^^^^^^^

.. java:method:: public List<Path> getObscuredElements()
   :outertype: Images.Obscure

