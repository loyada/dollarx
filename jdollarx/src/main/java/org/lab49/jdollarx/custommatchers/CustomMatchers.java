package org.lab49.jdollarx.custommatchers;

import org.lab49.jdollarx.BasicPath;
import org.lab49.jdollarx.InBrowser;
import org.lab49.jdollarx.Path;
import org.lab49.jdollarx.PathOperators;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.lab49.jdollarx.custommatchers.CustomMatchersUtil.wrap;

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
                mismatchDescription.appendText(wrap(el) + " is absent");
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
                mismatchDescription.appendText(wrap(el) + " is absent");
            }

            @Override
            protected boolean matchesSafely(final Path el) {
                this.el = el;
                return browser.isPresent(el);
            }
        };
    }


    public static Matcher<Path> isPresentNTimes(final Integer n, final InBrowser browser) {
        return new TypeSafeMatcher<Path>() {
            private int foundNTimes;
            private Path el;

            @Override
            public void describeTo(final Description description) {
                description.appendText(String.format("browser page contains %s %d time%s", wrap(el), n, n!=1 ? "s" : ""));
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(wrap(el) + " appears " + foundNTimes + " time" + (foundNTimes!=1 ? "s" : ""));
            }

            @Override
            protected boolean matchesSafely(final Path el) {
               this.el = el;
               foundNTimes = browser.numberOfAppearances(el);
                return foundNTimes == n;
            }
        };
    }

    public static Matcher<BasicPath> isDisplayedIn(final InBrowser browser) {
        return new TypeSafeMatcher<BasicPath>() {
            private BasicPath el;

            @Override
            public void describeTo(final Description description) {
                description.appendText(el + " is displayed");
            }

            @Override
            protected void describeMismatchSafely(final BasicPath el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(el + " is not displayed");
            }

            @Override
            protected boolean matchesSafely(final BasicPath el) {
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
                description.appendText("browser page does not contain " + wrap(el));
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

}
