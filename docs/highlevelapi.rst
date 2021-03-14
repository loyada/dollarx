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
* \ :java:ref:`Inputs.inputFollowedByUnlabeledText`\
* \ :java:ref:`Inputs.clearInput`\
* \ :java:ref:`Inputs.changeInputValue`\
* \ :java:ref:`Inputs.selectInFieldWithLabel`\
* \ :java:ref:`Inputs.changeInputValueWithEnter`\



Checkboxes
==========

* \ :java:ref:`CheckBoxes.checkBoxWithLabel`\
* \ :java:ref:`CheckBoxes.checkBoxWithProperties`\
* \ :java:ref:`CheckBox.isChecked`\
* \ :java:ref:`CheckBox.check`\
* \ :java:ref:`CheckBox.uncheck`\



Radio Buttons
=============
* \ :java:ref:`RadioInputs.withTextUnknownDOM`\
* \ :java:ref:`RadioInputs.withLabeledText`\
* \ :java:ref:`RadioInputs.withUnlabeledText`\
* \ :java:ref:`RadioInputs.withProperties`\
* \ :java:ref:`RadioInput.isSelected`\
* \ :java:ref:`RadioInput.select`\
