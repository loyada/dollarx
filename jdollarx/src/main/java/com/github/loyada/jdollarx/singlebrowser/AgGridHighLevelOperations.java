package com.github.loyada.jdollarx.singlebrowser;

import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.highlevelapi.Inputs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import static com.github.loyada.jdollarx.BasicPath.element;
import static com.github.loyada.jdollarx.BasicPath.input;
import static com.github.loyada.jdollarx.ElementProperties.hasAggregatedTextEqualTo;
import static com.github.loyada.jdollarx.ElementProperties.hasSomeText;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.clickAt;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.doubleClickOn;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.hoverOver;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.scrollElement;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isPresent;
import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * High level utilities for definitions of simplified grids and operations
 */
public final class AgGridHighLevelOperations {
    final private Path gridContainer;
    public static int retry_duration_in_millisec = 500;

    public AgGridHighLevelOperations(Path gridContainer){
        this.gridContainer = gridContainer;
    }

    public AgGrid buildMinimalGridFromHeader(List<String> headers) {
        return AgGrid.getBuilder()
                .withHeaders(headers)
                .withRowsAsStrings(EMPTY_LIST)
                .containedIn(gridContainer)
                .build();
    }

    /**
     * Ensure(or assert) that the cell in specific row and column has the expected value
     * @param rowNumber - number of row of the cell
     * @param columnTitle - the column of the cell
     * @param expectedValue - the value we assert in that cell
     */
    public void ensureCellValueIsPresent(int rowNumber, String columnTitle, String expectedValue) {
        AgGrid grid = buildMinimalGridFromHeader(singletonList(columnTitle));
        retry_if_needed(() -> {
            Path cell = grid.ensureVisibilityOfRowWithIndexAndColumn(rowNumber, columnTitle);
            assertThat(cell.that(hasAggregatedTextEqualTo(expectedValue)), isPresent());
        });
    }

    /**
     * Hover over speicic cell, after ensuring it is visible
     * @param rowNumber - row number
     * @param columnTitle - column
     * @return the cell
     */
    public Path hoverOverCell(int rowNumber, String columnTitle) {
        AgGrid grid = buildMinimalGridFromHeader(singletonList(columnTitle));
        return retry_if_needed(() -> {
            Path cell = grid.ensureVisibilityOfRowWithIndexAndColumn(rowNumber, columnTitle);
            hoverOver(cell);
            return cell;
        });
    }

    /**
     * define AgGrid with unordered columns
     * @param rows a list of the rows, in order
     * @return an AgGrid object
     */
    public AgGrid unorderedGrid(List<Map<String, String>> rows) {
        Set<String> headers = rows.get(0).keySet();
        return AgGrid.getBuilder()
                .withHeaders(new ArrayList<>(headers))
                .withRowsAsStrings(rows)
                .containedIn(gridContainer)
                .build();
    }

    /**
     * define a "strict" AgGrid with unordered columns. A strict grid means no other
     * rows exist.
     * @param rows a list of all the rows in the grid, in order
     * @return an AgGrid object
     */
    public AgGrid unorderedStrictGrid(List<Map<String, String>> rows) {
        Set<String> headers = rows.get(0).keySet();
        return AgGrid.getBuilder()
                .withHeaders(new ArrayList<>(headers))
                .withRowsAsStrings(rows)
                .containedIn(gridContainer)
                .isStrict()
                .build();
    }

    /**
     * Find a the first cell in the given column with the given value, ensure it is visible,
     * and click on it.
     *
     * @param columnName the column name
     * @param value the value of the cell we are looking for
     * @return the cell element
     */
    public Path clickOnColumnWithValue(String columnName, String value) {
        AgGrid grid = getMinimalGrid(columnName);
        return retry_if_needed(() -> {
            Path myCell = grid.ensureVisibilityOfCellInColumn(columnName,
                    hasAggregatedTextEqualTo(value));
            clickAt(myCell);
            return myCell;
        });
    }

    /**
     * Find a the first cell in the given column with the given value, ensure it is visible,
     * and click on the text inside it.
     *
     * @param columnName the column name
     * @param value the value of the cell we are looking for
     * @return the cell element
     */
    public Path clickOnTextInsideColumnWithValue(String columnName, String value) {
        AgGrid grid = getMinimalGrid(columnName);
        return retry_if_needed(() -> {
            Path myCell = grid.ensureVisibilityOfCellInColumn(columnName,
                    hasAggregatedTextEqualTo(value));
            clickAt(element.inside(myCell).that(hasSomeText).or(myCell.withText(value)));
            return myCell;
        });
    }


    /**
     * Ensure a specific cell is visible and return a Path to it
     * @param rowNumber row number
     * @param columnName column name
     * @return the request cell
     */
    public Path cellInGrid(int rowNumber, String columnName) {
        AgGrid grid = getMinimalGrid(columnName);
        return retry_if_needed(() -> {
            return grid.ensureVisibilityOfRowWithIndexAndColumn(rowNumber - 1, columnName);
        });
    }

    /**
     * create a minimal grid definition that has the column we are interersted in
     * @param columnName the column name
     * @return a grid object
     */
    public AgGrid getMinimalGrid(String columnName) {
        return new AgGridHighLevelOperations(gridContainer)
                .buildMinimalGridFromHeader(Collections.singletonList(columnName));
    }

    /**
     * Find a cell, and doubleclick it
     * @param columnName column name
     * @param rowNumber row number
     * @return the cell
     */
    public Path goToEditModeInCell(
            String columnName,
            int rowNumber) {
        AgGrid grid = getMinimalGrid(columnName);
        return retry_if_needed(() -> {
            Path myCell = grid.ensureVisibilityOfRowWithIndexAndColumn(rowNumber - 1, columnName);
            int index = grid.getRowIndexOfCell(myCell);
            doubleClickOn(myCell);
            return grid.ensureVisibilityOfRowWithIndexAndColumn(index, columnName);
        });
    }

    /**
     * Find a cell, and doubleclick it
     * @param columnName column name
     * @param value value of cell
     * @return the cell
     */
    public Path goToEditModeInCell(
            String columnName,
            String value) {
        AgGrid grid = getMinimalGrid(columnName);
        return retry_if_needed(() -> {
            Path myCell = grid.ensureVisibilityOfCellInColumn(columnName,
                    hasAggregatedTextEqualTo(value));

            int index = grid.getRowIndexOfCell(myCell);
            doubleClickOn(myCell);
            return grid.ensureVisibilityOfRowWithIndexAndColumn(index, columnName);
        });
    }

    /**
     * select an option from a dropdown in a cell
     * @param columnName column name
     * @param rowNumber row number
     * @param option option to choose
     */
    public void selectInCell(
            String columnName,
            int rowNumber,
            String option) {
        retry_if_needed(() -> {
            goToEditModeInCell(columnName, rowNumber);
            Path myOption = AgGrid.AgListOption.that(hasAggregatedTextEqualTo(option));
            scrollElement(AgGrid.AgList).downUntilElementIsPresent(myOption);
            clickAt(myOption);
        });
    }

    /**
     * return a path to the Row of a cell, assuming the cell is displayed.
     * This should be used whenever you want to perform an operation on the row or search inside the row.
     * @param cell the cell we have
     * @return the Path of the row
     */
    public Path getRowOfDisplayedCell(Path cell) {
        Path row = AgGrid.rowOfGrid(this.gridContainer)
                .containing(cell);
        int rowIndex = AgGrid.getRowIndex(row);
        return AgGrid.rowOfGrid(this.gridContainer).that(AgGrid.hasIndex(rowIndex))
                .describedBy(String.format("grid row, containing: %s", cell));
    }

    /**
     * select an option from a dropdown in a cell
     * @param columnName column name
     * @param rowNumber row number
     * @param newValue new Value
     */
    public void changeSimpleInputValueByRowNumber(
            String columnName,
            int rowNumber,
            String newValue) throws Operations.OperationFailedException {
        Path myCell = goToEditModeInCell(columnName, rowNumber);
        retry_if_needed(() -> {
            try {
                Inputs.changeInputValueWithEnter(input.inside(myCell), newValue);
            } catch (Operations.OperationFailedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * select an option from a dropdown in a cell
     * @param columnName column name
     * @param oldValue row number
     * @param newValue new Value
     */
    public void changeSimpleInputValueByValue(
            String columnName,
            String oldValue,
            String newValue) {
        Path myCell = goToEditModeInCell(columnName, oldValue);
        retry_if_needed(() -> {
            try {
                Inputs.changeInputValueWithEnter(input.inside(myCell), newValue);
            } catch (Operations.OperationFailedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Path getRowWithColumnAndValue(String column, String value) {
        AgGrid grid = buildMinimalGridFromHeader(List.of(column));

        Path myCell = grid.ensureVisibilityOfCellInColumn(column, hasAggregatedTextEqualTo(value));
        return getRowOfDisplayedCell(myCell);
    }

    /**
     * First find a row that has the given value in the column, then find the column "wantedColumn" in
     * the same row, ensure it is visible, and return the Path to it.
     * @param wantedColumn the column of the cell we want
     * @param column the column of the cell that is used to find the row
     * @param value the value of the column that is used to find the row
     * @return The path of the wanted cell
     */
    public Path getCellInRowWithColumnAndValue(String wantedColumn, String column, String value) {
        AgGrid grid = buildMinimalGridFromHeader(List.of(wantedColumn, column));
        Path myCell = grid.ensureVisibilityOfCellInColumn(column, hasAggregatedTextEqualTo(value));
        Path theRow = getRowOfDisplayedCell(myCell);
        grid.scrollToLeftSide();
        return grid.ensureVisibilityOfCellInColumnInVisibleRow(theRow, wantedColumn);
    }

    private static void retry_if_needed(Runnable runnable) {
        Operations.doWithRetries(runnable, 5, retry_duration_in_millisec/5 );
    }

    private static <T> T retry_if_needed(Callable<T> callable) {
        try {
            return Operations.doWithRetries(callable, 5, retry_duration_in_millisec / 5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
