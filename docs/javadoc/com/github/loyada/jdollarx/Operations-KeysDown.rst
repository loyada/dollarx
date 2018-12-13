.. java:import:: org.openqa.selenium JavascriptExecutor

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.openqa.selenium WebDriver

.. java:import:: org.openqa.selenium WebElement

.. java:import:: org.openqa.selenium.interactions Actions

.. java:import:: java.io IOException

.. java:import:: java.util.concurrent Callable

.. java:import:: java.util.function UnaryOperator

Operations.KeysDown
===================

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public static class KeysDown
   :outertype: Operations

   internal implementation not be instantiated directly - Action of key-down

Constructors
------------
KeysDown
^^^^^^^^

.. java:constructor:: public KeysDown(WebDriver driver, CharSequence keysToSend)
   :outertype: Operations.KeysDown

Methods
-------
inBrowser
^^^^^^^^^

.. java:method:: public void inBrowser()
   :outertype: Operations.KeysDown

   Send key-down to the browser in general

on
^^

.. java:method:: public void on(Path path) throws OperationFailedException
   :outertype: Operations.KeysDown

   Send key-down to an element in the browser

   :param path: the element to press a key down on
   :throws OperationFailedException: operation failed. Typically includes the reason.

