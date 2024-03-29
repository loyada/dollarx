package com.github.loyada.jdollarx.highlevelapi;

import com.github.loyada.jdollarx.ElementProperty;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.Path;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.function.UnaryOperator;

import static com.github.loyada.jdollarx.BasicPath.input;
import static com.github.loyada.jdollarx.highlevelapi.Inputs.checkboxType;
import static com.github.loyada.jdollarx.highlevelapi.Inputs.inputForLabel;
import static java.lang.String.format;

/**
 * High-level wrapper to define and interact with a checkbox input.
 * High level API's are not optimized. A definition of an element may interact with the browser
 * to understand the structure of the DOM.
 */
public class CheckBox {
    private final String asString;
    private final Path checkbox;
    private final InBrowser browser;

    private final WebElement el;

    /**
     * input of type "checkbox" with a label element
     * @param labelText - the text in the label
     */
    public CheckBox(InBrowser browser, String labelText) {
        this.checkbox = checkboxType(inputForLabel(browser, labelText));
        this.asString = format("checkbox for '%s'", labelText);
        this.browser =browser;
        this.el =  browser.find(checkbox);
    }

    /**
     * input of type "checkbox"
     * @param inputEl - the input element which is the checkbox
     * @param name - An optional name for the checkbox. .
     */
    public CheckBox(InBrowser browser, Path inputEl, String name) {
        this.checkbox = checkboxType(inputEl);
        String label = name==null? inputEl.toString() : name;
        this.asString = format("checkbox for '%s'", label);
        this.browser =browser;
        this.el =  browser.find(checkbox);
    }

    /**
     * input of type "checkbox" with the given properties
     * @param props - properties of the checkbox
     */
    public CheckBox(InBrowser browser, ElementProperty... props) {
        this.checkbox = checkboxType(input.that(props));
        this.asString = input.describedBy("checkbox").that(props).toString();
        this.browser =browser;
        this.el =  browser.find(checkbox);
    }

    /**
     * Is it checked?
     * @return whether it is checked
     */
    public boolean isChecked() {
        return  this.el.isSelected();
    }

    /**
     * Check it
     */
    public void check() {
        Operations.doWithRetries(()->{
            if (!isChecked()) {
                browser.clickAt(checkbox);
            }
            if (!isChecked()) {
                throw new RuntimeException(String.format("failed to uncheck %s", this));
            }
        }, 5, 100);
    }

    /**
     * Unchecked it
     */
    public void uncheck() {
        Operations.doWithRetries(()->{
            if (isChecked()) {
                browser.clickAt(checkbox);
            }
            if (isChecked()) {
                throw new RuntimeException(String.format("failed to uncheck %s", this));
            }
        }, 5, 100);
    }

    private void click() {
        final Actions actionBuilder = new Actions(browser.getDriver());
        UnaryOperator<Actions> func = e -> e.moveToElement(this.el).click();
        func.apply(actionBuilder).build().perform();
    }
    /**
     * Check it - quicker implementation
     */
    public void quickCheck() {
        Operations.doWithRetries(()->{
            if (isChecked()) {
                click();
            }
        }, 5, 50);
    }

    /**
     * Unchecked it -  quicker implementation
     */
    public void quickUncheck() {
        Operations.doWithRetries(()->{
            if (isChecked()) {
                click();
            }
        }, 5, 50);
    }

    public Path getCheckbox() {
        return this.checkbox;
    }

    @Override
    public String toString() {
        return asString;
    }

}
