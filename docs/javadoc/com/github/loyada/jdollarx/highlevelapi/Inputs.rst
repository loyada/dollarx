.. java:import:: com.github.loyada.jdollarx InBrowser

.. java:import:: com.github.loyada.jdollarx Operations.OperationFailedException

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: org.openqa.selenium Keys

Inputs
======

.. java:package:: com.github.loyada.jdollarx.highlevelapi
   :noindex:

.. java:type:: public final class Inputs

   High-level API to define and interact with various input elements. High level API's are not optimized. A definition of an element may interact with the browser to understand the structure of the DOM.

Methods
-------
changeInputValue
^^^^^^^^^^^^^^^^

.. java:method:: public static void changeInputValue(InBrowser browser, Path field, String text) throws OperationFailedException
   :outertype: Inputs

   Change input value: clear it and then enter another text in it

   :param browser: the browser
   :param field: Path to the input field
   :param text: the text to enter in the input field
   :throws OperationFailedException: failed to perform the operation

changeInputValueWithEnter
^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static void changeInputValueWithEnter(InBrowser browser, Path field, String text) throws OperationFailedException
   :outertype: Inputs

   Similar to changeInputValue, but adds an ENTER after setting the value of the input

   :param browser: the browser
   :param field: Path to the input field
   :param text: the text to enter in the input field
   :throws OperationFailedException: failed to perform the operation

checkboxType
^^^^^^^^^^^^

.. java:method:: public static Path checkboxType(Path inp)
   :outertype: Inputs

   Takes an input element and returns such an input of type checkbox.

   :param inp: the input element
   :return: a Path to the input

clearInput
^^^^^^^^^^

.. java:method:: public static void clearInput(InBrowser browser, Path field) throws OperationFailedException
   :outertype: Inputs

   Clear operation on an input element

   :param browser: the browser
   :param field: the input element

inputFollowedByUnlabeledText
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static Path inputFollowedByUnlabeledText(String text)
   :outertype: Inputs

   Input followed by text that does not have its on label element.

   :param text: the text following the input
   :return: a Path to the input element

inputForLabel
^^^^^^^^^^^^^

.. java:method:: public static Path inputForLabel(InBrowser browser, String labelText)
   :outertype: Inputs

   A lazy way to find an input based on the label. Mote that unlike It looks for a label element that has an ID. If it finds one, it returns a Path to an input with that ID. Otherwise it returns a Path to an input inside the label element.

   :param browser: the browser
   :param labelText: the label to look for
   :return: a Path to the input, on a best effort basis

radioType
^^^^^^^^^

.. java:method:: public static Path radioType(Path inp)
   :outertype: Inputs

   Takes an input element and returns such an input of type radio.

   :param inp: the input element
   :return: a Path to the input

selectInFieldWithLabel
^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static Path selectInFieldWithLabel(InBrowser browser, String labelText, String option)
   :outertype: Inputs

   Perform a selection of an option in a select element. It expects to find the label element with the given text before the select element

   :param browser: the browser
   :param labelText: The text of the select label
   :param option: The option text
   :return: the Path of the select element

