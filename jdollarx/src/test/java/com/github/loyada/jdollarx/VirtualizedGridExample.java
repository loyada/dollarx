package com.github.loyada.jdollarx;

import com.github.loyada.jdollarx.singlebrowser.AgGrid;
import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import com.github.loyada.jdollarx.singlebrowser.SingltonBrowserImage;
import com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import static com.github.loyada.jdollarx.BasicPath.body;
import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.BasicPath.image;
import static com.github.loyada.jdollarx.ElementProperties.contains;
import static com.github.loyada.jdollarx.ElementProperties.hasAggregatedTextEqualTo;
import static com.github.loyada.jdollarx.ElementProperties.hasId;
import static com.github.loyada.jdollarx.ElementProperties.hasSource;
import static com.github.loyada.jdollarx.singlebrowser.AgGrid.SortDirection.ascending;
import static com.github.loyada.jdollarx.singlebrowser.AgGrid.SortDirection.descending;
import static com.github.loyada.jdollarx.singlebrowser.AgGrid.SortDirection.none;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.clickAt;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.clickOn;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.contextClick;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.AgGridMatchers.isPresent;
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
          rowWithProperties.put("{name}", hasAggregatedTextEqualTo("tony smith"));
          rowWithProperties.put("language", hasAggregatedTextEqualTo("english"));
          rowWithProperties.put("country", contains(image.that(hasSource("https://flags.fmcdn.net/data/flags/mini/ie.png"))));

          // No virtualization - successful
          AgGrid nonVirtualizedGrid = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList( "language", "{name}", "country"))
                  .withRowsAsElementProperties(Arrays.asList(rowWithProperties))
                  .containedIn(div.that(hasId("myGrid")))
                  .withoutVirtualization()
                  .build();
          assertThat(nonVirtualizedGrid, isPresent());
 //         int index = nonVirtualizedGrid.findRowIndex(rowWithProperties);
 //         Path row = nonVirtualizedGrid.ensureVisibilityOfRowWithIndex(index);
 //         clickAt(nonVirtualizedGrid.CELL.inside(row));

 /*         Map<String, String> row1 = new LinkedHashMap<>();
          row1.put("name", "tony smith");
          row1.put("language", "english");
          row1.put("jan","$38,031");
          row1.put("dec","$86,416");
          Map<String, String> row2 = new LinkedHashMap<>();
          row2.put("name", "Andrew Connell");
          row2.put("language", "swedish");
          row2.put("jan","$17,697");
          row2.put("dec","$83,386");
*/
 // refer to column id instead of column text
        List<Entry<String, String>> row1 = Arrays.asList(
            new SimpleEntry<> ("{name}", "tony smith"),
            new SimpleEntry<> ("language", "english"),
            new SimpleEntry<> ("jan","$38,031"),
            new SimpleEntry<> ("dec","$86,416")
        );
        List<Entry<String, String>> row2 = Arrays.asList(
            new SimpleEntry<> ("{name}", "Andrew Connell"),
            new SimpleEntry<> ("language", "swedish"),
            new SimpleEntry<> ("jan","$17,697"),
            new SimpleEntry<> ("dec","$83,386")
        );
        // Successful
        AgGrid grid = AgGrid.getBuilder()
            .withHeaders(Arrays.asList("dec", "jan", "language", "{name}"))
            .withRowsAsStringsInOrder(Arrays.asList(row1, row2))
            .containedIn(div.that(hasId("myGrid")))
            .build();
        grid.showAllColumnsUsingMenuOfColumn("language");
        grid.showAllColumnsUsingFirstColumn();

        grid.sortBy("language", descending);
        grid.sortBy("language", ascending);
        grid.sortBy("language", none);

          new SingltonBrowserImage(body).show();
        //   grid.overrideTimeoutDuringOperation(100);
        grid.setScrollStep(500);
        assertThat(grid, isPresent());
        System.out.println(">>>> Done asserting grid with column ID instead of header text");


        row1 = Arrays.asList(
                    new SimpleEntry<> ("name", "tony smith"),
                    new SimpleEntry<> ("language", "english"),
                    new SimpleEntry<> ("jan","$38,031"),
                    new SimpleEntry<> ("dec","$86,416")
            );
        row2 = Arrays.asList(
                  new SimpleEntry<> ("name", "Andrew Connell"),
                  new SimpleEntry<> ("language", "swedish"),
                  new SimpleEntry<> ("jan","$17,697"),
                  new SimpleEntry<> ("dec","$83,386")
          );
          // Successful
          grid = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                  .withRowsAsStringsInOrder(Arrays.asList(row1, row2))
                  .containedIn(div.that(hasId("myGrid")))
                  .build();
          assertThat(grid, isPresent());
          System.out.println(">>>> Done asserting grid");
          clickRowExample();
          System.out.println(">>>> Done clicking a row");
          clickCellByColumnExample(grid);
          System.out.println(">>>> Done clicking a cell by column");
          grid.clickMenuOfHeader("name");
          clickCellExample(grid);
          System.out.println(">>>> Done clicking a cell");

          grid.clickOnSort("dec");
          grid.clickMenuOfHeader("name");

          // failure - wrong order of rows
           grid = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                  .withRowsAsStringsInOrder(Arrays.asList(row2, row1))
                  .containedIn(div.that(hasId("myGrid")))
                  .build();
          assertThat(grid, isPresent());


          // failure
          grid = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                  .withRowsAsStringsInOrder(Arrays.asList(row1))
                  .containedIn(div.that(hasId("myGrid")))
                  .isStrict()
                  .build();
          assertThat(grid, isPresent());

          InBrowserSinglton.driver.quit();

    }

    private static void clickCellExample(AgGrid grid) {
          grid.overrideTimeoutDuringOperation(5);
          Path cell = grid.ensureVisibilityOfRowWithIndexAndColumn(10, "dec");
          clickAt(cell);
    }


    private static void clickCellByColumnExample(AgGrid grid) {
        Path myCell = grid.ensureVisibilityOfCellInColumn("jan", hasAggregatedTextEqualTo("$4,298"));
        assertThat(myCell, CustomMatchers.isDisplayed());
        clickOn(myCell);
    };

    private static void clickRowExample() {

        Map<String, ElementProperty> rowWithProperties = new HashMap<>();
        rowWithProperties.put("name", hasAggregatedTextEqualTo("Kevin Cole"));
        rowWithProperties.put("language", hasAggregatedTextEqualTo("english"));
        rowWithProperties.put("country", contains(image.that(hasSource("https://flags.fmcdn.net/data/flags/mini/ie.png"))));
        AgGrid grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList( "language", "name", "country", "dec"))
                .withRowsAsElementProperties(new ArrayList<>())
                .containedIn(div.that(hasId("myGrid")))
                .build();
        grid.setScrollStep(500);

        grid.overrideTimeoutDuringOperation(1);

        System.out.println(">>>> Looking for the index of a row with scrolling");
        int index1 = grid.findRowIndex(rowWithProperties);
        System.out.println(">>>> Done looking for the index of a row with scrolling");
        System.out.println(">>>> Looking for the index of a row already present");
        int index2 = grid.findRowIndex(rowWithProperties);
        System.out.println(">>>> Done looking for the index of a row already present");
        assert(index1==index2);

        System.out.println(">>>> looking for the index of a previous row with horizontal scrolling, using ordered map for performance");
        Map<String, ElementProperty> previousRaw = new LinkedHashMap<>();
        previousRaw.put("name", hasAggregatedTextEqualTo("andrew cohen"));
        previousRaw.put("language", hasAggregatedTextEqualTo("spanish"));
        previousRaw.put("dec", hasAggregatedTextEqualTo("$72,351"));
        int IndexFound = grid.findRowIndex(previousRaw);
        System.out.println(">>>> Done looking for the index of a previous row with horizontal scrolling");
        assert(index1==IndexFound+1);

        Path row = grid.ensureVisibilityOfRowWithIndex(index1);
        contextClick(grid.CELL.inside(row));
    }
}
