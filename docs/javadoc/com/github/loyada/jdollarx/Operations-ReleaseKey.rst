.. java:import:: org.openqa.selenium JavascriptExecutor

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.openqa.selenium WebDriver

.. java:import:: org.openqa.selenium WebElement

.. java:import:: org.openqa.selenium.interactions Actions

.. java:import:: java.io IOException

.. java:import:: java.util List

.. java:import:: java.util Optional

.. java:import:: java.util.concurrent Callable

.. java:import:: java.util.function Predicate

.. java:import:: java.util.function UnaryOperator

Operations.ReleaseKey
=====================

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public static class ReleaseKey
   :outertype: Operations

   internal implementation not be instantiated directly - Action of releasing a key (key up)

Constructors
------------
ReleaseKey
^^^^^^^^^^

.. java:constructor:: public ReleaseKey(WebDriver driver, CharSequence keysToSend)
   :outertype: Operations.ReleaseKey

Methods
-------
inBrowser
^^^^^^^^^

.. java:method:: public void inBrowser()
   :outertype: Operations.ReleaseKey

   releasing a key in the browser in general

on
^^

.. java:method:: public void on(BasicPath path) throws OperationFailedException
   :outertype: Operations.ReleaseKey

   release a key on a specific element in the browser

   :param path: the element to release the key on
   :throws OperationFailedException: operation failed. Typically includes the reason.

