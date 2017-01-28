package com.github.loyada.jdollarx.custommatchers;


import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.RelationOperator;
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
    public static class NTimesMatcher extends TypeSafeMatcher<InBrowser> {
        private final Path path;
        private final int nTimes;
        private final RelationOperator relationOperator;
        int foundNTimes;

        NTimesMatcher(Path path, int nTimes, RelationOperator relationOperator) {
            this.path = path;
            this.nTimes = nTimes;
            this.relationOperator = relationOperator;
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText(String.format("browser page contains%s%s %d time%s",
                    RelationOperator.opAsEnglish(relationOperator), CustomMatchersUtil.wrap(path), nTimes, nTimes!=1 ? "s" : ""));
        }

        @Override
        protected void describeMismatchSafely(final InBrowser browser, final
        Description mismatchDescription) {
            mismatchDescription.appendText(CustomMatchersUtil.wrap(path) + " appears " + foundNTimes + " time" + (foundNTimes!=1 ? "s" : ""));
        }

        @Override
        protected boolean matchesSafely(final InBrowser browser) {
            foundNTimes = browser.numberOfAppearances(path);
            try{
                browser.findPageWithNumberOfOccurrences(path, nTimes, relationOperator);
                return true;
            } catch (NoSuchElementException e) {
                foundNTimes = browser.numberOfAppearances(path);
                return false;
            }
        }
    }
    private final Path path;
    private final int nTimes;

    public HasElementNTimes(final Path path, final int nTimes) {
       this.path = path;
        this.nTimes = nTimes;
    }

    public Matcher<InBrowser> times() {
        return new NTimesMatcher(path, nTimes, RelationOperator.exactly);
    }
    public Matcher<InBrowser> timesOrMore() {
        return new NTimesMatcher(path, nTimes, RelationOperator.orMore);
    }
    public Matcher<InBrowser> timesOrLess() {
        return new NTimesMatcher(path, nTimes, RelationOperator.orLess);
    }

}
