==========
Background
==========

.. currentmodule:: dollarx

.. contents:: :local:

Testing web applications is a challenge in multiple dimensions, even when ignoring the testing framework itself:

#. Writing correct code (learning curve, a lot of pitfalls)
#. Readability and maintainability
#. Race conditions and other gotchas
#. Performance - especially as the number of scenarios increases
#. Assertions - typically not expressive. Failure create useless errors.
#. Logging/Troubleshooting


Test cases
==========

First Scenario
--------------
Let's say we that in our web application we have a scenario in which have a list of names, and we want to assert that “John” is in the list.
Naive implementation:

.. code-block:: java

    List<WebElement> els = driver.findElements(By.cssSelector("li.name"));
    List<WebElement> filtered = els.stream().filter(el ->
                                  el.getText().equal("John")).
                                  collect(toList());
    assertThat(filtered, not(empty()));

This code looks innocent and reasonable, but has serious problems:

#. If there are 100 elements in the list, it will access the browser 101 times - extremely inefficient.
#. It retrieves potentially many elements that are not needed. Again, inefficient.
#. Race condition can lead to false negative - If the list of names is not empty, but the entry with the name "john" appears after a short delay, the first line will return immediately and we will miss "john", although it is there
#. What if "john" is actually inside an element wrapped by the "li"? We will miss it, since we only examine the "li".
#. If the list updates during execution, some of the elements we have a reference to, might disappear, resulting in a "StaleElementException" being thrown
#. The error message of the assertion, in case of a failure, is: "expected not empty, but was empty". This is not useful.

Besides these, it is also quite brittle to use a string for the selector of the elements.

To illustrate how insidious seemingly innocent code can be, let's examine 2 examples.

Second Scenario
---------------
Let's say that in our application there is a large table with a "clear" button, and we want to assert it worked properly.
Naive implementation:

.. code-block:: java

   List<WebElement> all = driver.findElements(By.xpath...);
   assertThat(all, is(empty());

Again, this code seems reasonable, but has serious issues:

#. Consider the failure (rare) scenario: we retrieve potentially many elements, and not do anything with them - clearly inefficient, but at least does not block.
#. Consider the success (common) scenario: the first line will block for several seconds until reaching the timeout and giving up. Again - very inefficient.
#. What if it takes a short time for the elements to clear? findElements() will return all the elements, and we will get a false failure.
#. Assertion error message is almost useless, without context


Finding elements
----------------
Selenium offers several way to find elements. The most commonly used are CSS selector, and Xpath.
Xpath is significantly more expressive, thus generally a better solution. The problem is that it has a tendency to be
complicated and brittle.
For example, an xpath for a DIV element with class "foo", is:

.. code-block:: java

   "//div[contains(conat(' ', normalize-space(@class), ' '), ' foo ')]"

Using a CSS selector instead is much simpler, but CSS is  more limited. Besides, even using CSS can be non-trivial.
Ideally, we want an API that combines the expressiveness of xpath, but be intuitive and simple ( div.withClass("foo") ).


DollarX
=======
The goal is to minimize the challenges described above, and abstract the complexity.
Let's reimplement the examples above with DollarX and analyze it.

First Scenario
--------------

.. code-block:: java

    assertThat(
         listItem.withClass("name").and(hasText("John")),
         isPresentIn(browser));

Let's re-examine the concerns in the previous implementation:

#. Even there are 100 elements in the list, it will access the browser only once, eliminating the previous implementation inefficiency
#. It finds at most a single element from the browser, eliminating the previous implementation inefficiency
#. If it takes the element with text "John" a short time to appear, it will wait until it appears, avoid the race condition issue in the previous implementation
#. What if "john" is actually inside an element wrapped by the "li"? we could use "hasAggregatedText" instead of "hasText".
#. Since the interaction with the browser is atomic, there is no chance of encountering "StateElementException".
#. In case of assertion error, the output is:

   "list item, that has class “name” and has the text “John” is expected to be present, but is absent”

 This is much more useful.

Second Scenario
---------------

 .. code-block:: java

     Path row = listItem.withClass("table-row").describedBy("row");
     assertThat( row, isAbsentFrom(browser));

Let's re-examine the previous implementation issues:

#. Consider the failure (rare) scenario: it will block until it can't find a DOM <i>without</i> this element.
#. Consider the success (common) scenario: It will look for a DOM <i>without</i> this element and returns immediately once it is true.
#. What if it takes a short time for the elements to clear? Again, since isAbsent looks for a page <i>without</i> this element, it will behave correctly and will not be sensitive to race conditions
#. Assertion error message, in case of failure is: "row is expected to be absent, but is present". This message is useful.


Summary
=======
The following anti-patterns are common when writing assertions in the browser:

#. Find all elements, then iterate over them looking for something, or take the nth element
#. Find an element, then look for an element under it, or with another relation to it.
#. Variation: The assertion involves several elements, so look for each of them separately
#. Use various “Sleep” statement to mitigate race conditions
#. Find all elements, and verify size is 0 (or: n, >n, <n)
#. “Enhance” the DOM to make it easier to test, thus changing behavior. This should be done judiciously.

The general approach to deal with it can be to write complicated XPath to find exactly what we expect atomically.
The problem with this approach is that xpath is very brittle and complicated. This is where Dollarx comes into the picture.
It allows to create an arbitrarily complex XPath that is much easier to build, understand and maintain. Thus it uses the power
of XPath but abstract away its challenges.












