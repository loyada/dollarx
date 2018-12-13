.. java:import:: org.openqa.selenium JavascriptExecutor

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.openqa.selenium WebDriver

.. java:import:: org.openqa.selenium WebElement

.. java:import:: org.openqa.selenium.interactions Actions

.. java:import:: java.io IOException

.. java:import:: java.util.concurrent Callable

.. java:import:: java.util.function UnaryOperator

Operations
==========

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public class Operations

   Internal implementation of various browser operations

Methods
-------
doWithRetries
^^^^^^^^^^^^^

.. java:method:: public static void doWithRetries(Runnable action, int numberOfRetries, int sleepInMillisec)
   :outertype: Operations

   Retry an action/assertion up to a number of times, with delay after each time. For example:

   .. parsed-literal::

      doWithRetries(() -> assertThat(div.withClass("foo"), isDisplayedIn(browser)), 5, 10);

   :param action: the action to try. It's a runnable - no input parapeters and does not return anything.
   :param numberOfRetries: - maximum number of retries
   :param sleepInMillisec: - delay between consecutive retries

doWithRetries
^^^^^^^^^^^^^

.. java:method:: public static <T> T doWithRetries(Callable<T> action, int numberOfRetries, int sleepInMillisec) throws Exception
   :outertype: Operations

   Retry an action up to a number of times, with delay after each time. For example:

   .. parsed-literal::

      WebElement el = doWithRetries(() -> browser.find(div.withClass("foo"), 5, 10);

   :param action: the action to try. It has no input parameters, but returns a value
   :param numberOfRetries: - maximum number of retries
   :param sleepInMillisec: - delay between consecutive retries
   :param <T>: any type that the function returns
   :throws Exception: the exception thrown by the last try in case it exceeded the number of retries.
   :return: returns the result of the callable

