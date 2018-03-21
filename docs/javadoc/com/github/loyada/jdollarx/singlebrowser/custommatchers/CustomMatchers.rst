.. java:import:: com.github.loyada.jdollarx.custommatchers HasText

.. java:import:: com.github.loyada.jdollarx.custommatchers IsPresent

.. java:import:: com.github.loyada.jdollarx.singlebrowser InBrowserSinglton

.. java:import:: org.hamcrest Description

.. java:import:: org.hamcrest Matcher

.. java:import:: org.hamcrest TypeSafeMatcher

CustomMatchers
==============

.. java:package:: com.github.loyada.jdollarx.singlebrowser.custommatchers
   :noindex:

.. java:type:: public class CustomMatchers

   A collection of Hamcrest custom matchers, that are optimized to be as atomic as possible when interacting with the browser or a W3C document, and return useful error messages in case of a failure. This is a simplified API, relevant when there is a singleton browser.

Methods
-------
hasText
^^^^^^^

.. java:method:: public static Matcher<Path> hasText(String text)
   :outertype: CustomMatchers

   Successful if element has the text equal to the given parameter in the browser/document. Example use:

   .. parsed-literal::

      assertThat( path, hasText());

   :param text: the text to equal to (case insensitive)
   :return: a custom Hamcrest matcher

isAbsent
^^^^^^^^

.. java:method:: public static Matcher<Path> isAbsent()
   :outertype: CustomMatchers

   Successful if the browser has no elements that correspond to the given path. The implementation of this is optimized. This is much better than doing not(isPresent()), because in case of success (i.e. the element is not there), it will return immidiately, while the isPresent() will block until timeout is reached. For example: \ ``assertThat( path, isAbsent());``\

   :return: a matcher that is successful if an element does not appear in the browser.

isDisplayed
^^^^^^^^^^^

.. java:method:: public static Matcher<Path> isDisplayed()
   :outertype: CustomMatchers

   Successful if given element is present and displayed in the browser. Relies on WebElement.isDisplayed(), thus non-atomic. For example: \ ``assertThat( path, isDisplayed());``\

   :return: a matcher that checks if an element is displayed in the browser

isEnabled
^^^^^^^^^

.. java:method:: public static Matcher<Path> isEnabled()
   :outertype: CustomMatchers

   Successful if given element is present and enabled in the browser. Relies on WebElement.isEnabled(), thus non-atomic. For example: \ ``assertThat( path, isEnabled());``\

   :return: a matcher that checks if an element is enabled in the browser

isPresent
^^^^^^^^^

.. java:method:: public static IsPresentNTimes isPresent(int nTimes)
   :outertype: CustomMatchers

   Successful if the the element appears the expected number of times in the browser. This matcher is optimized. Example use for browser interaction:

   .. parsed-literal::

      assertThat( path, ispresent(5).timesOrMore());
      assertThat( path, ispresent(5).times());
      assertThat( path, ispresent(5).timesOrLess());

   :param nTimes: - the reference number of times to be matched against. See examples.
   :return: a matcher that matches the number of times an element is present. See examples in the description.

isPresent
^^^^^^^^^

.. java:method:: public static Matcher<Path> isPresent()
   :outertype: CustomMatchers

   Successful if the the element is present in the browser. Example: \ ``assertThat( path, ispresent());``\

   :return: a matcher that checks if an element is present in the browser

isSelected
^^^^^^^^^^

.. java:method:: public static Matcher<Path> isSelected()
   :outertype: CustomMatchers

   Successful if given element is present and selected in the browser. Relies on WebElement.isSelected(), thus non-atomic. For example: \ ``assertThat( path, isSelected());``\

   :return: a matcher that checks if an element is selected in the browser

