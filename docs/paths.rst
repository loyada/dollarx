=====
Paths
=====

.. currentmodule:: dollarx

.. contents:: :local:

Introduction
============
\ :java:ref:`Path`\  is how an DOM element/elements is defined in DollarX.
It allows to build an arbitrarily complex path to an element, using xpath internally.

**Why xpath?**

Xpath is significantly more expressive than CSS. CSS is especially limited when trying to describe relationships between elements.
Even something as simple a div that contains a span can't be expressed in CSS.

**Why not use raw xpath?**

Raw xpath is difficult to create, understand, troubleshoot and maintain. Although it is expressive, it is quite difficult to work with.


Building Blocks
===============
**Note that a path instance is immutable. Any function on it creates and returns a new instance.**

The standard implementation is  \ :java:ref:`BasicPath`\ .
It includes predefined elements and allows to create new ones.

Suppose you have a \ :java:ref:`Path`\   el. You can declare another \ :java:ref:`Path`\   based on it by applying the following:

* Add a property, using a \ :java:ref:`Path.that`\ or \ :java:ref:`Path.and`\  clause. Simple examples:

   .. code-block:: java

      el.that(hasClass("abc"));
      el.that(hasClass("abc"), hasText("John"));
      el.that(hasClass("abc").and(hasText("John");
      el.that(hasClass("abc").or(hasText("John"));
      el.that(not(hasText("John"));



  Some common properties can be used directly, without a :java:ref:`Path.that`\  clause. For example:

  .. code-block:: java

      el.withClass("abc").withText("joe");

* Describe which occurrence of that element it is, or its index among its siblings. For example:

  .. code-block:: java

      firstOccurrenceOf(el.withClass("abc").withText("joe"));
      lastOccurrenceOf(el.withClass("abc").withText("joe"));

      occurrenceNumber(4).of(el.withText("joe"));

      childNumber(4).ofType(div.withClass("foo"));

* Describe logical operation with another Path element. For example

   .. code-block:: java

      el1.or(el2);  //  matches both

      PathOperators.not(el); // mathes any element except for el

* Add a relation to another path. Some examples:

   .. code-block:: java

      el.inside(div);
      div.contains(el);

      el2.after(el1);

      el2.beforeSibling(el1);


An \ :java:ref:`ElementProperty`\  used in a \ :java:ref:`Path.that`\  clause can be elaborate. It can be:
#.  a simple property, for example:

  .. code-block:: java

    el.that(hasClass("abc"));

    el.that(hasTextContaining("joe"));

    el.that(hasId("123")).and(hasText("foo"));

  A \ :java:ref:`Path.that`\  clause supports multiple properties, as well as logical operations. For example:

  .. code-block:: java

    el.that(hasClass("abc), hasId("123"));

    el.that(hasClass("abc).and(hasId("123"));

    el.that(hasClass("abc").or(hasClass("123"));


  The list of supported properties is long. Take a look at \ :java:ref:`ElementProperties`\  for details.

* A relation to another element. Examples:

   .. code-block:: java

      el.that(isInside(otherEl));


The list of supported properties of Paths is long. Please refer to the javadoc of \ :java:ref:`ElementProperties`\  and
\ :java:ref:`BasicPath`\  .


There are also easy ways to extend DollarX to support new properties. See the :ref:`recipes <recipes>` section for detail.


Single Browser Instance  Paths
==============================
Besides \ :java:ref:`BasicPath`\  , there is another implementation of Path, specifically for the case there is a single
instance of browser we connect to. It add some actions in the browsers that can be used in an OOP way, such as el.click().

This class is \ :java:ref:`SingleBrowserPath`\  .


String representation of Paths
==============================
One of the useful features in DollarX is the representation of \ :java:ref:`BasicPath`\ as string. It is clear, and in many \
cases English-like representation. This makes troubleshooting/debugging easier.
For examples, look at the :ref:`recipes <recipes>` .

The describedBy function
------------------------
When creating a path the relies on the definitions of other path, the description as strings can be complicated.

\ :java:ref:`Path.describedBy`\  allows to provide an alias description, which can be useful to simplify it.

For example:

.. code-block:: java

    Path thePasswordInput = input.inside(div.afterSibling(label.withText("password")).describedBy("the password input");
     System.out.println(thePasswordInput);
    // "the password input"

    Path contactsTable = div.withClasses("ag-table", "contacts");
    Path row = div.withClass("ag-row");

    Path contact = row.inside(table).describedBy("contact");

    System.out.println(contact.that(hasAggregatedTextContaining("john smith")));
    // output: contact, with aggregated text containing "john smith"

This is useful when an exception is thrown or when you have assertions failures.


Predefined elements
===================
Under \ :java:ref:`BasicPath`\ , there are many element types that are defined and can be statically imported.
See the JavaDoc of BasicPath.
If you need to create a new type of element, look at the :ref:`recipes <recipes>`.



Relations to other elements
===========================
The following is a list of supported Path element properties that related to other elements.
In \ :java:ref:`ElementProperties`\ (see JavaDoc for details):

* hasChildren - has at least one child
* hasNoChildren
* isOnlyChild
* hasChild, isParentOf -  the element is the direct parent of another element(s). The methods are equivalent.
* isChildOf, hasParent - the opposite of hasChild, isParentOf
* contains, hasDescendant - contain one or more elements
* hasAncestor, isContainedIn - is contained within another element
* isAfter. is after another elements in the DOM. 2 flavors:
      * Accept one or more elements
      * With a limit on the count of the elements. Such as: isAfter(exactly(n).occurrencesOf(div)) .
        The limit can be: exactly, atMost, atLeast .
* isBefore - the opposite of isAfter
* isSiblingOf. 2 flavors:
    * Accept one or more elements
    * With a limit on the count of the elements. Such as: isAfterSibling(atLeast(2).occurrencesOf(div))
* isAfterSibling - 2 versions, as in isSiblingOf
* isBeforeSibling - 2 versions, as in isSiblingOf
* isWithIndex, isNthSibling - states the index of the element among its siblings. 0 is first.
* withIndexInRange - similar to isWithIndex, but allows to provide a range
* isLastSibling
* isNthFromLastSibling - states the place of the element from its last sibling. 0 is last.

In addition, the following relation properties are in In \ :java:ref:`BasicPath`\ :

* \ :java:ref:`BasicPath.childOf`\  - similar to In \ :java:ref:`ElementProperties.isChildOf`\
* \ :java:ref:`BasicPath.parentOf`\  - the opposite of \ :java:ref:`BasicPath.childOf`\
* \ :java:ref:`BasicPath.contains`\ , \ :java:ref:`BasicPath.ancestorOf`\
* \ :java:ref:`BasicPath.inside`\ , \ :java:ref:`BasicPath.descendantOf`\   - the opposite of \ :java:ref:`BasicPath.contains`\
* \ :java:ref:`BasicPath.childNumber`\  - similar to \ :java:ref:`ElementProperties.isNthSibling`\, but first is 1.
  For example: childNumber(4).ofType(div.withClass("foo"))
* \ :java:ref:`BasicPath.occurrenceNumber`\  - the global occurrence of a given path in the DOM, starting with 1
  for example: occurrenceNumber(3).of(listItem)
* \ :java:ref:`BasicPath.withGlobalIndex`\  -similar to \ :java:ref:`BasicPath.occurrenceNumber`\, but a different syntax, and first is 0.
  el.withGlobalIndex(n) is an alias for occurrenceNumber(n + 1).of(el)
* \ :java:ref:`BasicPath.firstOccurrenceOf`\  - first occurrence of this element in the DOM
* \ :java:ref:`BasicPath.lastOccurrenceOf`\  - last occurrence of this element in the DOM



Common Properties
=================
See the :ref:`recipes <recipes>` section for more detail.

Properties related to CSS classes under \ :java:ref:`ElementProperties`\ :

* hasClass, hasClasses
* hasClassContaining
* hasNonOfTheClasses
* hasAnyOfClasses


Properties related to text under \ :java:ref:`ElementProperties`\ :

* hasSomeText
* hasText
* hasTextContaining
* hasTextEndingWith
* hasTextStartingWith
* hasAggregatedTextEqualTo
* hasAggregatedTextContaining
* hasAggregatedTextStartingWith
* hasAggregatedTextEndingWith

Logical operations on properties:

* \ :java:ref:`ElementProperty.and`\
* \ :java:ref:`ElementProperty.andNot`\
* \ :java:ref:`ElementProperty.or`\
* \ :java:ref:`ElementProperties.not`\










