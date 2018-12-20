package com.github.loyada.jdollarx.custommatchers;

import com.github.loyada.jdollarx.ElementProperties;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.PathOperators;
import com.github.loyada.jdollarx.PathParsers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static com.github.loyada.jdollarx.BasicPath.html;
import static com.github.loyada.jdollarx.ElementProperties.contains;

/**
 * A collection of Hamcrest custom matchers, that are optimized to be as atomic as possible when interacting with the browser or a W3C document,
 * and return useful error messages in case of a failure.
 */
public class CustomMatchers {

    private static Wait<WebDriver> getWaiter(InBrowser browser) {
        return new FluentWait<>(browser.getDriver()).
                withTimeout(1, TimeUnit.SECONDS)
                .pollingEvery(100, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);
    }

    /**
     * Successful if the browser has an element that corresponds to the given path.
     * Example use:
     * <pre>
     *    {@code assertThat( browser, hasElement(el));}
     * </pre>
     *
     * @param el the path to find
     * @return a matcher for a browser that contains the element
     */
    public static Matcher<InBrowser> hasElement(final Path el) {
        return new TypeSafeMatcher<InBrowser>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("browser page contains " + el);
            }

            @Override
            protected void describeMismatchSafely(final InBrowser browser, final
            Description mismatchDescription) {
                mismatchDescription.appendText(CustomMatchersUtil.wrap(el) + " is absent");
            }

            @Override
            protected boolean matchesSafely(final InBrowser browser) {
                return browser.isPresent(el);
            }
        };
    }

    /**
     * Successful if the the element appears the expected number of times in the browser or W3C document.
     * This matcher is optimized.
     *
     * Example use for browser interaction:
     * <pre>{@code
     *   InBrowser browser = new InBrowser(driver);
     *   assertThat( myElement, ispresent(5).timesOrMoreIn(browser));
     *   assertThat( myElement, ispresent(5).timesIn(browser));
     *   assertThat( myElement, ispresent(5).timesOrLessIn(browser));
     *   }
     * </pre>
     * Same examples apply in case you have a Document (org.w3c.dom.Document).
     * @param nTimes - the reference number of times to be matched against. See examples.
     * @return a matcher that matches the number of times an element is present. See examples in the description.
     */
    public static IsPresentNTimes isPresent(int nTimes) {
        return new IsPresentNTimes(nTimes);
    }

    /**
     * Successful if element is present in the browser/document.
     * Example use:
     * <pre>
     *    {@code assertThat( path, isPresent().in(browser));}
     * </pre>
     *
     * @return a custom Hamcrest matcher
     */
    public static IsPresent isPresent() {
       return new IsPresent();
    }

    /**
     * Successful if element has the text equal to the given parameter in the browser/document.
     * Example use:
     * <pre>
     *     {@code assertThat( path, hasText().in(browser)); }
     * </pre>
     * @param text the text to equal to (case insensitive)
     * @return a custom Hamcrest matcher
     */
    public static HasText hasText(String text) {
        return new HasText(text);
    }

    /**
     * Successful if element is present in the browser or a W3C document. Useful especially when you have a reference count.
     * This matcher is optimized.
     *
     * For example:
     * <pre>{@code
     *    assertThat(browser, hasElements(path).present(5).times());
     *    assertThat(browser, hasElements(path).present(5).timesOrMore());
     *    assertThat(document, hasElements(path).present(5).timesOrLess());
     *    }
     * </pre>
     *
     * @param path The path of the elements to find
     * @return a matcher for the number of times an element is present.
     */
    public static HasElements hasElements(Path path) {
       return new HasElements(path);
    }

    /**
     * Successful if given element is present in the browser.
     * For example:
     * <pre>
     *    {@code assertThat( path, isPresentIn(browser));}
     * </pre>
     *
     * @param browser the browser instance to look in
     * @return a matcher that checks if an element is present in a browser
     */
    public static Matcher<Path> isPresentIn(final InBrowser browser) {
        return new TypeSafeMatcher<Path>() {
            private Path el;

            @Override
            public void describeTo(final Description description) {
                description.appendText("browser page contains " + el.toString());
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(CustomMatchersUtil.wrap(el) + " is absent");
            }

            @Override
            protected boolean matchesSafely(final Path el) {
                this.el = el;
                return browser.isPresent(el);
            }
        };
    }

    /**
     * Successful if given element is present in the document.
     * For example:
     * <pre>
     *    {@code assertThat( path, isPresentIn(document));}
     * </pre>
     *
     * @param document - a W#C document
     * @return a matcher that checks if an element is present in a document
     */
    public static Matcher<Path> isPresentIn(final Document document) {
        return new TypeSafeMatcher<Path>() {
            private Path el;

            @Override
            public void describeTo(final Description description) {
                description.appendText("document contains " + el);
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(el.toString() + " is absent");
            }

            @Override
            protected boolean matchesSafely(final Path el) {
                this.el = el;
                return pathExistsInDocument(document, el);

            }
        };
    }

    /**
     * Successful if given element is present and displayed in the browser. Relies on WebElement.isDisplayed(), thus non-atomic.
     * For example:
     * <pre>
     * {@code assertThat( path, isDisplayedIn(browser));}
     * </pre>
     * @param browser the browser instance to look in
     * @return a matcher that checks if an element is displayed in the browser
     */
    public static Matcher<Path> isDisplayedIn(final InBrowser browser) {
        return new TypeSafeMatcher<Path>() {
            private Path el;

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
                Wait<WebDriver> wait = getWaiter(browser);
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
    public static Matcher<Path> isNotDisplayedIn(final InBrowser browser) {
        return new TypeSafeMatcher<Path>() {
            private Path el;

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
                Wait<WebDriver> wait = getWaiter(browser);
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
     * <pre>
     *     {@code assertThat( path, isSelectedIn(browser)); }
     * </pre>
     *
     * @param browser the browser instance to look in
     * @return a matcher that checks if an element is selected in the browser
     */
    public static Matcher<Path> isSelectedIn(final InBrowser browser) {
        return new TypeSafeMatcher<Path>() {
            private Path el;

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
     * <pre>
     *     {@code assertThat( path, isEnabledIn(browser));}
     * </pre>
     *
     * @param browser the browser instance to look in
     * @return a matcher that checks if an element is enabled in the browser
     */
    public static Matcher<Path> isEnabledIn(final InBrowser browser) {
        return new TypeSafeMatcher<Path>() {
            private Path el;

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
     * Successful if given browser has no elements that correspond to the given path. The implementation of this is optimized.
     * For example:
     * <pre>
     *   {@code assertThat( browser, hasNoElement(path));}
     * </pre>
     *
     * @param el - the path that is expected not to exist in the browser
     * @return a matcher that is successful if the element does not appear in the browser
     */
    public static Matcher<InBrowser> hasNoElement(final Path el) {
        return new TypeSafeMatcher<InBrowser>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("browser page does not contain " + CustomMatchersUtil.wrap(el));
            }

            @Override
            protected void describeMismatchSafely(final InBrowser browser, final
            Description mismatchDescription) {
                mismatchDescription.appendText(el.toString() + " is present");
            }

            @Override
            protected boolean matchesSafely(final InBrowser browser) {
                return browser.isPresent(PathOperators.not(el));
            }
        };
    }

    /**
     * Successful if given browser has no elements that correspond to the given path. Equivalent to hasNoElement() matcher.
     * This is much better than doing not(isPresent()), because in case of success (i.e. the element is not there), it will return immidiately,
     * while the isPresent() will block until timeout is reached.
     * For example:
     * <pre>
     *    {@code assertThat( path, isAbsentFrom(browser)); }
     * </pre>
     *
     * @param browser the browser instance to look in
     * @return a matcher that is successful if the element does not appear in the browser
     */
    public static Matcher<Path> isAbsentFrom(final InBrowser browser) {
        return new TypeSafeMatcher<Path>() {
            private Path el;

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
                Path documentWithoutElement = html.that(ElementProperties.not(contains(el)));
                return browser.isPresent(documentWithoutElement);
            }
        };
    }

    /**
     * Successful if given document has no elements that correspond to the given path.
     * For example:
     * <pre>
     *    {@code assertThat( path, isAbsentFrom(doc));}
     * </pre>
     *
     * @param document - a W3C document
     * @return a matcher that is successful if the element does not appear in the document
     */
    public static Matcher<Path> isAbsentFrom(final Document document) {
        return new TypeSafeMatcher<Path>() {
            private Path el;

            @Override
            public void describeTo(final Description description) {
                description.appendText("document does not contain " + el);
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(el.toString() + " is present");
            }

            @Override
            protected boolean matchesSafely(final Path el) {
                this.el = el;
                return !pathExistsInDocument(document, el);
            }
        };
    }

    private static boolean pathExistsInDocument(Document document, Path el) {
        final NodeList nodes;
        try {
            nodes = PathParsers.findAllByPath(document, el);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("could not parse");
        }
        return nodes.getLength() > 0;
    }

}
