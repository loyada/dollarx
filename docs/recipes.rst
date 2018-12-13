.. _recipes:

=======
Recipes
=======

.. currentmodule:: dollarx

.. contents:: :local:

Troubleshooting and Debugging
=============================

I created a path instance and I am not sure what it maps to. What can I do?
---------------------------------------------------------------------------

There are two options:

* Examine the string representation of it. The toString() method of Path provides a readable description of it, that looks like english.

   For example:

.. code-block:: java

    Path dialog = div.withClass("ui-dialog");
    Path myInput = input.inside(dialog).that(hasAttribute("name", "goo"), isAfter(atLeast(2).occurrencesOf(div)));
    System.out.println(myInput);
    // output: input, inside (div, that has class ui-dialog), that [has name: "goo", is after at least 2 occurrences of: div]


* Examine the xpath. This is not the recommended approach, since the whole point of Dollarx is to abstract xpath. But if you are unsure, you can
  always call getXPath(), which returns an Optional of the xpath:

.. code-block:: java

   System.out.println(myInput.getXPath().get());
   // output: "div[contains(concat(' ', normalize-space(@class), ' '), ' ui-dialog ')]//input[@name='goo'][count(preceding::div)>=2]"

Note that DollarX will not include the prefix to the xpath by default (e.g. "//") until it is needed to access the element, and then it will be added automatically, so if you copy-pase it to something like Chrome DevTools, remember to add the prefix yourself.


Working With Paths
==================

I am not sure what is the correct way to define the path that I want. It looks like there are many ways that seem similar. What is the right way?
-------------------------------------------------------------------------------------------------------------------------------------------------

This is by design: one of the key features of DollarX is to a flexible grammar that allows you to define paths in the way which \
is most comfortable for you to express, similarly to how we use spoken language.
If when you read or output the string representation of two paths, their description seems equivalent in English, then they are equivalent from DollarX's perspective.

In the following example, all the "row" values are equivalent.

.. code-block:: java

   Path dialog = div.that(hasClass("ui-dialog"));
   Path row = element.withClass("condition").inside(dialog);
   // or....
   row = element.inside(dialog).withClass("condition");
   row = element.inside(dialog).that(hasClass("condition"));
   row = element.inside(dialog).and(hasClass("condition"));
   row = element.that(hasClass("condition"), isInside(dialog));
   row = element.that(hasClass("condition").and(isInside(dialog)));
   row = element.that(hasClass("condition")).and(isInside(dialog));
   row = element.inside(dialog).that(hasClass("condition"));
   row = element.withClass("condition").and(isInside(dialog));
   row = element.withClass("condition").and(isContainedIn(dialog));
   row = element.that(hasAncesctor(dialog)).and(hasClass("condition"));

   // if you prefer to break the definition to two steps:
   Path condition = element.withClass("condition");
   row = condition.inside(dialog);

Similarly, let's look at the string representation of equivalent paths...

.. code-block:: java

   println(element.withClass("condition").inside(dialog));
   println(element.that(hasClass("condition")).and(isInside(dialog)));
   println(element.that(hasAncesctor(dialog)).and(hasClass("condition")));

   output:
   any element, that has class condition, inside (div, that has class ui-dialog)
   any element, that [has class condition, has ancestor: (div, that has class ui-dialog)]
   any element, that [has ancestor: (div, that has class ui-dialog), has class condition]

As can be seen, all are logically equivalent, thus the xpath they represent is equivalent.


I created a path and want to easily inspect its matches in the browser page I am connected to using Selenium
------------------------------------------------------------------------------------------------------------

Several options:

* Highlight the element in the browser.
  \ :java:ref:`DebugUtil.hightlight`\  , \ :java:ref:`DebugUtil.highlightAll`\ - highlights the element(s) in the browser for 2 seconds.

* Examine the DOM while troubleshooting. DebugUtil offers several methods to do that.
  \ :java:ref:`DebugUtil.getDOMOfAll`\  , \ :java:ref:`DebugUtil.getDOM`\  - returns the elements that match the given path instance in the currect page, presented in a clear, readable way. Typically, these functions eliminate the need
  to use Chrome Devtools for debugging.

  The typical steps are:
     #. Add a breakpoint in the place in your code you expect to find the Path element, or elements, in the browser page.
     #. Run in debug mode and stop in the breakpoint
     #. Evaluate getDOM(myPath) or getDOMOfAll(myPath). The result is equivalent or better to what you would in Chrome Devtools.

* Download the page and convert it to a W3C Document, then inspect it looking for the element you are interested in.
  Luckily DollarX includes functions that do it.

     * \ :java:ref:`DebugUtil.getPageAsW3CDoc`\  - returns a W3C Document object representation of the page in the browser
     * \ :java:ref:`PathParsers.findAllByPath`\  - returns a list of nodes of all the matches to the path in the document.
       BTW - This function is used widely in the unit tests of DollarX to test Paths.



I want to find or interact with an element based on the text content, while ignoring case
-----------------------------------------------------------------------------------------
The property \ :java:ref:`ElementProperties.hasText`\  and the other ones in the examples below are case-insensitive.

.. code-block:: java

   Path myElement = ....;
   Path myElementWithText = myElement.that(hasText("abc"));
   // or alternatively
   Path myElementWithText = myElement.withText("abc");

   // partial match
   myElement.that(hasTextContaining("abc"));
   myElement.that(hasTextStartingWith("abc"));
   myElement.that(hasTextEndingWith("abc"));

I want to find or interact with an element based on the text content, but the some of the text might be in nested elements inside my element
--------------------------------------------------------------------------------------------------------------------------------------------
Use the property \ :java:ref:`ElementProperties.hasAggregatedText`\  - it will aggregate the text under the element, \
including nested elements, while normalizing any "white space" characters.

Like \ :java:ref:`ElementProperties.hasText`\, \ it is case-insensitive, and offers partial matching:

\ :java:ref:`ElementProperties.hasAggregatedTextContaining`\, \ :java:ref:`ElementProperties.hasAggregatedTextStartingWith`\, \ :java:ref:`ElementProperties.hasAggregatedTextEndingWith`\.




I want to assert that a table/list that includes specific rows appears on the page
----------------------------------------------------------------------------------
The guideline is to define a path of the table that contains all the expected rows in their right order, and then \
assert that this table is present. The reason is that we want to maximize atomicity in order to minimize race conditions and round trips to browser.
Let's take a trivial example of a table of names with two rows:

.. code-block:: java

   Path theTable = div.withClass("the-table");
   row = div.withClass("row");
   row1 = row.that(hasAggregatedText("john smith"), isWithIndex(0));
   row2 = row.that(hasAggregatedText("jane king"), isWithIndex(1));

   assert (theTable.that(contains(row1, row2)), isPresent());



Working with Grids
==================

I can't find a cell within a grid, but when I scroll I see it exists
--------------------------------------------------------------------

This is likely the result of a virtualized grid. This grid updates the DOM to include only cells that may be visible to the user.
This kind of optimization can be effective when dealing with large tables.

In such a case, in order to find a cell, you need to scroll until it is present in the DOM.

DollarX allows you to do it with  \ :java:ref:`InBrowser.scrollElement`\  or \ :java:ref:`InBrowserSinglton.scrollElement`\  . The implementation is in  \ :java:ref:`Operations.ScrollElement`\ .

For example:

.. code-block:: java

    Path table = div.withClass("ag-body-viewport");

    InBrowserSinglton.scrollElement(table).rightUntilElementIsPresent(div.that(hasAggregatedTextContaining("$86,416")));
    InBrowserSinglton.scrollElement(table).leftUntilElementIsPresent(div.that(hasAggregatedTextContaining("Tony Smith")));
    InBrowserSinglton.scrollElement(table).downUntilElementIsPresent(div.that(hasAggregatedTextContaining("isabella cage")));


Is there an easy way to assert the content of AgGrid?
-----------------------------------------------------

Yes! Take a look at   \ :java:ref:`AgGrid.isPresent`\   .

For example, you can define a virtualized grid with a flexible content, such as images:

.. code-block:: java


     import com.github.loyada.jdollarx.singlebrowser.custommatchers.AgGrid;

     Map<String, ElementProperty> rowWithProperties = new HashMap<>();
     rowWithProperties.put("name", hasAggregatedTextEqualTo("tony smith"));
     rowWithProperties.put("country", contains(image.that(hasSource("https://flags.fmcdn.net/data/flags/mini/ie.png"))));

     // We define a table with a single row
     AgGrid grid = AgGrid.getBuilder()
                  .withHeaders(Arrays.asList("name", "country"))
                  .withRowsAsElementProperties(Arrays.asList(rowWithProperties))
                  .containedIn(div.that(hasId("myGrid")))
                  .build();
     assertThat(grid, AgGrid.isPresent());


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
    assertThat(grid, AgGrid.isPresent());


The class supports virtualized and non-virtualized tables.
If you want your assertion to fail if you have more rows than you define, define the grid as strict.


Dealing With Race Conditions
============================
Typically, using DollarX correctly (minimizing interactions with the browser) eliminates most of the
 issues with race conditions.
 However, there may be cases in which we may have an intermittent failure. For example - we want to click
 an element, but it is currently hidden by a modal, and it may take a while for the application to remove that
modal.
For these cases, use  \ :java:ref:`Operations.doWithRetries`\   .
For example:

 .. code-block:: java

    Operations.doWithRetries(() -> browser.clickOn(myElement), 5, 10);

This code tries to click the myElement.If it fails (for example - the element is not clickable), it will wait
 10 milliseconds and then try again. This will continue up to 5 times.
Once it reached 5 retries, it will throw the exception thrown by the clickOn().

Another example:

.. code-block:: java

    doWithRetries(() -> assertThat(myElement, isDisplayed()), 5, 10);

This code asserts the myElement is displayed. If the assertion fails, it will wait and retry.
After 5 times, it will throw an assertion error.


Extensions and Customization
============================

I found a property that is unsupported by DollarX out-of-the-box. Can I add it?
-------------------------------------------------------------------------------
Definitely!

There are several options to do so:

* If it a new type of element, use \ :java:ref:`BasicPath.customElement`\ to define a simple new element. For example:

 .. code-block:: java

    // define a new element type: <label></label>
    Path label = customElement("label");


* If it's just a matter of <attribute>=<value> on a DOM element, you can use \ :java:ref:`ElementProperties.hasAttribute`\ .
  For example, suppose you want to define your own \ :java:ref:`ElementProperty`\, called "hasRole". it would look like:

 .. code-block:: java

    ElementProperty hasRole(String role) {
       return hasAttribute("role", role);
    }

    // now you can use it:
    Path myInput = input.that(hasRole("password"));

* For more flexibility, you can use, \ :java:ref:`ElementProperties.hasRawXpathProperty`\  . It allows you to define \
  any constant xpath and string representation. See the JavaDoc for more details.
* For more flexibility, use \ :java:ref:`CustomElementProperties.createPropertyGenerator`\ . This method accepts functions \
  that creates the xpath as well as the string representation of the property.
  Once defined, it can be used as a parameter of \ :java:ref:`CustomElementProperties.hasProperty`.
  For example, to define the "hasRole" property, similarly to the previous section, we could do:

  .. code-block:: java

     Function<String, ElementProperty> role = createPropertyGenerator(
                value -> format("@role='%s'", value),
                value -> format("has role %s", value));

      // Now we can use it:
      Path myEl = div.that(hasProperty(role, "foo")).and(hasClass("x"));

      assertThat(myEl.toString(), is(equalTo("div, that has role foo, and has class x")));


  \ :java:ref:`CustomElementProperties.createPropertyGenerator`\  also has a version that accepts BiFunctions, which allows to create properties such as :

   .. code-block:: java

      BiFunction<String, Integer, ElementProperty> dataNum = createPropertyGenerator(
                (attr,value) -> format("@data-%s='%d'", attr, value),
                (attr, value) -> format("has data %s of %d", attr, value));

      // The following will match a div element with attribute: data-foo="4" and the class "x":
      Path el = div.that(hasProperty(dataNum, "foo", 4)).and(hasClass("x"));

      assertThat(el.toString(), is(equalTo("div, that has data foo of 4, and has class x")));



I need to use Selenium WebDriver directly. Can I use it in conjunction with DollarX?
------------------------------------------------------------------------------------
Yes!

You can get from \ :java:ref:`Path`\  to Selenium WebElement and vice-versa.

* To get a WebElement from a \ :java:ref:`Path`\, call  \ :java:ref:`InBrowser.find`\  or \ :java:ref:`InBrowser.findAll`\, or the equivalents \
  in \ :java:ref:`InBrowserSinglton`\ .

* To get a \ :java:ref:`Path`\  from a WebElement, create a \ :java:ref:`Path`\  as a wrapper of a WebElement. Example:

  .. code-block:: java

   // InBrowser browser = ......;

   WebElement webEl = browser.find(myPath);

   Path insideWebEl = BasicPath.Builder().withUnderlying(webEl).build();

   // Now I can define a path inside webEl:
   Path myFooEl = insideWebEl.withClass("foo");

   // and perform an assertion on it:
   assertThat(myFooEl, isPresentIn(browser));









