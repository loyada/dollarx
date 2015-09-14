package info.testtools.jdollarx.custommatchers;

import info.testtools.jdollarx.InBrowser;
import info.testtools.jdollarx.Path;
import info.testtools.jdollarx.RelationOperator;
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
public class IsPresentNTimes {
    private final int nTimes;

    public IsPresentNTimes(int nTimes){
        this.nTimes = nTimes;
    }

    static class NTimesMatcher extends TypeSafeMatcher<Path> {
        private  Path path;
        private final int nTimes;
        private final RelationOperator relationOperator;
        private final InBrowser browser;
        int foundNTimes;

        public NTimesMatcher(final int nTimes, final RelationOperator relationOperator, final InBrowser browser) {
            this.nTimes = nTimes;
            this.relationOperator = relationOperator;
            this.browser = browser;
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText( String.format("browser page contains %s%s%d time%s",
                     CustomMatchersUtil.wrap(path), RelationOperator.opAsEnglish(relationOperator), nTimes, nTimes != 1 ? "s" : ""));
        }

        @Override
        protected void describeMismatchSafely(final Path el, final
        Description mismatchDescription) {
            mismatchDescription.appendText(CustomMatchersUtil.wrap(el) + " appears " + foundNTimes + " time" + (foundNTimes!=1 ? "s" : ""));
        }

        @Override
        protected boolean matchesSafely(final Path el) {
            this.path = el;
            try{
                browser.findPageWithNumberOfOccurrences(el, nTimes, relationOperator);
                return true;
            } catch (NoSuchElementException e) {
                foundNTimes = browser.numberOfAppearances(el);
                return false;
            }
        }
    }

    public Matcher<Path> timesIn(InBrowser browser){
        return new NTimesMatcher(nTimes, RelationOperator.exactly, browser);
    }

    public Matcher<Path> timesOrMoreIn(InBrowser browser){
        return new NTimesMatcher(nTimes, RelationOperator.orMore, browser);
    }

    public Matcher<Path> timesOrLessIn(InBrowser browser){
        return new NTimesMatcher(nTimes, RelationOperator.orLess, browser);
    }
}
