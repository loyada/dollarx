.. java:import:: com.github.loyada.jdollarx BasicPath

.. java:import:: com.github.loyada.jdollarx ElementProperty

.. java:import:: com.github.loyada.jdollarx Operations

.. java:import:: com.github.loyada.jdollarx Operations.OperationFailedException

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: com.github.loyada.jdollarx.singlebrowser.sizing ElementResizer

.. java:import:: com.google.common.collect ImmutableList

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.openqa.selenium NotFoundException

.. java:import:: org.openqa.selenium WebElement

.. java:import:: java.util Arrays

.. java:import:: java.util HashMap

.. java:import:: java.util HashSet

.. java:import:: java.util LinkedHashMap

.. java:import:: java.util List

.. java:import:: java.util Map

.. java:import:: java.util Optional

.. java:import:: java.util OptionalInt

.. java:import:: java.util Set

.. java:import:: java.util.function Function

.. java:import:: java.util.function Predicate

.. java:import:: java.util.regex Pattern

.. java:import:: java.util.stream IntStream

AgGrid.AgGridBuilder
====================

.. java:package:: com.github.loyada.jdollarx.singlebrowser
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

   :param headers: - the headers of the columns. In case you prefer to use a column ID, wrap it with {}. For \ example, "{the-id}" will refer to a header with a column ID of "the-id". This is useful when a column has no textual header.
   :return: AgGridBuilder

withRowsAsElementProperties
^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public AgGridBuilder withRowsAsElementProperties(List<Map<String, ElementProperty>> rows)
   :outertype: AgGrid.AgGridBuilder

   Define the rows in the table, in order.

   :param rows: - A list of rows, where each row is a map of the column name(or column ID) to the property that describes the expected content. To use a column Id as a key, wrap it with curly braces.
   :return: AgGridBuilder

withRowsAsElementPropertiesInOrder
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public AgGridBuilder withRowsAsElementPropertiesInOrder(List<List<Map.Entry<String, ElementProperty>>> rows)
   :outertype: AgGrid.AgGridBuilder

   Define the rows in the table, in order. This version can be faster, in case the columns are ordered as they appear in the table, and the table is virtualized

   :param rows: - A list of rows, where each row is a map of the column name to the property that describes the expected content
   :return: AgGridBuilder

withRowsAsStrings
^^^^^^^^^^^^^^^^^

.. java:method:: public AgGridBuilder withRowsAsStrings(List<Map<String, String>> rows)
   :outertype: AgGrid.AgGridBuilder

   Define the rows in the table, in order.

   :param rows: - A list of rows, where each row is a map of the column name(or column ID) to the text. To use a column Id as a key, wrap it with curly braces.
   :return: AgGridBuilder

withRowsAsStringsInOrder
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public AgGridBuilder withRowsAsStringsInOrder(List<List<Map.Entry<String, String>>> rows)
   :outertype: AgGrid.AgGridBuilder

   Define the rows in the table, in order. This version can be faster, in case the columns

   :param rows: - A list of rows, where each row is a map of the column name(or column ID) to the text. To use a column Id as a key, wrap it with curly braces.
   :return: AgGridBuilder

withoutVirtualization
^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public AgGridBuilder withoutVirtualization()
   :outertype: AgGrid.AgGridBuilder

   without virtualization. The default is with virtualization.

   :return: AgGridBuilder

