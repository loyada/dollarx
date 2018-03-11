.. java:import:: org.openqa.selenium WebDriver

.. java:import:: org.openqa.selenium WebElement

.. java:import:: java.util List

.. java:import:: java.util Optional

Path
====

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public interface Path

   The heart of DollarX is the definition of Paths that represent W3C elements, whether in the browser or in a document. Path describes an element, or elements, using API that emulates regular description in English, and can represent almost any element that can be expressed as the XPATH. Path is immutable - any additional constraint creates a new Path instance and does not change the original one. It is important to remember that defining new Paths is very cheap, since it does not interact with the browser. For example, in selenium, if we want to define a WebElement inside another WebElement, it must involve interaction with the browser (which involves potential race conditions):

   .. parsed-literal::

      WebElement wrapper = driver.findElement(By.cssSelector("div.foo"));
        WebElement field = wrapper.findElement(By.cssSelector(".bar"));

   In DollarX it will look like:

   .. parsed-literal::

      final Path wrapper = element.withClass("foo");
         final Path field = element.withClass("bad").inside(wrapper);

   \ **Several points to note:**\

   =======================

   1. Once defined, Path values are final and can be reused without cost, as opposed to functions.

   2. Creating arbitrarily complex Path is easy this way. It is far more maintainable than using explicit xpath.

   3. Creating Paths has nothing to do with the browser.

   4. The API offers many alternative ways to define equivalent Paths. The guideline is: If it means the same in English, it is mapped to an equivalent Path. For example, the following are equivalent:

   .. parsed-literal::

      element.that(hasClass("condition").and(isInside(dialog)));
           element.that(hasClass("condition")).and(isInside(dialog));
           element.inside(dialog).that(hasClass("condition"));
           element.withClass("condition").and(isInside(dialog));
           element.withClass("condition").and(isContainedIn(dialog));
           element.that(hasAncesctor(dialog)).and(hasClass("condition"));

           // if you prefer to break the definition to two steps:
           Path condition = element.withClass("condition");
           condition.inside(dialog);

   5. Path::toString returns a string that reads like english and expresses exactly the definition of the path. This is very useful when troubleshooting.

   6. To check what is the xpath a Path is mapped to, call \ ``path.getXpath().get()``\

   7. Since it can be used as a wrapper of Selenium WebElement, you are not tied to using only DollarX - but can use it interchangeably with straight Selenium WebDriver.

   The pattern in DollarX is to define exactly what you want to interact with or assert, as a Path, and then interact with the browser. This maximizes atomicity and performance, and avoid many of the pitfalls involved with interactions with a dynamic SPA. The standard implementation for Path is \ :java:ref:`BasicPath`\ .

Methods
-------
after
^^^^^

.. java:method::  Path after(Path path)
   :outertype: Path

   The element appears after the given path

   :param path: - the element that appear before
   :return: a new Path with the added constraint

afterSibling
^^^^^^^^^^^^

.. java:method::  Path afterSibling(Path path)
   :outertype: Path

   The element is a sibling of the given path, and appears after it

   :param path: - the sibling element that appears before
   :return: a new Path with the added constraint

ancestorOf
^^^^^^^^^^

.. java:method::  Path ancestorOf(Path path)
   :outertype: Path

   The element contains the given path, i.e. the given path parameter is inside the element

   :param path: - the element that is inside our element
   :return: a new Path with the added constraint

and
^^^

.. java:method::  Path and(ElementProperty... prop)
   :outertype: Path

   Alias equivalent to \ :java:ref:`that`\ . Added for readability. Example:

   .. parsed-literal::

      div.that(hasClass("a")).and(hasText("foo"));

   :param prop: a list of element properties (constraints)
   :return: a new Path

before
^^^^^^

.. java:method::  Path before(Path path)
   :outertype: Path

   The element appears before the given path

   :param path: - the element that appear after
   :return: a new Path with the added constraint

beforeSibling
^^^^^^^^^^^^^

.. java:method::  Path beforeSibling(Path path)
   :outertype: Path

   The element is a sibling of the given path, and appears before it

   :param path: - the sibling element that appears after
   :return: a new Path with the added constraint

childOf
^^^^^^^

.. java:method::  Path childOf(Path path)
   :outertype: Path

   The element is a direct child of the given path

   :param path: - the parent element
   :return: a new Path with the added constraint

containing
^^^^^^^^^^

.. java:method::  Path containing(Path path)
   :outertype: Path

   The element contains the given path, i.e. the given path parameter is inside the element

   :param path: - the element that is inside our element
   :return: a new Path with the added constraint

contains
^^^^^^^^

.. java:method::  Path contains(Path path)
   :outertype: Path

   The element contains the given path, i.e. the given path parameter is inside the element

   :param path: - the element that is inside our element
   :return: a new Path with the added constraint

descendantOf
^^^^^^^^^^^^

.. java:method::  Path descendantOf(Path path)
   :outertype: Path

   The element is contained in the given path element, i.e. the given path parameter is wrapping it

   :param path: - the element that is wrapping our element
   :return: a new Path with the added constraint

describedBy
^^^^^^^^^^^

.. java:method::  Path describedBy(String description)
   :outertype: Path

   A useful method to give a readable description to the path, for example: Suppose that instead of describing it's DOM positions and attributes, you prefer to describe it as "search result". Then you'd call: searchResult = myElement.describedBy("search result"); Now, calling System.out.println(firstOccurrenceOf(searchResult)), will print: "first occurrence of search result" This will replace its toString() result.

   :param description: a readable description to that expresses the functionality of the path
   :return: a new Path similar to the old one but that is described by the given description

getAlternateXPath
^^^^^^^^^^^^^^^^^

.. java:method::  Optional<String> getAlternateXPath()
   :outertype: Path

   :return: Should not be used unless you are developing for DollarX.

getDescribedBy
^^^^^^^^^^^^^^

.. java:method::  Optional<String> getDescribedBy()
   :outertype: Path

   :return: optional readable functional description of the Path

getElementProperties
^^^^^^^^^^^^^^^^^^^^

.. java:method::  List<ElementProperty> getElementProperties()
   :outertype: Path

   :return: Should not be typically used, unless you are developing for DollarX

getUnderlyingSource
^^^^^^^^^^^^^^^^^^^

.. java:method::  Optional<WebElement> getUnderlyingSource()
   :outertype: Path

   :return: The WebElement that is used as the underlying reference for the Path. In most cases, this Optional is empty.

getXPath
^^^^^^^^

.. java:method::  Optional<String> getXPath()
   :outertype: Path

   The Optional xpath is maps to. Note that the prefix that marks it is inside the document (for example; "//" as the prefix of the xpath) can be omitted. This is not a concern - it will be added automatically by DollarX when interacting with the browser.

   :return: an optional containing the xpath this Path is mapped to. The optional is empty only in case it is used as a wrapper of a WebElement (not the typical case).

getXpathExplanation
^^^^^^^^^^^^^^^^^^^

.. java:method::  Optional<String> getXpathExplanation()
   :outertype: Path

inside
^^^^^^

.. java:method::  Path inside(Path path)
   :outertype: Path

   Element that is inside another element

   :param path: - the containing element
   :return: a new Path with the added constraint

insideTopLevel
^^^^^^^^^^^^^^

.. java:method::  Path insideTopLevel()
   :outertype: Path

   Returns an element that is explicitly inside the document. This is usually not needed - it will be added implicitly when needed.

   :return: a new Path

or
^^

.. java:method::  Path or(Path path)
   :outertype: Path

   match more than a single path. Example: div.or(span) - matches both div and span

   :param path: the alternative path to match
   :return: returns a new path that matches both the original one and the given parameter

parentOf
^^^^^^^^

.. java:method::  Path parentOf(Path path)
   :outertype: Path

   The element is a parent of the given path

   :param path: - the child element
   :return: a new Path with the added constraint

that
^^^^

.. java:method::  Path that(ElementProperty... prop)
   :outertype: Path

   returns a path with the provided properties. For example: div.that(hasText("abc"), hasClass("foo"));

   :param prop: - one or more properties. See ElementProperties documentation for details
   :return: a new path with the added constraints

withClass
^^^^^^^^^

.. java:method::  Path withClass(String cssClass)
   :outertype: Path

   The element has the given class name

   :param cssClass: the class name
   :return: a new Path with the added constraint

withClasses
^^^^^^^^^^^

.. java:method::  Path withClasses(String... cssClasses)
   :outertype: Path

   The element has the given class names

   :param cssClasses: the class names
   :return: a new Path with the added constraint

withGlobalIndex
^^^^^^^^^^^^^^^

.. java:method::  Path withGlobalIndex(Integer index)
   :outertype: Path

   Return the nth occurrence of the element in the entire document. Count starts at 1. The following expressions are equivalent: occurrenceNumber(4).of(myElement)); myElement.withGlobalIndex(4); Return the nth occurrence of the element in the entire document. Count starts at 1. For example: occurrenceNumber(3).of(listItem)

   :param index: the number of occurrence
   :return: a new Path with the added constraint

withText
^^^^^^^^

.. java:method::  Path withText(String txt)
   :outertype: Path

   Element with text equals (ignoring case) to txt. Equivalent to \ ``path.that(hasText(txt))``\

   :param txt: - the text to equal to, ignoring case
   :return: a new Path with the added constraint

withTextContaining
^^^^^^^^^^^^^^^^^^

.. java:method::  Path withTextContaining(String txt)
   :outertype: Path

   The element has text, containing the given txt parameter. The match is case insensitive.

   :param txt: the text to match to
   :return: a new Path with the added constraint

