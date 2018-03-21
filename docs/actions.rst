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

* click-on, click-at location
* double click
* drag-and-drop
* scroll
* key-down, key-up - on an element or in the browser in general
* send text to element or to browser in general
* hover over an element
