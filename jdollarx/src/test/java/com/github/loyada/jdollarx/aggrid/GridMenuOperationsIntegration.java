package com.github.loyada.jdollarx.aggrid;

import com.github.loyada.jdollarx.DriverSetup;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.AgGrid;
import com.github.loyada.jdollarx.singlebrowser.AgGridHighLevelOperations;
import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import com.github.loyada.jdollarx.singlebrowser.custommatchers.AgGridMatchers;
import com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.BasicPath.element;
import static com.github.loyada.jdollarx.ElementProperties.*;
import static com.github.loyada.jdollarx.singlebrowser.AgGrid.HEADER_CELL;
import static com.github.loyada.jdollarx.singlebrowser.AgGrid.SortDirection.*;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.*;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isPresent;
import static org.junit.Assert.assertThat;

public class GridMenuOperationsIntegration {
    private AgGrid grid;
    private List<Map.Entry<String, String>> row1, row2;

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
        row1 = Arrays.asList(
                new SimpleEntry<>("{name}", "tony smith"),
                new SimpleEntry<>("language", "english"),
                new SimpleEntry<>("jan","$38,031"),
                new SimpleEntry<>("dec","$86,416")
        );
        row2 = Arrays.asList(
                new SimpleEntry<>("{name}", "Andrew Connell"),
                new SimpleEntry<>("language", "swedish"),
                new SimpleEntry<>("jan","$17,697"),
                new SimpleEntry<>("dec","$83,386")
        );        grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList("dec", "jan", "language", "{name}"))
                .withRowsAsStringsInOrder(Arrays.asList(row1, row2))
                .containedIn(div.that(hasId("myGrid")))
                .build();
    }

    @Test
    public void openColumnsSelectionMenuAndGetMenu() {
        Path menu = grid.openColumnsSelectionMenuAndGetMenu();
        assertThat(menu, CustomMatchers.isDisplayed());
    }

    @Test
    public void showSpecificColumnsUsingMenuOfColumn() {
        grid.showSpecificColumnsUsingMenuOfColumn("language", List.of("dec", "language"));
        Path header = HEADER_CELL.withClass("ag-header-cell-sortable");
        assertThat( header, isPresent(2).times());
    }

    @Test
    public void showSpecificColumnsUsingMenuOfFirstColumn() {
        grid.showSpecificColumnsUsingMenuOfColumn(List.of("dec", "language"));
        Path header = HEADER_CELL.withClass("ag-header-cell-sortable");
        assertThat( header, isPresent(2).times());
    }

    @Test
    public void showAllColumnsUsingMenuOfColumn() {
        Path header = HEADER_CELL.withClass("ag-header-cell-sortable");
        int expectedNumberOfHeaders = findAll(header).size();

        grid.showSpecificColumnsUsingMenuOfColumn("language", List.of("dec", "language"));
        grid.showAllColumnsUsingMenuOfColumn("language");
        assertThat(header, isPresent(expectedNumberOfHeaders).times());
    }

    @Test
    public void showAllColumnsUsingFirstColumn() {
        Path header = HEADER_CELL.withClass("ag-header-cell-sortable");
        int expectedNumberOfHeaders = findAll(header).size();

        grid.showSpecificColumnsUsingMenuOfColumn(List.of("dec", "language"));
        grid.showAllColumnsUsingFirstColumn();
        assertThat(header, isPresent(expectedNumberOfHeaders).times());
    }

    @Test
    public void sortDescending() throws Operations.OperationFailedException {
        grid.sortBy("Nov", descending);
        row1 = Arrays.asList(
                new SimpleEntry<>("{name}", "Sophie Dane"),
                new SimpleEntry<>("language", "Norwegian"),
                new SimpleEntry<>("jan","$84,878")
        );
        row2 = Arrays.asList(
                new SimpleEntry<>("{name}", "Jessica Smith"),
                new SimpleEntry<>("language", "Portuguese"),
                new SimpleEntry<>("jan","$66,642")
        );
        grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList("jan", "language", "{name}"))
                .withRowsAsStringsInOrder(Arrays.asList(row1, row2))
                .containedIn(div.that(hasId("myGrid")))
                .build();
        assertThat(grid, AgGridMatchers.isPresent());
    }

    @Test
    public void sortAsending() throws Operations.OperationFailedException {
        grid.sortBy("Nov", ascending);
        row1 = Arrays.asList(
                new SimpleEntry<>("{name}", "Charlotte Cole"),
                new SimpleEntry<>("language", "French"),
                new SimpleEntry<>("jan","$51,737")
        );
        row2 = Arrays.asList(
                new SimpleEntry<>("{name}", "Isla Bryson"),
                new SimpleEntry<>("language", "English"),
                new SimpleEntry<>("jan","$40,820")
        );
        grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList("jan", "language", "{name}"))
                .withRowsAsStringsInOrder(Arrays.asList(row1, row2))
                .containedIn(div.that(hasId("myGrid")))
                .build();
        assertThat(grid, AgGridMatchers.isPresent());
    }

    @Test
    public void sortNone() throws Operations.OperationFailedException {
        grid.sortBy("Nov", ascending);
        grid.sortBy("Nov", none);

        // grid should look the same as before any sorting
        assertThat(grid, AgGridMatchers.isPresent());
    }

    @Test
    public void hoverOverCell() throws Operations.OperationFailedException {
        grid.sortBy("Nov", ascending);
        AgGridHighLevelOperations agGridHighLevelOperations = new AgGridHighLevelOperations(div.that(hasId("myGrid")));
        Path cell = agGridHighLevelOperations.hoverOverCell(3, "Nov" );
        Path hoveredCell = cell.that(hasClass("ag-column-hover"));
        assertThat(hoveredCell.that(hasAggregatedTextContaining("832")), CustomMatchers.isDisplayed());
    }

    @Test
    public void ensureCellValueIsPresentWorks() {
        AgGridHighLevelOperations agGridHighLevelOperations = new AgGridHighLevelOperations(div.that(hasId("myGrid")));
        agGridHighLevelOperations.ensureCellValueIsPresent(40, "{name}", "Chloe Keegan");
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
