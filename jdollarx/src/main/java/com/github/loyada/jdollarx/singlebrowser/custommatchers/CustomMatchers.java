package com.github.loyada.jdollarx.singlebrowser.custommatchers;


import com.github.loyada.jdollarx.*;
import com.github.loyada.jdollarx.custommatchers.HasText;
import com.github.loyada.jdollarx.custommatchers.IsPresent;
import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


public class CustomMatchers {

    public static IsPresentNTimes isPresent(int nTimes) {
        return new IsPresentNTimes(nTimes);
    }

    public static Matcher<Path> isPresent() {
        return  new IsPresent().in(new InBrowser(InBrowserSinglton.driver));
    }

    public static Matcher<Path> hasText(String text) {
        return new HasText(text).in(new InBrowser(InBrowserSinglton.driver));
    }

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
