package com.github.loyada.jdollarx.singlebrowser.custommatchers;

import com.github.loyada.jdollarx.ElementProperty;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import com.google.common.collect.ImmutableList;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.BasicPath.html;
import static com.github.loyada.jdollarx.BasicPath.span;
import static com.github.loyada.jdollarx.ElementProperties.hasAggregatedTextEqualTo;
import static com.github.loyada.jdollarx.ElementProperties.hasAnyOfClasses;
import static com.github.loyada.jdollarx.ElementProperties.hasAttribute;
import static com.github.loyada.jdollarx.ElementProperties.hasClass;
import static com.github.loyada.jdollarx.ElementProperties.hasRole;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.clickOn;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.find;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.scrollElement;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;

/**
 * Custom class to validate the presence of an AgGrid, since this can be tricky.
 * It supports virtualized and non-virtualized tables.
 * It should be used like other custom matchers in the package.
 */
public class AgGrid {
    private static final String COL_ID = "col-id";
    private static Path HEADER_CELL = div.that(hasClass("ag-header-cell"));
    private static Path HEADER_TXT = span.that(hasRef("eText"));
    private static Path ROW = div.that(hasRole("row"));
    private static Path CELL = div.that(hasRole("gridcell"));
    private final List<String> headers;
    private final List<Map<String, ElementProperty>> rows;
    private final boolean virtualized;
    private final boolean strict;
    private final Path tableViewport;

    private Path tableHorizontalScroll;
    private final Path tableContent;
    private final Path headerWrapper;
    private Map<String, String> colIdByHeader  = new HashMap<>();
    private int operationTimeout = 5, finalTimeout = 5000;

    private static ElementProperty hasRef(String role) {
        return hasAttribute("ref", role);
    }

    public static AgGridBuilder getBuilder() {
        return new AgGridBuilder();
    }

    public static class AgGridBuilder {
        private List<String> headers;
        boolean isVirtualized = true;
        private List<Map<String, ElementProperty>> rows;
        private Path container = html;
        private boolean strict = false;

        private AgGridBuilder(){}

        /**
         * The headers of the columns
         * @param headers - the headers of the columns
         * @return AgGridBuilder
         */
        public AgGridBuilder withHeaders(List<String> headers) {
            this.headers = ImmutableList.copyOf(headers);
            return this;
        }

        /**
         * without virtualization. The default is with virtualization.
         * @return AgGridBuilder
         */
        public AgGridBuilder withoutVirtualization() {
            this.isVirtualized = false;
            return this;
        }

        /**
         *  optional - define the container of the grid
         * @param container the Path of the container of the grid
         * @return AgGridBuilder
         */
        public AgGridBuilder containedIn(Path container) {
            this.container = container;
            return this;
        }

        /**
         * The assertions will be strict - if there are extra rows, it will fail.
         * @return AgGridBuilder
         */
        public AgGridBuilder isStrict() {
            this.strict = true;
            return this;
        }

        /**
         * Define the rows in the table, in order.
         * @param rows - A list of rows, where each row is a map of the
         *             column name to the property that describes the expected content
         * @return AgGridBuilder
         */
        public AgGridBuilder withRowsAsElementProperties(List<Map<String, ElementProperty>> rows) {
            this.rows = rows.stream().map(row -> {
                HashMap<String, ElementProperty> newRow = new HashMap<>();
                row.forEach(newRow::put);
                return newRow;
            }).collect(toList());
            return this;
        }

        /**
         * Define the rows in the table, in order.
         * @param rows - A list of rows, where each row is a map of the
         *             column name to the text.
         * @return AgGridBuilder
         */
        public AgGridBuilder withRowsAsStrings(List<Map<String, String>> rows) {
            this.rows = rows.stream().map(row -> {
                HashMap<String, ElementProperty> newRow = new HashMap<>();
                row.forEach((String key, String text) ->
                    newRow.put(key, hasAggregatedTextEqualTo(text)));
                return newRow;
            }).collect(toList());
            return this;
        }

        /**
         * Create an AgGrid definition
         * @return AgGrid instance
         */
        public AgGrid build() {
            if (headers==null || rows==null){
                throw new IllegalArgumentException();
            }
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
        this.headerWrapper = div.that(hasAnyOfClasses("ag-pinned-right-header", "ag-pinned-left-header","ag-header-viewport")).inside(tableContainer);
        this.tableContent = div.that(hasAnyOfClasses("ag-body-viewport", "ag-body")).inside(tableContainer);
        this.tableViewport = div.withClass("ag-body-viewport").inside(tableContainer);
        this.tableHorizontalScroll = div.withClass("ag-center-cols-viewport").inside(tableContainer);
        this.strict = strict;
    }


    /**
     * Override the default timeout threshold for finding elements while scrolling the table.
     * The default is 5 milliseconds
     * @param millisecs - the timeout in milliseconds
     */
    public void overrideTimeoutDuringOperation(int millisecs) {
        this.operationTimeout = millisecs;
    }

    /**
     * Override the default timeout threshold it reverts to when table operations are done.
     * The default is 5000 milliseconds
     * @param millisecs - the timeout in milliseconds
     */
    public void overrideTimeoutWhenDone(int millisecs) {
        this.finalTimeout = millisecs;
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
        checkAndAdaptToCorrectAgGridVersion();
        headers.forEach( columnText -> {
                    Path headerEl = HEADER_CELL
                            .inside(headerWrapper)
                            .containing(HEADER_TXT.that(hasAggregatedTextEqualTo(columnText)))
                            .describedBy(format("header '%s'", columnText));
                    WebElement headerCell = virtualized ?
                            scrollElement(tableHorizontalScroll).rightUntilElementIsPresent(headerEl) :
                            InBrowserSinglton.find(headerEl);
                    String columnId = headerCell.getAttribute(COL_ID);
                    if (columnId==null)
                        throw new UnsupportedOperationException("could not find column id for " + headerEl);
                    colIdByHeader.put(columnText, columnId);
                    if(virtualized)
                        scrollElement(tableHorizontalScroll).toTopLeftCorner();
        });
    }

    private void checkAndAdaptToCorrectAgGridVersion() {
        if (!InBrowserSinglton.isPresent(tableHorizontalScroll)) {
            tableHorizontalScroll = tableViewport;
        }
    }

    /**
     * Click on the menu of a the column with the given header
     * @param headerText - the header text
     */
    public void clickMenuOfHeader(String headerText) {
        Path headerEl = getVisibleHeaderPath(headerText);
        clickOn(span.that(hasRef("eMenu")).inside(headerEl));
    }

    /**
     * Click on the 'sort' column with the given header
     * @param headerText - the header text
     */
    public void clickOnSort(String headerText) {
        Path headerEl = getVisibleHeaderPath(headerText);
        clickOn(div.that(hasRef("eLabel")).inside(headerEl));
    }

    /**
     * Make sure the given column header is visible, and returns a Path element to access it.
     * This is useful to perform direct operations on that element or access other DOM elements contained in the header.
     * @param headerText - the header text
     * @return the Path element to access the column header
     */
    public Path getVisibleHeaderPath(String headerText) {
        setOperationTimeout();

        try {
            scrollElement(tableViewport).toTopLeftCorner();
            scrollElement(tableHorizontalScroll).toLeftCorner();
            Path headerEl = HEADER_CELL
                    .inside(headerWrapper)
                    .containing(HEADER_TXT.that(hasAggregatedTextEqualTo(headerText)))
                    .describedBy(format("header '%s'", headerText));
            scrollElement(tableViewport).rightUntilElementIsVisible(headerEl);
            return headerEl;
        } finally {
            setFinalTimeout();
        }
    }


    private void setOperationTimeout() {
        driver.manage().timeouts().implicitlyWait(operationTimeout, MILLISECONDS);
    }

    private void setFinalTimeout() {
        driver.manage().timeouts().implicitlyWait(finalTimeout, MILLISECONDS);
    }

    /**
     * Scroll until the row with the given index is visible, and return a Path element that matches it.
     * Useful for performing operations or accessing fields in the wanted row.
     * @param n the number of row in the table, as visible to the user
     * @return a Path element that allows to access the row
     */
    public Path ensureVisibilityOfRowWithIndex(int n) {
        setOperationTimeout();
        try {
            scrollElement(tableViewport).toTopLeftCorner();
            scrollElement(tableHorizontalScroll).toLeftCorner();
            final Path nthRow = ROW.that(hasIndex(n)).inside(tableContent)
                    .describedBy(format("row with index %d", n));
            scrollElement(tableViewport).downUntilElementIsVisible(nthRow);
            return nthRow;
        } finally {
            setFinalTimeout();
        }
    }

    /**
     * Scroll until the row with the given index, as well as the given column, is visible.
     * It return a Path element that matches the wanted cell in row.
     * Useful for performing operations or accessing fields in the wanted cell (for example: edit it)
     * @param index the number of row in the table, as visible to the user
     * @param columnTitle the header title of the wanted cell in the row
     * @return the Path element to access the wanted cell in the wanted row
     */
    public Path ensureVisibilityOfRowWithIndexAndColumn(int index, String columnTitle) {
        try {
            Path nthRow = ensureVisibilityOfRowWithIndex(index);
            setOperationTimeout();
            String id = colIdByHeader.get(columnTitle);
            if (id == null) {
                throw new IllegalArgumentException(columnTitle);
            }

            Path cell = CELL.inside(nthRow).describedBy(format("cell in %s", nthRow));
            Path cellOfTheColumn = cell.that(hasColumnId(id));
            scrollElement(tableHorizontalScroll).toLeftCorner();
            scrollElement(tableHorizontalScroll).rightUntilElementIsVisible(cellOfTheColumn);
            return cellOfTheColumn;
        } finally {
            setFinalTimeout();
        }
    }
    
    private void findRowInBrowser(int index) {
        Map<String, ElementProperty> row = rows.get(index);

        scrollElement(tableViewport).toTopLeftCorner();
        scrollElement(tableHorizontalScroll).toLeftCorner();
        final Path myRow = ROW.that(hasIndex(index)).inside(tableContent)
            .describedBy(format("row with index %d", index));
        scrollElement(tableViewport).downUntilElementIsPresent(myRow);

        row.forEach((columnTitle, hasExpectedValue) -> {
            String id = colIdByHeader.get(columnTitle);
            if (id==null) {
                throw new IllegalArgumentException(columnTitle);
            }

            Path cell = CELL.inside(myRow).describedBy(format("cell in %s", myRow));
            Path myCell = cell.that(hasColumnId(id))
                .and(hasExpectedValue);
            scrollElement(tableHorizontalScroll).rightUntilElementIsPresent(myCell);
            scrollElement(tableHorizontalScroll).toLeftCorner();
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

    private void verifyAGridIsPresent() {
        InBrowserSinglton.find(HEADER_CELL.inside(headerWrapper));
    }

    private void findTableInBrowser() {
        verifyAGridIsPresent();

        if (virtualized) {
            setOperationTimeout();
        }
        findColumnMapping();
        IntStream rowsIndex = IntStream.range(0, rows.size());
        if (virtualized) {
            rowsIndex.forEach(this::findRowInBrowser);
        } else {
            rowsIndex.forEach(this::findNonVirtualizedRowInBrowser);
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
                scrollElement(tableViewport).downUntilElementIsPresent(myRow);
            } else {
                find(myRow);
            }
        } catch(NoSuchElementException e) {
            // Good! no extra rows
            return;
        }
        throw new NoSuchElementException(format("grid with exactly %d rows. Found too many rows.", index));
    }


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
                    if (grid.virtualized)
                        grid.setFinalTimeout();
                }
            }
        };
    }
}
