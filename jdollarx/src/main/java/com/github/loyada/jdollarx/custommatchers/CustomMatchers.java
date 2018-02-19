package com.github.loyada.jdollarx.custommatchers;

import com.github.loyada.jdollarx.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;

public class CustomMatchers {

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

    public static IsPresentNTimes isPresent(int nTimes) {
        return new IsPresentNTimes(nTimes);
    }

    public static IsPresent isPresent() {
       return new IsPresent();
    }
    public static HasText hasText(String text) {return new HasText(text);}

    public static HasElements hasElements(Path path) {
       return new HasElements(path);
    }

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
                return browser.isDisplayed(el);
            }
        };
    }

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

    public static Matcher<Path> isEnabledIn(final InBrowser browser) {
        return new TypeSafeMatcher<Path>() {
            private Path el;

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
                return browser.isPresent(PathOperators.not(el));
            }
        };
    }

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
