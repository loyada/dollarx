.. DollarX documentation master file, created by
   sphinx-quickstart on Tue Mar  6 23:30:13 2018.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Welcome to DollarX's documentation!
===================================
``DollarX`` is a library dedicated for testing web applications, meant to simplify interactions with the browser
 and assertions, making it significantly more maintainable, while optimizing performance and minimizing race-conditions.
 It relies on Selenium WebDriver.


Features
--------

* Thoughtful, expressive API to define W3C elements and interact with the browser

* Eliminates race conditions and optimizes performance

* Easy to use and troubleshoot

* Works on top of Selenium and integrates easily with its API

* Easily extensible using utility functions

* Includes a collection of custom Hamcrest matchers, that are optimized and provide useful error messages

* Two flavours: Standard API supports multiple browser instances. Simplified API supports a single instance of browser.

* Includes Java and Scala implementation (DollarX - Scala, JDollarX - Java)


Example
-------
Basic example:

.. code-block:: java

    //Boilerplate
    InBrowserSinglton.driver = DriverSetup.createStandardChromeDriver(); // assuming we have such utility function
    InBrowserSinglton.driver.get("http://xxx.yyy.zzz");

    // definitions
    Path carouselItem = div.withClasses("carousel-cell");
    Path viewableItem = carouselItem.withClass("is-selected");

    // action
    dragAndDrop(firstOccurrenceOf(viewableItem)).to(lastOccurrenceOf(viewableItem));

    // assertion
    assertThat(viewableItem, isPresent(4).timesOrMore());




Introduction
============

DollarX has 3 components:

* Definitions of ``Paths`` that defines DOM element
* Actions in the browser
* Assertions


DOM Path builder based on XPath
===============================

* Flexible API that allows to declare complex xpath easily, and creates immutable objects
* Grammar is intuitive and similar to English
* Easy to troubleshoot, since toString() of an element is basically English text
* Supports almost any relevant xpath(1.0) expression
* Expandable easily using utility functions
* "Collaborates" with standard Selenium WebElements
* Can be used independently from the other DollarX components


Interactions with the browser
=============================

* Relies on the Path Builder
* Two flavors:

   #. The standard, with multi-browser instances support.
   #. A simplified API package, for a single browser instance


Custom Matchers:
================

* Relies on the Path Builder, Browser interactions.
* Extends Hamcrest and ScalaTest matchers
* Optimized for performance and atomicity (minimize race condition issues)
* Provides useful error message for failures
* Two flavors, similarly to the interaction with the browser
* The general purpose, standard version, supports assertions in both the browser and a given W3C document


JavaDoc
=======
`See standard JavaDoc here <http://htmlpreview.github.com/?https://github.com/loyada/dollarx/javadoc/index.html>`_


Contents:
=========
.. toctree::
:maxdepth: 2

       background
       paths
       actions
       assertions



Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`
