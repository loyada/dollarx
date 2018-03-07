.. java:import:: java.util Arrays

.. java:import:: java.util Collections

.. java:import:: java.util List

.. java:import:: java.util Optional

.. java:import:: java.util.stream Collectors

ElementProperties
=================

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public final class ElementProperties

   Various constrains on \ :java:ref:`Path`\  instances, that are used with the methods \ :java:ref:`Path.that`\  and \ :java:ref:`Path.and`\ .

Fields
------
hasChildren
^^^^^^^^^^^

.. java:field:: public static final ElementProperty hasChildren
   :outertype: ElementProperties

   The element has 1 or more children (the opposite from hasNoChildren). Examples where it might be useful: an non-empty list, non-empty table, etc.

hasNoChildren
^^^^^^^^^^^^^

.. java:field:: public static final ElementProperty hasNoChildren
   :outertype: ElementProperties

   The element has no children. Examples where it might be useful: an empty list, empty table, etc.

hasSomeText
^^^^^^^^^^^

.. java:field:: public static ElementProperty hasSomeText
   :outertype: ElementProperties

   Element has non-empty text

isHidden
^^^^^^^^

.. java:field:: public static ElementProperty isHidden
   :outertype: ElementProperties

   Element that is hidden. This is limited to only examine embeded css style, so it not useful in some cases.

isLastSibling
^^^^^^^^^^^^^

.. java:field:: public static final ElementProperty isLastSibling
   :outertype: ElementProperties

   The element is the last sibling (ie: last child) of its parent.

isOnlyChild
^^^^^^^^^^^

.. java:field:: public static final ElementProperty isOnlyChild
   :outertype: ElementProperties

   The element is the only direct child of its parent. It has no siblings. For example: a table with a single row.

Methods
-------
contains
^^^^^^^^

.. java:method:: public static ElementProperty contains(Path... paths)
   :outertype: ElementProperties

   The given elements in the parameters list are contained in the current element

   :param paths: - a list of elements that are descendants of the current element
   :return: An element property that can be applied with Path::that

hasAggregatedTextContaining
^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty hasAggregatedTextContaining(String txt)
   :outertype: ElementProperties

   When aggregating all the text under this element, it contains the given substring (ignoring case)

   :param txt: a substring of the aggregated, case insensitive, text inside the element
   :return: An element property that can be applied with Path::that

hasAggregatedTextEndingWith
^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty hasAggregatedTextEndingWith(String txt)
   :outertype: ElementProperties

   When aggregating all the text under this element, it ends with the given substring (ignoring case)

   :param txt: the suffix of the aggregated, case insensitive, text inside the element
   :return: An element property that can be applied with Path::that

hasAggregatedTextEqualTo
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty hasAggregatedTextEqualTo(String txt)
   :outertype: ElementProperties

   When aggregating all the text under this element, it equals to the given parameter (ignoring case)

   :param txt: the aggregated, case insensitive, text inside the element
   :return: An element property that can be applied with Path::that

hasAggregatedTextStartingWith
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty hasAggregatedTextStartingWith(String txt)
   :outertype: ElementProperties

   When aggregating all the text under this element, it starts with the given substring (ignoring case)

   :param txt: the prefix of the aggregated, case insensitive, text inside the element
   :return: An element property that can be applied with Path::that

hasAncesctor
^^^^^^^^^^^^

.. java:method:: public static ElementProperty hasAncesctor(Path path)
   :outertype: ElementProperties

   Element is inside the given parameter

   :param path: the ancestor of the current element
   :return: An element property that can be applied with Path::that

hasAnyOfClasses
^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty hasAnyOfClasses(String... cssClasses)
   :outertype: ElementProperties

   Element that has at least one of the classes given

   :param cssClasses: - the class names to match to
   :return: a element property that can be applied with Path::that

hasAttribute
^^^^^^^^^^^^

.. java:method:: public static ElementProperty hasAttribute(String attribute, String value)
   :outertype: ElementProperties

hasChild
^^^^^^^^

.. java:method:: public static ElementProperty hasChild(Path... paths)
   :outertype: ElementProperties

   Element is the parent of the given list of elements

   :param paths: - a list of elements that are children of the current element
   :return: An element property that can be applied with Path::that

hasClass
^^^^^^^^

.. java:method:: public static ElementProperty hasClass(String className)
   :outertype: ElementProperties

   Has the class given in the parameter

   :param className: the class the element has
   :return: An element property that can be applied with Path::that

hasClassContaining
^^^^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty hasClassContaining(String classSubString)
   :outertype: ElementProperties

   Element that has a class with name that contain the given parameter

   :param classSubString: a string that should be contained in the class
   :return: An element property that can be applied with Path::that

hasClasses
^^^^^^^^^^

.. java:method:: public static ElementProperty hasClasses(String... cssClasses)
   :outertype: ElementProperties

   Element that has all of the given classes

   :param cssClasses: - the class names to match to
   :return: a element property that can be applied with Path::that

hasDescendant
^^^^^^^^^^^^^

.. java:method:: public static ElementProperty hasDescendant(Path... paths)
   :outertype: ElementProperties

   The given elements in the parameters list are contained in the current element

   :param paths: - a list of elements that are descendants of the current element
   :return: An element property that can be applied with Path::that

hasId
^^^^^

.. java:method:: public static ElementProperty hasId(String id)
   :outertype: ElementProperties

   Element has ID equals to the given parameter

   :param id: - the ID to match to
   :return: a element property that can be applied with Path::that

hasNChildren
^^^^^^^^^^^^

.. java:method:: public static ElementPropertyWithNumericalBoundaries hasNChildren(Integer n)
   :outertype: ElementProperties

   The element has n direct children

   :param n: the number of children
   :return: a element property that can be applied with Path::that

hasName
^^^^^^^

.. java:method:: public static ElementProperty hasName(String name)
   :outertype: ElementProperties

   Element with a "name" attribute equal to the given parameter. Useful for input elements.

   :param name: the value of the name property
   :return: a element property that can be applied with Path::that

hasNonOfTheClasses
^^^^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty hasNonOfTheClasses(String... cssClasses)
   :outertype: ElementProperties

   Element that has none of the given classes

   :param cssClasses: - a list of class names, none of which is present in element
   :return: An element property that can be applied with Path::that

hasParent
^^^^^^^^^

.. java:method:: public static ElementProperty hasParent(Path path)
   :outertype: ElementProperties

   Element is direct child of the element matched by the given parameter

   :param path: - the parent of the current element
   :return: An element property that can be applied with Path::that

hasRawXpathProperty
^^^^^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty hasRawXpathProperty(String rawXpathProps, String rawExplanation)
   :outertype: ElementProperties

   Custom property that allows to state the raw expath of a property, and give a string description of it. Example:

   .. parsed-literal::

      Path el = span.that(hasRawXpathProperty("string(.)='x'", "is awesome"), isOnlyChild);
       assertThat(el.getXPath().get(), equalTo("span[string(.)='x'][count(preceding-sibling::*)=0" +
                                                     "and count(following-sibling::*)=0]"));
       assertThat(el.toString(), is(equalTo("span, that is awesome, and is only child")));

   :param rawXpathProps: - the xpath property condition string. Will be wrapped with [] in the xpath
   :param rawExplanation: - a textual readable description of this property
   :return: a element property that can be applied with Path::that

hasRole
^^^^^^^

.. java:method:: public static ElementProperty hasRole(String role)
   :outertype: ElementProperties

   Element with a "role" attribute equal to the given role.

   :param role: the value of the role property
   :return: a element property that can be applied with Path::that

hasText
^^^^^^^

.. java:method:: public static ElementProperty hasText(String txt)
   :outertype: ElementProperties

   Element has text equals to the given string parameter, ignoring case.

   :param txt: - the text to match to
   :return: a element property that can be applied with Path::that

hasTextContaining
^^^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty hasTextContaining(String txt)
   :outertype: ElementProperties

   The text in the element contains the given parameter, ignoring case

   :param txt: - the substring to match to
   :return: An element property that can be applied with Path::that

hasTextEndingWith
^^^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty hasTextEndingWith(String txt)
   :outertype: ElementProperties

   Element has text that ends with the given parameter

   :param txt: - the text to match to
   :return: a element property that can be applied with Path::that

hasTextStartingWith
^^^^^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty hasTextStartingWith(String txt)
   :outertype: ElementProperties

   Element has text that starts with the given parameter

   :param txt: - the text to match to
   :return: a element property that can be applied with Path::that

isAfter
^^^^^^^

.. java:method:: public static ElementProperty isAfter(Path... paths)
   :outertype: ElementProperties

   Element appears after all the given parameters in the document

   :param paths: - elements that precede the current element
   :return: An element property that can be applied with Path::that

isAfter
^^^^^^^

.. java:method:: public static ElementProperty isAfter(NPath nPath)
   :outertype: ElementProperties

   Element is after at-least/at-most/exactly the given number of the given element. Example use: import static com.github.loyada.jdollarx.atLeast; input.that(isAfter(atLeast(2).occurrencesOf(div)));

   :param nPath: - at-least/at-most/exactly the given number of the given element
   :return: An element property that can be applied with Path::that

isAfterSibling
^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty isAfterSibling(Path... paths)
   :outertype: ElementProperties

   Element is a sibling of all the elements defined by the given paths, AND is after all those siblings

   :param paths: a list of paths referring to elements
   :return: An element property that can be applied with Path::that

isAfterSibling
^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty isAfterSibling(NPath nPath)
   :outertype: ElementProperties

   Element is a sibling of the at-least/at-most/exactly n elements given, and appears after them. Example:

   .. parsed-literal::

      Path el = element.that(isAfterSibling(exactly(2).occurrencesOf(div)));
      assertThat(el.toString(), is(equalTo("any element, that is after 2 siblings of type: div")));

   :param nPath: a count of elements that are siblings appearing before current elements.
   :return: An element property that can be applied with Path::that

isAncestorOf
^^^^^^^^^^^^

.. java:method:: public static ElementProperty isAncestorOf(Path... paths)
   :outertype: ElementProperties

   The given elements in the parameters list are contained in the current element

   :param paths: - a list of elements that are descendants of the current element
   :return: An element property that can be applied with Path::that

isBefore
^^^^^^^^

.. java:method:: public static ElementProperty isBefore(Path... paths)
   :outertype: ElementProperties

   Element is before all the elements given in the parameters

   :param paths: - all the elements that appear after the current element
   :return: An element property that can be applied with Path::that

isBefore
^^^^^^^^

.. java:method:: public static ElementProperty isBefore(NPath nPath)
   :outertype: ElementProperties

   Element is before at-least/at-most/exactly the given number of the given element. Example use:

   .. parsed-literal::

      import static com.github.loyada.jdollarx.isBefore;
      input.that(isBefore(atLeast(2).occurrencesOf(div)));

   :param nPath: - at-least/at-most/exactly the given number of the given element
   :return: An element property that can be applied with Path::that

isBeforeSibling
^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty isBeforeSibling(Path... paths)
   :outertype: ElementProperties

   Element is a sibling of all the elements defined by the given paths, AND is before all those siblings

   :param paths: a list of paths referring to elements
   :return: An element property that can be applied with Path::that

isBeforeSibling
^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty isBeforeSibling(NPath nPath)
   :outertype: ElementProperties

   Element is a sibling of the at-least/at-most/exactly n elements given, and appears before them. Example:

   .. parsed-literal::

      Path el = element.that(isBeforeSibling(exactly(2).occurrencesOf(div)));
      assertThat(el.toString(), is(equalTo("any element, that is before 2 siblings of type: div")));

   :param nPath: a count of elements that are siblings appearing after current elements.
   :return: An element property that can be applied with Path::that

isChildOf
^^^^^^^^^

.. java:method:: public static ElementProperty isChildOf(Path path)
   :outertype: ElementProperties

   Element is direct child of the element matched by the given parameter

   :param path: - the parent of the current element
   :return: An element property that can be applied with Path::that

isContainedIn
^^^^^^^^^^^^^

.. java:method:: public static ElementProperty isContainedIn(Path path)
   :outertype: ElementProperties

isDescendantOf
^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty isDescendantOf(Path path)
   :outertype: ElementProperties

   Element is inside the given parameter

   :param path: the ancestor of the current element
   :return: An element property that can be applied with Path::that

isInside
^^^^^^^^

.. java:method:: public static ElementProperty isInside(Path path)
   :outertype: ElementProperties

   Element is inside the given parameter

   :param path: the ancestor of the current element
   :return: An element property that can be applied with Path::that

isNthFromLastSibling
^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty isNthFromLastSibling(Integer reverseIndex)
   :outertype: ElementProperties

   The element is the nth-from-last sibling. Example usage: find the element before the last one in a list.

   :param reverseIndex: - the place from last, starting at 0 for the last sibling.
   :return: a element property that can be applied with Path::that

isNthSibling
^^^^^^^^^^^^

.. java:method:: public static ElementProperty isNthSibling(Integer index)
   :outertype: ElementProperties

   The element is the nth sibling. Example usage: find the 4th element in a list.

   :param index: - starting at 0 for the first one
   :return: a element property that can be applied with Path::that

isParentOf
^^^^^^^^^^

.. java:method:: public static ElementProperty isParentOf(Path... paths)
   :outertype: ElementProperties

   Element is the parent of the given list of elements

   :param paths: - a list of elements that are children of the current element
   :return: An element property that can be applied with Path::that

isSiblingOf
^^^^^^^^^^^

.. java:method:: public static ElementProperty isSiblingOf(Path... paths)
   :outertype: ElementProperties

   Element is a sibling of all the elements defined by the given paths

   :param paths: a list of paths referring to elements
   :return: An element property that can be applied with Path::that

isWithIndex
^^^^^^^^^^^

.. java:method:: public static ElementProperty isWithIndex(Integer index)
   :outertype: ElementProperties

   Element that is the nth sibling of its parent

   :param index: - the index of the element among its sibling, starting with 0
   :return: An element property that can be applied with Path::that

not
^^^

.. java:method:: public static ElementProperty not(ElementProperty prop)
   :outertype: ElementProperties

   Element does NOT have the given property.

   :param prop: - the property which the element must not have
   :return: An element property that can be applied with Path::that

withIndexInRange
^^^^^^^^^^^^^^^^

.. java:method:: public static ElementProperty withIndexInRange(int first, int last)
   :outertype: ElementProperties

   The index among its siblings is between first and last parameters. For example: taking a row from a table, which we know is between row number 2 and 4.

   :param first: - lower index (inclusive, starting at 0)
   :param last: - upper index (inclusive, starting at 0)
   :return: a element property that can be applied with Path::that

