package com.github.loyada.jdollarx;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.lang.Thread.sleep;


/**
 * Internal implementation of various browser operations
 */
public class Operations {

    public static class OperationFailedException extends IOException {
        public OperationFailedException(String message, Throwable cause) {
            super(message, cause);
        }
        public OperationFailedException(String message) {
            super(message);
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
        private int step;
        private final int LARGE_NUM=100000;

        private static final Predicate<WebElement> TRUTHY = e -> true;

        public ScrollElement(final WebDriver driver, Path wrapper) {
            this.driver = driver;
            this.wrapper = wrapper;
            step=60;
        }

        public ScrollElement(final WebDriver driver, Path wrapper, int stepSizeOverride) {
            this.driver = driver;
            this.wrapper = wrapper;
            step = stepSizeOverride;
        }

        /**
         * Scroll down until the  DOM contains the expected element.
         * Using 40 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @return the WebElement or throws an exception of not found
         */
        public WebElement toTopLeftCorner(Path expectedElement) {
            return downUntilElementIsPresent(expectedElement, step, LARGE_NUM);
        }

        /**
         * Scroll down until the  DOM contains the expected element.
         * Using 40 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @return the WebElement or throws an exception of not found
         */
        public WebElement downUntilElementIsPresent(Path expectedElement) {
            return downUntilElementIsPresent(expectedElement, step, LARGE_NUM);
        }

        /**
         * Scroll up until the DOM contains the expected element, and
         * the given condition for that element is met.
         * Using 40 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @param predicate - a condition regarding the expected element that is required to be met
         * @return the WebElement or throws an exception of not found
         */
        public WebElement upUntilPredicate(Path expectedElement, Predicate<WebElement> predicate) {
            return upUntilPredicate(expectedElement, step, LARGE_NUM, predicate);
        }

        /**
         * Scroll down until the DOM contains the expected element, and
         * the given condition for that element is met.
         * Using 40 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @param predicate - a condition regarding the expected element that is required to be met
         * @return the WebElement or throws an exception of not found
         */
        public WebElement downUntilPredicate(Path expectedElement, Predicate<WebElement> predicate) {
            return downUntilPredicate(expectedElement, step, LARGE_NUM, predicate);
        }

        /**
         * Scroll up until the virtualized DOM contains the expect element.
         * Using 40 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @return the WebElement or throws an exception of not found
         */
        public WebElement upUntilElementIsPresent(Path expectedElement) {
            return upUntilElementIsPresent(expectedElement, step, LARGE_NUM);
        }

        /**
         * Scroll right until the virtualized DOM contains the expect element.
         * Using 40 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @return the WebElement or throws an exception of not found
         */
        public WebElement rightUntilElementIsPresent(Path expectedElement) {
            return rightUntilElementIsPresent(expectedElement, step, LARGE_NUM);
        }

        /**
         * Scroll right until the virtualized DOM contains the expect element, and it is visible
         * Using 40 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @return the WebElement or throws an exception of not found
         */
        public WebElement rightUntilElementIsVisible(Path expectedElement) {
            return rightUntilPredicate(expectedElement, step, LARGE_NUM, WebElement::isDisplayed);
        }

        /**
         * Scroll right until the  DOM contains the expected element, and the
         * given predicate regarding that element is met.
         * Using 40 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @param predicate - a condition regarding the expected element that is required to be met
         * @return the WebElement or throws an exception of not found
         */
        public WebElement rightUntilPredicate(Path expectedElement, Predicate<WebElement> predicate) {
            return rightUntilPredicate(expectedElement, step, LARGE_NUM, predicate);
        }

        /**
         * Scroll left until the DOM contains the expected element.
         * Using 40 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @return the WebElement or throws an exception of not found
         */
        public WebElement leftUntilElementIsPresent(Path expectedElement) {
            return leftUntilElementIsPresent(expectedElement, step, LARGE_NUM);
        }

        /**
         * Scroll left until the DOM contains the expected element, and it's displayed.
         * Using 40 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @return the WebElement or throws an exception if not found
         */
        public WebElement leftUntilElementIsDisplayed(Path expectedElement) {
            return leftUntilElementIsDisplayed(expectedElement, step, LARGE_NUM);
        }

        /**
         * Scroll left until the  DOM contains the expected element, and the
         * given predicate regarding that element is met.
         * Using 40 pixels steps, until the end of the table
         * @param expectedElement - the element we are looking for
         * @param predicate - a condition regarding the expected element that is required to be met
         * @return the WebElement or throws an exception of not found
         */
        public WebElement leftUntilPredicate(Path expectedElement, Predicate<WebElement> predicate) {
            return leftUntilPredicate(expectedElement, step, LARGE_NUM, predicate);
        }

        /**
         * Scroll down until the DOM contains the expected element.
         * @param expectedElement - the element we are looking for
         * @param scrollStep - scroll step in pixels
         * @param maxNumberOfScrolls maximum number of scroll operations
         * @return the WebElement or throws an exception of not found
         */
        public WebElement downUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls ) {
            return downUntilPredicate(
                    expectedElement,
                    scrollStep,
                    maxNumberOfScrolls,
                    TRUTHY
            );
        }

        /**
         * Scroll down until the DOM contains the expected element, and
         * the supplied condition for that element is met.
         * @param expectedElement - the element we are looking for
         * @param scrollStep - scroll step in pixels
         * @param maxNumberOfScrolls maximum number of scroll operations
         * @param predicate - a condition regarding the expected element that is required to be met
         * @return the WebElement or throws an exception of not found
         */
        public WebElement downUntilPredicate(Path expectedElement, int scrollStep, int maxNumberOfScrolls,Predicate<WebElement> predicate) {
            return scrollWrapperUntilElementConditional(
                    expectedElement,
                    scrollStep,
                    maxNumberOfScrolls,
                    predicate,
                    "elem = arguments[0];elem.scrollTop = elem.scrollTop+arguments[1];return elem.scrollHeight-elem.scrollTop-elem.clientHeight;");
        }

        /**
         * Scroll up until the DOM contains the expected element, and
         * the supplied condition for that element is met.
         * @param expectedElement - the element we are looking for
         * @param scrollStep - scroll step in pixels
         * @param maxNumberOfScrolls maximum number of scroll operations
         * @param predicate - a condition regarding the expected element that is required to be met
         * @return the WebElement or throws an exception of not found
         */
        public WebElement upUntilPredicate(Path expectedElement, int scrollStep, int maxNumberOfScrolls, Predicate<WebElement> predicate) {
            return scrollWrapperUntilElementConditional(
                    expectedElement,
                    scrollStep,
                    maxNumberOfScrolls,
                    predicate,
                    "elem = arguments[0];elem.scrollTop = elem.scrollTop-arguments[1];return elem.scrollTop;");
        }


        /**
         * Scroll up until the virtualized DOM contains the expect element.
         * @param expectedElement - the element we are looking for
         * @param scrollStep - scroll step in pixels
         * @param maxNumberOfScrolls maximum number of scroll operations
         * @return the WebElement or throws an exception of not found
         */
        public WebElement upUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls ) {
            return upUntilPredicate(
                    expectedElement,
                    scrollStep,
                    maxNumberOfScrolls,
                    TRUTHY
            );
        }

        /**
         * Scroll right until the virtualized DOM contains the expect element.
         * @param expectedElement - the element we are looking for
         * @param scrollStep - scroll step in pixels
         * @param maxNumberOfScrolls maximum number of scroll operations
         * @return the WebElement or throws an exception of not found
         */
        public WebElement rightUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls ) {
            return rightUntilPredicate(
                    expectedElement,
                    scrollStep,
                    maxNumberOfScrolls,
                    TRUTHY
            );
        }

        /**
         * Scroll right until the DOM contains the expected element and the supplied predicate for the element is met.
         * @param expectedElement - the element we are looking for
         * @param scrollStep - scroll step in pixels
         * @param maxNumberOfScrolls maximum number of scroll operations
         * @param predicate - a condition regarding the expected element that is required to be met
         * @return the WebElement or throws an exception of not found
         */
        public WebElement rightUntilPredicate(Path expectedElement, int scrollStep, int maxNumberOfScrolls, Predicate<WebElement> predicate) {
            return scrollWrapperUntilElementConditional(
                    expectedElement,
                    scrollStep,
                    maxNumberOfScrolls,
                    predicate,
                    "elem = arguments[0];elem.scrollLeft = elem.scrollLeft+arguments[1];return elem.scrollWidth-elem.scrollLeft-elem.clientWidth;");
        }

        /**
         * Scroll left until the DOM contains the expected element and the supplied predicate for the element is met.
         * @param expectedElement - the element we are looking for
         * @param scrollStep - scroll step in pixels
         * @param maxNumberOfScrolls maximum number of scroll operations
         * @param predicate - a condition regarding the expected element that is required to be met
         * @return the WebElement or throws an exception of not found
         */
        public WebElement leftUntilPredicate(Path expectedElement, int scrollStep, int maxNumberOfScrolls, Predicate<WebElement> predicate) {
            return scrollWrapperUntilElementConditional(
                    expectedElement,
                    scrollStep,
                    maxNumberOfScrolls,
                    predicate,
                    "elem = arguments[0];elem.scrollLeft = elem.scrollLeft-arguments[1];return elem.scrollLeft;");
        }

        /**
         * Scroll left until the DOM contains the expected element.
         * @param expectedElement - the element we are looking for
         * @param scrollStep - scroll step in pixels
         * @param maxNumberOfScrolls maximum number of scroll operations
         * @return the WebElement or throws an exception of not found
         */
        public WebElement leftUntilElementIsPresent(Path expectedElement, int scrollStep, int maxNumberOfScrolls ) {
            return leftUntilPredicate(
                    expectedElement,
                    scrollStep,
                    maxNumberOfScrolls,
                    TRUTHY
            );
        }

        private WebElement leftUntilElementIsDisplayed(Path expectedElement, int scrollStep, int maxNumberOfScrolls ) {
            return leftUntilPredicate(
                    expectedElement,
                    scrollStep,
                    maxNumberOfScrolls,
                    WebElement::isDisplayed
            );
        }

        private WebElement scrollWrapperUntilElementConditional(
                Path expectedElement,
                int scrollStep,
                int maxNumberOfScrolls,
                Predicate<WebElement> elementPredicate,
                String script) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            InBrowser browser = new InBrowser(driver);
            WebElement wrapperEl = browser.find(wrapper);

            try {
                return doWithRetries(() -> {
                    long left = 1;
                    final int MAX_FAILURES = 5;
                    int failures = 0;
                    for (int i = 0; i < maxNumberOfScrolls; i++) {
                        List<WebElement> els = browser.findAll(expectedElement);
                        Optional<WebElement> foundOne = els.stream()
                                .filter(elementPredicate)
                                .findFirst();
                        if (foundOne.isPresent())
                            return foundOne.get();
                        if (left <= 0)
                            break;

                        try {
                            Object ret = js.executeScript(script, wrapperEl, scrollStep);
                            left = (ret.getClass() == Double.class) ? ((Double) ret).longValue() : (long) ret;
                        } catch (Exception e) {
                            e.printStackTrace();
                            failures++;
                            if (failures >= MAX_FAILURES) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    throw new NoSuchElementException(expectedElement.toString());
                }, 3, 200);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        /**
         * Scroll to top-left corner
         */
        public void toTopLeftCorner() {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            InBrowser browser = new InBrowser(driver);
            WebElement wrapperEl = browser.find(wrapper);
            js.executeScript("elem = arguments[0];elem.scrollTop = 0; elem.scrollLeft = 0", wrapperEl);
        }

        /**
         * Scroll to top-most point
         */
        public void toTopCorner() {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            InBrowser browser = new InBrowser(driver);
            WebElement wrapperEl = browser.find(wrapper);
            js.executeScript("elem = arguments[0];elem.scrollTop = 0;", wrapperEl);
        }

        /**
         * Scroll to left-most point
         */
        public void toLeftCorner() {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            InBrowser browser = new InBrowser(driver);
            WebElement wrapperEl = browser.find(wrapper);
            js.executeScript("elem = arguments[0]; elem.scrollLeft = 0", wrapperEl);
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

    /**
     * Retry an action/assertion up to a number of times, with delay after each time.
     * For example:
     * <pre>
     * {@code
     *   doWithRetries(() -> assertThat(div.withClass("foo"), isDisplayedIn(browser)), 5, 10);
     * }
     * </pre>
     * @param action the action to try. It's a runnable - no input parapeters and does not return anything.
     * @param numberOfRetries - maximum number of retries
     * @param sleepInMillisec - delay between consecutive retries
     */
    public static  void doWithRetries(
            Runnable action,
            int numberOfRetries,
            int sleepInMillisec
            ){
            int triesLeft = numberOfRetries;
            while (true) {
                try {
                    action.run();
                    return;
                } catch (Exception|AssertionError e) {
                    triesLeft-=1;
                    if (triesLeft<=0) {
                        throw e;
                    }
                    try {
                        sleep(sleepInMillisec);
                    } catch (InterruptedException intEx) {
                        throw new RuntimeException(intEx);
                    }
                }
        }
    }

    /**
     * Retry an action up to a number of times, with delay after each time.
     * For example:
     * <pre>
     * {@code
     *   WebElement el = doWithRetries(() -> browser.find(div.withClass("foo"), 5, 10);
     * }
     * </pre>
     * @param action the action to try. It has no input parameters, but returns a value
     * @param numberOfRetries - maximum number of retries
     * @param sleepInMillisec - delay between consecutive retries
     * @param <T> any type that the function returns
     * @return returns the result of the callable
     * @throws Exception the exception thrown by the last try in case it exceeded the number of retries.
     */
    public static <T> T doWithRetries(
            Callable<T> action,
            int numberOfRetries,
            int sleepInMillisec
    ) throws Exception {
        int triesLeft = numberOfRetries;
        while (true) {
            try {
                return action.call();
            } catch (Exception|AssertionError e) {
                triesLeft-=1;
                if (triesLeft<=0) {
                    throw e;
                }
                try {
                    sleep(sleepInMillisec);
                } catch (InterruptedException intEx) {
                    throw new RuntimeException(intEx);
                }
            }
        }
    }


    public static <T, V extends Exception> T doWithRetriesForException(
            Callable<T> action,
            Class<V> exceptionClass,
            int numberOfRetries,
            int sleepInMillisec
    ) throws Exception {
        int triesLeft = numberOfRetries;
        while (true) {
            try {
                return action.call();
            } catch (Exception e) {
                if (exceptionClass.isInstance(e)) {
                    triesLeft -= 1;
                    if (triesLeft <= 0) {
                        throw e;
                    }
                    try {
                        sleep(sleepInMillisec);
                    } catch (InterruptedException intEx) {
                        throw new RuntimeException(intEx);
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    private static void preformActions(WebDriver driver, UnaryOperator<Actions> func) {
        final Actions actionBuilder = new Actions(driver);
        func.apply(actionBuilder).build().perform();
    }
}
