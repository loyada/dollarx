package info.testtools.jdollarx.custommatchers;

import info.testtools.jdollarx.InBrowser;
import info.testtools.jdollarx.Path;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

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
                 description.appendText( String.format("browser page contains %s %d time%s", CustomMatchersUtil.wrap(el), nTimes, nTimes != 1 ? "s" : ""));
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(CustomMatchersUtil.wrap(el) + " appears " + foundNTimes + " time" + (foundNTimes!=1 ? "s" : ""));
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
