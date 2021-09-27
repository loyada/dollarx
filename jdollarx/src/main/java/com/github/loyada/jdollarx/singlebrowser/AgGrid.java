package com.github.loyada.jdollarx.singlebrowser;

import com.github.loyada.jdollarx.BasicPath;
import com.github.loyada.jdollarx.ElementProperty;
import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.Operations.OperationFailedException;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.sizing.ElementResizer;
import com.google.common.collect.ImmutableList;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static com.github.loyada.jdollarx.BasicPath.*;
import static com.github.loyada.jdollarx.ElementProperties.*;
import static com.github.loyada.jdollarx.singlebrowser.AgGrid.SortDirection.getAllClasses;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.clickAt;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.clickOn;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.find;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.findAll;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.getCssClasses;
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
    public static final Path CELL = div.that(hasRole("gridcell").or(hasRole("presentation"))).describedBy("cell");
    public static final Path HEADER_MENU = span.withClass("ag-header-cell-menu-button");
    private static final Path MENU = div.withClass("ag-menu");
    private static final Path POPUP=div.withClass("ag-popup");
    public  static final Path CHECKBOX = div.withClass("ag-checkbox");
    public static final Path AgGridRoot = div.withClass("ag-root-wrapper");
    public static final Path AgBody = div.withClass("ag-body-viewport");
    public static final Path AgList = div.that(hasRole("listbox"))
            .inside(AgGridRoot.or(div.withClass("ag-popup")))
            .describedBy("grid dropdown");
    public static final Path AgListOption = div.that(hasRole("option")).inside(AgList);

    private final List<String> headers;
    private final List<Map<String, ElementProperty>> rows;
    private final boolean virtualized;
    private final boolean strict;
    private final Path tableViewport;
    private int stepSize = 60;

    private Path tableHorizontalScroll;
    private final Path tableContent;
    private final Path headerWrapper;
    private final Map<String, String> colIdByHeader  = new HashMap<>();
    private int operationTimeout = 5, finalTimeout = 5000;
    private static final Pattern columnIdFormat = Pattern.compile("\\{([^}]*.?)\\}");


    public static AgGridBuilder getBuilder() {
        return new AgGridBuilder();
    }

    public enum SortDirection {
        ascending("ag-header-cell-sorted-asc"),
        descending("ag-header-cell-sorted-desc"),
        none("ag-header-cell-sorted-none");

        private final String cssClassName;

        SortDirection(String cssClassName) {
            this.cssClassName = cssClassName;
        }

        String getCssClassName() {
            return cssClassName;
        }

        static String[] getAllClasses() {
            return Arrays.stream(values())
                    .map(SortDirection::getCssClassName)
                    .toArray(String[]::new);
        }

        static SortDirection byCssClass(String cssClassName) {
            return Arrays.stream(values())
                    .filter(d->cssClassName.contains(d.getCssClassName()))
                    .findFirst()
                    .orElse(null);
        }

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
         * @param headers - the headers of the columns. In case you prefer to use a column ID, wrap it with {}. For \
         *               example, "{the-id}" will refer to a header with a column ID of "the-id". This is useful when a column has no textual header.
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
         *             column name(or column ID) to the property that describes the expected content.
         *             To use a column Id as a key, wrap it with curly braces.
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
         *             column name(or column ID) to the text.
         *             To use a column Id as a key, wrap it with curly braces.
         * @return AgGridBuilder
         */
        public AgGridBuilder withRowsAsStringsInOrder(List<List<Map.Entry<String, String>>> rows) {
            this.rows = rows.stream().map(row -> {
                LinkedHashMap<String, ElementProperty> newRow = new LinkedHashMap<>();
                row.forEach((Map.Entry<String, String> entry) -> {
                    String value = entry.getValue();
                    newRow.put(entry.getKey(), hasAggregatedTextEqualTo(value==null ? "" : value));
                });
                return newRow;
            }).collect(toList());
            return this;
        }

        /**
         * Define the rows in the table, in order.
         * @param rows - A list of rows, where each row is a map of the
         *             column name(or column ID) to the text.
         *             To use a column Id as a key, wrap it with curly braces.
         * @return AgGridBuilder
         */
        public AgGridBuilder withRowsAsStrings(List<Map<String, String>> rows) {
            this.rows = rows.stream().map(row -> {
                LinkedHashMap<String, ElementProperty> newRow = new LinkedHashMap<>();
                row.forEach((String key, String text) -> {
                    newRow.put(key, hasAggregatedTextEqualTo(text==null ? "" : text));
                });
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

    public static Path rowOfGrid(Path gridContainer) {
        return AgGrid.ROW
                .that(isInside(AgBody.inside(gridContainer)))
                .and(contains(AgGrid.CELL))
                .describedBy("grid row");
    }

    public Path row() {
        return AgGrid.ROW.that(isInside(tableViewport)).and(contains(AgGrid.CELL));
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

    static ElementProperty hasIndex(int ind) {
        return hasAttribute("row-index", Integer.toString(ind));
    }

    private static ElementProperty hasColumnId(String id) {
        return hasAttribute(COL_ID, id);
    }

    private WebElement getWebElForHeader(Path columnHeader, boolean virtualized) {
        return virtualized ?
                findHeader(columnHeader) :
                InBrowserSinglton.find(columnHeader);
    }

    private void findColumnMapping() {
        checkAndAdaptToCorrectAgGridVersion();
        headers.forEach( columnText -> {
            Path columnHeader = getColumnHeaderCell(columnText);
            WebElement headerCell = getWebElForHeader(columnHeader, virtualized);
            String columnId;
            // This hack deals with a re-rendering in AgGrid (defect?)
            try {
                 columnId = headerCell.getAttribute(COL_ID);
            } catch (StaleElementReferenceException e) {
                getWebElForHeader(columnHeader, virtualized);
                columnId = getWebElForHeader(columnHeader, virtualized).getAttribute(COL_ID);
            }

            if (columnId==null)
                throw new UnsupportedOperationException("could not find column id for " + columnHeader);
            colIdByHeader.put(columnText, columnId);
        });
        scrollElement(tableHorizontalScroll).toTopLeftCorner();
    }

    private Path getColumnHeaderCell(String columnText) {
        Path headerCell = HEADER_CELL.inside(headerWrapper);
        Path columnHeader =  (columnIdFormat.matcher(columnText).matches()) ?
             headerCell.that(hasAttribute(COL_ID, columnText.substring(1, columnText.length()-1))) :
             headerCell.containing(HEADER_TXT.that(hasAggregatedTextEqualTo(columnText)));

        return columnHeader.describedBy(format("header '%s'", columnText));
    }

    private WebElement findHeader(Path headerEl) {
        Operations.ScrollElement scroll = scrollElementWithStepOverride(tableHorizontalScroll, stepSize);
        try {
            WebElement webElement = scroll.rightUntilElementIsPresent(headerEl);
            // Deal with an issue with how AgGrid is rendered
            Thread.sleep(50);
            try{
                webElement.getAttribute(COL_ID);
                return webElement;
            } catch (StaleElementReferenceException e) {
                return findHeader(headerEl);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
     * @param headerText - the header text, or the column ID. A string wrapped with curly braces is interpreted as the column ID.
     */
    public void clickMenuOfHeader(String headerText) {
        checkAndAdaptToCorrectAgGridVersion();
        Path headerEl = getVisibleHeaderPath(headerText);
        clickAt(span.that(hasRef("eMenu")).inside(headerEl));
    }

    /**
     * Click on the 'sort' column with the given header
     * @param headerText - the header text, or the column ID. A string wrapped with curly braces is interpreted as the column ID.
     */
    public void clickOnSort(String headerText) {
        checkAndAdaptToCorrectAgGridVersion();
        setOperationTimeout();
        try {
            checkAndAdaptToCorrectAgGridVersion();
            Path headerEl = getVisibleHeaderPath(headerText);
            Path sortButton = div.that(hasRef("eLabel")).
                    inside(div.that(hasClassContaining("ag-header-cell-sort"))).
                    inside(headerEl);
            scrollElement(tableHorizontalScroll).rightUntilPredicate(sortButton, getColumnVisiblityTest());
            clickOn(sortButton);
        } finally {
            setFinalTimeout();
        }
    }

    /**
     * Click on 'sort' so that the given column is sorted in the direction provided.
     * @param headerText - the header text, or the column ID. A string wrapped with curly braces is interpreted as the column ID.
     * @param direction - wanted direction. either descending or ascending.
     * @throws OperationFailedException operation failed - typically the configuration of the grid does not allow to sort as wanted.
     */
    public void sortBy(String headerText, SortDirection direction) throws OperationFailedException {
        Path columnHeader = getVisibleHeaderPath(headerText);
        Path sortElement = div.inside(columnHeader).that(hasAnyOfClasses(getAllClasses()));
        checkAndAdaptToCorrectAgGridVersion();
        setOperationTimeout();
        try {
            // since the sort configuration can be different from grid to grid, we can't predetermine the number of
            // required clicks, so we have to click and check repeatedly until it is sorted correctly, or we give up.
            int maxNumberOfTries = SortDirection.values().length;
            int numberOfClicks = 0;
            while (numberOfClicks<=maxNumberOfTries) {
                String currentSortClass = find(sortElement).getAttribute("class");
                SortDirection currentSortDirection = SortDirection.byCssClass(currentSortClass);
                if (currentSortDirection!=direction) {
                    clickOnSort(headerText);
                } else return;
                numberOfClicks++;
            }
            throw new OperationFailedException("check the sort configuration of the grid");
        } finally {
            setFinalTimeout();
        }
    }

    /**
     ** Show all columns by using the popup menu of the given header.
     * @param headerText - the header text, or the column ID, to open the popup menu from. A string wrapped with curly braces is interpreted as the column ID.
     */
    public void showAllColumnsUsingMenuOfColumn(String headerText) {
        showAllColumnsUsingMenuOfColumn(headerText, true);
        clickAt(BasicPath.body);
    }

    private Path findTheRightHeader(String headerText) {
        scrollElement(tableViewport).toTopLeftCorner();
        return (headerText==null) ? HEADER_CELL.inside(headerWrapper) : getVisibleHeaderPath(headerText);
    }

    private Path openColumnMenuAndGetMenu(String headerText, String cssClass) {
        Path columnHeader = findTheRightHeader(headerText);
        clickAt(HEADER_MENU.inside(columnHeader));
        Path headerMenu = MENU.inside(POPUP);
        Path menuTabHeader = div.that(hasClassContaining("ag-tab"), hasRef("eHeader")).
                inside(headerMenu).
                describedBy("column menu header");
        Path wantedTab = span.withClass("ag-tab").parentOf(span.withClass(cssClass));
        List<String> currentTabClasses = getCssClasses(wantedTab.inside(menuTabHeader));
        if (!currentTabClasses.contains("ag-tab-selected")) {
            clickAt(wantedTab.inside(menuTabHeader));
        }
        return div.that(hasRef("eBody")).inside(headerMenu);
    }

    /**
     * open the popup menu for the column
     * @param headerText - the header text, or the column ID, to open the popup menu from. A string wrapped with curly braces is interpreted as the column ID.
     * @return the Path to the popup menu
     */
    public Path openColumnMenuTabAndGetMenu(String headerText) {
        return openColumnMenuAndGetMenu(headerText, "ag-icon-menu");
    }

    /**
     * open the popup filter for the column
     * @param headerText - the header text, or the column ID, to open the popup menu from. A string wrapped with curly braces is interpreted as the column ID.
     * @return the Path to the popup menu
     */
    public Path openColumnFilterTabAndGetMenu(String headerText) {
        return openColumnMenuAndGetMenu(headerText, "ag-icon-filter");
    }

    /**
     * open the popup columns show/hide selection by using a popup of the given column
     * @param headerText - the header text, or the column ID, to open the popup menu from. A string wrapped with curly braces is interpreted as the column ID.
     * @return the Path to the popup menu
     */
    public Path openColumnsSelectionMenuAndGetMenu(String headerText) {
        return openColumnMenuAndGetMenu(headerText, "ag-icon-columns");
    }

    /**
     *open the popup columns show/hide selection by using a popup of the first column (assumes it is active)
     * @return the Path to the popup menu
     */
    public Path openColumnsSelectionMenuAndGetMenu() {
        return openColumnMenuAndGetMenu(null, "ag-icon-columns");
    }

    private Path showAllColumnsUsingMenuOfColumn(String headerText, boolean isVisible) {
        checkAndAdaptToCorrectAgGridVersion();
        setOperationTimeout();
        try {
            Path tabBody = openColumnsSelectionMenuAndGetMenu(headerText);
            Path selectAllColumns = div.that(hasRef("eSelect")).inside(MENU);
            Path selectAllIcon = div.withClass("ag-checkbox-input-wrapper").inside(selectAllColumns);
            ensureCheckboxIsSelected(selectAllColumns, selectAllIcon);
            if (!isVisible) {
                clickAt(selectAllColumns);
            }
            return tabBody;
        } finally {
            setFinalTimeout();
        }
    }

    private void ensureCheckboxIsSelected(Path selectAllColumns, Path selectAllIcon) {
        int tries = 3;
        while (!getCssClasses(selectAllIcon).contains("ag-checked") && tries>0) {
            tries--;
            clickAt(selectAllColumns);
        }
    }

    private Predicate<WebElement> getOptionVisiblityTest(Path menuWrapper) {
        WebElement content = find(menuWrapper);
        int bottom = content.getSize().height + content.getLocation().getY();
        return el -> el.isDisplayed() && el.getLocation().y < bottom;
    }

    /**
     * Show only specific columns, by opening the popup menu of the given column
     * @param headerText - the header text, or the column ID, to open the popup menu from. A string wrapped with curly braces is interpreted as the column ID.
     * @param columns - the columns to show
     */
    public void showSpecificColumnsUsingMenuOfColumn(String headerText, List<String> columns) {
        Path tabBody = showAllColumnsUsingMenuOfColumn(headerText, false);
        Path columnList = div.childOf(div.that(hasRef("primaryColsListPanel"))).inside(tabBody);
        checkAndAdaptToCorrectAgGridVersion();
        setOperationTimeout();
        try {
            columns.forEach(column -> {
                scrollElement(columnList).toTopCorner();
                Path checkbox = CHECKBOX.beforeSibling(span.that(hasRef("eLabel")).and(hasText(column))).inside(columnList);
                scrollElementWithStepOverride(columnList, 10).downUntilPredicate(checkbox, getOptionVisiblityTest(columnList));
                clickAt(checkbox);
            });
            clickAt(BasicPath.body);
        } finally {
            setFinalTimeout();
        }
    }

    /**
     * Show only specific columns, by opening the popup menu of the first column. Assumes that the first column has the popup menu.
     * @param columns - the columns to show
     */
    public void showSpecificColumnsUsingMenuOfColumn(List<String> columns) {
        showSpecificColumnsUsingMenuOfColumn(null, columns);

    }

    /**
     * Show all columns, by opening the popup menu of the first column. Assumes that the first column has the popup menu.
     */
    public void showAllColumnsUsingFirstColumn() {
        showAllColumnsUsingMenuOfColumn(null, true);
        clickAt(BasicPath.body);
    }


    /**
     * Make sure the given column header is visible, and returns a Path element to access it.
     * This is useful to perform direct operations on that element or access other DOM elements contained in the header.
     * @param headerText - the header text, or the column ID. A string wrapped with curly braces is interpreted as the column ID.
     * @return the Path element to access the column header
     */
    public Path getVisibleHeaderPath(String headerText) {
        checkAndAdaptToCorrectAgGridVersion();
        setOperationTimeout();

        try {
            scrollElement(tableViewport).toTopLeftCorner();
            scrollElement(tableHorizontalScroll).toLeftCorner();
            Path headerEl = getColumnHeaderCell(headerText);
            scrollElement(tableHorizontalScroll).rightUntilPredicate(headerEl, getColumnVisiblityTest());
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

    /**
     * assuming the row is already present in the DOM, get its internal index in the table.
     *
     * @param row the row we are interested in. Should be the value returned from findRowInBrowser() or ensureVisibilityOfRowWithIndex()
     * @return the internal index of the row in the table
     */
    public static int getRowIndex(Path row) {
        try {
            return parseInt(find(row).getAttribute("row-index"));
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("getRowIndex() requires the row to be present in the DOM. Use another function to ensure it is there first.", e);
        }
    }

    /**
     * assuming the row is already present in the DOM, get its internal index in the table.
     * @param cell - the cell in the row we are interested in. Should be the return value of ensureVisibilityOfRowWithIndexAndColumn()
     * @return the internal index of the row in the table
     */
    public int getRowIndexOfCell(Path cell) {
        return getRowIndex(ROW.parentOf(cell));
    }

    /**
     * Find internal index of row within table. This method typically will make sure the row is also visible if it
     * exists, in case the user needs to interact with it, but in some cases ensureVisiblityOfRow will be required.
     * @param row - the definition of the row content
     * @return the internal index of the row, if it was found
     */
    public int findRowIndex(Map<String, ElementProperty> row) {
        if (!virtualized) {
            Path rowEl =  findNonVirtualizedRowInBrowser(row);
            return getRowIndex(rowEl);
        }

        setOperationTimeout();
        checkAndAdaptToCorrectAgGridVersion();
        try {
            if (colIdByHeader.isEmpty()) {
                findColumnMapping();
            }

            // try to find in current DOM
            Thread.sleep(50);
            List<Integer> presentRowIndexes = getCurrentIndexes();
            Optional<Integer> matchingIndexInCurrentDOM = tryFindRowIndexWithinList(row, presentRowIndexes);
            if (matchingIndexInCurrentDOM.isPresent())
                return matchingIndexInCurrentDOM.get();

            // couldn't find in current place. have to scan from beginning...
            Set<Integer> checkedIndexes = new HashSet<>(presentRowIndexes);
               OptionalInt foundRow = range(0, 10000000).
                       filter(ind -> !checkedIndexes.contains(ind)).
                       filter(i -> {
                            try {
                                Path myRow = findRowInBrowser(i, row);
                                scrollElementWithStepOverride(tableViewport, stepSize).downUntilPredicate(myRow, getRowVisiblityTest());
                                return true;
                            } catch (NoSuchElementException e) {
                                return false;
                            }
                        }).
                        findFirst();
                return foundRow.orElseThrow(NotFoundException::new);
        }  catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            setFinalTimeout();
        }
    }

    private Optional<Integer> tryFindRowIndexWithinList(Map<String, ElementProperty> row, List<Integer> indexes) {
        return indexes.stream().
                        filter(index -> {
                            try {
                                final Path myRow = ROW.that(hasIndex(index)).inside(tableContent)
                                        .and(contains(AgGrid.CELL))
                                        .describedBy(format("row with index %d", index));
                                validateRowContent(row, myRow);
                                return true;
                            } catch (NoSuchElementException e) {
                                return false;
                            }
                        }).
                        findFirst();
    }

    private List<Integer> getCurrentIndexes() {
        final Path anyRow = ROW.inside(tableContent).and(contains(AgGrid.CELL));
        List<String> indexes = (List<String>) InBrowserSinglton.getAttributeOfAll(anyRow, "row-index");
        return indexes.stream()
                .map(Integer::parseInt)
                .collect(toList());
    }

    private Path findRowInBrowser(int index, Map<String, ElementProperty> row) {
        scrollElement(tableViewport).toTopLeftCorner();
        scrollElement(tableHorizontalScroll).toLeftCorner();
        final Path myRow = ROW.that(hasIndex(index)).inside(tableContent)
            .describedBy(format("row with index %d", index));

        try {
            scrollElement(tableViewport).downUntilElementIsPresent(myRow);
        } catch (NoSuchElementException e) {
            throw new IndexOutOfBoundsException(format("row %d was not found. cause: %s", index, e));
        }
        validateRowContent(row, myRow);
        return myRow;
    }

    private void validateRowContent(Map<String, ElementProperty> row, Path myRow) {
        scrollElement(tableHorizontalScroll).toLeftCorner();
        Function<String, Path> getCollumn = (String columnTitle) -> {
            String id = colIdByHeader.get(columnTitle);
            if (id==null) {
                throw new IllegalArgumentException(format("column %s was not in grid definition", columnTitle));
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
            Path cellOfTheColumn = CELL.that(hasColumnId(id)).describedBy(String.format("cell in column '%s'", columnTitle));
            scrollElementWithStepOverride(tableHorizontalScroll, stepSize).rightUntilPredicate(cellOfTheColumn, getColumnVisiblityTest());
            Path correctCellInColumn = cellOfTheColumn.that(cellContent);
            scrollElementWithStepOverride(tableViewport, stepSize).downUntilPredicate(correctCellInColumn, getRowVisiblityTest());
            return correctCellInColumn;
        } finally {
            setFinalTimeout();
        }
    }

    public void scrollToLeftSide() {
        scrollElement(tableHorizontalScroll).toLeftCorner();
    }

    public void scrollToTop() {
        scrollElement(tableViewport).toTopCorner();
    }

    /**
     * Find a specific cell under a column, when row is already known and displayed, ensure it is
     * displayed, and return its Path
     * @param row the row, which is assumed to be already displayed
     * @param columnTitle the title of the column to look under
     * @return the Path of the found cell. allows to interact with it
     */
    public Path ensureVisibilityOfCellInColumnInVisibleRow(Path row, String columnTitle) {
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
            scrollElement(tableHorizontalScroll).toLeftCorner();
            Path cellOfTheColumn = CELL.that(hasColumnId(id)).inside(row);
            scrollElementWithStepOverride(tableHorizontalScroll, stepSize).rightUntilPredicate(cellOfTheColumn, getColumnVisiblityTest());
            return cellOfTheColumn;
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
