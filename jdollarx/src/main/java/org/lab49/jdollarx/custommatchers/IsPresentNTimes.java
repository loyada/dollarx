package org.lab49.jdollarx.custommatchers;

import org.lab49.jdollarx.InBrowser;
import org.lab49.jdollarx.Path;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.lab49.jdollarx.custommatchers.CustomMatchersUtil.wrap;

public class IsPresentNTimes {
    private final int nTimes;

    public IsPresentNTimes(int nTimes){
        this.nTimes = nTimes;
    }

    public Matcher<Path> timesIn(InBrowser browser){
        return new TypeSafeMatcher<Path>() {
            private int foundNTimes;
            private Path el;

            @Override
            public void describeTo(final Description description) {
                 description.appendText( String.format("browser page contains %s %d time%s", wrap(el), nTimes, nTimes != 1 ? "s" : ""));
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
                return foundNTimes == nTimes;
            }
        };
    }
}
