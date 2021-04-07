package com.github.loyada.jdollarx.highlevelapi;

import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Operations.OperationFailedException;
import com.github.loyada.jdollarx.Path;
import org.openqa.selenium.Keys;

import static com.github.loyada.jdollarx.BasicPath.*;
import static com.github.loyada.jdollarx.ElementProperties.hasAggregatedTextEqualTo;
import static com.github.loyada.jdollarx.ElementProperties.hasId;
import static com.github.loyada.jdollarx.HighLevelPaths.hasType;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * High-level API to define and interact with various input elements.
 * High level API's are not optimized. A definition of an element may interact with the browser
 * to understand the structure of the DOM.
 */
public final class Inputs {
    private Inputs() {}

    public static int MAX_INPUT_SIZE = 20;

    /**
     * A lazy way to find an input based on the label. Mote that unlike
     * It looks for a label element that has an ID. If it finds one, it returns
     * a Path to an input with that ID. Otherwise it returns a Path to an  input
     * inside the label element.
     * @param browser the browser
     * @param labelText the label to look for
     * @return a Path to the input, on a best effort basis
     */
    public static Path inputForLabel(InBrowser browser, String labelText) {
        Path myLabel = label.that(hasAggregatedTextEqualTo(labelText.trim()));
        String theId = browser.find(myLabel).getAttribute("for");
        if (isNullOrEmpty(theId)) {
            return input.inside(myLabel);
        }
        return input.that(hasId(theId));
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
     * Takes an input element and returns such an input of type checkbox.
     * @param inp the input element
     * @return a Path to the input
     */
    public static Path checkboxType(Path inp) {
        return inp.that(hasType("checkbox"));
    }

    /**
     * Takes an input element and returns such an input of type radio.
     * @param inp the input element
     * @return a Path to the input
     */
    public static Path radioType(Path inp) {
        return inp.that(hasType("radio"));
    }

    /**
     * Clear operation on an input element
     * @param browser the browser
     * @param field the input element
     */
    public static void clearInput(InBrowser browser, Path field) throws OperationFailedException {
        browser.find(field).clear();
        for (int i=0; i<MAX_INPUT_SIZE; i++)
            browser.sendKeys(Keys.BACK_SPACE).to(field);
    }

    /**
     * Perform a selection of an option in a select element.
     * It expects to find the label element with the given text before the select element
     * @param browser the browser
     * @param labelText The text of the select label
     * @param option The option text
     * @return the Path of the select element
     */
    public static Path selectInFieldWithLabel(InBrowser browser, String labelText, String option) {
        Path selector = select.after(label.withText(labelText));
        browser.clickOn(selector);
        browser.getSelect(selector).selectByVisibleText(option);
        return selector;
    }

    /**
     * Change input value: clear it and then enter another text in it
     * @param browser the browser
     * @param field Path to the input field
     * @param text the text to enter in the input field
     * @throws OperationFailedException failed to perform the operation
     */
    public static void changeInputValue(InBrowser browser, Path field, String text)
            throws OperationFailedException {
        clearInput(browser, field);
        browser.sendKeys(text).to(field);
    }

    /**
     * Similar to changeInputValue, but adds an ENTER after setting the value of the input
     * @param browser the browser
     * @param field Path to the input field
     * @param text the text to enter in the input field
     * @throws OperationFailedException failed to perform the operation
     */
    public static void changeInputValueWithEnter(InBrowser browser, Path field, String text)
            throws OperationFailedException {
       changeInputValue(browser, field, text);
        browser.sendKeys(Keys.ENTER).to(field);
    }
}
