.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: org.openqa.selenium JavascriptExecutor

.. java:import:: org.openqa.selenium WebElement

.. java:import:: java.util Map

SizingUtils
===========

.. java:package:: com.github.loyada.jdollarx.singlebrowser.sizing
   :noindex:

.. java:type::  class SizingUtils

Fields
------
HEIGHT
^^^^^^

.. java:field:: static final String HEIGHT
   :outertype: SizingUtils

WIDTH
^^^^^

.. java:field:: static final String WIDTH
   :outertype: SizingUtils

Methods
-------
getScrollableDimensions
^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: static Map<String, Long> getScrollableDimensions(Path path)
   :outertype: SizingUtils

getStylingDimensions
^^^^^^^^^^^^^^^^^^^^

.. java:method:: static Map<String, String> getStylingDimensions(Path path)
   :outertype: SizingUtils

getVisibleDimensions
^^^^^^^^^^^^^^^^^^^^

.. java:method:: static Map<String, Long> getVisibleDimensions(Path path)
   :outertype: SizingUtils

setDimensions
^^^^^^^^^^^^^

.. java:method:: static void setDimensions(Path path, int width, int height)
   :outertype: SizingUtils

setDimensions
^^^^^^^^^^^^^

.. java:method:: static void setDimensions(Path path, String width, String height)
   :outertype: SizingUtils

