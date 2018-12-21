.. java:import:: com.github.loyada.jdollarx ElementProperty

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: com.github.loyada.jdollarx.singlebrowser InBrowserSinglton

.. java:import:: com.google.common.collect ImmutableList

.. java:import:: org.hamcrest Description

.. java:import:: org.hamcrest Matcher

.. java:import:: org.hamcrest TypeSafeMatcher

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.openqa.selenium WebElement

.. java:import:: java.util HashMap

.. java:import:: java.util List

.. java:import:: java.util Map

.. java:import:: java.util.concurrent TimeUnit

.. java:import:: java.util.stream IntStream

AgGrid.AgGridBuilder
====================

.. java:package:: com.github.loyada.jdollarx.singlebrowser.custommatchers
   :noindex:

.. java:type:: public static class AgGridBuilder
   :outertype: AgGrid

Fields
------
isVirtualized
^^^^^^^^^^^^^

.. java:field::  boolean isVirtualized
   :outertype: AgGrid.AgGridBuilder

Methods
-------
build
^^^^^

.. java:method:: public AgGrid build()
   :outertype: AgGrid.AgGridBuilder

   Create an AgGrid definition

   :return: AgGrid instance

containedIn
^^^^^^^^^^^

.. java:method:: public AgGridBuilder containedIn(Path container)
   :outertype: AgGrid.AgGridBuilder

   optional - define the container of the grid

   :param container: the Path of the container of the grid
   :return: AgGridBuilder

isStrict
^^^^^^^^

.. java:method:: public AgGridBuilder isStrict()
   :outertype: AgGrid.AgGridBuilder

   The assertions will be strict - if there are extra rows, it will fail.

   :return: AgGridBuilder

withHeaders
^^^^^^^^^^^

.. java:method:: public AgGridBuilder withHeaders(List<String> headers)
   :outertype: AgGrid.AgGridBuilder

   The headers of the columns

   :param headers: - the headers of the columns
   :return: AgGridBuilder

withRowsAsElementProperties
^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public AgGridBuilder withRowsAsElementProperties(List<Map<String, ElementProperty>> rows)
   :outertype: AgGrid.AgGridBuilder

   Define the rows in the table, in order.

   :param rows: - A list of rows, where each row is a map of the column name to the property that describes the expected content
   :return: AgGridBuilder

withRowsAsStrings
^^^^^^^^^^^^^^^^^

.. java:method:: public AgGridBuilder withRowsAsStrings(List<Map<String, String>> rows)
   :outertype: AgGrid.AgGridBuilder

   Define the rows in the table, in order.

   :param rows: - A list of rows, where each row is a map of the column name to the text.
   :return: AgGridBuilder

withoutVirtualization
^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public AgGridBuilder withoutVirtualization()
   :outertype: AgGrid.AgGridBuilder

   without virtualization. The default is with virtualization.

   :return: AgGridBuilder

