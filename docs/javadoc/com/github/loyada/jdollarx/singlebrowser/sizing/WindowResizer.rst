.. java:import:: org.openqa.selenium Dimension

.. java:import:: org.openqa.selenium Point

.. java:import:: java.util Map

WindowResizer
=============

.. java:package:: com.github.loyada.jdollarx.singlebrowser.sizing
   :noindex:

.. java:type:: public class WindowResizer implements AutoCloseable

   An AutoCloseable resizer for the browser. When closing, it reverts the the original state

Constructors
------------
WindowResizer
^^^^^^^^^^^^^

.. java:constructor:: public WindowResizer(int expectedWidth, int expectedHeight)
   :outertype: WindowResizer

   Resize a browser to the requested dimensions. First it changes the window size, and then it updates the size of the html inside it.

   :param expectedWidth: expected width
   :param expectedHeight: expected height

Methods
-------
close
^^^^^

.. java:method:: @Override public void close()
   :outertype: WindowResizer

   Revert state

getTotalHeight
^^^^^^^^^^^^^^

.. java:method:: public Long getTotalHeight()
   :outertype: WindowResizer

   get total scrollable height of the browser

   :return: height

getTotalWidth
^^^^^^^^^^^^^

.. java:method:: public Long getTotalWidth()
   :outertype: WindowResizer

   get total scrollable width of the browser

   :return: width

getVisibleHeight
^^^^^^^^^^^^^^^^

.. java:method:: public Long getVisibleHeight()
   :outertype: WindowResizer

   get visible height of the browser

   :return: height

getVisibleWidth
^^^^^^^^^^^^^^^

.. java:method:: public Long getVisibleWidth()
   :outertype: WindowResizer

   get visible width of the browser

   :return: width

