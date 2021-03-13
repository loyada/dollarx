package com.github.loyada.jdollarx.singlebrowser.highlevelapi;

import com.github.loyada.jdollarx.ElementProperty;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.highlevelapi.RadioInput;

import java.util.concurrent.TimeUnit;

import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;

/**
 * High-level API to define a with high level instance of radio input
 * High level API's are not optimized. A definition of an element may interact with the browser
 * to understand the structure of the DOM.
 */
public final class RadioInputs {
    private RadioInputs(){}

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
    public static RadioInput withTextUnknownDOM(String text, int originalImplicitWait, TimeUnit timeUnit) {
       return RadioInput.withTextUnknownDOM(new InBrowser(driver), text, originalImplicitWait, timeUnit);
    }

    /**
     * create and return a RadioInput, that has a "label" element with the given text.
     * Note that this is not a pure declaration and it looks for the label in the browser.
     * @param labelText - the text in the label
     * @return - a RadioInput instance
     */
    public static RadioInput withLabeledText(String labelText) {
        return RadioInput.withLabeledText(new InBrowser(driver), labelText);
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
    public static RadioInput withUnlabeledText(String text) {
        return RadioInput.withUnlabeledText(new InBrowser(driver), text);
    }



    /**
     * return a radio button with some custom properties
     * @param props - some custom properties for the radio button
     * @return a radio input with some custom properties
     */
    public static RadioInput withProperties(ElementProperty... props){
        return new RadioInput(new InBrowser(driver), props);
    }
}
