package com.github.loyada.jdollarx.singlebrowser;


import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public final class InBrowserSinglton {
    public static WebDriver driver;

    /**
     * Convert from a InBrowserSinglton to an non-singleton instance of InBrowser
     * @return a new instance of InBrowser
     */
    private static InBrowser getBrowser() { return new InBrowser(driver);}

    /**
     * Equivalent to WebDriver.findElement(). If the Path contains a WebElement than it will look for an element inside that WebElement.
     * Otherwise it looks starting at the top level.
     * It also alters the xpath if needed to search from top level correctly.
     * @param el a Path instance
     * @return returns a WebElement or throws an ElementNotFoundException
     */
    public static WebElement find(final Path el) {
        return getBrowser().find(el);
    }

    /**
     * Equivalent to WebDriver.findElements(). If the Path contains a WebElement than it will look for an element inside that WebElement.
     * Otherwise it looks starting at the top level.
     * It also alters the xpath if needed to search from top level correctly.
     * @param el a Path instance
     * @return a list of WebElements.
     */
    public static List<WebElement> findAll(final Path el) {
        return getBrowser().findAll(el);
    }

    /////////////// predicates

    /**
     * Typically should not be used directly. There are usually better options.
     * @param el a Path instance
     * @return tbe number of appearances of an element.
     */
    public static Integer numberOfAppearances(final Path el) {
        return getBrowser().numberOfAppearances(el);
    }
    /**
     * @param el a Path instance
     * @return true if the element is present.
     */
    public static boolean isPresent(final Path el) {
        return getBrowser().isPresent(el);
    }

    /**
     * Relies on Selenium WebElement::isEnabled, thus non-atomic.
     * @param el the path of the element to find
     * @return true if the element is present and enabled
     */
    public static boolean isEnabled(final Path el) {
        return getBrowser().isEnabled(el);
    }

    /**
     * Relies on Selenium WebElement::isSelected, thus non-atomic.
     * @param el the path of the element to find
     * @return true if the element is present and selected
     */
    public static boolean isSelected(final Path el) {
        return getBrowser().isSelected(el);
    }

    /**
     * Relies on Selenium WebElement::isDisplayed, thus non-atomic.
     * @param el the path of the element to find
     * @return true if the element is present and displayed
     */
    public static boolean isDisplayed(final Path el) {
       return getBrowser().isDisplayed(el);
    }


    ////////////////////////////////////////////////////
    //// actions

    /**
     * Click on the element that corresponds to the given path. Requires the element to be clickable.
     * @param el a Path instance
     * @return the WebElement clicked on
     */
    public static WebElement clickOn(final Path el) {
        return getBrowser().clickOn(el);
    }

    /**
     * Click on the location of the element that corresponds to the given path.
     * @param el a Path instance
     * @return the WebElement clicked at
     */
    public static WebElement clickAt(final Path el) {
        return getBrowser().clickAt(el);
    }

    /**
     * Hover over on the location of the element that corresponds to the given path.
     * @param el a Path instance
     * @return the WebElement found
     */
    public static WebElement hoverOver(Path el) {
        return getBrowser().hoverOver(el);
    }

    /**
     * scroll to the location of the element that corresponds to the given path.
     * @param el a Path instance
     * @return the WebElement found
     */
    public static WebElement scrollTo(final Path el) {
        return getBrowser().scrollTo(el);
    }

    /**
     * Doubleclick on the element that corresponds to the given path. Requires the element to be clickable.
     * @param el a Path instance
     */
    public static void doubleClickOn(final Path el) {
        getBrowser().doubleClickOn(el);
    }

    /**
     * send keys to the browser, or to a specific element.
     * Two flavors of use:
     * sendKeys("abc").toBrowser();
     * sendKeys("abc").to(path);
     *
     * @param charsToSend  the keys to send. Can be "abc", or "a", "b", "c"
     * @return a KeySender instance that allows to send to the browser in general or to a specific element in the DOM
     */
    public static Operations.KeysSender sendKeys(CharSequence... charsToSend) {
        return getBrowser().sendKeys(charsToSend);
    }

    /**
     * Press key down in the browser, or on a specific element. Two flavors of use:
     * pressKeyDown(Keys.TAB).inBrowser();
     * pressKeyDown(Keys.TAB).on(path);
     *
     * @param thekey the key to press
     * @return a KeysDown instance that allows to send to the browser in general or to a specific element in the DOM. See example.
     */
    public static Operations.KeysDown pressKeyDown(CharSequence thekey) {
        return getBrowser().pressKeyDown(thekey);
    }

    /**
     * Release key in the browser, or on a specific element. Two flavors of use:
     * releaseKey(Keys.TAB).inBrowser();
     * releaseKey(Keys.TAB).on(path);
     *
     * @param thekey the key to release
     * @return a ReleaseKey instance that allows to send to the browser in general or to a specific element in the DOM. See example.
     */
    public static Operations.ReleaseKey releaseKey(CharSequence thekey) {
        return getBrowser().releaseKey(thekey);
    }

    /**
     * Drag and drop in the browser. Several flavors of use:
     * dragAndDrop(source).to(target);
     * dragAndDrop(source).to(xCor, yCor);
     *
     * @param path the path of the element to drag and drop
     * @return a DragAndDrop instance that allows to drag and drop to another element or to another location
     */
    public static Operations.DragAndDrop dragAndDrop(final Path path) {
        return getBrowser().dragAndDrop(path);
    }
}
