package org.lab49.jdollarx.custommatchers;

import org.lab49.jdollarx.InBrowser;
import org.lab49.jdollarx.Path;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.lab49.jdollarx.custommatchers.CustomMatchersUtil.wrap;

public class IsPresent {

    public IsPresent(){}

    public static Matcher<Path> in(final InBrowser browser) {
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
}
