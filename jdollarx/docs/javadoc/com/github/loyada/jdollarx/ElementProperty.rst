ElementProperty
===============

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public interface ElementProperty

   The main interface to add an additional constraint on a \ :java:ref:`Path`\ . Used with the method \ :java:ref:`Path.that`\  , and \ :java:ref:`Path.and`\ .

Methods
-------
and
^^^

.. java:method::  ElementProperty and(ElementProperty prop)
   :outertype: ElementProperty

   returns a new property, that is a combination of the current property AND the given property parameter. Meaning the element is required to have both properties. Obviously, this can be used multiple times: prop1.and(prop2).or(prop3).and(prop4)

   :param prop: another property to perform a logical "AND" with
   :return: a new property that is equivalent to: (this property AND prop)

andNot
^^^^^^

.. java:method::  ElementProperty andNot(ElementProperty prop)
   :outertype: ElementProperty

   returns a new property, that is equivalent to the current property, BUT NOT the property parameter. Obviously, this can be used multiple times: prop1.andNot(prop2).or(prop3.andNot(prop4))

   :param prop: another property to perform a logical "NAND" with
   :return: a new property that is equivalent to: (this property and not prop)

or
^^

.. java:method::  ElementProperty or(ElementProperty prop)
   :outertype: ElementProperty

   returns a new property, that is a combination of the current property OR the given property parameter. Meaning the element is required to have any of the two properties. Obviously, this can be used multiple times: prop1.or(prop2).or(prop3).and(prop4)

   :param prop: another property to perform a logical "OR" with
   :return: a new property that is equivalent to: (this property OR prop)

toXpath
^^^^^^^

.. java:method::  String toXpath()
   :outertype: ElementProperty

