.. java:import:: org.openqa.selenium JavascriptExecutor

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.openqa.selenium WebDriver

.. java:import:: org.openqa.selenium WebElement

.. java:import:: org.openqa.selenium.interactions Actions

.. java:import:: java.io IOException

.. java:import:: java.util.concurrent Callable

.. java:import:: java.util.function Function

.. java:import:: java.util.function UnaryOperator

Operations.ScrollElement
========================

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public static class ScrollElement
   :outertype: Operations

   internal implementation not be instantiated directly - Action of scroll within an element

Constructors
------------
ScrollElement
^^^^^^^^^^^^^

.. java:constructor:: public ScrollElement(WebDriver driver, Path wrapper)
   :outertype: Operations.ScrollElement

Methods
-------
down
^^^^

.. java:method:: public void down(Integer n)
   :outertype: Operations.ScrollElement

   scroll down number of pixels

   :param n: pixels

downUntilElementIsPresent
^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement downUntilElementIsPresent(Path expectedElement)
   :outertype: Operations.ScrollElement

   Scroll down until the virtualized DOM contains the expect element. Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :return: the WebElement or throws an exception of not found

downUntilElementIsPresent
^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement downUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls)
   :outertype: Operations.ScrollElement

   Scroll down until the virtualized DOM contains the expect element.

   :param expectedElement: - the element we are looking for
   :param scrollStep: - scroll step in pixels
   :param maxNumberOfScrolls: maximum number of scroll operations
   :return: the WebElement or throws an exception of not found

left
^^^^

.. java:method:: public void left(Integer n)
   :outertype: Operations.ScrollElement

   scroll left number of pixels

   :param n: pixels

leftUntilElementIsDisplayed
^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement leftUntilElementIsDisplayed(Path expectedElement)
   :outertype: Operations.ScrollElement

   Scroll left until the virtualized DOM contains the expect element, and it's displayed. Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :return: the WebElement or throws an exception if not found

leftUntilElementIsPresent
^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement leftUntilElementIsPresent(Path expectedElement)
   :outertype: Operations.ScrollElement

   Scroll left until the virtualized DOM contains the expect element. Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :return: the WebElement or throws an exception of not found

leftUntilElementIsPresent
^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement leftUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls)
   :outertype: Operations.ScrollElement

   Scroll left until the virtualized DOM contains the expect element.

   :param expectedElement: - the element we are looking for
   :param scrollStep: - scroll step in pixels
   :param maxNumberOfScrolls: maximum number of scroll operations
   :return: the WebElement or throws an exception of not found

right
^^^^^

.. java:method:: public void right(Integer n)
   :outertype: Operations.ScrollElement

   scroll right number of pixels

   :param n: pixels

rightUntilElementIsDisplayed
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement rightUntilElementIsDisplayed(Path expectedElement)
   :outertype: Operations.ScrollElement

   Scroll right until the virtualized DOM contains the expect element, and it's displayed. Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :return: the WebElement or throws an exception if not found

rightUntilElementIsPresent
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement rightUntilElementIsPresent(Path expectedElement)
   :outertype: Operations.ScrollElement

   Scroll right until the virtualized DOM contains the expect element. Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :return: the WebElement or throws an exception of not found

rightUntilElementIsPresent
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement rightUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls)
   :outertype: Operations.ScrollElement

   Scroll right until the virtualized DOM contains the expect element.

   :param expectedElement: - the element we are looking for
   :param scrollStep: - scroll step in pixels
   :param maxNumberOfScrolls: maximum number of scroll operations
   :return: the WebElement or throws an exception of not found

toLeftCorner
^^^^^^^^^^^^

.. java:method:: public void toLeftCorner()
   :outertype: Operations.ScrollElement

   Scroll to left-most point

toTopCorner
^^^^^^^^^^^

.. java:method:: public void toTopCorner()
   :outertype: Operations.ScrollElement

   Scroll to top-most point

toTopLeftCorner
^^^^^^^^^^^^^^^

.. java:method:: public WebElement toTopLeftCorner(Path expectedElement)
   :outertype: Operations.ScrollElement

   Scroll down until the virtualized DOM contains the expect element. Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :return: the WebElement or throws an exception of not found

toTopLeftCorner
^^^^^^^^^^^^^^^

.. java:method:: public void toTopLeftCorner()
   :outertype: Operations.ScrollElement

   Scroll to top-left corner

up
^^

.. java:method:: public void up(Integer n)
   :outertype: Operations.ScrollElement

   scroll up number of pixels

   :param n: pixels

upUntilElementIsPresent
^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement upUntilElementIsPresent(Path expectedElement)
   :outertype: Operations.ScrollElement

   Scroll up until the virtualized DOM contains the expect element. Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :return: the WebElement or throws an exception of not found

upUntilElementIsPresent
^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement upUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls)
   :outertype: Operations.ScrollElement

   Scroll up until the virtualized DOM contains the expect element.

   :param expectedElement: - the element we are looking for
   :param scrollStep: - scroll step in pixels
   :param maxNumberOfScrolls: maximum number of scroll operations
   :return: the WebElement or throws an exception of not found

