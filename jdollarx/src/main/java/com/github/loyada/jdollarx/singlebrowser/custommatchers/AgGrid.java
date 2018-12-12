package com.github.loyada.jdollarx.singlebrowser.custommatchers;

import com.github.loyada.jdollarx.*;
import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.BasicPath.html;
import static com.github.loyada.jdollarx.ElementProperties.*;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.find;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.scrollElement;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class AgGrid {
    private static final String COL_ID = "col-id";
    private static Path HEADER_CELL = div.that(hasClass("ag-header-cell"));
    private static Path ROW = div.that(hasRole("row"));
    private static Path CELL = div.that(hasRole("gridcell"));
    private final List<String> headers;
    private final List<Map<String, ElementProperty>> rows;
    private final boolean virtualized;
    private final boolean strict;

    private final Path tableContent;
    private final Path headerWrapper;
    private Map<String, String> colIdByHeader  = new HashMap<>();

    public static AgGridBuilder getBuilder() {
        return new AgGridBuilder();
    }

    public static class AgGridBuilder {
        private List<String> headers;
        boolean isVirtualized = true;
        private List<Map<String, ElementProperty>> rows;
        private Path container = html;
        private boolean strict = false;

        public AgGridBuilder withHeaders(List<String> headers) {
            this.headers = headers;
            return this;
        }

        public AgGridBuilder withoutVirtualization() {
            this.isVirtualized = false;
            return this;
        }

        public AgGridBuilder containedIn(Path container) {
            this.container = container;
            return this;
        }

        public AgGridBuilder isStrict() {
            this.strict = true;
            return this;
        }

        public AgGridBuilder withRowsAsElementProperties(List<Map<String, ElementProperty>> rows) {
            this.rows = rows;
            return this;
        }

        public AgGridBuilder withRowsAsStrings(List<Map<String, String>> rows) {
            this.rows = rows.stream().map(row -> {
                HashMap<String, ElementProperty> newRow = new HashMap<>();
                row.forEach((String key, String text) ->
                        newRow.put(key, hasAggregatedTextEqualTo(text)));
                return newRow;
            }).collect(toList());
            return this;
        }

        public AgGrid build() {
            return new AgGrid(headers, rows, isVirtualized, strict, container);
        }
    }

    private AgGrid(List<String> headers,
                  List<Map<String, ElementProperty>> rows,
                  boolean virtualized,
                  boolean strict,
                  Path tableContainer) {
        this.headers = headers;
        this.rows = rows;
        this.virtualized = virtualized;
        this.headerWrapper = div.withClass("ag-header-viewport").inside(tableContainer);
        this.tableContent = div.withClass("ag-body-viewport").inside(tableContainer);
        this.strict = strict;
    }

    @Override
    public String toString() {
        return "AgGrid{" +
                "headers=" + headers +
                ", rows=" + rows +
                '}';
    }

    private static ElementProperty hasIndex(int ind) {
        return hasAttribute("row-index", Integer.toString(ind));
    }

    private static ElementProperty hasColumnId(String id) {
        return hasAttribute(COL_ID, id);
    }

    private void findColumnMapping() {
        InBrowserSinglton.driver.manage().timeouts().implicitlyWait(1, TimeUnit.MILLISECONDS);

        headers.forEach( columnText -> {
                    Path headerEl = HEADER_CELL.inside(headerWrapper)
                                    .that(hasAggregatedTextEqualTo(columnText))
                                    .describedBy(format("header '%s'", columnText));
                    WebElement headerCell = virtualized ?
                            scrollElement(tableContent).rightUntilElementIsPresent(headerEl) :
                            InBrowserSinglton.find(headerEl);
                    String columnId = headerCell.getAttribute(COL_ID);
                    if (columnId==null)
                        throw new UnsupportedOperationException("could not find column id for " + headerEl);
                    colIdByHeader.put(columnText, columnId);
                    if(virtualized)
                        scrollElement(tableContent).toTopLeftCorner();
        });
    }

    private void findRowInBrowser(int index) {
        Map<String, ElementProperty> row = rows.get(index);

        scrollElement(tableContent).toTopLeftCorner();
        final Path myRow = ROW.that(hasIndex(index)).inside(tableContent)
                .describedBy(format("row with index %d", index));
        scrollElement(tableContent).downUntilElementIsPresent(myRow);

        row.forEach((columnTitle, hasExpectedValue) -> {
            String id = colIdByHeader.get(columnTitle);
            if (id==null) {
                throw new IllegalArgumentException(columnTitle);
            }

            Path cell = CELL.inside(myRow).describedBy(format("cell in %s", myRow));
            Path myCell = cell.that(hasColumnId(id))
                            .and(hasExpectedValue);
            scrollElement(tableContent).rightUntilElementIsPresent(myCell);
            scrollElement(tableContent).toLeftCorner();
        });
    }

    private void findNonVirtualizedRowInBrowser(int index) {
        Map<String, ElementProperty> row = rows.get(index);

        row.forEach((columnTitle, hasExpectedValue) -> {
            String id = colIdByHeader.get(columnTitle);
            if (id==null) {
                throw new IllegalArgumentException(columnTitle);
            }

            Path myRow = ROW.that(hasIndex(index)).inside(tableContent)
                    .describedBy(format("row with index %d", index));
            Path cell = CELL.inside(myRow).describedBy(format("cell in %s", myRow));
            Path myCell = cell.that(hasColumnId(id))
                    .and(hasExpectedValue);
            InBrowserSinglton.find(myCell);
        });
    }

    private void findTableInBrowser() {
        findColumnMapping();
        IntStream rowsIndex = IntStream.range(0, rows.size());
        if (virtualized) {
            rowsIndex.forEach(ind -> this.findRowInBrowser(ind));
        } else {
            rowsIndex.forEach(ind -> this.findNonVirtualizedRowInBrowser(ind));
        }

        if (strict) {
            verifyNoRowWithIndex(rows.size());
        }
    }

    private void verifyNoRowWithIndex(int index) {
        Path myRow = ROW.that(hasIndex(index)).inside(tableContent)
                .describedBy(format("row with index %d", index));
        try {
            if (virtualized){
                scrollElement(tableContent).downUntilElementIsPresent(myRow);
            } else {
                find(myRow);
            }
        } catch(NoSuchElementException e) {
            // Good! no extra rows
            return;
        }
        throw new NoSuchElementException(format("grid with exactly %d rows. Found too many rows.", index));
    }


    public static Matcher<AgGrid> isPresent() {
        return new TypeSafeMatcher<AgGrid>() {
            private AgGrid grid;
            private Exception ex;

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
                }
            }
        };
    }
}
