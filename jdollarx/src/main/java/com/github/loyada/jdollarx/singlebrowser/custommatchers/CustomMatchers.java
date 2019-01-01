package com.github.loyada.jdollarx.singlebrowser.custommatchers;


import com.github.loyada.jdollarx.ElementProperties;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.custommatchers.HasText;
import com.github.loyada.jdollarx.custommatchers.IsPresent;
import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.util.concurrent.TimeUnit;

import static com.github.loyada.jdollarx.BasicPath.html;
import static com.github.loyada.jdollarx.ElementProperties.contains;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementSelectionStateToBe;

/**
 * A collection of Hamcrest custom matchers, that are optimized to be as atomic as possible when interacting with the browser or a W3C document,
 * and return useful error messages in case of a failure.
 * This is a simplified API, relevant when there is a singleton browser.
 */
public final class CustomMatchers {

    private static Wait<WebDriver> getWaiter() {
         return new FluentWait<>(InBrowserSinglton.driver).
        withTimeout(1, TimeUnit.SECONDS)
        .pollingEvery(100, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class);
    }

    /**
     * Successful if the the element appears the expected number of times in the browser.
     * This matcher is optimized.
     *
     * Example use for browser interaction:
     *  <pre>
     *   {@code
     *   assertThat( path, ispresent(5).timesOrMore());
     *   assertThat( path, ispresent(5).times());
     *   assertThat( path, ispresent(5).timesOrLess());
     *   }
     *   </pre>
     *
     * @param nTimes - the reference number of times to be matched against. See examples.
     * @return a matcher that matches the number of times an element is present. See examples in the description.
     */
    public static IsPresentNTimes isPresent(int nTimes) {
        return new IsPresentNTimes(nTimes);
    }

    /**
     * Successful if the the element is present in the browser.
     *
     * Example:
     *   {@code assertThat( path, ispresent()); }
     *
     * @return a matcher that checks if an element is present in the browser
     */
    public static Matcher<Path> isPresent() {
        return  new IsPresent().in(new InBrowser(InBrowserSinglton.driver));
    }

    /**
     * Successful if element has the text equal to the given parameter in the browser/document.
     * Note that internally it creates a new path that includes the "hasText" constraint, and
     * then searches for it, so it is atomic.
     * Example use:
     * <pre>
     * {@code assertThat( path, hasText("John")); }
     *</pre>
     * @param text the text to equal to (case insensitive)
     * @return a custom Hamcrest matcher
     */
    public static Matcher<Path> hasText(String text) {
        InBrowser browser = new InBrowser(InBrowserSinglton.driver);
        return new HasText(text).in(browser);
    }

    /**
     * Successful if given element is present and displayed in the browser. Relies on WebElement.isDisplayed(), thus non-atomic.
     * For example:
     * {@code assertThat( path, isDisplayed()); }
     *
     * @return a matcher that checks if an element is displayed in the browser
     */
    public static Matcher<Path> isDisplayed() {
        return new TypeSafeMatcher<Path>() {
            private Path el;
            private final InBrowser browser = new InBrowser(InBrowserSinglton.driver);

            @Override
            public String toString() {
                return "the given Path is present on the browser page and in displayed";
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText(el + " is displayed");
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(el + " is not displayed");
            }

            @Override
            protected boolean matchesSafely(final Path el) {
                this.el = el;
                Wait<WebDriver> wait = getWaiter();
                try {
                    wait.until(ExpectedConditions.visibilityOf(browser.find(el)));
                    return true;
                } catch (Throwable e) {
                    return false;
                }
            }
        };
    }


    /**
     * Successful if given element is either not present, or present and not displayed in the browser. Relies on WebElement.isDisplayed(), thus non-atomic.
     * For example:
     * {@code assertThat( path, isNotDisplayed()); }
     *
     * @return a matcher that checks if an element is displayed in the browser
     */
    public static Matcher<Path> isNotDisplayed() {
        return new TypeSafeMatcher<Path>() {
            private Path el;
            private final InBrowser browser = new InBrowser(InBrowserSinglton.driver);

            @Override
            public String toString() {
                return "The given Path is not displayed (either because it is hidden, or because it is not present)";
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText(el + " is not displayed");
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(el + " is displayed");
            }

            @Override
            protected boolean matchesSafely(final Path el) {
                this.el = el;
                Wait<WebDriver> wait = getWaiter();
                try {
                    wait.until(ExpectedConditions.invisibilityOf(browser.find(el)));
                    return true;
                } catch (NoSuchElementException ex) {
                    return true;
                } catch (Throwable e) {
                    return false;
                }
            }
        };
    }

    /**
     * Successful if given element is present and selected in the browser. Relies on WebElement.isSelected(), thus non-atomic.
     * For example:
     * {@code assertThat( path, isSelected()); }
     *
     * @return a matcher that checks if an element is selected in the browser
     */
    public static Matcher<Path> isSelected() {
        return new TypeSafeMatcher<Path>() {
            private Path el;
            private final InBrowser browser = new InBrowser(InBrowserSinglton.driver);

            @Override
            public String toString() {
                return "The given Path is selected in the browser";
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText(el + " is selected");
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(el + " is not selected");
            }

            @Override
            protected boolean matchesSafely(final Path el) {
                this.el = el;
                Wait<WebDriver> wait = getWaiter();
                try {
                    wait.until(elementSelectionStateToBe(browser.find(el), true));
                    return true;
                } catch (Throwable ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Successful if given element is present and is not selected in the browser. Relies on WebElement.isSelected(), thus non-atomic.
     * For example:
     * {@code assertThat( path, isSelected()); }
     *
     * @return a matcher that checks if an element is selected in the browser
     */
    public static Matcher<Path> isNotSelected() {
        return new TypeSafeMatcher<Path>() {
            private Path el;
            private final InBrowser browser = new InBrowser(InBrowserSinglton.driver);

            @Override
            public String toString() {
                return "The given Path is not selected in the browser";
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText(el + " is not selected");
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(el + " is selected, or is not in the DOM");
            }

            @Override
            protected boolean matchesSafely(final Path el) {
                this.el = el;
                Wait<WebDriver> wait = getWaiter();
                try {
                    wait.until(elementSelectionStateToBe(
                            browser.find(el), false));
                    return true;
                } catch (Throwable ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Successful if given element is present and enabled in the browser. Relies on WebElement.isEnabled(), thus non-atomic.
     * For example:
     * {@code assertThat( path, isEnabled()); }
     *
     * @return a matcher that checks if an element is enabled in the browser
     */
    public static Matcher<Path> isEnabled() {
        return new TypeSafeMatcher<Path>() {
            private Path el;
            private final InBrowser browser = new InBrowser(InBrowserSinglton.driver);

            @Override
            public String toString() {
                return "The given Path is enabled in the browser";
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText(el + " is enabled");
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(el + " is not enabled, or is not in the DOM");
            }

            @Override
            protected boolean matchesSafely(final Path el) {
                this.el = el;
                return browser.isEnabled(el);
            }
        };
    }

    /**
     * Successful if the browser has no elements that correspond to the given path. The implementation of this is optimized.
     * This is much better than doing not(isPresent()), because in case of success (i.e. the element is not there), it will return immidiately,
     * while the isPresent() will block until timeout is reached.
     * For example:
     * {@code assertThat( path, isAbsent()); }
     *
     * @return a matcher that is successful if an element does not appear in the browser.
     */
    public static Matcher<Path> isAbsent() {
        return new TypeSafeMatcher<Path>() {
            private Path el;
            private final InBrowser browser = new InBrowser(InBrowserSinglton.driver);

            @Override
            public String toString() {
                return "Browser page does not contain the given Path";
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("browser page does not contain " + el);
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(el.toString() + " is present");
            }

            @Override
            protected boolean matchesSafely(final Path el) {
                this.el = el;
                return browser.isPresent(html.that(ElementProperties.not(contains(el))));
            }
        };
    }
}
