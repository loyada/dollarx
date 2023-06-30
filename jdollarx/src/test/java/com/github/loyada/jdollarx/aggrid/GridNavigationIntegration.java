package com.github.loyada.jdollarx.aggrid;

import com.github.loyada.jdollarx.DriverSetup;
import com.github.loyada.jdollarx.ElementProperty;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.AgGrid;
import com.github.loyada.jdollarx.singlebrowser.AgGridHighLevelOperations;
import com.github.loyada.jdollarx.singlebrowser.TemporaryChangedTimeout;
import com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.github.loyada.jdollarx.BasicPath.button;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.*;
import static org.hamcrest.Matchers.equalTo;

import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.BasicPath.image;
import static com.github.loyada.jdollarx.ElementProperties.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class GridNavigationIntegration {
    Path container  = div.that(hasId("myGrid"));

    @BeforeClass
    public static void setup() {
        driver = DriverSetup.createStandardChromeDriver();
        driver.get("https://www.ag-grid.com/example.php");
    }


    @Before
    public void refresh() {
        driver.navigate().refresh();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        find( div.withClass("ag-body-viewport"));
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.MILLISECONDS);
        try(TemporaryChangedTimeout timeout = new TemporaryChangedTimeout(10, TimeUnit.SECONDS)) {
            clickOn(button.withText("accept all cookies"));
        } catch (Exception ex) {
            // no such button
        }
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();;
    }

    @Test
    public void lowLevelApiToScroll() {
        Path columns = div.withClass("ag-center-cols-viewport").inside(container);
        Path viewport = div.withClass("ag-body-viewport");
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.MILLISECONDS);

        scrollElement(viewport).toTopCorner();
        scrollElement(columns).toLeftCorner();
        scrollElement(columns).rightUntilElementIsPresent(div.that(hasAggregatedTextContaining("$86,416")));
        scrollElement(columns).leftUntilElementIsPresent(div.that(hasAggregatedTextContaining("Tony Smith")));
        scrollElement(viewport).downUntilElementIsPresent(div.that(hasAggregatedTextContaining("isabella cage")));
        scrollElement(viewport).upUntilElementIsPresent(div.that(hasAggregatedTextContaining("Gil Lopes")));
    }

    @Test
    public void findRowIndexWithScrolling() {
        Map<String, ElementProperty> rowWithProperties =  Map.of(
                "name", hasAggregatedTextEqualTo("Kevin Cole"),
                "language", hasAggregatedTextEqualTo("english"),
                "country", contains(image.that(hasSource("https://flags.fmcdn.net/data/flags/mini/ie.png")))
        );
        AgGrid grid = getAgGrid();
        // scroll to row
        Integer index1 = grid.findRowIndex(rowWithProperties);
        // row already present - fast
        Integer index2 = grid.findRowIndex(rowWithProperties);
        assertThat(index1, equalTo(index2));
    }

    @Test
    public void findRowIndexOfPreviousRowUsingOrderedMap() {
        Map<String, ElementProperty> rowWithProperties =  Map.of(
                "name", hasAggregatedTextEqualTo("Kevin Cole"),
                "language", hasAggregatedTextEqualTo("english"),
                "country", contains(image.that(hasSource("https://flags.fmcdn.net/data/flags/mini/ie.png")))
        );
        AgGrid grid = getAgGrid();
        // scroll to row
        Integer index1 = grid.findRowIndex(rowWithProperties);

        Map<String, ElementProperty> previousRow = new LinkedHashMap<>();
        previousRow.put("name", hasAggregatedTextEqualTo("andrew cohen"));
        previousRow.put("language", hasAggregatedTextEqualTo("spanish"));
        previousRow.put("dec", hasAggregatedTextEqualTo("$72,351"));
        int indexFound = grid.findRowIndex(previousRow);
        assertThat(index1, equalTo(indexFound+1));
    }

    @Test
    public void clickOnRowWithIndexScrollIfNeeded() {
        AgGrid grid = getAgGrid();
        Path row = grid.ensureVisibilityOfRowWithIndex(60);
        contextClick(AgGrid.CELL.inside(row));
    }

    @Test
    public void ensureVisibilityOfColumnWithSpecificValue() {
        AgGrid grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                .withRowsAsStringsInOrder(Collections.EMPTY_LIST)
                .containedIn(div.that(hasId("myGrid")))
                .build();
        Path myCell = grid.ensureVisibilityOfCellInColumn("jan", hasAggregatedTextEqualTo("$4,298"));
        assertThat(myCell, CustomMatchers.isDisplayed());
    }



    @Test
    public void ensureVisibilityOfColumnWithSpecificIdHighLevel() {
        AgGridHighLevelOperations gridHighLevelOperations = new AgGridHighLevelOperations(div.that(hasId("myGrid")));
        Path octCell = gridHighLevelOperations.getCellInRowWithColumnAndValueById("oct", "jan", "$4,298");
        assertThat(octCell.that(hasAggregatedTextEqualTo("$38,124")), CustomMatchers.isDisplayed());
    }


    @Test
    public void clickCellByColumnWithSpecificValue() {
        AgGrid grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                .withRowsAsStringsInOrder(Collections.EMPTY_LIST)
                .containedIn(div.that(hasId("myGrid")))
                .build();
        Path myCell = grid.ensureVisibilityOfCellInColumn("jan", hasAggregatedTextEqualTo("$4,298"));
        clickOn(myCell);
    }

    @Test
    public void ensureVisibilityOfColumnInRowWithIndex() {
        AgGrid grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                .withRowsAsStringsInOrder(Collections.EMPTY_LIST)
                .containedIn(div.that(hasId("myGrid")))
                .build();
        grid.overrideTimeoutDuringOperation(5);
        Path cell = grid.ensureVisibilityOfRowWithIndexAndColumn(10, "dec");
        assertThat(cell, CustomMatchers.isDisplayed());
    }

    @Test
    public void clickCellByColumnAndRowIndex() {
        AgGrid grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                .withRowsAsStringsInOrder(Collections.EMPTY_LIST)
                .containedIn(div.that(hasId("myGrid")))
                .build();
        Path cell = grid.ensureVisibilityOfRowWithIndexAndColumn(10, "dec");
        clickAt(cell);
    }




    private AgGrid getAgGrid() {
        AgGrid grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList( "language", "name", "country", "dec"))
                .withRowsAsElementProperties(new ArrayList<>())
                .containedIn(div.that(hasId("myGrid")))
                .build();
        grid.setScrollStep(50);
        grid.overrideTimeoutDuringOperation(1);
        return grid;
    }


    @Test
    public void clickRow() {
        AgGridHighLevelOperations gridHighLevelOperations = new AgGridHighLevelOperations(div.that(hasId("myGrid")));
        AgGrid grid = gridHighLevelOperations.buildMinimalGridFromHeader(List.of("jan", "name"));

        Path myCell = grid.ensureVisibilityOfCellInColumn("jan", hasAggregatedTextEqualTo("$4,298"));
        Path myRow = gridHighLevelOperations.getRowOfDisplayedCell(myCell);
        Path colCell = grid.ensureVisibilityOfCellInColumnInVisibleRow(myRow, "name");
        clickOn(colCell);
    }

    @Test
    public void clickRowHighLevel() {
        AgGridHighLevelOperations gridHighLevelOperations = new AgGridHighLevelOperations(div.that(hasId("myGrid")));
        Path myCell = gridHighLevelOperations.getCellInRowWithColumnAndValue("name", "dec", "$36,045");
        clickOn(myCell);
    }
}
