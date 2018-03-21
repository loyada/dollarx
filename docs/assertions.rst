==========
Assertions
==========

.. currentmodule:: dollarx

.. contents:: :local:

DollarX includes custom Hamcrest Matchers to allow to create assertions. Their benefits are:

* Expressiveness and readability. For example:

   .. code-block:: java

      assertThat(myElement, isPresent(1000).timesOrMore());

* Useful error messages.  For example, suppose we have the following assertion:

 .. code-block:: java

      assertThat(div.withText("flex-item"), isPresent(1000).timesOrMore());

 and there are only 4 elements with "John". The assertion error will be:

 .. code-block:: java

    Expected: browser page contains (div, that has class flex-item) at least 1000 times
       but: (div, that has class flex-item) appears 4 times

* Optimized for atomicity and speed. For example, the previous assertion will construct a single XPath \
  that find a DOM with at least 1000 elements that we look for.
  This improves correctness and performance.

  Another example: isAbsent() matcher, constructs an xpath that finds a DOM without the element that is
  expected to be absent. This means that in case of success (the common case), it returns immediately, \
  while the standard way of calling WebDriver.find will block for several seconds, until timeout is reached \
  and then it will throw an exception that will need to be caught by in the code of the test.


All the matchers are in \ :java:ref:`CustomMatchers`\  and \ :java:ref:`singlebrowser.custommatchers.CustomMatchers`\. There are 2 flavors - one that supports multiple \
instances of browsers, and a simplified one that supports a single instance of browser.
See the JavaDoc for details.




