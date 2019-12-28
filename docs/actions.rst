=======
Actions
=======

.. currentmodule:: dollarx

.. contents:: :local:

There are 2 versions of the API for browser operations: The standard one, that supports multiple connected browser
instance, under \ :java:ref:`InBrowser`\, and  the singleton browser version, under \ :java:ref:`InBrowserSinglton`\   .

The difference is that the single-browser-instance API is simplified. For example:

.. code-block:: java

     // standard interface:
     // Assume we have: InBrowser browser = .... ;
     browser.clickAt(myElement);
     browser.dragAndDrop(myElement).to(otherElement);

     // Single browser version:
     // assume we have static imports from InBrowserSingleton
     clickAt(myElement);
     dragAndDrop(myElement).to(otherElement);

Supported Operations
====================
Supported operations include:

* \ :java:ref:`InBrowser.clickOn`\,  \ :java:ref:`InBrowser.clickAt`\  - click an element. The difference between \
  the two is that the former expects the element to be clickable.
* \ :java:ref:`InBrowser.doubleClickOn`\
* \ :java:ref:`InBrowser.dragAndDrop`\ - to another element or to a location by coordinates
* \ :java:ref:`InBrowser.scroll`\ - scroll up,down,left or right.
* \ :java:ref:`InBrowser.scrollTo`\ - scroll to the location of an element
* \ :java:ref:`InBrowser.scrollElement`\ - scroll within an element. Useful for grid, especially when they are virtualized
* \ :java:ref:`InBrowser.pressKeyDown`\, \ :java:ref:`InBrowser.releaseKey`\ - on an element or in the browser in general
* \ :java:ref:`InBrowser.sendKeys`\ - send text to element or to browser in general
* \ :java:ref:`InBrowser.hoverOver`\  - hover over an element


Select Menu
===========
Since the Select API in Selenium is good for most use cases, dollarx did not re-implement it.
Instead, to use it, you would do something like:

.. code-block:: java

     //For clarity, I avoided any static imports. In real life, I encourage using them.

     // my dropdown menu is the first "select" element in the form.
     Path selectElement = BasicPath.select.inside(BasicPath.form);

     Select myDropdownMenu = InBrowserSingleton.getSelect(selectElement);

     // use the Selenium Select API ...
     myDropdownMenu.selectByVisibleText("apple");


