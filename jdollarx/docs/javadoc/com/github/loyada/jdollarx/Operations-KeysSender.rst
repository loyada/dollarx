.. java:import:: org.openqa.selenium JavascriptExecutor

.. java:import:: org.openqa.selenium WebDriver

.. java:import:: org.openqa.selenium WebElement

.. java:import:: org.openqa.selenium.interactions Actions

.. java:import:: java.io IOException

.. java:import:: java.util.function UnaryOperator

Operations.KeysSender
=====================

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public static class KeysSender
   :outertype: Operations

   internal implementation not be instantiated directly - Action of sending keys to browser

Constructors
------------
KeysSender
^^^^^^^^^^

.. java:constructor:: public KeysSender(WebDriver driver, CharSequence... charsToSend)
   :outertype: Operations.KeysSender

Methods
-------
to
^^

.. java:method:: public void to(Path path) throws OperationFailedException
   :outertype: Operations.KeysSender

   Send keys to a specific element in the browser

   :param path: the element to send the keys to
   :throws OperationFailedException: operation failed. Typically includes the reason.

toBrowser
^^^^^^^^^

.. java:method:: public void toBrowser()
   :outertype: Operations.KeysSender

   Send characters tp the browser in general

