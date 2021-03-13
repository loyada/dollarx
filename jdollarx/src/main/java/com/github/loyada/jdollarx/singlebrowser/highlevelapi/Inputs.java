package com.github.loyada.jdollarx.singlebrowser.highlevelapi;

import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.Path;

import static com.github.loyada.jdollarx.BasicPath.input;
import static com.github.loyada.jdollarx.BasicPath.textNode;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;

/**
 * High-level API to define and interact with various input elements.
 * High level API's are not optimized. A definition of an element may interact with the browser
 * to understand the structure of the DOM.
 */
public final class Inputs {
    private Inputs() {}

    /**
     * A lazy way to find an input based on the label. Mote that unlike
     * It looks for a label element that has an ID. If it finds one, it returns
     * a Path to an input with that ID. Otherwise it returns a Path to an  input
     * inside the label element.
     * @param labelText the label to look for
     * @return a Path to the input, on a best effort basis
     */
    public static Path inputForLabel(String labelText) {
        return com.github.loyada.jdollarx.highlevelapi.Inputs.inputForLabel(new InBrowser(driver), labelText);
    }

    /**
     * Input followed by text that does not have its on label element.
     * @param text the text following the input
     * @return a Path to the input element
     */
    public static Path inputFollowedByUnlabeledText(String text) {
        return input.immediatelyBeforeSibling(textNode(text));
    }

    /**
     * Clear operation on an input element
     * @param field the input element
     */
    public static void clearInput(Path field) throws Operations.OperationFailedException {
        com.github.loyada.jdollarx.highlevelapi.Inputs.clearInput(new InBrowser(driver), field);
    }

    /**
     * Perform a selection of an option in a select element.
     * It expects to find the label element with the given text before the select element
     * @param labelText The text of the select label
     * @param option The option text
     */
    public static void selectInFieldWithLabel(String labelText, String option) {
        com.github.loyada.jdollarx.highlevelapi.Inputs.selectInFieldWithLabel(
                new InBrowser(driver), labelText, option);
    }

    /**
     * Change input value: clear it and then enter another text in it
     * @param field Path to the input field
     * @param text the text to enter in the input field
     * @throws Operations.OperationFailedException failed to perform the operation
     */
    public static void changeInputValue(Path field, String text) throws Operations.OperationFailedException {
        com.github.loyada.jdollarx.highlevelapi.Inputs.changeInputValue(
                new InBrowser(driver), field, text);
    }

    /**
     * Similar to changeInputValue, but adds an ENTER after setting the value of the input
     * @param field Path to the input field
     * @param text the text to enter in the input field
     * @throws Operations.OperationFailedException failed to perform the operation
     */
    public static void changeInputValueWithEnter(Path field, String text) throws Operations.OperationFailedException {
        com.github.loyada.jdollarx.highlevelapi.Inputs.changeInputValueWithEnter(
                new InBrowser(driver), field, text);
    }


    }
