package com.github.loyada.jdollarx;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.io.IOException;
import java.util.function.UnaryOperator;


/**
 * Internal implementation of various browser operations
 */
public class Operations {

    public static class OperationFailedException extends IOException {
        public OperationFailedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * internal implementation not be instantiated directly - Action of sending keys to browser
     */
    public static class KeysSender {
        private final WebDriver driver;
        private final CharSequence[] charsToSend;

        public KeysSender(WebDriver driver, CharSequence... charsToSend) {
            this.driver = driver;
            this.charsToSend = charsToSend;
        }

        /**
         * Send characters tp the browser in general
         */
        public void toBrowser() {
            preformActions(driver, e -> e.sendKeys(charsToSend));
        }

        /**
         * Send keys to a specific element in the browser
         * @param path the element to send the keys to
         * @throws OperationFailedException operation failed. Typically includes the reason.
         */
        public void to(Path path) throws OperationFailedException {
            try {
                preformActions(driver, e -> e.sendKeys(InBrowserFinder.find(driver, path), charsToSend));
            } catch (Exception e) {
                throw new OperationFailedException("could not send keys to " + path, e);
            }
        }
    }


    /**
     * internal implementation not be instantiated directly - Action of  key-down
     */
    public static class KeysDown {
        private final WebDriver driver;
        private final CharSequence key;

        public KeysDown(WebDriver driver, CharSequence keysToSend) {
            this.driver = driver;
            this.key = keysToSend;
        }

        /**
         * Send key-down to the browser in general
         */
        public void inBrowser() {
            preformActions(driver, a -> a.keyDown(key));
        }

        /**
         * Send key-down to an element in the browser
         * @param path the element to press a key down on
         * @throws OperationFailedException operation failed. Typically includes the reason.
         */
        public void on(Path path) throws OperationFailedException {
            try {
                preformActions(driver, a -> a.keyDown(InBrowserFinder.find(driver, path), key));
            } catch (Exception e) {
                throw new OperationFailedException("could not key down on " + path, e);
            }
        }
    }

    /**
     * internal implementation not be instantiated directly - Action of releasing a key (key up)
     */
    public static class ReleaseKey {
        private final WebDriver driver;
        private final CharSequence key;

        public ReleaseKey(WebDriver driver, CharSequence keysToSend) {
            this.driver = driver;
            this.key = keysToSend;
        }

        /**
         * releasing a key in the browser in general
         */
        public void inBrowser() {
            preformActions(driver, a -> a.keyUp(key));
        }

        /**
         * release a key on a specific element in the browser
         * @param path the element to release the key on
         * @throws OperationFailedException operation failed. Typically includes the reason.
         */
        public void on(BasicPath path) throws OperationFailedException {
            try {
                preformActions(driver, a -> a.keyUp(InBrowserFinder.find(driver, path), key));
            } catch (Exception e) {
                throw new OperationFailedException("could not release keys on " + path, e);
            }
        }
    }



    /**
     * internal implementation not be instantiated directly - Action of scroll
     */
    public static class Scroll {

        private final WebDriver driver;

        public Scroll(final WebDriver driver) {
            this.driver = driver;
        }

        /**
         * Scroll until the location of an element
         * @param path the element to scroll to
         */
        public void to(Path path) {
            preformActions(driver, a -> a.moveToElement(InBrowserFinder.find(driver, path)));
        }

        private void scrollInternal(Integer x, Integer y) {
            ((JavascriptExecutor) driver).executeScript(String.format("scroll(%d,%d)", x, y));

        }

        /**
         * scroll left number of pixels
         * @param n pixels
         */
        public void left(Integer n) {
            scrollInternal(-1 * n, 0);
        }

        /**
         * scroll right number of pixels
         * @param n pixels
         */
        public void right(Integer n) {
            scrollInternal(n, 0);
        }

        /**
         * scroll up number of pixels
         * @param n pixels
         */
        public void up(Integer n) {
            scrollInternal(0, -1 * n);
        }

        /**
         * scroll down number of pixels
         * @param n pixels
         */
        public void down(Integer n) {
            scrollInternal(0, n);
        }

    }

    /**
     * internal implementation not be instantiated directly - Action of scroll within an element
     */
    public static class ScrollElement {

        private final WebDriver driver;
        private final Path wrapper;
        private final int STEP=40;
        private final int LARGE_NUM=100000;

        public ScrollElement(final WebDriver driver, Path wrapper) {
            this.driver = driver;
            this.wrapper = wrapper;
        }

        /**
         * Scroll down until the virtualized DOM contains the expect element.
         * Using 20 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @return the WebElement or throws an exception of not found
         */
        public WebElement downUntilElementIsPresent(Path expectedElement) {
            return downUntilElementIsPresent(expectedElement, STEP, LARGE_NUM);
        }

        /**
         * Scroll up until the virtualized DOM contains the expect element.
         * Using 20 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @return the WebElement or throws an exception of not found
         */
        public WebElement upUntilElementIsPresent(Path expectedElement) {
            return upUntilElementIsPresent(expectedElement, STEP, LARGE_NUM);
        }

        /**
         * Scroll right until the virtualized DOM contains the expect element.
         * Using 20 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @return the WebElement or throws an exception of not found
         */
        public WebElement rightUntilElementIsPresent(Path expectedElement) {
            return rightUntilElementIsPresent(expectedElement, STEP, LARGE_NUM);
        }

        /**
         * Scroll left until the virtualized DOM contains the expect element.
         * Using 20 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @return the WebElement or throws an exception of not found
         */
        public WebElement leftUntilElementIsPresent(Path expectedElement) {
            return leftUntilElementIsPresent(expectedElement, STEP, LARGE_NUM);
        }

        /**
         * Scroll down until the virtualized DOM contains the expect element.
         * @param expectedElement - the element we are looking for
         * @param scrollStep - scroll step in pixels
         * @param maxNumberOfScrolls maximum number of scroll operations
         * @return the WebElement or throws an exception of not found
         */
        public WebElement downUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls ) {
            return scrollWrapperUntilElementIsPresent(expectedElement, scrollStep, maxNumberOfScrolls,
                    "elem = arguments[0];elem.scrollTop = elem.scrollTop+arguments[1];return elem.scrollHeight-elem.scrollTop-elem.clientHeight;");
        }

        /**
         * Scroll up until the virtualized DOM contains the expect element.
         * @param expectedElement - the element we are looking for
         * @param scrollStep - scroll step in pixels
         * @param maxNumberOfScrolls maximum number of scroll operations
         * @return the WebElement or throws an exception of not found
         */
        public WebElement upUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls ) {
            return scrollWrapperUntilElementIsPresent(expectedElement, scrollStep, maxNumberOfScrolls,
                    "elem = arguments[0];elem.scrollTop = elem.scrollTop-arguments[1];return elem.scrollTop;");
        }

        /**
         * Scroll right until the virtualized DOM contains the expect element.
         * @param expectedElement - the element we are looking for
         * @param scrollStep - scroll step in pixels
         * @param maxNumberOfScrolls maximum number of scroll operations
         * @return the WebElement or throws an exception of not found
         */
        public WebElement rightUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls ) {
            return scrollWrapperUntilElementIsPresent(expectedElement, scrollStep, maxNumberOfScrolls,
                    "elem = arguments[0];elem.scrollLeft = elem.scrollLeft+arguments[1];return elem.scrollWidth-elem.scrollLeft-elem.clientWidth;");
        }

        /**
         * Scroll left until the virtualized DOM contains the expect element.
         * @param expectedElement - the element we are looking for
         * @param scrollStep - scroll step in pixels
         * @param maxNumberOfScrolls maximum number of scroll operations
         * @return the WebElement or throws an exception of not found
         */
        public WebElement leftUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls ) {
            return scrollWrapperUntilElementIsPresent(expectedElement, scrollStep, maxNumberOfScrolls,
                    "elem = arguments[0];elem.scrollLeft = elem.scrollLeft-arguments[1];return elem.scrollLeft;");
        }

        private WebElement scrollWrapperUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls, String script) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            InBrowser browser = new InBrowser(driver);
            WebElement wrapperEl = browser.find(wrapper);
            long left=1;
            for (int i=0; i<maxNumberOfScrolls; i++) {
                try {
                    left = (long) js.executeScript(script, wrapperEl, scrollStep);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    WebElement el= browser.find(expectedElement);
                    return el;
                } catch( NoSuchElementException ex) {
                    if (left<=0)
                        break;
                }
            }
            throw new NoSuchElementException(expectedElement.toString());
        }
        /**
         * scroll left number of pixels
         * @param n pixels
         */
        public void left(Integer n) {
            scrollInternal(-1 * n, 0);
        }

        /**
         * scroll right number of pixels
         * @param n pixels
         */
        public void right(Integer n) {
            scrollInternal(n, 0);
        }

        /**
         * scroll up number of pixels
         * @param n pixels
         */
        public void up(Integer n) {
            scrollInternal(0, -1 * n);
        }

        /**
         * scroll down number of pixels
         * @param n pixels
         */
        public void down(Integer n) {
            scrollInternal(0, n);
        }

        private void scrollInternal(Integer x, Integer y) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            InBrowser browser = new InBrowser(driver);
            WebElement wrapperEl = browser.find(wrapper);
            if (y!=0) {
                js.executeScript("elem = arguments[0];elem.scrollTop = elem.scrollTop+arguments[1];", wrapperEl, y);
            }
            if (x!=0) {
                js.executeScript("elem.scrollLeft = elem.scrollLeft+arguments[1];", wrapperEl, y);
            }
        }

    }


    /**
     * internal implementation not be instantiated directly - Action of drag-and-drop
     */
    public static class DragAndDrop {
        private final WebDriver driver;
        private final Path path;

        public DragAndDrop(final WebDriver driver, final Path path) {
            this.driver = driver;
            this.path = path;
        }

        /**
         * drag and drop to the given element's location
         * @param target - the target(drop) element
         * @throws OperationFailedException operation failed. Typically includes the reason.
         */
        public void to(Path target) throws OperationFailedException {
            try {
                WebElement to = InBrowserFinder.find(driver, target);
                preformActions(driver, a -> a.clickAndHold(InBrowserFinder.find(driver, path)).moveToElement(to).release(to));
            } catch (Exception e) {
                throw new OperationFailedException("could not drag and drop " + path + " to " + target, e);
            }
        }

        /**
         * drag and drop to the given coordinates
         * @param x coordinates
         * @param y coordinates
         * @throws OperationFailedException operation failed. Typically includes the reason.
         */
        public void to(Integer x, Integer y) throws OperationFailedException {
            try {
                preformActions(driver, a -> a.clickAndHold(InBrowserFinder.find(driver, path)).moveByOffset(x, y).release());
            } catch (Exception e) {
                throw new OperationFailedException("could not drag and drop " + path, e);
            }
        }
    }

    private static void preformActions(WebDriver driver, UnaryOperator<Actions> func) {
        final Actions actionBuilder = new Actions(driver);
        func.apply(actionBuilder).build().perform();
    }
}
