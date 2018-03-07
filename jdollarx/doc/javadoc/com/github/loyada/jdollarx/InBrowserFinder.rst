.. java:import:: org.openqa.selenium By

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.openqa.selenium WebDriver

.. java:import:: org.openqa.selenium WebElement

.. java:import:: java.util Collections

.. java:import:: java.util List

.. java:import:: java.util Optional

InBrowserFinder
===============

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public class InBrowserFinder

Methods
-------
find
^^^^

.. java:method:: static WebElement find(WebDriver driver, Path el)
   :outertype: InBrowserFinder

findAll
^^^^^^^

.. java:method:: public static List<WebElement> findAll(WebDriver driver, Path el)
   :outertype: InBrowserFinder

findPageWithNumberOfOccurrences
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static WebElement findPageWithNumberOfOccurrences(WebDriver driver, Path el, int numberOfOccurrences)
   :outertype: InBrowserFinder

findPageWithNumberOfOccurrences
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static WebElement findPageWithNumberOfOccurrences(WebDriver driver, Path el, int numberOfOccurrences, RelationOperator relationOperator)
   :outertype: InBrowserFinder

findPageWithout
^^^^^^^^^^^^^^^

.. java:method:: static WebElement findPageWithout(WebDriver driver, Path el)
   :outertype: InBrowserFinder

