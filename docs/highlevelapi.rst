==============
High-Level API
==============

.. currentmodule:: dollarx

.. contents:: :local:

The purpose of the high level API package is to provide a simplified API to common interactions with elements of type
inputs, radio buttons, checkboxes, dropdowns etc.

The examples below are from the integrations tests of dollarx.

It is less flexible and sometimes suboptimal, but its value is a higher level abstraction.

For example:

    .. code-block:: java

        RadioInput radio = RadioInputs.withLabeledText("Option 123");
        radio.select();
        assertTrue(radio.isSelected());


The code above does not require the developer to know the exact organization of the DOM. It will find it by
itself (on a best effort basis), assuming there is a "label" element with the text "Option 123" that corrsponds
to the input

If you don't even know if therer is a "label" element for this input, but just know that there is some
text the correspond to this field, and you want to let dollarx figure out the right structure, you can do
it with the higher level function withTextUnknownDOM():

 .. code-block:: java

        RadioInput myInput = RadioInputs.withTextUnknownDOM("Female", 5, TimeUnit.SECONDS);
        myInput.select();
        assertTrue(myInput.isSelected());



Another example, for checking a checkbox:

    .. code-block:: java

        CheckBox checkbox =  CheckBoxes.checkBoxWithProperties(ElementProperties.isNthSibling(3));
        checkbox.check();;
        assertTrue(checkbox.isChecked());
        assertThat(checkbox.toString(), equalTo("checkbox, that is in place 3 among its siblings"));


Inputs
======
* \ :java:ref:`Inputs.inputForLabel`\ - input referenced by the label element with the given text
* \ :java:ref:`Inputs.inputFollowedByUnlabeledText`\ - it is followed by text, without a label element.
* \ :java:ref:`Inputs.genericFormInputAfterField`\ - a generic, reasonable, guess that works for many forms
* \ :java:ref:`Inputs.genericFormInputBeforeField`\ - a generic, reasonable, guess that works for many forms
* \ :java:ref:`Inputs.clearInput`\ - clear the input. If it is unsuccessful, it throws an exception.
* \ :java:ref:`Inputs.clearInputNonStrict`\ - clear the input as much as it can
* \ :java:ref:`Inputs.changeInputValue`\ - replace any existing value in the input with a new one
* \ :java:ref:`Inputs.selectInFieldWithLabel`\  - select an option in a select element
* \ :java:ref:`Inputs.changeInputValueWithEnter`\  - update the value and send ENTER



Checkboxes
==========
* \ :java:ref:`CheckBoxes.checkBoxWithLabel`\
* \ :java:ref:`CheckBoxes.checkBoxWithProperties`\ checkbox with a list of properties
* \ :java:ref:`CheckBox.isChecked`\  - is this checkbox checked?
* \ :java:ref:`CheckBox.check`\
* \ :java:ref:`CheckBox.uncheck`\



Radio Buttons
=============
* \ :java:ref:`RadioInputs.withTextUnknownDOM`\ - there is some text next to the input, but the DOM structure is unknown
* \ :java:ref:`RadioInputs.withLabeledText`\
* \ :java:ref:`RadioInputs.withUnlabeledText`\
* \ :java:ref:`RadioInputs.withProperties`\
* \ :java:ref:`RadioInput.isSelected`\
* \ :java:ref:`RadioInput.select`\
