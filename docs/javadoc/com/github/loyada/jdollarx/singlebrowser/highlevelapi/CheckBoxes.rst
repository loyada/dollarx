.. java:import:: com.github.loyada.jdollarx ElementProperty

.. java:import:: com.github.loyada.jdollarx InBrowser

.. java:import:: com.github.loyada.jdollarx.highlevelapi CheckBox

CheckBoxes
==========

.. java:package:: com.github.loyada.jdollarx.singlebrowser.highlevelapi
   :noindex:

.. java:type:: public final class CheckBoxes

   High-level wrapper to define a checkbox input. High level API's are not optimized. A definition of an element may interact with the browser to understand the structure of the DOM.

Methods
-------
checkBoxWithLabel
^^^^^^^^^^^^^^^^^

.. java:method:: public static CheckBox checkBoxWithLabel(String labelText)
   :outertype: CheckBoxes

   input of type "checkbox" with a label

   :param labelText: - the text in the label
   :return: a high level instance of CheckBox

checkBoxWithProperties
^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static CheckBox checkBoxWithProperties(ElementProperty... props)
   :outertype: CheckBoxes

   input of type "checkbox" with the given properties

   :param props: properties of the checkbox
   :return: a high level instance of CheckBox

