.. java:import:: com.github.loyada.jdollarx ElementProperty

.. java:import:: com.github.loyada.jdollarx InBrowser

.. java:import:: com.github.loyada.jdollarx Path

CheckBox
========

.. java:package:: com.github.loyada.jdollarx.highlevelapi
   :noindex:

.. java:type:: public class CheckBox

   High-level wrapper to define and interact with a checkbox input. High level API's are not optimized. A definition of an element may interact with the browser to understand the structure of the DOM.

Constructors
------------
CheckBox
^^^^^^^^

.. java:constructor:: public CheckBox(InBrowser browser, String labelText)
   :outertype: CheckBox

   input of type "checkbox" with a label

   :param labelText: - the text in the label

CheckBox
^^^^^^^^

.. java:constructor:: public CheckBox(InBrowser browser, ElementProperty... props)
   :outertype: CheckBox

   input of type "checkbox" with the given properties

   :param props: - properties of the checkbox

Methods
-------
check
^^^^^

.. java:method:: public void check()
   :outertype: CheckBox

   Check it

isChecked
^^^^^^^^^

.. java:method:: public boolean isChecked()
   :outertype: CheckBox

   Is it checked?

   :return: whether it is checked

toString
^^^^^^^^

.. java:method:: @Override public String toString()
   :outertype: CheckBox

uncheck
^^^^^^^

.. java:method:: public void uncheck()
   :outertype: CheckBox

   Unchecked it

