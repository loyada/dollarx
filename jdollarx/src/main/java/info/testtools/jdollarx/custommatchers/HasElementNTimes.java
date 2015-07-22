package info.testtools.jdollarx.custommatchers;


import info.testtools.jdollarx.InBrowser;
import info.testtools.jdollarx.Path;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class HasElementNTimes {

    private final Path path;
    private final int nTimes;

    public HasElementNTimes(final Path path, final int nTimes) {
       this.path = path;
        this.nTimes = nTimes;
    }

    public Matcher<InBrowser> times() {
        return new TypeSafeMatcher<InBrowser>() {
            int foundNTimes;

            @Override
            public void describeTo(final Description description) {
                description.appendText(String.format("browser page contains %s %d time%s", CustomMatchersUtil.wrap(path), nTimes, nTimes!=1 ? "s" : ""));
            }

            @Override
            protected void describeMismatchSafely(final InBrowser browser, final
            Description mismatchDescription) {
                mismatchDescription.appendText(CustomMatchersUtil.wrap(path) + " appears " + foundNTimes + " time" + (foundNTimes!=1 ? "s" : ""));
            }

            @Override
            protected boolean matchesSafely(final InBrowser browser) {
                foundNTimes = browser.numberOfAppearances(path);
                return foundNTimes == nTimes;
            }
        };
    }

}
