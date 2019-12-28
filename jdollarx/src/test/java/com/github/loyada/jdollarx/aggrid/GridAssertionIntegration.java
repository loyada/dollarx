package com.github.loyada.jdollarx.aggrid;

import com.github.loyada.jdollarx.*;
import com.github.loyada.jdollarx.singlebrowser.AgGrid;
import org.hamcrest.Matchers;
import org.junit.*;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.BasicPath.image;
import static com.github.loyada.jdollarx.ElementProperties.*;
import static com.github.loyada.jdollarx.ElementProperties.hasAggregatedTextEqualTo;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.AgGridMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;

public class GridAssertionIntegration {
    Path container  = div.that(hasId("myGrid"));

    @BeforeClass
    public static void setup() {
        driver = DriverSetup.createStandardChromeDriver();
        driver.get("https://www.ag-grid.com/example.php");
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.MILLISECONDS);
    }


    @Before
    public void refresh() {
        driver.navigate().refresh();
    }


    @Test
    public void nonVirtualizedGridSuccess() {
        Map<String, ElementProperty> rowWithProperties = new HashMap<>();
        rowWithProperties.put("{name}", hasAggregatedTextEqualTo("tony smith"));
        rowWithProperties.put("language", hasAggregatedTextEqualTo("english"));
        rowWithProperties.put("country", contains(image.that(hasSource("https://flags.fmcdn.net/data/flags/mini/ie.png"))));

        AgGrid nonVirtualizedGrid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList( "language", "{name}", "country"))
                .withRowsAsElementProperties(Arrays.asList(rowWithProperties))
                .containedIn(container)
                .withoutVirtualization()
                .build();
        assertThat(nonVirtualizedGrid, isPresent());
    }

    @Test
    public void orderedSuccess1() {
        Map<String, String> row1 = new LinkedHashMap<>();
        row1.put("name", "tony smith");
        row1.put("language", "english");
        row1.put("jan","$38,031");
        row1.put("dec","$86,416");
        Map<String, String> row2 = new LinkedHashMap<>();
        row2.put("name", "Andrew Connell");
        row2.put("language", "swedish");
        row2.put("jan","$17,697");
        row2.put("dec","$83,386");
        AgGrid grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                .withRowsAsStrings(Arrays.asList(row1, row2))
                .containedIn(container)
                .build();
        grid.setScrollStep(500);
        assertThat(grid, isPresent());
    }

    @Test
    public  void orderedSuccessWithListOfEntries() {
        List<Entry<String, String>> row1 = Arrays.asList(
                new SimpleEntry<>("name", "tony smith"),
                new SimpleEntry<>("language", "english"),
                new SimpleEntry<>("jan","$38,031"),
                new SimpleEntry<>("dec","$86,416")
        );
        List<Entry<String, String>> row2 = Arrays.asList(
                new SimpleEntry<>("name", "Andrew Connell"),
                new SimpleEntry<>("language", "swedish"),
                new SimpleEntry<>("jan","$17,697"),
                new SimpleEntry<>("dec","$83,386")
        );
        AgGrid grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                .withRowsAsStringsInOrder(Arrays.asList(row1, row2))
                .containedIn(container)
                .build();
        assertThat(grid, isPresent());
    }

    @Test
    public void orderedSuccessWithColumnIdAndListsOfEntries() {
        List<Entry<String, String>> row1 = Arrays.asList(
                new SimpleEntry<>("{name}", "tony smith"),
                new SimpleEntry<>("language", "english"),
                new SimpleEntry<>("jan","$38,031"),
                new SimpleEntry<>("dec","$86,416")
        );
        List<Entry<String, String>> row2 = Arrays.asList(
                new SimpleEntry<>("{name}", "Andrew Connell"),
                new SimpleEntry<>("language", "swedish"),
                new SimpleEntry<>("jan","$17,697"),
                new SimpleEntry<>("dec","$83,386")
        );
        AgGrid grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList("dec", "jan", "language", "{name}"))
                .withRowsAsStringsInOrder(Arrays.asList(row1, row2))
                .containedIn(container)
                .build();
        grid.setScrollStep(500);
        assertThat(grid, isPresent());
    }

    @Test
    public void orderedSuccessWithColumnIdAndElementProperties() {
        List<Entry<String, ElementProperty>> row1 = Arrays.asList(
                new SimpleEntry<>("{name}", hasAggregatedTextEqualTo("tony smith")),
                new SimpleEntry<>("language", hasAggregatedTextEqualTo("english")),
                new SimpleEntry<>("jan",hasAggregatedTextEqualTo("$38,031")),
                new SimpleEntry<>("dec",hasAggregatedTextEqualTo("$86,416"))
        );
        List<Entry<String, ElementProperty>> row2 = Arrays.asList(
                new SimpleEntry<>("{name}", hasAggregatedTextEqualTo("Andrew Connell")),
                new SimpleEntry<>("language", hasAggregatedTextEqualTo("swedish")),
                new SimpleEntry<>("jan",hasAggregatedTextEqualTo("$17,697")),
                new SimpleEntry<>("dec",hasAggregatedTextEqualTo("$83,386"))
        );
        AgGrid grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList("dec", "jan", "language", "{name}"))
                .withRowsAsElementPropertiesInOrder(Arrays.asList(row1, row2))
                .containedIn(container)
                .build();
        grid.setScrollStep(500);
        assertThat(grid, isPresent());
    }

    @Test(expected = AssertionError.class)
    public void orderedFailureRowsInWrongOrder(){
        List<Entry<String, String>> row1 = Arrays.asList(
                new SimpleEntry<> ("name", "tony smith"),
                new SimpleEntry<> ("language", "english"),
                new SimpleEntry<> ("jan","$38,031"),
                new SimpleEntry<> ("dec","$86,416")
        );
        List<Entry<String, String>> row2 = Arrays.asList(
                new SimpleEntry<> ("name", "Andrew Connell"),
                new SimpleEntry<> ("language", "swedish"),
                new SimpleEntry<> ("jan","$17,697"),
                new SimpleEntry<> ("dec","$83,386")
        );
        AgGrid grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                .withRowsAsStringsInOrder(Arrays.asList(row2, row1))
                .containedIn(container)
                .build();
        assertThat(grid, isPresent());
    }

    @Test
    public void orderedFailureInStrictModeWithoutAllRows(){
        List<Entry<String, String>> row1 = Arrays.asList(
                new SimpleEntry<> ("name", "tony smith"),
                new SimpleEntry<> ("language", "english"),
                new SimpleEntry<> ("jan","$38,031"),
                new SimpleEntry<> ("dec","$86,416")
        );
        List<Entry<String, String>> row2 = Arrays.asList(
                new SimpleEntry<> ("name", "Andrew Connell"),
                new SimpleEntry<> ("language", "swedish"),
                new SimpleEntry<> ("jan","$17,697"),
                new SimpleEntry<> ("dec","$83,386")
        );
        AgGrid grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                .withRowsAsStringsInOrder(Arrays.asList(row1, row2))
                .containedIn(container)
                .isStrict()
                .build();
        try {
            assertThat(grid, isPresent());
            Assert.fail();
        } catch(AssertionError e) {
            assertThat(e.getMessage(), Matchers.containsString("could not find grid with exactly 2 rows. Found too many rows."));
        }
    }


    @AfterClass
    public static void tearDown() {
       driver.quit();;
    }
}
