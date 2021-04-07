package com.github.loyada.jdollarx.singlebrowser;

import com.github.loyada.jdollarx.BasicPath;
import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.highlevelapi.Inputs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        Path cell = grid.ensureVisibilityOfRowWithIndexAndColumn(rowNumber, columnTitle);
        assertThat(cell.that(hasAggregatedTextEqualTo(expectedValue)), isPresent());
    }

    /**
     * Hover over speicic cell, after ensuring it is visible
     * @param rowNumber - row number
     * @param columnTitle - column
     * @return the cell
     */
    public Path hoverOverCell(int rowNumber, String columnTitle) {
        AgGrid grid = buildMinimalGridFromHeader(singletonList(columnTitle));
        Path cell = grid.ensureVisibilityOfRowWithIndexAndColumn(rowNumber, columnTitle);
        hoverOver(cell);
        return cell;
    }

    /**
     * define AgGrid with unordered rows
     * @param rows a list of the rows, ignoring the order
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
     * Find a the first cell in the given column with the given value, ensure it is visible,
     * and click on it.
     *
     * @param columnName the column name
     * @param value the value of the cell we are looking for
     * @return the cell element
     */
    public Path clickOnColumnWithValue(String columnName, String value) {
        AgGrid grid = getMinimalGrid(columnName);
        Path myCell = grid.ensureVisibilityOfCellInColumn(columnName,
                hasAggregatedTextEqualTo(value));
        clickAt(myCell);
        return myCell;
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
        Path myCell = grid.ensureVisibilityOfCellInColumn(columnName,
                hasAggregatedTextEqualTo(value));
        clickAt(element.inside(myCell).that(hasSomeText).or(myCell.withText(value)));
        return myCell;
    }


    /**
     * Ensure a specific cell is visible and return a Path to it
     * @param rowNumber row number
     * @param columnName column name
     * @return the request cell
     */
    public Path cellInGrid(int rowNumber, String columnName) {
        AgGrid grid = getMinimalGrid(columnName);
        return grid.ensureVisibilityOfRowWithIndexAndColumn(rowNumber - 1, columnName);
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
        Path myCell = grid.ensureVisibilityOfRowWithIndexAndColumn(rowNumber - 1, columnName);
        int index = grid.getRowIndexOfCell(myCell);
        doubleClickOn(myCell);
        return grid.ensureVisibilityOfRowWithIndexAndColumn(index, columnName);
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
        Path myCell = grid.ensureVisibilityOfCellInColumn(columnName,
                hasAggregatedTextEqualTo(value));

        int index = grid.getRowIndexOfCell(myCell);
        doubleClickOn(myCell);
        return grid.ensureVisibilityOfRowWithIndexAndColumn(index, columnName);
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
        goToEditModeInCell(columnName, rowNumber);
        Path myOption = AgGrid.AgListOption.that(hasAggregatedTextEqualTo(option));
        scrollElement(AgGrid.AgList).downUntilElementIsPresent(myOption);
        clickAt(myOption);
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
        Inputs.changeInputValueWithEnter(input.inside(myCell), newValue);
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
            String newValue) throws Operations.OperationFailedException {
        Path myCell = goToEditModeInCell(columnName, oldValue);
        Inputs.changeInputValueWithEnter(input.inside(myCell), newValue);
    }

}
