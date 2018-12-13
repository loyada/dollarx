.. java:import:: org.openqa.selenium JavascriptExecutor

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.openqa.selenium WebDriver

.. java:import:: org.openqa.selenium WebElement

.. java:import:: org.openqa.selenium.interactions Actions

.. java:import:: java.io IOException

.. java:import:: java.util.concurrent Callable

.. java:import:: java.util.function UnaryOperator

Operations.DragAndDrop
======================

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public static class DragAndDrop
   :outertype: Operations

   internal implementation not be instantiated directly - Action of drag-and-drop

Constructors
------------
DragAndDrop
^^^^^^^^^^^

.. java:constructor:: public DragAndDrop(WebDriver driver, Path path)
   :outertype: Operations.DragAndDrop

Methods
-------
to
^^

.. java:method:: public void to(Path target) throws OperationFailedException
   :outertype: Operations.DragAndDrop

   drag and drop to the given element's location

   :param target: - the target(drop) element
   :throws OperationFailedException: operation failed. Typically includes the reason.

to
^^

.. java:method:: public void to(Integer x, Integer y) throws OperationFailedException
   :outertype: Operations.DragAndDrop

   drag and drop to the given coordinates

   :param x: coordinates
   :param y: coordinates
   :throws OperationFailedException: operation failed. Typically includes the reason.

