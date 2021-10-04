package com.github.loyada.jdollarx.singlebrowser;


import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.Path;
import com.google.common.eventbus.Subscribe;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simplified API built to interact with a single instance of a running browser.
 * See {@link com.github.loyada.jdollarx.InBrowser} for an API that supports multiple browser instances.
 */
public final class InBrowserSinglton {
    public static WebDriver driver;
    private static int implicitTimeout;
    private static TimeUnit timeoutUnit;

    /**
     * Convert from a InBrowserSinglton to an non-singleton instance of InBrowser
     * @return a new instance of InBrowser
     */
    private static InBrowser getBrowser() { return InBrowser.fromSingleton();}

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


    /**
     * Get a specific attribute of all the elements matching the given path.
     * The implementation is optimized, and avoids multiple round trips to browser.
     *
     * @param el the elements to find
     * @param attribute - the attribute to extract
     * @return a list of string/int. In case the el is not found, it returns an empty list.
     */
    public static <T> List<T> getAttributeOfAll(final Path el, String attribute) {
        @SuppressWarnings("unchecked")
        List<T> res = (List<T>) getBrowser().getAttributeOfAll(el, attribute);
        return res;
    }

    /**
     * Count number of elements that are currently present.
     * @param el the element definition
     * @return number of elements
     */
    public static int countAll(Path el) {
        return getBrowser().countAll(el);
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
     * Assuming the element exists, check if it is covered by another element
     * @param el the wanted element
     * @return is it covered
     */
    public static boolean isCovered(final Path el) { return getBrowser().isCovered(el);}

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
     * Context click (right click) on the location of the element that corresponds to the given path.
     * @param el a Path instance
     * @return the WebElement clicked at
     */
    public static WebElement contextClick(final Path el) {
        return getBrowser().contextClick(el);
    }

    /**
     * Context click (right click) on the location of the element that corresponds to the given path.
     * @param el a Path instance
     * @return the WebElement clicked at
     */
    public static WebElement rightClick(final Path el) {
        return contextClick(el);
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
     * scroll the browser. Several flavors of use:
     * <pre>
     * {@code
     *    browser.scroll().to(path);
     *    browser.scroll().left(50);
     *    browser.scroll().right(50);
     *    browser.scroll().up(50);
     *    browser.scroll().down(50);
     * }
     * </pre>
     *
     * @return a Scroll instance that allows to scroll by offset or to a location of a DOM element
     */
    public static Operations.Scroll scroll() {
        return getBrowser().scroll();
    }


    /**
     * scroll within the given element. Useful especially when working with grids.
     *
     * @param el a Path instance
     * @return the WebElement found
     */
    public static Operations.ScrollElement scrollElement(final Path el) {
        return getBrowser().scrollElement(el);
    }

    /**
     * scroll within the given element. Useful especially when working with grids.
     *
     * @param el a Path instance
     * @param step step size override
     * @return the WebElement found
     */
    public static Operations.ScrollElement scrollElementWithStepOverride(final Path el, int step) {
        return getBrowser().scrollElementWithStepOverride(el, step);
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
     * <pre>{@code
     *      sendKeys("abc").toBrowser();
     *      sendKeys("abc").to(path);
     *     }
     * </pre>
     * @param charsToSend  the keys to send. Can be "abc", or "a", "b", "c"
     * @return a KeySender instance that allows to send to the browser in general or to a specific element in the DOM
     */
    public static Operations.KeysSender sendKeys(CharSequence... charsToSend) {
        return getBrowser().sendKeys(charsToSend);
    }

    /**
     * Press key down in the browser, or on a specific element. Two flavors of use:
     * <pre>{@code
     * pressKeyDown(Keys.TAB).inBrowser();
     * pressKeyDown(Keys.TAB).on(path);
     *     }
     * </pre>
     * @param thekey the key to press
     * @return a KeysDown instance that allows to send to the browser in general or to a specific element in the DOM. See example.
     */
    public static Operations.KeysDown pressKeyDown(CharSequence thekey) {
        return getBrowser().pressKeyDown(thekey);
    }

    /**
     * Release key in the browser, or on a specific element. Two flavors of use:
     * <pre>{@code
     * releaseKey(Keys.TAB).inBrowser();
     * releaseKey(Keys.TAB).on(path);
     *     }
     * </pre>
     * @param thekey the key to release
     * @return a ReleaseKey instance that allows to send to the browser in general or to a specific element in the DOM. See example.
     */
    public static Operations.ReleaseKey releaseKey(CharSequence thekey) {
        return getBrowser().releaseKey(thekey);
    }

    /**
     * Drag and drop in the browser. Several flavors of use:
     * <pre>{@code
     * dragAndDrop(source).to(target);
     * dragAndDrop(source).to(xCor, yCor);
     *     }
     * </pre>
     * @param path the path of the element to drag and drop
     * @return a DragAndDrop instance that allows to drag and drop to another element or to another location
     */
    public static Operations.DragAndDrop dragAndDrop(final Path path) {
        return getBrowser().dragAndDrop(path);
    }

    /**
     * Get a Selenium select element, which provides a high level API to interacting with a "select" menu.
     * Since the Selenium API is good enough, there was no need to create a specialized dollarx version.
     * @param el - must be a "select" path, with "option" elements for the various selectable options.
     * @return org.openqa.selenium.support.ui.Select instance
     */
    public static Select getSelect(final Path el) {
        return getBrowser().getSelect(el);
    }

    /**
     * Get all classes of given Path element.
     * @param el the element to look for
     * @return a list of classes
     */
    public static List<String> getCssClasses(final Path el) {
       return getBrowser().getCssClasses(el);
    }


    public static int waitUntilStable(Path el, int waitBetweenChecksInMillis) {
        int currentCount = countAll(el);
        int previousCount;
        int iterations = 0;
        do {
            previousCount = currentCount;
            try {
                Thread.sleep(waitBetweenChecksInMillis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            currentCount = countAll(el);
            iterations += 1;
        } while (currentCount!=previousCount);
        return iterations;
    }


    /**
     * Manager implicit timeouts
     * @param implicitTimeout similar to Selenium API
     * @param unit similar to Selenium API
     */
    public static void setImplicitTimeout(int implicitTimeout, TimeUnit unit) {
        InBrowserSinglton.implicitTimeout = implicitTimeout;
        InBrowserSinglton.timeoutUnit = unit;
        driver.manage().timeouts().implicitlyWait(implicitTimeout, unit);
    }

    public static int getImplicitTimeout() {
        return implicitTimeout;
    }

    public static TimeUnit getTimeoutUnit() {
        return timeoutUnit;
    }

    public static long getImplicitTimeoutInMillisec() {
        return timeoutUnit.toMillis(implicitTimeout);
    }
}
