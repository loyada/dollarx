package com.github.loyada.jdollarx.aggrid;

import com.github.loyada.jdollarx.DriverSetup;
import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.AgGrid;
import com.github.loyada.jdollarx.singlebrowser.AgGridHighLevelOperations;
import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import com.github.loyada.jdollarx.singlebrowser.TemporaryChangedTimeout;
import com.github.loyada.jdollarx.singlebrowser.custommatchers.AgGridMatchers;
import com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.github.loyada.jdollarx.BasicPath.button;
import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.ElementProperties.hasAggregatedTextEqualTo;
import static com.github.loyada.jdollarx.ElementProperties.hasId;
import static com.github.loyada.jdollarx.singlebrowser.AgGrid.HEADER_CELL;
import static com.github.loyada.jdollarx.singlebrowser.AgGrid.SortDirection.ascending;
import static com.github.loyada.jdollarx.singlebrowser.AgGrid.SortDirection.descending;
import static com.github.loyada.jdollarx.singlebrowser.AgGrid.SortDirection.none;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.clickOn;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.find;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.findAll;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;

public class GridMenuOperationsIntegrationX {
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
        InBrowserSinglton.setImplicitTimeout(20,  TimeUnit.MILLISECONDS);
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

        try(TemporaryChangedTimeout timeout = new TemporaryChangedTimeout(10, TimeUnit.SECONDS)) {
                clickOn(button.withText("accept all cookies"));
        } catch (Exception ex) {
                // no such button
        }
    }

    @Test
    public void openColumnsSelectionMenuAndGetMenu() {
        Path menu = grid.openColumnsSelectionMenuAndGetMenu();
        assertThat(menu, CustomMatchers.isDisplayed());
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
        agGridHighLevelOperations.hoverOverCell(3, "Nov" );
    }

    @Test
    public void editDropDownInCell() {
        // given
        AgGridHighLevelOperations agGridHighLevelOperations = new AgGridHighLevelOperations(div.that(hasId("myGrid")));

        // when
        agGridHighLevelOperations.selectInCell("Country", 50, "Iceland");

        // then
        agGridHighLevelOperations.ensureCellValueIsPresent(50, "Country", "Iceland");
    }

    @Test
    public void editInputInCellByColumnAndValue() {
        // given
        AgGridHighLevelOperations agGridHighLevelOperations = new AgGridHighLevelOperations(div.that(hasId("myGrid")));

        // when
        agGridHighLevelOperations.changeSimpleInputValueByValue("Bank Balance", "$15,749", "9999");

        // then
        AgGrid minimalGrid = agGridHighLevelOperations.getMinimalGrid("Bank Balance");
        minimalGrid.ensureVisibilityOfCellInColumn("Bank Balance",
                hasAggregatedTextEqualTo("$9,999"));    }

    @Test
    public void editInputInCellByRowIndexAndColumn() throws Operations.OperationFailedException {
        // given
        AgGridHighLevelOperations agGridHighLevelOperations = new AgGridHighLevelOperations(div.that(hasId("myGrid")));

        // when
        agGridHighLevelOperations.changeSimpleInputValueByRowNumber("Bank Balance", 50, "999999");

        // then
        AgGrid minimalGrid = agGridHighLevelOperations.getMinimalGrid("Bank Balance");
        minimalGrid.ensureVisibilityOfCellInColumn("Bank Balance",
                hasAggregatedTextEqualTo("$999,999"));    }


    @Test
    public void ensureCellValueIsPresentWorks() {
        AgGridHighLevelOperations agGridHighLevelOperations = new AgGridHighLevelOperations(div.that(hasId("myGrid")));
        agGridHighLevelOperations.ensureCellValueIsPresent(40, "{name}", "Chloe Keegan");
    }

    @Test
    public void containsRowNegativeCase() {
        AgGridHighLevelOperations agGridHighLevelOperations = new AgGridHighLevelOperations(div.that(hasId("myGrid")));
        Map <String, String> row = Map.of(
                "{name}", "Sophie Foo",
                "language", "Norwegian",
                "jan","$84,878"
        );
        assertThat(agGridHighLevelOperations, Matchers.not(AgGridMatchers.containsRow(row)));
    }


    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
