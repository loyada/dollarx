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
**Note that  \ :java:ref:`Path`\ is always immutable. Any function on it returns a new instance.**

The standard implementation is  \ :java:ref:`BasicPath`\ .
It includes predefined elements and allows to create new ones.

Suppose you have a \ :java:ref:`Path`\   el. You can declare another \ :java:ref:`Path`\   based on it by applying the following:

* Add a property, using a \ :java:ref:`Path.that`\ or \ :java:ref:`Path.and`\  clause. Simple example:

   .. code-block:: java

      el.that(hasClass("abc"));

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


There are also easy ways to extend DollarX to support new properties. See the Recipes section for detail.


Single Browser Instance  Paths
==============================
Besides \ :java:ref:`BasicPath`\  , there is another implementation of Path, specifically for the case there is a single
instance of browser we connect to. It add some actions in the browsers that can be used in an OOP way, such as el.click().

This class is \ :java:ref:`SingleBrowserPath`\  .









