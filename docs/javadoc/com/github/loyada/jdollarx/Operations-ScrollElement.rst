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

   Scroll down until the DOM contains the expected element. Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :return: the WebElement or throws an exception of not found

downUntilElementIsPresent
^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement downUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls)
   :outertype: Operations.ScrollElement

   Scroll down until the DOM contains the expected element.

   :param expectedElement: - the element we are looking for
   :param scrollStep: - scroll step in pixels
   :param maxNumberOfScrolls: maximum number of scroll operations
   :return: the WebElement or throws an exception of not found

downUntilPredicate
^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement downUntilPredicate(Path expectedElement, Predicate<WebElement> predicate)
   :outertype: Operations.ScrollElement

   Scroll down until the DOM contains the expected element, and the given condition for that element is met. Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :param predicate: - a condition regarding the expected element that is required to be met
   :return: the WebElement or throws an exception of not found

downUntilPredicate
^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement downUntilPredicate(Path expectedElement, int scrollStep, int maxNumberOfScrolls, Predicate<WebElement> predicate)
   :outertype: Operations.ScrollElement

   Scroll down until the DOM contains the expected element, and the supplied condition for that element is met.

   :param expectedElement: - the element we are looking for
   :param scrollStep: - scroll step in pixels
   :param maxNumberOfScrolls: maximum number of scroll operations
   :param predicate: - a condition regarding the expected element that is required to be met
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

   Scroll left until the DOM contains the expected element, and it's displayed. Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :return: the WebElement or throws an exception if not found

leftUntilElementIsPresent
^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement leftUntilElementIsPresent(Path expectedElement)
   :outertype: Operations.ScrollElement

   Scroll left until the DOM contains the expected element. Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :return: the WebElement or throws an exception of not found

leftUntilElementIsPresent
^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement leftUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls)
   :outertype: Operations.ScrollElement

   Scroll left until the DOM contains the expected element.

   :param expectedElement: - the element we are looking for
   :param scrollStep: - scroll step in pixels
   :param maxNumberOfScrolls: maximum number of scroll operations
   :return: the WebElement or throws an exception of not found

leftUntilPredicate
^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement leftUntilPredicate(Path expectedElement, Predicate<WebElement> predicate)
   :outertype: Operations.ScrollElement

   Scroll left until the DOM contains the expected element, and the given predicate regarding that element is met. Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :param predicate: - a condition regarding the expected element that is required to be met
   :return: the WebElement or throws an exception of not found

leftUntilPredicate
^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement leftUntilPredicate(Path expectedElement, int scrollStep, int maxNumberOfScrolls, Predicate<WebElement> predicate)
   :outertype: Operations.ScrollElement

   Scroll left until the DOM contains the expected element and the supplied predicate for the element is met.

   :param expectedElement: - the element we are looking for
   :param scrollStep: - scroll step in pixels
   :param maxNumberOfScrolls: maximum number of scroll operations
   :param predicate: - a condition regarding the expected element that is required to be met
   :return: the WebElement or throws an exception of not found

right
^^^^^

.. java:method:: public void right(Integer n)
   :outertype: Operations.ScrollElement

   scroll right number of pixels

   :param n: pixels

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

rightUntilElementIsVisible
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement rightUntilElementIsVisible(Path expectedElement)
   :outertype: Operations.ScrollElement

   Scroll right until the virtualized DOM contains the expect element, and it is visible Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :return: the WebElement or throws an exception of not found

rightUntilPredicate
^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement rightUntilPredicate(Path expectedElement, Predicate<WebElement> predicate)
   :outertype: Operations.ScrollElement

   Scroll right until the DOM contains the expected element, and the given predicate regarding that element is met. Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :param predicate: - a condition regarding the expected element that is required to be met
   :return: the WebElement or throws an exception of not found

rightUntilPredicate
^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement rightUntilPredicate(Path expectedElement, int scrollStep, int maxNumberOfScrolls, Predicate<WebElement> predicate)
   :outertype: Operations.ScrollElement

   Scroll right until the DOM contains the expected element and the supplied predicate for the element is met.

   :param expectedElement: - the element we are looking for
   :param scrollStep: - scroll step in pixels
   :param maxNumberOfScrolls: maximum number of scroll operations
   :param predicate: - a condition regarding the expected element that is required to be met
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

   Scroll down until the DOM contains the expected element. Using 40 pixels steps, until the end of the table

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

upUntilPredicate
^^^^^^^^^^^^^^^^

.. java:method:: public WebElement upUntilPredicate(Path expectedElement, Predicate<WebElement> predicate)
   :outertype: Operations.ScrollElement

   Scroll up until the DOM contains the expected element, and the given condition for that element is met. Using 40 pixels steps, until the end of the table

   :param expectedElement: - the element we are looking for
   :param predicate: - a condition regarding the expected element that is required to be met
   :return: the WebElement or throws an exception of not found

upUntilPredicate
^^^^^^^^^^^^^^^^

.. java:method:: public WebElement upUntilPredicate(Path expectedElement, int scrollStep, int maxNumberOfScrolls, Predicate<WebElement> predicate)
   :outertype: Operations.ScrollElement

   Scroll up until the DOM contains the expected element, and the supplied condition for that element is met.

   :param expectedElement: - the element we are looking for
   :param scrollStep: - scroll step in pixels
   :param maxNumberOfScrolls: maximum number of scroll operations
   :param predicate: - a condition regarding the expected element that is required to be met
   :return: the WebElement or throws an exception of not found

