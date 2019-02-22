package com.github.loyada.jdollarx.singlebrowser.custommatchers;

import com.github.loyada.jdollarx.singlebrowser.AgGrid;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.NoSuchElementException;

import static java.lang.String.format;

/**
 * Hamcrest matchers for an AgGrid
 */
public class AgGridMatchers {
    /**
     * Verify that the grid, as defined, is present in the browser.
     * In case of an assertion error, gives a useful error message.
     * The assertion can be strict, in which case only the defined rows are expected to exist.
     *
     * @return a Hamcrest matcher
     */
    public static Matcher<AgGrid> isPresent() {
        return new TypeSafeMatcher<AgGrid>() {
            private AgGrid grid;
            private Exception ex;

            @Override
            public String toString() {
                return "The given Ag-Grid instance is present in the browser";
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("browser page contains " + grid);
            }

            @Override
            protected void describeMismatchSafely(final AgGrid el,
                                                  final Description mismatchDescription) {
                mismatchDescription.appendText(
                        format("%s is absent.\n Reason: could not find %s", grid, this.ex.getMessage()));
            }

            @Override
            protected boolean matchesSafely(final AgGrid grid) {
                this.grid = grid;
                try {
                    grid.findTableInBrowser();
                    return true;
                } catch(NoSuchElementException e) {
                    this.ex = e;
                    return false;
                } finally {
                    // return implicit timeout to a more reasonable value
                    if (grid.isVirtualized())
                        grid.setFinalTimeout();
                }
            }
        };
    }
}
