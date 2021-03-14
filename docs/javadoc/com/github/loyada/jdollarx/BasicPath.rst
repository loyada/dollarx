.. java:import:: com.google.common.collect ImmutableList

.. java:import:: org.openqa.selenium WebElement

.. java:import:: java.util Arrays

.. java:import:: java.util Collections

.. java:import:: java.util List

.. java:import:: java.util Optional

.. java:import:: java.util.stream Collectors

.. java:import:: java.util.stream Stream

BasicPath
=========

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public final class BasicPath implements Path

   The standard implementation of Path in DollarX

Fields
------
anchor
^^^^^^

.. java:field:: public static final BasicPath anchor
   :outertype: BasicPath

   An anchor(or "a") element

body
^^^^

.. java:field:: public static final BasicPath body
   :outertype: BasicPath

button
^^^^^^

.. java:field:: public static final BasicPath button
   :outertype: BasicPath

canvas
^^^^^^

.. java:field:: public static final BasicPath canvas
   :outertype: BasicPath

div
^^^

.. java:field:: public static final BasicPath div
   :outertype: BasicPath

element
^^^^^^^

.. java:field:: public static final BasicPath element
   :outertype: BasicPath

   Any element

form
^^^^

.. java:field:: public static final BasicPath form
   :outertype: BasicPath

header
^^^^^^

.. java:field:: public static final BasicPath header
   :outertype: BasicPath

   Any header element

header1
^^^^^^^

.. java:field:: public static final BasicPath header1
   :outertype: BasicPath

header2
^^^^^^^

.. java:field:: public static final BasicPath header2
   :outertype: BasicPath

header3
^^^^^^^

.. java:field:: public static final BasicPath header3
   :outertype: BasicPath

header4
^^^^^^^

.. java:field:: public static final BasicPath header4
   :outertype: BasicPath

header5
^^^^^^^

.. java:field:: public static final BasicPath header5
   :outertype: BasicPath

header6
^^^^^^^

.. java:field:: public static final BasicPath header6
   :outertype: BasicPath

html
^^^^

.. java:field:: public static final BasicPath html
   :outertype: BasicPath

iframe
^^^^^^

.. java:field:: public static final BasicPath iframe
   :outertype: BasicPath

image
^^^^^

.. java:field:: public static final BasicPath image
   :outertype: BasicPath

input
^^^^^

.. java:field:: public static final BasicPath input
   :outertype: BasicPath

label
^^^^^

.. java:field:: public static final BasicPath label
   :outertype: BasicPath

listItem
^^^^^^^^

.. java:field:: public static final BasicPath listItem
   :outertype: BasicPath

   An "li" element

option
^^^^^^

.. java:field:: public static final BasicPath option
   :outertype: BasicPath

section
^^^^^^^

.. java:field:: public static final BasicPath section
   :outertype: BasicPath

select
^^^^^^

.. java:field:: public static final BasicPath select
   :outertype: BasicPath

span
^^^^

.. java:field:: public static final BasicPath span
   :outertype: BasicPath

svg
^^^

.. java:field:: public static final BasicPath svg
   :outertype: BasicPath

table
^^^^^

.. java:field:: public static final BasicPath table
   :outertype: BasicPath

td
^^

.. java:field:: public static final BasicPath td
   :outertype: BasicPath

textarea
^^^^^^^^

.. java:field:: public static final BasicPath textarea
   :outertype: BasicPath

th
^^

.. java:field:: public static final BasicPath th
   :outertype: BasicPath

title
^^^^^

.. java:field:: public static final BasicPath title
   :outertype: BasicPath

tr
^^

.. java:field:: public static final BasicPath tr
   :outertype: BasicPath

unorderedList
^^^^^^^^^^^^^

.. java:field:: public static final BasicPath unorderedList
   :outertype: BasicPath

   An "ul" element

Methods
-------
after
^^^^^

.. java:method:: @Override public Path after(Path path)
   :outertype: BasicPath

   The element appears after the given path

   :param path: - the element that appear before
   :return: a new path with the added constraint

afterSibling
^^^^^^^^^^^^

.. java:method:: @Override public Path afterSibling(Path path)
   :outertype: BasicPath

   The element has a preceding sibling that matches to the given Path parameter

   :param path: - the sibling element that appears before
   :return: a new path with the added constraint

ancestorOf
^^^^^^^^^^

.. java:method:: @Override public Path ancestorOf(Path path)
   :outertype: BasicPath

   :param path: - the element that is inside our element
   :return: a new path with the added constraint

and
^^^

.. java:method:: @Override public Path and(ElementProperty... prop)
   :outertype: BasicPath

   Alias equivalent to that(). Added for readability. Example:

   .. parsed-literal::

      div.that(hasClass("a")).and(hasText("foo"));

   :param prop: a list of element properties (constraints)
   :return: a new Path

before
^^^^^^

.. java:method:: @Override public Path before(Path path)
   :outertype: BasicPath

   The element is before the given path parameter

   :param path: - the element that appear after
   :return: a new path with the added constraint

beforeSibling
^^^^^^^^^^^^^

.. java:method:: @Override public Path beforeSibling(Path path)
   :outertype: BasicPath

   The element is a sibling of the given path and appears before it

   :param path: - the sibling element that appears after
   :return: a new path with the added constraint

builder
^^^^^^^

.. java:method:: public static PathBuilder builder()
   :outertype: BasicPath

childNumber
^^^^^^^^^^^

.. java:method:: public static ChildNumber childNumber(Integer n)
   :outertype: BasicPath

   the element is the nth child of its parent. Count starts at 1. For example:

   .. parsed-literal::

      childNumber(4).ofType(div.withClass("foo"))

   :param n: the index of the child - starting at 1
   :return: a ChildNumber instance, which is used with as in the example.

childOf
^^^^^^^

.. java:method:: @Override public Path childOf(Path path)
   :outertype: BasicPath

   :param path: - the parent element
   :return: a new path with the added constraint

containing
^^^^^^^^^^

.. java:method:: @Override public Path containing(Path path)
   :outertype: BasicPath

   :param path: - the element that is inside our element
   :return: a new path with the added constraint

contains
^^^^^^^^

.. java:method:: @Override public Path contains(Path path)
   :outertype: BasicPath

   :param path: - the element that is inside our element
   :return: a new path with the added constraint

customElement
^^^^^^^^^^^^^

.. java:method:: public static BasicPath customElement(String el)
   :outertype: BasicPath

   Create a custom element Path using a simple API instead of the builder pattern. Example:

   .. parsed-literal::

      Path myDiv = customElement("div");

   :param el: - the element type in W3C. will be used for the toString as well.
   :return: a Path representing the element

customNameSpaceElement
^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static BasicPath customNameSpaceElement(String el)
   :outertype: BasicPath

descendantOf
^^^^^^^^^^^^

.. java:method:: @Override public Path descendantOf(Path path)
   :outertype: BasicPath

   The element is inside the given path parameter

   :param path: - the element that is wrapping our element
   :return: a new path with the added constraint

describedBy
^^^^^^^^^^^

.. java:method:: @Override public Path describedBy(String description)
   :outertype: BasicPath

firstOccurrenceOf
^^^^^^^^^^^^^^^^^

.. java:method:: public static Path firstOccurrenceOf(Path path)
   :outertype: BasicPath

   First global occurrence of an element in the document.

   :param path: the element to find
   :return: a new path with the added constraint

getAlternateXPath
^^^^^^^^^^^^^^^^^

.. java:method:: @Override public Optional<String> getAlternateXPath()
   :outertype: BasicPath

getDescribedBy
^^^^^^^^^^^^^^

.. java:method:: @Override public Optional<String> getDescribedBy()
   :outertype: BasicPath

getElementProperties
^^^^^^^^^^^^^^^^^^^^

.. java:method:: @Override public List<ElementProperty> getElementProperties()
   :outertype: BasicPath

getUnderlyingSource
^^^^^^^^^^^^^^^^^^^

.. java:method:: @Override public Optional<WebElement> getUnderlyingSource()
   :outertype: BasicPath

getXPath
^^^^^^^^

.. java:method:: @Override public Optional<String> getXPath()
   :outertype: BasicPath

getXpathExplanation
^^^^^^^^^^^^^^^^^^^

.. java:method:: @Override public Optional<String> getXpathExplanation()
   :outertype: BasicPath

immediatelyAfterSibling
^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: @Override public Path immediatelyAfterSibling(Path path)
   :outertype: BasicPath

   The sibling right before the current element matches to the given Path parameter

   :param path: - the sibling element that appears right before
   :return: a new path with the added constraint

immediatelyBeforeSibling
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: @Override public Path immediatelyBeforeSibling(Path path)
   :outertype: BasicPath

   The sibling right after the element matches the given path parameter

   :param path: - the sibling element that appears after
   :return: a new path with the added constraint

inside
^^^^^^

.. java:method:: @Override public Path inside(Path path)
   :outertype: BasicPath

   Element that is inside another element

   :param path: - the containing element
   :return: a new Path with the added constraint

insideTopLevel
^^^^^^^^^^^^^^

.. java:method:: @Override public Path insideTopLevel()
   :outertype: BasicPath

   Returns an element that is explicitly inside the document. This is usually not needed - it will be added implicitly when needed.

   :return: a new Path

lastOccurrenceOf
^^^^^^^^^^^^^^^^

.. java:method:: public static Path lastOccurrenceOf(Path path)
   :outertype: BasicPath

   Last global occurrence of an element in the document

   :param path: the element to find
   :return: a new path with the added constraint

occurrenceNumber
^^^^^^^^^^^^^^^^

.. java:method:: public static GlobalOccurrenceNumber occurrenceNumber(Integer n)
   :outertype: BasicPath

   used in the form : occurrenceNumber(4).of(myElement)). Return the nth occurrence of the element in the entire document. Count starts at 1. For example:

   .. parsed-literal::

      occurrenceNumber(3).of(listItem)

   :param n: the number of occurrence
   :return: GlobalOccurrenceNumber instance, which is used as in the example.

or
^^

.. java:method:: @Override public Path or(Path path)
   :outertype: BasicPath

   match more than a single path. Example: div.or(span) - matches both div and span

   :param path: the alternative path to match
   :return: returns a new path that matches both the original one and the given parameter

parentOf
^^^^^^^^

.. java:method:: @Override public Path parentOf(Path path)
   :outertype: BasicPath

   :param path: - the child element
   :return: a new path with the added constraint

textNode
^^^^^^^^

.. java:method:: public static Path textNode(String text)
   :outertype: BasicPath

   Define a text node in the DOM, with the given text. Typically you don't need to use it, but it is relevant if you have something like:

   .. parsed-literal::

         Male
         Female

      input.immediatelyBeforeSibling(textNode("Male"));

   :param text: the text in the node. Note that it is trimmed, and case insensitive.
   :return: a Path of a text node

that
^^^^

.. java:method:: @Override public Path that(ElementProperty... prop)
   :outertype: BasicPath

   returns a path with the provided properties. For example: div.that(hasText("abc"), hasClass("foo"));

   :param prop: - one or more properties. See ElementProperties documentation for details
   :return: a new path with the added constraints

toString
^^^^^^^^

.. java:method:: @Override public String toString()
   :outertype: BasicPath

withClass
^^^^^^^^^

.. java:method:: @Override public Path withClass(String cssClass)
   :outertype: BasicPath

   Equivalent to \ ``this.that(hasClass(cssClass))``\

   :param cssClass: the class name
   :return: a new path with the added constraint

withClasses
^^^^^^^^^^^

.. java:method:: @Override public Path withClasses(String... cssClasses)
   :outertype: BasicPath

   Equivalent to \ ``this.that(hasClasses(cssClasses))``\

   :param cssClasses: the class names
   :return: a new path with the added constraint

withGlobalIndex
^^^^^^^^^^^^^^^

.. java:method:: @Override public Path withGlobalIndex(Integer n)
   :outertype: BasicPath

   An alias of: \ ``occurrenceNumber(n + 1).of(this)``\

   :param n: - the global occurrence index of the path, starting from 0
   :return: a new path with the added constraint

withText
^^^^^^^^

.. java:method:: @Override public Path withText(String txt)
   :outertype: BasicPath

   Element with text equals (ignoring case) to txt. Equivalent to:

   .. parsed-literal::

      path.that(hasText(txt))

   :param txt: - the text to equal to, ignoring case
   :return: a new Path with the added constraint

withTextContaining
^^^^^^^^^^^^^^^^^^

.. java:method:: @Override public Path withTextContaining(String txt)
   :outertype: BasicPath

   Equivalent to \ ``this.that(hasTextContaining(txt))``\ .

   :param txt: the text to match to. The match is case insensitive.
   :return: a new path with the added constraint

