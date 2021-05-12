package com.github.loyada.jdollarx;


import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

import static java.util.Arrays.asList;

/**
 * A wrapper around Selenium WebDriver, used for interaction with the browser.
 * In case only a single instance of the browser is used, {@link com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton}
 * offers a simpler API.
 */
public class InBrowser {
    private final WebDriver driver;
    private int implicitTimeout;
    private TimeUnit timeoutUnit;

    /**
     * Creates a connection to a browser, using the given driver
     * @param driver a WebDriver instance
     */
    public InBrowser(final WebDriver driver) {
        this.driver = driver;
    }

    public static InBrowser fromSingleton() {
        InBrowser browser = new InBrowser(InBrowserSinglton.driver);
        browser.timeoutUnit = InBrowserSinglton.getTimeoutUnit();
        browser.implicitTimeout = InBrowserSinglton.getImplicitTimeout();
        return browser;
    }

    /**
     *
     * @return the underlying WebDriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Finds an element in the browser, based on the xpath representing el. It is similar to WebDriver.findElement(),
     * If el also has a WebElement (ie: getUnderlyingSource() is not empty), then it looks inside that
     * WebElement. This is useful also to integrate with existing WebDriver code.
     *
     * @param el - the path to find
     * @return - A WebElement instance from selenium, or throws NoSuchElementException exception
     */
    public WebElement find(final Path el) {
        return InBrowserFinder.find(driver, el);
    }

    /**
     * Don't use this directly. There are better ways to do equivalent operation.
     * @param el the path to find
     * @param numberOfOccurrences the base number to find
     * @param relationOperator whether we look for exactly the numberOfOccurrences, at least, or at most occurrences
     * @return the first WebElement found
     */
    public WebElement findPageWithNumberOfOccurrences(final Path el, int numberOfOccurrences, RelationOperator relationOperator) {
        return InBrowserFinder.findPageWithNumberOfOccurrences(driver, el, numberOfOccurrences, relationOperator);
    }

    /**
     * Finds an page in the browser, that does not contain the given path
     * @param el - the path that must not appear in the page
     * @return returns the page element or raises NoSuchElementException
     */
    public WebElement findPageWithout(final Path el) {
        return InBrowserFinder.findPageWithout(driver, el);
    }

    /**
     * Finds all elements in the browser, based on the xpath representing el. It is similar to WebDriver.findElements(),
     * If el also has a WebElement (ie: getUnderlyingSource() is not empty), then it looks inside that
     * WebElement. This is useful also to integrate with existing WebDriver code.
     *
     * @param el - the path to find
     * @return - A list of WebElement from selenium, or throws NoSuchElementException exception
     */
    public List<WebElement> findAll(final Path el) {
        return InBrowserFinder.findAll(driver, el);

    }

    /**
     * Get a specific attribute of all the elements matching the given path.
     * The implementation is optimized, and avoids multiple round trips to browser.
     *
     * @param el the elements to find
     * @param attribute - the attribute to extract
     * @return a list of string/int. In case the el is not found, it returns an empty list.
     */
    public List<?> getAttributeOfAll(final Path el, String attribute) {
        return InBrowserFinder.getAttributeOfAll(driver, el, attribute);
    }

    /////////////// predicates

    /**
     * Returns the number of elements in the browser that match the given path. Typically you
     * should not use this method directly. Instead, use CustomMatchers.
     * @param el the element to find
     * @return the number of elements in the browser that match the given path
     */
    public Integer numberOfAppearances(Path el) {
        return InBrowserFinder.findAll(driver, el).size();
    }

    /**
     * is the element present? Typically you should not use this method directly.
     * Instead, use CustomMatchers.
     * @param el the path to find
     * @return true if the element is present
     */
    public boolean isPresent(Path el) {
        try {
            find(el);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * is the element present? Typically you should not use this method directly.
     * Instead, use CustomMatchers.
     * @param el the path to find
     * @return true if it is not present
     */
    public boolean isNotPresent(Path el) {
        try {
            findPageWithout(el);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * is the element present and enabled? Typically you should not use this method directly.
     * Instead, use CustomMatchers.
     * @param el the element
     * @return true if it is present and enabled
     */
    public boolean isEnabled(Path el) {
        try {
            return find(el).isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * is the element present and selected? Typically you should not use this method directly.
     * Instead, use CustomMatchers.
     * @param el the element
     * @return true if it is present and selected
     */
    public boolean isSelected(Path el) {
        try {
            return find(el).isSelected();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * is the element present and displayed? Typically you should not use this method directly.
     * Instead, use CustomMatchers. Also, this is limited to checking the inlined css style, so
     * it is quite limited.
     * @param el the element
     * @return true if it is present and selected
     */
    public boolean isDisplayed(Path el) {
        try {
            return find(el).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Assuming the element exists, check if it is covered by another element
     * @param el the wanted element
     * @return is it covered
     */
    public boolean isCovered(Path el) {
        String script = getScriptToFindIfElementIsExposed();
        Object res = ((JavascriptExecutor) driver).executeScript(script, find(el));
        return (boolean) res;
    }

    private static String getScriptToFindIfElementIsExposed() {
        return "const rect=arguments[0].getBoundingClientRect();" +
                "const x=rect.left;" +
                "const y=rect.top;" +
                "topElt=document.elementFromPoint(x,y);" +
                "return !arguments[0].isSameNode(topElt);";
    }



    ////////////////////////////////////////////////////
    //// actions

    /**
     * Click on the first element that fits the given path. Only works for clickable elements.
     * If the element is currently not clickable, will wait up to a second for it to be clickable.
     * @param el the element
     * @return the clicked on WebElement
     */
    public WebElement clickOn(Path el) {
        WebElement found = find(el);
        Wait<WebDriver> wait = getWaiter();
        wait.until(ExpectedConditions.elementToBeClickable(found));
        found.click();
        return found;
    }

    /**
     * Click at the location the first element that fits the given path. Does not require a clickable element.
     * @param el the element
     * @return the clicked on WebElement
     */
    public WebElement clickAt(Path el) {
        WebElement found = find(el);
        preformActions(e -> e.moveToElement(found).click());
        return found;
    }

    /**
     * Context-click (right click) at the location the first element that fits the given path. Does not require a clickable element.
     * @param el the element
     * @return the clicked on WebElement
     */
    public WebElement contextClick(Path el) {
        WebElement found = find(el);
        preformActions(e -> e.moveToElement(found).contextClick());
        return found;
    }

    /**
     * Context-click (right click) at the location the first element that fits the given path. Does not require a clickable element.
     * @param el the element
     * @return the clicked on WebElement
     */
    public WebElement rightClick(Path el) {
        return contextClick(el);
    }

    /**
     * Hover over the location of the first element that fits the given path
     * @param el the element
     * @return the clicked on WebElement
     */
    public WebElement hoverOver(Path el) {
        final WebElement found = find(el);
        preformActions(e -> e.moveToElement(found));
        return found;
    }

    /**
     * Scroll to the location of the first element that fits the given path
     * @param el the element
     * @return the clicked on WebElement
     */
    public WebElement scrollTo(Path el) {
        return hoverOver(el);
    }

    private void preformActions(UnaryOperator<Actions> func) {
        final Actions actionBuilder = new Actions(driver);
        func.apply(actionBuilder).build().perform();
    }

    /**
     * Doubleclick the location of the first element that fits the given path.
     * @param el the element
     */
    public void doubleClickOn(Path el) {
        WebElement found = find(el);
        Wait<WebDriver> wait = getWaiter();
        wait.until(ExpectedConditions.elementToBeClickable(found));
        preformActions(e -> e.doubleClick(found));
    }

    /**
     * send keys to the browser, or to a specific element. Two flavors of use:
     * browser.sendKeys("abc").toBrowser();
     * browser.sendKeys("abc").to(path);
     *
     * @param charsToSend  The characters to send. Can be "abc" or "a", "b", "c"
     * @return a KeySender instance that allows to send keys to the browser in general, or to a specific DOM element
     */
    public Operations.KeysSender sendKeys(CharSequence... charsToSend) {
        return new Operations.KeysSender(driver, charsToSend);
    }

    /**
     * Press key down in the browser, or on a specific element. Two flavors of use:
     * browser.pressKeyDown(Keys.TAB).inBrowser();
     * browser.pressKeyDown(Keys.TAB).on(path);
     *
     * @param thekey a key to press
     * @return returns a KeysDown instance that allows to press a key on the browser in general or on a specific DOM element
     */
    public Operations.KeysDown pressKeyDown(CharSequence thekey) {
        return new Operations.KeysDown(driver, thekey);
    }

    /**
     * Release key down in the browser, or on a specific element. Two flavors of use:
     * <pre>
     * {@code
     *    browser.releaseKey(Keys.TAB).inBrowser();
     *    browser.releaseKey(Keys.TAB).on(path);
     * }
     * </pre>
     *
     * @param thekey a key to release
     * @return returns a ReleaseKey instance that allows to release on the browser in general or on a specific DOM element
     */
    public Operations.ReleaseKey releaseKey(CharSequence thekey) {
        return new Operations.ReleaseKey(driver, thekey);
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
    public Operations.Scroll scroll() {
        return new Operations.Scroll(driver);
    }

    public Operations.ScrollElement scrollElement(Path wrapper) {
        return new Operations.ScrollElement(driver, wrapper);
    }

    public Operations.ScrollElement scrollElementWithStepOverride(Path wrapper, int step) {
        return new Operations.ScrollElement(driver, wrapper, step);
    }

    /**
     * Drag and drop in the browser. Several flavors of use:
     * browser.dragAndDrop(source).to(target);
     * browser.dragAndDrop(source).to(xCor, yCor);
     *
     * @param path the source element
     * @return a DragAndDrop instance, that allows to drag and drop to a location or to another DOM element
     */
    public Operations.DragAndDrop dragAndDrop(Path path) {
        return new Operations.DragAndDrop(driver, path);
    }

    private Wait<WebDriver> getWaiter() {
        final Duration duration;
        if (implicitTimeout!=0 && timeoutUnit!=null) {
            long implicitTimeoutInMillis = TimeUnit.MILLISECONDS.convert(implicitTimeout, timeoutUnit);
            duration  = Duration.ofMillis(implicitTimeoutInMillis);
        } else {
            duration = Duration.ofMillis(10000);
        }

        return new FluentWait<>(driver).withTimeout(duration)
                .pollingEvery(Duration.ofMillis(100))
                .ignoring(org.openqa.selenium.NoSuchElementException.class);
    }

    /**
     * Get a Selenium select element, which provides a high level API to interacting with a "select" menu.
     * Since the Selenium API is good enough, there was no need to create a specialized dollarx version.
     * @param el - must be a "select" path, with "option" elements for the various selectable options.
     * @return org.openqa.selenium.support.ui.Select instance
     */
    public Select getSelect(final Path el) {
        return new Select(find(el));
    }

    /**
     * Get all classes of given Path element.
     * @param el the element to look for
     * @return a list of classes
     */
    public List<String> getCssClasses(final Path el) {
        String[] res = find(el).getAttribute("class").trim().split("\\s+");
        return asList(res);
    }

    public void setImplicitTimeout(int implicitTimeout, TimeUnit unit) {
        this.implicitTimeout = implicitTimeout;
        this.timeoutUnit = unit;
        driver.manage().timeouts().implicitlyWait(implicitTimeout, unit);
    }

    public int getImplicitTimeout() {
        return implicitTimeout;
    }

    public TimeUnit getTimeoutUnit() {
        return timeoutUnit;
    }

    public long getImplicitTimeoutInMillisec() {
        return timeoutUnit.toMillis(implicitTimeout);
    }
}
