.. java:import:: com.github.loyada.jdollarx ElementProperties

.. java:import:: com.github.loyada.jdollarx InBrowser

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: com.github.loyada.jdollarx PathOperators

.. java:import:: com.github.loyada.jdollarx PathParsers

.. java:import:: org.hamcrest Description

.. java:import:: org.hamcrest Matcher

.. java:import:: org.hamcrest TypeSafeMatcher

.. java:import:: org.openqa.selenium WebDriver

.. java:import:: org.openqa.selenium.support.ui ExpectedConditions

.. java:import:: org.openqa.selenium.support.ui FluentWait

.. java:import:: org.openqa.selenium.support.ui Wait

.. java:import:: org.w3c.dom Document

.. java:import:: org.w3c.dom NodeList

.. java:import:: javax.xml.xpath XPathExpressionException

.. java:import:: java.util NoSuchElementException

.. java:import:: java.util.concurrent TimeUnit

CustomMatchers
==============

.. java:package:: com.github.loyada.jdollarx.custommatchers
   :noindex:

.. java:type:: public class CustomMatchers

   A collection of Hamcrest custom matchers, that are optimized to be as atomic as possible when interacting with the browser or a W3C document, and return useful error messages in case of a failure.

Methods
-------
hasElement
^^^^^^^^^^

.. java:method:: public static Matcher<InBrowser> hasElement(Path el)
   :outertype: CustomMatchers

   Successful if the browser has an element that corresponds to the given path. Example use:

   .. parsed-literal::

      assertThat( browser, hasElement(el));

   :param el: the path to find
   :return: a matcher for a browser that contains the element

hasElements
^^^^^^^^^^^

.. java:method:: public static HasElements hasElements(Path path)
   :outertype: CustomMatchers

   Successful if element is present in the browser or a W3C document. Useful especially when you have a reference count. This matcher is optimized. For example:

   .. parsed-literal::

      assertThat(browser, hasElements(path).present(5).times());
         assertThat(browser, hasElements(path).present(5).timesOrMore());
         assertThat(document, hasElements(path).present(5).timesOrLess());

   :param path: The path of the elements to find
   :return: a matcher for the number of times an element is present.

hasNoElement
^^^^^^^^^^^^

.. java:method:: public static Matcher<InBrowser> hasNoElement(Path el)
   :outertype: CustomMatchers

   Successful if given browser has no elements that correspond to the given path. The implementation of this is optimized. For example:

   .. parsed-literal::

      assertThat( browser, hasNoElement(path));

   :param el: - the path that is expected not to exist in the browser
   :return: a matcher that is successful if the element does not appear in the browser

hasText
^^^^^^^

.. java:method:: public static HasText hasText(String text)
   :outertype: CustomMatchers

   Successful if element has the text equal to the given parameter in the browser/document. Example use:

   .. parsed-literal::

      assertThat( path, hasText().in(browser));

   :param text: the text to equal to (case insensitive)
   :return: a custom Hamcrest matcher

isAbsentFrom
^^^^^^^^^^^^

.. java:method:: public static Matcher<Path> isAbsentFrom(InBrowser browser)
   :outertype: CustomMatchers

   Successful if given browser has no elements that correspond to the given path. Equivalent to hasNoElement() matcher. This is much better than doing not(isPresent()), because in case of success (i.e. the element is not there), it will return immidiately, while the isPresent() will block until timeout is reached. For example:

   .. parsed-literal::

      assertThat( path, isAbsentFrom(browser));

   :param browser: the browser instance to look in
   :return: a matcher that is successful if the element does not appear in the browser

isAbsentFrom
^^^^^^^^^^^^

.. java:method:: public static Matcher<Path> isAbsentFrom(Document document)
   :outertype: CustomMatchers

   Successful if given document has no elements that correspond to the given path. For example:

   .. parsed-literal::

      assertThat( path, isAbsentFrom(doc));

   :param document: - a W3C document
   :return: a matcher that is successful if the element does not appear in the document

isDisplayedIn
^^^^^^^^^^^^^

.. java:method:: public static Matcher<Path> isDisplayedIn(InBrowser browser)
   :outertype: CustomMatchers

   Successful if given element is present and displayed in the browser. Relies on WebElement.isDisplayed(), thus non-atomic. For example:

   .. parsed-literal::

      assertThat( path, isDisplayedIn(browser));

   :param browser: the browser instance to look in
   :return: a matcher that checks if an element is displayed in the browser

isEnabledIn
^^^^^^^^^^^

.. java:method:: public static Matcher<Path> isEnabledIn(InBrowser browser)
   :outertype: CustomMatchers

   Successful if given element is present and enabled in the browser. Relies on WebElement.isEnabled(), thus non-atomic. For example:

   .. parsed-literal::

      assertThat( path, isEnabledIn(browser));

   :param browser: the browser instance to look in
   :return: a matcher that checks if an element is enabled in the browser

isNotDisplayedIn
^^^^^^^^^^^^^^^^

.. java:method:: public static Matcher<Path> isNotDisplayedIn(InBrowser browser)
   :outertype: CustomMatchers

   Successful if given element is either not present, or present and not displayed in the browser. Relies on WebElement.isDisplayed(), thus non-atomic. For example: \ ``assertThat( path, isNotDisplayed());``\

   :return: a matcher that checks if an element is displayed in the browser

isPresent
^^^^^^^^^

.. java:method:: public static IsPresentNTimes isPresent(int nTimes)
   :outertype: CustomMatchers

   Successful if the the element appears the expected number of times in the browser or W3C document. This matcher is optimized. Example use for browser interaction:

   .. parsed-literal::

      InBrowser browser = new InBrowser(driver);
        assertThat( myElement, ispresent(5).timesOrMoreIn(browser));
        assertThat( myElement, ispresent(5).timesIn(browser));
        assertThat( myElement, ispresent(5).timesOrLessIn(browser));

   Same examples apply in case you have a Document (org.w3c.dom.Document).

   :param nTimes: - the reference number of times to be matched against. See examples.
   :return: a matcher that matches the number of times an element is present. See examples in the description.

isPresent
^^^^^^^^^

.. java:method:: public static IsPresent isPresent()
   :outertype: CustomMatchers

   Successful if element is present in the browser/document. Example use:

   .. parsed-literal::

      assertThat( path, isPresent().in(browser));

   :return: a custom Hamcrest matcher

isPresentIn
^^^^^^^^^^^

.. java:method:: public static Matcher<Path> isPresentIn(InBrowser browser)
   :outertype: CustomMatchers

   Successful if given element is present in the browser. For example:

   .. parsed-literal::

      assertThat( path, isPresentIn(browser));

   :param browser: the browser instance to look in
   :return: a matcher that checks if an element is present in a browser

isPresentIn
^^^^^^^^^^^

.. java:method:: public static Matcher<Path> isPresentIn(Document document)
   :outertype: CustomMatchers

   Successful if given element is present in the document. For example:

   .. parsed-literal::

      assertThat( path, isPresentIn(document));

   :param document: - a W#C document
   :return: a matcher that checks if an element is present in a document

isSelectedIn
^^^^^^^^^^^^

.. java:method:: public static Matcher<Path> isSelectedIn(InBrowser browser)
   :outertype: CustomMatchers

   Successful if given element is present and selected in the browser. Relies on WebElement.isSelected(), thus non-atomic. For example:

   .. parsed-literal::

      assertThat( path, isSelectedIn(browser));

   :param browser: the browser instance to look in
   :return: a matcher that checks if an element is selected in the browser

