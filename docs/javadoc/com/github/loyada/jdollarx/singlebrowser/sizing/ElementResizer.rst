.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: java.util Map

ElementResizer
==============

.. java:package:: com.github.loyada.jdollarx.singlebrowser.sizing
   :noindex:

.. java:type:: public class ElementResizer implements AutoCloseable

   An AutoCloseable of a resizer for a Path element. When closing, it reverts the the original state

Constructors
------------
ElementResizer
^^^^^^^^^^^^^^

.. java:constructor:: public ElementResizer(Path path, int expectedWidth, int expectedHeight)
   :outertype: ElementResizer

   Resize an element in the browser

   :param path: The element to resize
   :param expectedWidth: expected width
   :param expectedHeight: expected height

Methods
-------
close
^^^^^

.. java:method:: @Override public void close()
   :outertype: ElementResizer

   Revert state

getTotalHeight
^^^^^^^^^^^^^^

.. java:method:: public Long getTotalHeight()
   :outertype: ElementResizer

   get total scrollable height of the element

   :return: height

getTotalWidth
^^^^^^^^^^^^^

.. java:method:: public Long getTotalWidth()
   :outertype: ElementResizer

   get total scrollable width of the element

   :return: width

getVisibleHeight
^^^^^^^^^^^^^^^^

.. java:method:: public Long getVisibleHeight()
   :outertype: ElementResizer

   get visible height of the element

   :return: height

getVisibleWidth
^^^^^^^^^^^^^^^

.. java:method:: public Long getVisibleWidth()
   :outertype: ElementResizer

   get visible width of the element

   :return: width

getVisibleWidth
^^^^^^^^^^^^^^^

.. java:method:: public static Long getVisibleWidth(Path el)
   :outertype: ElementResizer

   get visible width of the element

   :param el: - the path to examine
   :return: width

