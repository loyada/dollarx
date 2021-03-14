.. java:import:: com.github.loyada.jdollarx ElementProperty

.. java:import:: com.github.loyada.jdollarx InBrowser

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: java.util.concurrent TimeUnit

RadioInput
==========

.. java:package:: com.github.loyada.jdollarx.highlevelapi
   :noindex:

.. java:type:: public class RadioInput

   High-level API to define and interact with. High level API's are not optimized. A definition of an element may interact with the browser to understand the structure of the DOM.

Constructors
------------
RadioInput
^^^^^^^^^^

.. java:constructor:: public RadioInput(InBrowser browser, Path thePath)
   :outertype: RadioInput

   a radio button input with the given path. The given path is not validated.

   :param thePath: the Path of the radio button

RadioInput
^^^^^^^^^^

.. java:constructor:: public RadioInput(InBrowser browser, ElementProperty... props)
   :outertype: RadioInput

   a radio input with some properties

   :param props: the properties of the radio button

Methods
-------
isSelected
^^^^^^^^^^

.. java:method:: public boolean isSelected()
   :outertype: RadioInput

   is it currently selected?

   :return: whether this radio button is selected

select
^^^^^^

.. java:method:: public void select()
   :outertype: RadioInput

   Ensure it is selected

toString
^^^^^^^^

.. java:method:: @Override public String toString()
   :outertype: RadioInput

withLabeledText
^^^^^^^^^^^^^^^

.. java:method:: public static RadioInput withLabeledText(InBrowser browser, String labelText)
   :outertype: RadioInput

   create and return a RadioInput, that has a "label" element with the given text. Note that this is not a pure declaration and it looks for the label in the browser.

   :param labelText: - the text in the label
   :return: - a RadioInput instance

withTextUnknownDOM
^^^^^^^^^^^^^^^^^^

.. java:method:: public static RadioInput withTextUnknownDOM(InBrowser browser, String text, int originalImplicitWait, TimeUnit timeUnit)
   :outertype: RadioInput

   In case the organization of the DOM is unclear, it will try both labeled input and unlabeled input. When doing so, it will change the implicit wait temporarily to a small value, and then revert the implicit timeout to the values provided. Use this only if you are not sure about the structure of the DOM.

   :param text: - the text following the radio button
   :param originalImplicitWait: - the current implicit wait
   :param timeUnit: - the current time unit of the implicit wait
   :return: a RadioInput instance

withUnlabeledText
^^^^^^^^^^^^^^^^^

.. java:method:: public static RadioInput withUnlabeledText(InBrowser browser, String text)
   :outertype: RadioInput

   create and return a RadioInput, that has straight text after it (not in a "label" element). i.e.:

   .. parsed-literal::

      Male
      Female

   :param text: - the text following the radio button
   :return: - a RadioInput instance

