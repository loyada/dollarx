package com.github.loyada.jdollarx;

import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import com.github.loyada.jdollarx.singlebrowser.custommatchers.AgGrid;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.BasicPath.image;
import static com.github.loyada.jdollarx.ElementProperties.contains;
import static com.github.loyada.jdollarx.ElementProperties.hasAggregatedTextEqualTo;
import static com.github.loyada.jdollarx.ElementProperties.hasId;
import static com.github.loyada.jdollarx.ElementProperties.hasSource;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.clickAt;
import static org.hamcrest.MatcherAssert.assertThat;


public class VirtualizedGridExample {
      public static void main(String[] argc) throws Operations.OperationFailedException {
          InBrowserSinglton.driver = DriverSetup.createStandardChromeDriver();
          InBrowserSinglton.driver.get("https://www.ag-grid.com/example.php");
          InBrowser browser =  new InBrowser(InBrowserSinglton.driver) ;
          browser.getDriver().manage().timeouts().implicitlyWait(1, TimeUnit.MILLISECONDS);
          Path table = div.withClass("ag-body-viewport");

 //         InBrowserSinglton.scrollElement(table).rightUntilElementIsPresent(div.that(hasAggregatedTextContaining("$86,416")));
 //         InBrowserSinglton.scrollElement(table).leftUntilElementIsPresent(div.that(hasAggregatedTextContaining("Tony Smith")));
 //         InBrowserSinglton.scrollElement(table).downUntilElementIsPresent(div.that(hasAggregatedTextContaining("isabella cage")));
 //         InBrowserSinglton.scrollElement(table).upUntilElementIsPresent(div.that(hasAggregatedTextContaining("Gil Lopes")));

 //         InBrowserSinglton.scrollElement(table).toTopLeftCorner();




          Map<String, ElementProperty> rowWithProperties = new HashMap<>();
          rowWithProperties.put("name", hasAggregatedTextEqualTo("tony smith"));
          rowWithProperties.put("language", hasAggregatedTextEqualTo("english"));
          rowWithProperties.put("country", contains(image.that(hasSource("https://flags.fmcdn.net/data/flags/mini/ie.png"))));

          // No virtualization - successful
          AgGrid nonVirtualizedGrid = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList( "language", "name", "country"))
                  .withRowsAsElementProperties(Arrays.asList(rowWithProperties))
                  .containedIn(div.that(hasId("myGrid")))
                  .withoutVirtualization()
                  .build();
          assertThat(nonVirtualizedGrid, AgGrid.isPresent());

          Map<String, String> row1 = new HashMap<>();
          row1.put("name", "tony smith");
          row1.put("language", "english");
          row1.put("jan","$38,031");
          row1.put("dec","$86,416");
          Map<String, String> row2 = new HashMap<>();
          row2.put("name", "Andrew Connell");
          row2.put("language", "swedish");
          row2.put("jan","$17,697");
          row2.put("dec","$83,386");

          // Successful
          AgGrid grid = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                  .withRowsAsStrings(Arrays.asList(row1, row2))
                  .containedIn(div.that(hasId("myGrid")))
                  .build();
          assertThat(grid, AgGrid.isPresent());
          clickCellExample(grid);
          grid.clickOnSort("name");

          // failure - wrong order of rows
           grid = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                  .withRowsAsStrings(Arrays.asList(row2, row1))
                  .containedIn(div.that(hasId("myGrid")))
                  .build();
          assertThat(grid, AgGrid.isPresent());


          // failure
          grid = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                  .withRowsAsStrings(Arrays.asList(row1))
                  .containedIn(div.that(hasId("myGrid")))
                  .isStrict()
                  .build();
          assertThat(grid, AgGrid.isPresent());

          InBrowserSinglton.driver.quit();

    }

    private static void clickCellExample(AgGrid grid) {
          grid.overrideTimeoutDuringOperation(5);
          Path cell = grid.ensureVisibilityOfRowWithIndexAndColumn(10, "dec");
          clickAt(cell);
    }
}
