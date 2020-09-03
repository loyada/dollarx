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

public final class Inputs {
    private Inputs() {}

    public static Path inputForLabel(InBrowser browser, String labelText) {
        Path myLabel = label.that(hasAggregatedTextEqualTo(labelText.trim()));
        String theId = browser.find(myLabel).getAttribute("for");
        if (isNullOrEmpty(theId)) {
            return input.inside(myLabel);
        }
        return input.that(hasId(theId));
    }

    public static Path inputFollowedByUnlabeledText(String text) {
        return input.immediatelyBeforeSibling(textNode(text));
    }

    public static Path checkboxType(Path inp) {
        return inp.that(hasType("checkbox"));
    }

    public static Path radioType(Path inp) {
        return inp.that(hasType("radio"));
    }

    public static void clearInput(InBrowser browser, Path field) throws OperationFailedException {
        browser.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE)).to(field);
    }

    public static Path selectInFieldWithLabel(InBrowser browser, String labelText, String option) {
        Path selector = select.after(label.withText(labelText));
        browser.clickOn(selector);
        browser.getSelect(selector).selectByVisibleText(option);
        return selector;
    }

    public static void selectInFieldWithLabelWithEnter(InBrowser browser, String labelText, String option) throws OperationFailedException {
        Path selector = selectInFieldWithLabel(browser, labelText, option);
        browser.sendKeys(Keys.ENTER).to(selector);
    }

    public static void changeInputValue(InBrowser browser, Path field, String text) throws OperationFailedException {
        clearInput(browser, field);
        browser.sendKeys(text).to(field);
    }

    public static void changeInputValueWithEnter(InBrowser browser, Path field, String text) throws OperationFailedException {
       changeInputValue(browser, field, text);
        browser.sendKeys(Keys.ENTER).to(field);
    }

}
