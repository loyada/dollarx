package com.github.loyada.jdollarx.singlebrowser.custommatchers;


import com.github.loyada.jdollarx.*;
import com.github.loyada.jdollarx.custommatchers.HasText;
import com.github.loyada.jdollarx.custommatchers.IsPresent;
import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * A collection of Hamcrest custom matchers, that are optimized to be as atomic as possible when interacting with the browser or a W3C document,
 * and return useful error messages in case of a failure.
 * This is a simplified API, relevant when there is a singleton browser.
 */
public class CustomMatchers {

    /**
     * Successful if the the element appears the expected number of times in the browser.
     * This matcher is optimized.
     *
     * Example use for browser interaction:
     *   assertThat( path, ispresent(5).timesOrMore());
     *   assertThat( path, ispresent(5).times());
     *   assertThat( path, ispresent(5).timesOrLess());
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
     *   assertThat( path, ispresent());
     *
     * @return a matcher that checks if an element is present in the browser
     */
    public static Matcher<Path> isPresent() {
        return  new IsPresent().in(new InBrowser(InBrowserSinglton.driver));
    }

    /**
     * Successful if element has the text equal to the given parameter in the browser/document.
     * Example use:
     * assertThat( path, hasText().in(browser));
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
     * assertThat( path, isDisplayed());
     *
     * @return a matcher that checks if an element is displayed in the browser
     */
    public static Matcher<Path> isDisplayed() {
        return new TypeSafeMatcher<Path>() {
            private Path el;
            private final InBrowser browser = new InBrowser(InBrowserSinglton.driver);

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
                return browser.isDisplayed(el);
            }
        };
    }

    /**
     * Successful if given element is present and selected in the browser. Relies on WebElement.isSelected(), thus non-atomic.
     * For example:
     * assertThat( path, isSelected());
     *
     * @return a matcher that checks if an element is selected in the browser
     */
    public static Matcher<Path> isSelected() {
        return new TypeSafeMatcher<Path>() {
            private Path el;
            private final InBrowser browser = new InBrowser(InBrowserSinglton.driver);

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
                return browser.isSelected(el);
            }
        };
    }

    /**
     * Successful if given element is present and enabled in the browser. Relies on WebElement.isEnabled(), thus non-atomic.
     * For example:
     * assertThat( path, isEnabled());
     *
     * @return a matcher that checks if an element is enabled in the browser
     */
    public static Matcher<Path> isEnabled() {
        return new TypeSafeMatcher<Path>() {
            private Path el;
            private final InBrowser browser = new InBrowser(InBrowserSinglton.driver);

            @Override
            public void describeTo(final Description description) {
                description.appendText(el + " is enabled");
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(el + " is not enabled");
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
     * assertThat( path, isAbsent());
     *
     * @return a matcher that is successful if an element does not appear in the browser.
     */
    public static Matcher<Path> isAbsent() {
        return new TypeSafeMatcher<Path>() {
            private Path el;
            private final InBrowser browser = new InBrowser(InBrowserSinglton.driver);

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
                return browser.isPresent(PathOperators.not(el));
            }
        };
    }
}
