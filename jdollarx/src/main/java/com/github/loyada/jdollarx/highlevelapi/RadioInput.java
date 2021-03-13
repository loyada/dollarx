package com.github.loyada.jdollarx.highlevelapi;

import com.github.loyada.jdollarx.ElementProperty;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;
import org.openqa.selenium.NoSuchElementException;

import java.util.concurrent.TimeUnit;

import static com.github.loyada.jdollarx.BasicPath.input;
import static com.github.loyada.jdollarx.highlevelapi.Inputs.*;

/**
 * High-level API to define and interact with.
 * High level API's are not optimized. A definition of an element may interact with the browser
 * to understand the structure of the DOM.
 */
public class RadioInput{
    private final String asString;
    private Path radio;
    private final InBrowser browser;

    /**
     * a radio button input with the given path. The given path is not validated.
     * @param thePath the Path of the radio button
     */
    public RadioInput(InBrowser browser, Path thePath) {
        this.radio = thePath;
        this.asString = "radio button with definition of " + thePath;
        this.browser =browser;
    }

    /**
     * a radio input with some properties
     * @param props the properties of the radio button
     */
    public RadioInput(InBrowser browser, ElementProperty... props) {
        this.radio = radioType(input).that(props);
        this.asString = input.describedBy("radio button").that(props).toString();
        this.browser =browser;
    }

    /**
     * In case the organization of the DOM is unclear, it will try both labeled input and unlabeled input.
     * When doing so, it will change the implicit wait temporarily to a small value, and then revert  the
     * implicit timeout to the values provided.
     * Use this only if you are not sure about the structure of the DOM.
     * @param text - the text following the radio button
     * @param originalImplicitWait - the current implicit wait
     * @param timeUnit - the current time unit of the implicit wait
     * @return a RadioInput instance
     */
    public static RadioInput withTextUnknownDOM(InBrowser browser, String text, int originalImplicitWait, TimeUnit timeUnit) {
        browser.getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS );
        try {
           Path labeledInput =  inputForLabel(browser, text);
           return new RadioInput(browser, radioType(labeledInput.or(inputFollowedByUnlabeledText(text))));
        } catch (NoSuchElementException e) {
            return withUnlabeledText(browser, text);
        } finally {
            browser.getDriver().manage().timeouts().implicitlyWait(originalImplicitWait, timeUnit);
        }
    }

    /**
     * create and return a RadioInput, that has a "label" element with the given text.
     * Note that this is not a pure declaration and it looks for the label in the browser.
     * @param labelText - the text in the label
     * @return - a RadioInput instance
     */
    public static RadioInput withLabeledText(InBrowser browser, String labelText) {
        return new RadioInput(browser, radioType(inputForLabel(browser, labelText)));
    }

    /**
     * create and return a RadioInput, that has straight text after it (not in a "label" element).
     * i.e.:
     * <pre>
     *       <input type="radio" name="gender" value="male" checked> Male<br>
     *       <input type="radio" name="gender" value="female"> Female<br>
     * </pre>
     * @param text - the text following the radio button
     * @return - a RadioInput instance
     */
    public static RadioInput withUnlabeledText(InBrowser browser, String text) {
        return new RadioInput(browser, radioType(inputFollowedByUnlabeledText(text)));
    }

    /**
     * is it currently selected?
     * @return whether this radio button is selected
     */
    public boolean isSelected() {
        return browser.find(radio).isSelected();
    }

    /**
     * Ensure it is selected
     */
    public void select() {
        if (!isSelected()) {
            browser.clickAt(radio);
        }
    }

    @Override
    public String toString() {
        return asString;
    }
}
