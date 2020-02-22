package com.github.loyada.jdollarx.highlevelapi;

import com.github.loyada.jdollarx.ElementProperty;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;

import static com.github.loyada.jdollarx.BasicPath.input;
import static com.github.loyada.jdollarx.highlevelapi.Inputs.checkboxType;
import static com.github.loyada.jdollarx.highlevelapi.Inputs.inputForLabel;
import static java.lang.String.format;

public class CheckBox {
    private final String asString;
    private Path checkbox;
    private final InBrowser browser;

    /**
     * input of type "checkbox" with a label
     * @param labelText - the text in the label
     */
    public CheckBox(InBrowser browser, String labelText) {
        this.checkbox = checkboxType(inputForLabel(browser, labelText));
        this.asString = format("checkbox for '%s'", labelText);
        this.browser =browser;
    }

    /**
     * input of type "checkbox" with the given properties
     * @param props
     */
    public CheckBox(InBrowser browser, ElementProperty... props) {
        this.checkbox = checkboxType(input.that(props));
        this.asString = input.describedBy("checkbox").that(props).toString();
        this.browser =browser;
    }

    /**
     * Is it checked?
     * @return whether it is checked
     */
    public boolean isChecked() {
        return browser.find(checkbox).isSelected();
    }

    /**
     * Check it
     */
    public void check() {
        if (!isChecked()) {
            browser.clickAt(checkbox);
        }
    }

    /**
     * Unchecked it
     */
    public void uncheck() {
        if (isChecked()) {
            browser.clickAt(checkbox);
        }
    }

    @Override
    public String toString() {
        return asString;
    }

}
