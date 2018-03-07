.. java:import:: com.google.common.collect ImmutableList

.. java:import:: org.openqa.selenium WebElement

.. java:import:: java.util Arrays

.. java:import:: java.util Collections

.. java:import:: java.util List

.. java:import:: java.util Optional

.. java:import:: java.util.stream Collectors

.. java:import:: java.util.stream Stream

BasicPath.ChildNumber
=====================

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public static final class ChildNumber
   :outertype: BasicPath

   Allows to define an element that has a predefined number of similar preceding siblings. Count starts at 1 (same as you would use in English). Example:

   .. parsed-literal::

      ChildNumber(5).ofType(div);

Constructors
------------
ChildNumber
^^^^^^^^^^^

.. java:constructor:: public ChildNumber(Integer n)
   :outertype: BasicPath.ChildNumber

   Does not return any usable Path by itself. Must be used like: \ ``ChildNumber(5).ofType(div)``\

   :param n: the number of child. Count starts at 1.

Methods
-------
ofType
^^^^^^

.. java:method:: public Path ofType(Path path)
   :outertype: BasicPath.ChildNumber

   an element that has n similar preceding siblings. For example: \ ``ChildNumber(5).ofType(element.withText("john"))``\  will correspond to the fifth child that has text "john"

   :param path: the element to find
   :return: a new Path instance

