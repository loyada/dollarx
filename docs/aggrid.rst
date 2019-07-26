==============
AgGrid Testing
==============

.. currentmodule:: dollarx

.. contents:: :local:

Introduction
============
Since Ag-Grid is so popular, DollarX provides specific support for it using \ :java:ref:`AgGrid`\   .


Defining a grid
===============
The AgGrid class provides a builder that allows to define a grid. Here is an example of a grid with two rows, containing
only text:

.. code-block:: java

     import com.github.loyada.jdollarx.singlebrowser.AgGrid;

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

    // A table with two rows
    AgGrid grid2 = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                  .withRowsAsStrings(Arrays.asList(row1, row2))
                  .containedIn(div.that(hasId("myGrid")))
                  .build();


Here is an example of a grid of one row, with more flexible content (e.g. an image):

.. code-block:: java

     Map<String, ElementProperty> rowWithProperties = new HashMap<>();
     rowWithProperties.put("name", hasAggregatedTextEqualTo("tony smith"));
     rowWithProperties.put("country", contains(image.that(hasSource("https://flags.fmcdn.net/data/flags/mini/ie.png"))));

     // We define a table with a single row, and content that is not just text
     AgGrid grid = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList("name", "country"))
                  .withRowsAsElementProperties(Arrays.asList(rowWithProperties))
                  .containedIn(div.that(hasId("myGrid")))
                  .build();



If you know the order of the column in the table, and you want to potentially speed up the operation in case the order is
the way you define it, you can use a declaration that maintains the order, as follows:

.. code-block:: java

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
        // A table with two rows, with known order of columns
        AgGrid grid = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                  .withRowsAsStringsInOrder(Arrays.asList(row1, row2))
                  .containedIn(div.that(hasId("myGrid")))
                  .build();

You can also define the grid to be:
* non virtualized - if you know your grid is indeed not virtualized, it would make operations much faster
* strict - when asserting, would verify that the table contains only the data in your declaration

Note that by default, not all columns or all rows are required to be defined.

Refering To Columns/Headers by ID
---------------------------------
If there is a column without a textual header, you may want to still refer to it.
This can be achieved by wrapping the ID in curly braces, such as the following example.

.. code-block:: java

        // match on the column with ID of "country" (as opposed to the text in the header)
        Map<String, String> row = Map.of("{country}", "USA");
        AgGrid grid = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList("{country}"))
                  .withRowsAsStrings(Arrays.asList(row))
                  .containedIn(div.that(hasId("myGrid")))
                  .build();



Changing Default Step Size When Scrolling
-----------------------------------------
The Default step size when scrolling through the grid is 60 pixels. You can override that by calling
\ :java:ref:`AgGrid.setScrollStep`\  . This could increase performance significantly, especially with large grids.


Assertion of the content of an AgGrid
=====================================

The Hamcrest matcher supporting that, is  \ :java:ref:`AgGrid.isPresent`\   .

For example, you can define a virtualized grid with a flexible content, such as images:

.. code-block:: java


     import com.github.loyada.jdollarx.singlebrowser.AgGrid;

     Map<String, ElementProperty> rowWithProperties = new HashMap<>();
     rowWithProperties.put("name", hasAggregatedTextEqualTo("tony smith"));
     rowWithProperties.put("country", contains(image.that(hasSource("https://flags.fmcdn.net/data/flags/mini/ie.png"))));

     // We define a table with a single row
     AgGrid grid = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList("name", "country"))
                  .withRowsAsElementProperties(Arrays.asList(rowWithProperties))
                  .containedIn(div.that(hasId("myGrid")))
                  .build();
     assertThat(grid, AgGridMatchers.isPresent());


Note that when you run the code above. It scrolls through the grid to find the elements, in order to deal
with the virtualization.


If your table just has text, then the definition is a bit simpler:

.. code-block:: java

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

    // A table with two rows
    AgGrid grid = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList("dec", "jan", "language", "name"))
                  .withRowsAsStrings(Arrays.asList(row1, row2))
                  .containedIn(div.that(hasId("myGrid")))
                  .build();
    assertThat(grid, AgGridMatchers.isPresent());


The class supports virtualized and non-virtualized tables.
If you want your assertion to fail if you have more rows than you define, define the grid as strict.


What about accessing a specific cell/row/header in an AgGrid?
=============================================================

In order to access a specific row in an AgGrid, you provide the index of the row as it appears in the table. The library
will scroll until the row is visible (dealing with DOM virtualization), and return a Path element that allows you to access
the row. For example, suppose we want to scroll to the 100th row and click it:

.. code-block:: java

          // override the default timeout to make scrolling through rows faster. This is optional.
          // When operation is done the timeout will go back to be relatively long.
          grid.overrideTimeoutDuringOperation(2);

          Path row = grid.ensureVisibilityOfRowWithIndex(100);
          clickAt(row);



In order to interact with a specific cell, you follow the same pattern - providing the index of the row and the name/title of
the column. Suppose I want to click on the checkbox inside the cell of the 100th row, under the "country" column:

.. code-block:: java

       // scroll until the expected cell, in the 100th row of the grid, under the column "country"
       Path cell = grid.ensureVisibilityOfRowWithIndexAndColumn(100, "country");

       Path checkbox = span.withClass("ag-icon").that(hasNoneOfTheClasses("ag-hidden"));
       clickAt(checkbox.inside(cell));



Another example, this time finding a row with a specific content, then interacting with it:

.. code-block:: java

         AgGrid grid = AgGrid.getBuilder()
                .withHeaders(Arrays.asList( "language", "name", "country"))
                .withRowsAsElementProperties(new ArrayList<>())
                .containedIn(div.that(hasId("myGrid")))
                .build();

        Map<String, ElementProperty> rowWithProperties = new HashMap<>();
        rowWithProperties.put("name", hasAggregatedTextEqualTo("Kevin Cole"));
        rowWithProperties.put("language", hasAggregatedTextEqualTo("english"));
        rowWithProperties.put("country", contains(image.that(hasSource("https://flags.fmcdn.net/data/flags/mini/ie.png"))));

        grid.overrideTimeoutDuringOperation(1);

        // assert that the row exists in the grid and get its index
        int index = grid.findRowIndex(rowWithProperties);

        // ensure the row with the given index is visible, and return a path that allows to access it.
        Path row = grid.ensureVisibilityOfRowWithIndex(index);

        clickAt(grid.CELL.inside(row));



In order to interact with a column header, we can follow a similar pattern, or use special methods:

.. code-block:: java

       // "manual" approach - access the header element
       Path header = grid.getVisibleHeaderPath("country");
       clickAt(header);

       // second approach - shortcut methods:
       // sort column
       grid.clickOnSort("country");

       // open header menu
       grid.clickMenuOfHeader("country");



If you don't know the row number of the content you expect, but you know the column and the content of your cell, you
can find it by the column, as follows:


.. code-block:: java

        // scroll as needed until the expected cell is displayed or throw an exception
        Path myCell = grid.ensureVisibilityOfCellInColumn("jan", hasAggregatedTextEqualTo("$4,298"));

        // now we can interact with the cell we found, for example:
        clickOn(myCell);

        // if we want the ROW of that cell:
        Path row = grid.ROW.containing(myCell);






