package com.github.loyada.jdollarx.singlebrowser.highlevelapi;

import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.Path;

import static com.github.loyada.jdollarx.BasicPath.input;
import static com.github.loyada.jdollarx.BasicPath.textNode;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;

/**
 * High level API for interactions with input fields
 */
public final class Inputs {
    private Inputs() {}

    public static Path inputForLabel(String labelText) {
        return com.github.loyada.jdollarx.highlevelapi.Inputs.inputForLabel(new InBrowser(driver), labelText);
    }

    public static Path inputFollowedByUnlabeledText(String text) {
        return input.immediatelyBeforeSibling(textNode(text));
    }

    public static void clearInput(Path field) throws Operations.OperationFailedException {
        com.github.loyada.jdollarx.highlevelapi.Inputs.clearInput(new InBrowser(driver), field);
    }

    public static void selectInFieldWithLabel(String labelText, String option) {
        com.github.loyada.jdollarx.highlevelapi.Inputs.selectInFieldWithLabel(
                new InBrowser(driver), labelText, option);
    }

    public static void selectInFieldWithLabelWithEnter(String labelText, String option) throws Operations.OperationFailedException {
        com.github.loyada.jdollarx.highlevelapi.Inputs.selectInFieldWithLabelWithEnter(
                new InBrowser(driver), labelText, option);
    }

    public static void changeInputValue(InBrowser browser, Path field, String text) throws Operations.OperationFailedException {
        com.github.loyada.jdollarx.highlevelapi.Inputs.changeInputValue(
                new InBrowser(driver), field, text);
    }

    public static void changeInputValueWithEnter(Path field, String text) throws Operations.OperationFailedException {
        com.github.loyada.jdollarx.highlevelapi.Inputs.changeInputValueWithEnter(
                new InBrowser(driver), field, text);
    }


    }
