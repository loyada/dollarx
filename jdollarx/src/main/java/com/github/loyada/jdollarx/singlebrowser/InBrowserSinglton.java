package com.github.loyada.jdollarx.singlebrowser;


import com.github.loyada.jdollarx.BasicPath;
import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class InBrowserSinglton {
    public static WebDriver driver;

    private static InBrowser getBrowser() { return new InBrowser(driver);}

    public static WebElement find(final Path el) {
        return getBrowser().find(el);
    }

    public static List<WebElement> findAll(final Path el) {
        return getBrowser().findAll(el);
    }

    /////////////// predicates
    public static Integer numberOfAppearances(final Path el) {
        return getBrowser().numberOfAppearances(el);
    }
    public static boolean isPresent(final Path el) {
        return getBrowser().isPresent(el);
    }

    public static boolean isEnabled(final Path el) {
        return getBrowser().isEnabled(el);
    }
    public static boolean isSelected(final Path el) {
        return getBrowser().isSelected(el);
    }
    public static boolean isDisplayed(final Path el) {
       return getBrowser().isDisplayed(el);
    }


    ////////////////////////////////////////////////////
    //// actions
    public static WebElement clickOn(final Path el) {
        return getBrowser().clickOn(el);
    }

    public static WebElement clickAt(final Path el) {
        return getBrowser().clickAt(el);
    }

    public static WebElement hoverOver(Path el) {
        return getBrowser().hoverOver(el);
    }

    public static WebElement scrollTo(final Path el) {
        return getBrowser().scrollTo(el);
    }

    public static void doubleClickOn(final Path el) {
        getBrowser().doubleClickOn(el);
    }

    public static Operations.KeysSender sendKeys(CharSequence... charsToSend) {
        return getBrowser().sendKeys(charsToSend);
    }

    public static Operations.KeysDown pressKeyDown(Keys thekey) {
        return getBrowser().pressKeyDown(thekey);
    }

    public static Operations.ReleaseKey releaseKey(Keys thekey) {
        return getBrowser().releaseKey(thekey);
    }

    public static Operations.DragAndDrop dragAndDrop(final BasicPath path) {
        return getBrowser().dragAndDrop(path);
    }
}
