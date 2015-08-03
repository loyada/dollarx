package info.dollarx.custommatchers.hamcrest;


import info.dollarx.Browser;
import info.dollarx.Path;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.NoSuchElementException;

/**
 * This matcher is optimized for the success use-case. In that case it match for a single element
 * with exact number of elements wanted.
 * In case of failure, it will make another call to get the actual number of elements on
 * the page, in order to provide a detailed error message.
 * So the trade off is: In case of success it's faster, In case of failure it's slower. It makes sense since most
 * of the time we expect success.
 */
public class HasElementNTimes {

    private final Path path;
    private final int nTimes;

    public HasElementNTimes(final Path path, final int nTimes) {
       this.path = path;
        this.nTimes = nTimes;
    }

    public Matcher<Browser> times() {
        return new TypeSafeMatcher<Browser>() {
            int foundNTimes;

            @Override
            public void describeTo(final Description description) {
                description.appendText(String.format("browser page contains %s %d time%s", CustomMatchersUtil.wrap(path), nTimes, nTimes!=1 ? "s" : ""));
            }

            @Override
            protected void describeMismatchSafely(final Browser browser, final
            Description mismatchDescription) {
                mismatchDescription.appendText(CustomMatchersUtil.wrap(path) + " appears " + foundNTimes + " time" + (foundNTimes!=1 ? "s" : ""));
            }

            @Override
            protected boolean matchesSafely(final Browser browser) {
                foundNTimes = browser.numberOfAppearances(path);
                try{
                    browser.findPageWithNumberOfOccurrences(path, nTimes);
                    return true;
                } catch (NoSuchElementException e) {
                    foundNTimes = browser.numberOfAppearances(path);
                    return foundNTimes == nTimes;
                }
            }
        };
    }

}
