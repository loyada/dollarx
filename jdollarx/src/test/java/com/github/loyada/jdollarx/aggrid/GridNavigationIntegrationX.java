package com.github.loyada.jdollarx.aggrid;

import com.github.loyada.jdollarx.DriverSetup;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.AgGrid;
import com.github.loyada.jdollarx.singlebrowser.TemporaryChangedTimeout;
import com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static com.github.loyada.jdollarx.BasicPath.button;
import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.ElementProperties.hasAggregatedTextEqualTo;
import static com.github.loyada.jdollarx.ElementProperties.hasId;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.clickOn;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.find;
import static org.hamcrest.MatcherAssert.assertThat;

public class GridNavigationIntegrationX {
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
    public void ensureVisibilityOfColumnWithSpecificId() {
        AgGrid grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                .withRowsAsStringsInOrder(Collections.EMPTY_LIST)
                .containedIn(div.that(hasId("myGrid")))
                .build();
        Path janCell = grid.ensureVisibilityOfCellInColumn("jan", hasAggregatedTextEqualTo("$4,298"));
        Path octCell = grid.ensureVisibilityOfCellInColumnInVisibleRowById(AgGrid.ROW.containing(janCell), "oct");
        assertThat(octCell, CustomMatchers.isDisplayed());
    }
}
