package com.github.loyada.jdollarx.singlebrowser;

import com.github.loyada.jdollarx.ElementProperty;
import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.sizing.ElementResizer;
import com.google.common.collect.ImmutableList;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.BasicPath.html;
import static com.github.loyada.jdollarx.BasicPath.span;
import static com.github.loyada.jdollarx.ElementProperties.contains;
import static com.github.loyada.jdollarx.ElementProperties.hasAggregatedTextEqualTo;
import static com.github.loyada.jdollarx.ElementProperties.hasAnyOfClasses;
import static com.github.loyada.jdollarx.ElementProperties.hasAttribute;
import static com.github.loyada.jdollarx.ElementProperties.hasClass;
import static com.github.loyada.jdollarx.ElementProperties.hasRole;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.clickAt;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.clickOn;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.find;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.findAll;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.scrollElement;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.scrollElementWithStepOverride;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

/**
 * Custom class to validate the presence of an AgGrid, and interact with it, since it can be tricky.
 * It supports virtualized and non-virtualized tables.
 * It should be used like other custom matchers in the package.
 */
public class AgGrid {
    public static final String COL_ID = "col-id";
    public static final Path HEADER_CELL = div.that(hasClass("ag-header-cell"));
    public static final Path HEADER_TXT = span.that(hasRef("eText"));
    public static final Path ROW = div.that(hasRole("row"));
    public static final Path CELL = div.that(hasRole("gridcell"));
    private final List<String> headers;
    private final List<Map<String, ElementProperty>> rows;
    private final boolean virtualized;
    private final boolean strict;
    private final Path tableViewport;
    private int stepSize = 60;

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
         * Define the rows in the table, in order. This version can be faster, in case the columns
         * are ordered as they appear in the table, and the table is virtualized
         * @param rows - A list of rows, where each row is a map of the
         *             column name to the property that describes the expected content
         * @return AgGridBuilder
         */
        public AgGridBuilder withRowsAsElementPropertiesInOrder(List<List<Map.Entry<String, ElementProperty>>> rows) {
            this.rows = rows.stream().map(row -> {
                LinkedHashMap<String, ElementProperty> newRow = new LinkedHashMap<>();
                row.forEach((Map.Entry<String, ElementProperty> entry) -> newRow.put(entry.getKey(), entry.getValue()));
                return newRow;
            }).collect(toList());
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
         * Define the rows in the table, in order. This version can be faster, in case the columns
         * @param rows - A list of rows, where each row is a map of the
         *             column name to the text.
         * @return AgGridBuilder
         */
        public AgGridBuilder withRowsAsStringsInOrder(List<List<Map.Entry<String, String>>> rows) {
            this.rows = rows.stream().map(row -> {
                LinkedHashMap<String, ElementProperty> newRow = new LinkedHashMap<>();
                row.forEach((Map.Entry<String, String> entry) -> newRow.put(entry.getKey(), hasAggregatedTextEqualTo(entry.getValue())));
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
                LinkedHashMap<String, ElementProperty> newRow = new LinkedHashMap<>();
                row.forEach((String key, String text) -> newRow.put(key, hasAggregatedTextEqualTo(text)));
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

    public boolean isVirtualized() {
        return virtualized;
    }

    /**
     * Override the default step size of scrolling when moving through a grid
     * @param size step size in pixels
     */
    public void setScrollStep(int size) {
        stepSize=size;
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
                            findHeader(headerEl) :
                            InBrowserSinglton.find(headerEl);
                    String columnId = headerCell.getAttribute(COL_ID);
                    if (columnId==null)
                        throw new UnsupportedOperationException("could not find column id for " + headerEl);
                    colIdByHeader.put(columnText, columnId);
        });
        scrollElement(tableHorizontalScroll).toTopLeftCorner();
    }

    private WebElement findHeader(Path headerEl) {
        Operations.ScrollElement scroll = scrollElementWithStepOverride(tableHorizontalScroll, stepSize);
        try {
            return scroll.rightUntilElementIsPresent(headerEl);
        } catch (NoSuchElementException e) {
            if (virtualized) {
                scroll.toTopLeftCorner();
                return scroll.rightUntilElementIsPresent(headerEl);
            }
             else throw e;
        }
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
        clickAt(span.that(hasRef("eMenu")).inside(headerEl));
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
        checkAndAdaptToCorrectAgGridVersion();
        setOperationTimeout();

        try {
            scrollElement(tableViewport).toTopLeftCorner();
            scrollElement(tableHorizontalScroll).toLeftCorner();
            Path headerEl = HEADER_CELL
                    .inside(headerWrapper)
                    .containing(HEADER_TXT.that(hasAggregatedTextEqualTo(headerText)))
                    .describedBy(format("header '%s'", headerText));
            scrollElement(tableViewport).rightUntilPredicate(headerEl, getColumnVisiblityTest());
            return headerEl;
        } finally {
            setFinalTimeout();
        }
    }


    private void setOperationTimeout() {
        driver.manage().timeouts().implicitlyWait(operationTimeout, MILLISECONDS);
    }

    public void setFinalTimeout() {
        driver.manage().timeouts().implicitlyWait(finalTimeout, MILLISECONDS);
    }

    private int getBottomOfTable() {
        WebElement content = find(tableContent);
        return content.getSize().height + content.getLocation().getY();
    }

    private int getRightmostOfTable() {
        Long width = ElementResizer.getVisibleWidth(tableContent);
        WebElement content = find(tableContent);
        return (int) (width + content.getLocation().getX());
    }

    private Predicate<WebElement> getRowVisiblityTest() {
        int bottomOfTable = getBottomOfTable();
        return el -> el.isDisplayed() && el.getLocation().y < bottomOfTable;
    }

    private Predicate<WebElement> getColumnVisiblityTest() {
        int rightmost = getRightmostOfTable();
        return el -> el.isDisplayed() && el.getLocation().x < rightmost ;
    }

    /**
     * Scroll until the row with the given index is visible, and return a Path element that matches it.
     * Useful for performing operations or accessing fields in the wanted row.
     * @param n the number of row in the table, as visible to the user
     * @return a Path element that allows to access the row
     */
    public Path ensureVisibilityOfRowWithIndex(int n) {
        checkAndAdaptToCorrectAgGridVersion();
        setOperationTimeout();
        final Path nthRow = ROW.that(hasIndex(n)).inside(tableContent)
                .describedBy(format("row with index %d", n));
        Predicate<WebElement> isVisible = getRowVisiblityTest();

        try {
             if(findAll(nthRow).
                     stream()
                     .anyMatch(isVisible))
                return nthRow;
        } catch (Exception e) {
            // will have to search through the table
        }

        try {
            scrollElement(tableViewport).toTopLeftCorner();
            scrollElement(tableHorizontalScroll).toLeftCorner();
            scrollElementWithStepOverride(tableViewport, stepSize).downUntilPredicate(nthRow, isVisible);
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
        setOperationTimeout();
        checkAndAdaptToCorrectAgGridVersion();

        try {
            if (colIdByHeader.isEmpty()) {
                findColumnMapping();
            }
            Path nthRow = ensureVisibilityOfRowWithIndex(index);
            String id = colIdByHeader.get(columnTitle);
            if (id == null) {
                throw new IllegalArgumentException(columnTitle);
            }

            Path cell = CELL.inside(nthRow).describedBy(format("cell in %s", nthRow));
            Path cellOfTheColumn = cell.that(hasColumnId(id));
            scrollElement(tableHorizontalScroll).toLeftCorner();
            scrollElementWithStepOverride(tableHorizontalScroll, stepSize).rightUntilPredicate(cellOfTheColumn, getColumnVisiblityTest());
            return cellOfTheColumn;
        } finally {
            setFinalTimeout();
        }
    }

    public int findRowIndex(Map<String, ElementProperty> row) {
        if (!virtualized) {
            Path rowEl =  findNonVirtualizedRowInBrowser(row);
            return parseInt(find(rowEl).getAttribute("row-index"));
        }

        setOperationTimeout();
        checkAndAdaptToCorrectAgGridVersion();
        try {
            if (colIdByHeader.isEmpty()) {
                findColumnMapping();
            }
            OptionalInt foundRow = range(0, 10000000).
                    filter(i -> {
                       try {
                            findRowInBrowser(i, row);
                            return true;
                       } catch (NoSuchElementException e) {
                           return false;
                       }
                    }).
                    findFirst();
            return foundRow.orElseThrow(NotFoundException::new);
        } finally {
            setFinalTimeout();
        }
    }

    private Path findRowInBrowser(int index, Map<String, ElementProperty> row) {
        scrollElement(tableViewport).toTopLeftCorner();
        scrollElement(tableHorizontalScroll).toLeftCorner();
        final Path myRow = ROW.that(hasIndex(index)).inside(tableContent)
            .describedBy(format("row with index %d", index));

        try {
            scrollElementWithStepOverride(tableViewport, stepSize).downUntilElementIsPresent(myRow);
        } catch (NoSuchElementException e) {
            throw new IndexOutOfBoundsException(format("row %d was not found. cause: %s", index, e));
        }
        validateRowContent(row, myRow);
        scrollElementWithStepOverride(tableViewport, stepSize).downUntilPredicate(myRow, getRowVisiblityTest());
        return myRow;
    }

    private void validateRowContent(Map<String, ElementProperty> row, Path myRow) {
        scrollElement(tableHorizontalScroll).toLeftCorner();
        Function<String, Path> getCollumn = (String columnTitle) -> {
            String id = colIdByHeader.get(columnTitle);
            if (id==null) {
                throw new IllegalArgumentException(columnTitle);
            }
            Path cell = CELL.inside(myRow).describedBy(format("cell in %s", myRow));
            return cell.that(hasColumnId(id));
        };

        row.forEach((columnTitle, hasExpectedValue) -> {
            Path columnCell = getCollumn.apply(columnTitle);
            try {
                scrollToFindCellWithValue(hasExpectedValue, columnCell);
            } catch(NoSuchElementException e) {
                scrollElement(tableHorizontalScroll).toLeftCorner();
                scrollToFindCellWithValue(hasExpectedValue, columnCell);
            }
        });
    }

    private void scrollToFindCellWithValue(ElementProperty hasExpectedValue, Path columnCell) {
        scrollElementWithStepOverride(tableHorizontalScroll, stepSize).rightUntilElementIsPresent(columnCell);
        find(columnCell.that(hasExpectedValue));
    }

    private Path findNonVirtualizedRowInBrowser(Path rowWithOptionalIndex, Map<String, ElementProperty> contentByColumn) {
        Path rowInTable = rowWithOptionalIndex.inside(tableContent)
                .describedBy(format("%s", rowWithOptionalIndex));

        // Since the number of columns could be large, keep each separate to allow easier troubleshooting,
        // and avoid huge xpath
        List<Path> cells = contentByColumn.entrySet().stream().map( entry -> {
            String columnTitle = entry.getKey();
            ElementProperty hasExpectedValue = entry.getValue();

            String id = colIdByHeader.get(columnTitle);
            if (id == null) {
                throw new IllegalArgumentException(columnTitle);
            }
            return CELL.that(hasColumnId(id)).and(hasExpectedValue);
        }).collect(toList());
        Path theRow = cells.stream().reduce(
                rowInTable,
                (r, cell) -> r.that(contains(cell))
        );
        InBrowserSinglton.find(theRow);
        return theRow;
    }


    private Path findNonVirtualizedRowInBrowser(Map<String, ElementProperty> contentByColumn) {
        return findNonVirtualizedRowInBrowser(ROW.describedBy("row"), contentByColumn);
    }

    private Path findNonVirtualizedRowInBrowser(Integer index, Map<String, ElementProperty> contentByColumn) {
        Path rowWithIndex = ROW.that(hasIndex(index)).describedBy(format("row with index %d", index));
        return findNonVirtualizedRowInBrowser(rowWithIndex, contentByColumn );
    }

    private void verifyAGridIsPresent() {
        InBrowserSinglton.find(HEADER_CELL.inside(headerWrapper));
    }

    /**
     * Find a specific cell under a column, without knowing the row, ensure it is
     * displayed, and return its Path
     * @param columnTitle the title of the column to look under
     * @param cellContent a property that describes the content of the expect cell
     * @return the Path of the found cell. allows to interact with it
     */
    public Path ensureVisibilityOfCellInColumn(String columnTitle, ElementProperty cellContent) {
        setOperationTimeout();
        checkAndAdaptToCorrectAgGridVersion();

        try {
            if (colIdByHeader.isEmpty()) {
                findColumnMapping();
            }

            String id = colIdByHeader.get(columnTitle);
            if (id == null) {
                throw new IllegalArgumentException(columnTitle);
            }
            scrollElement(tableViewport).toTopLeftCorner();
            scrollElement(tableHorizontalScroll).toLeftCorner();
            Path cellOfTheColumn = CELL.that(hasColumnId(id));
            scrollElementWithStepOverride(tableHorizontalScroll, stepSize).rightUntilPredicate(cellOfTheColumn, getColumnVisiblityTest());
            Path correctCellInColumn = cellOfTheColumn.that(cellContent);
            scrollElementWithStepOverride(tableViewport, stepSize).downUntilPredicate(correctCellInColumn, getRowVisiblityTest());
            return correctCellInColumn;
        } finally {
            setFinalTimeout();
        }
    }

    public void findTableInBrowser() {
        verifyAGridIsPresent();

        if (virtualized) {
            setOperationTimeout();
        }
        findColumnMapping();
        IntStream rowsIndex = range(0, rows.size());
        if (virtualized) {
            rowsIndex.forEach(i -> findRowInBrowser (i, rows.get(i)));
        } else {
            rowsIndex.forEach(i -> findNonVirtualizedRowInBrowser(i, rows.get(i)));
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

}
