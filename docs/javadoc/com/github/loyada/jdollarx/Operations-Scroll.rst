.. java:import:: org.openqa.selenium JavascriptExecutor

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.openqa.selenium WebDriver

.. java:import:: org.openqa.selenium WebElement

.. java:import:: org.openqa.selenium.interactions Actions

.. java:import:: java.io IOException

.. java:import:: java.util.concurrent Callable

.. java:import:: java.util.function Function

.. java:import:: java.util.function UnaryOperator

Operations.Scroll
=================

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public static class Scroll
   :outertype: Operations

   internal implementation not be instantiated directly - Action of scroll

Constructors
------------
Scroll
^^^^^^

.. java:constructor:: public Scroll(WebDriver driver)
   :outertype: Operations.Scroll

Methods
-------
down
^^^^

.. java:method:: public void down(Integer n)
   :outertype: Operations.Scroll

   scroll down number of pixels

   :param n: pixels

left
^^^^

.. java:method:: public void left(Integer n)
   :outertype: Operations.Scroll

   scroll left number of pixels

   :param n: pixels

right
^^^^^

.. java:method:: public void right(Integer n)
   :outertype: Operations.Scroll

   scroll right number of pixels

   :param n: pixels

to
^^

.. java:method:: public void to(Path path)
   :outertype: Operations.Scroll

   Scroll until the location of an element

   :param path: the element to scroll to

up
^^

.. java:method:: public void up(Integer n)
   :outertype: Operations.Scroll

   scroll up number of pixels

   :param n: pixels

