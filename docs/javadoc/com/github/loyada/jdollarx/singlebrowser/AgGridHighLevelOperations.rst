.. java:import:: com.github.loyada.jdollarx Operations

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: com.github.loyada.jdollarx.singlebrowser.highlevelapi Inputs

.. java:import:: java.util ArrayList

.. java:import:: java.util Collections

.. java:import:: java.util List

.. java:import:: java.util Map

.. java:import:: java.util Set

AgGridHighLevelOperations
=========================

.. java:package:: com.github.loyada.jdollarx.singlebrowser
   :noindex:

.. java:type:: public final class AgGridHighLevelOperations

   High level utilities for definitions of simplified grids and operations

Constructors
------------
AgGridHighLevelOperations
^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:constructor:: public AgGridHighLevelOperations(Path gridContainer)
   :outertype: AgGridHighLevelOperations

Methods
-------
buildMinimalGridFromHeader
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public AgGrid buildMinimalGridFromHeader(List<String> headers)
   :outertype: AgGridHighLevelOperations

cellInGrid
^^^^^^^^^^

.. java:method:: public Path cellInGrid(int rowNumber, String columnName)
   :outertype: AgGridHighLevelOperations

   Ensure a specific cell is visible and return a Path to it

   :param rowNumber: row number
   :param columnName: column name
   :return: the request cell

changeSimpleInputValueByRowNumber
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void changeSimpleInputValueByRowNumber(String columnName, int rowNumber, String newValue) throws Operations.OperationFailedException
   :outertype: AgGridHighLevelOperations

   select an option from a dropdown in a cell

   :param columnName: column name
   :param rowNumber: row number
   :param newValue: new Value

changeSimpleInputValueByValue
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void changeSimpleInputValueByValue(String columnName, String oldValue, String newValue) throws Operations.OperationFailedException
   :outertype: AgGridHighLevelOperations

   select an option from a dropdown in a cell

   :param columnName: column name
   :param oldValue: row number
   :param newValue: new Value

clickOnColumnWithValue
^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public Path clickOnColumnWithValue(String columnName, String value)
   :outertype: AgGridHighLevelOperations

   Find a the first cell in the given column with the given value, ensure it is visible, and click on it.

   :param columnName: the column name
   :param value: the value of the cell we are looking for
   :return: the cell element

ensureCellValueIsPresent
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void ensureCellValueIsPresent(int rowNumber, String columnTitle, String expectedValue)
   :outertype: AgGridHighLevelOperations

   Ensure(or assert) that the cell in specific row and column has the expected value

   :param rowNumber: - number of row of the cell
   :param columnTitle: - the column of the cell
   :param expectedValue: - the value we assert in that cell

getMinimalGrid
^^^^^^^^^^^^^^

.. java:method:: public AgGrid getMinimalGrid(String columnName)
   :outertype: AgGridHighLevelOperations

   create a minimal grid definition that has the column we are interersted in

   :param columnName: the column name
   :return: a grid object

goToEditModeInCell
^^^^^^^^^^^^^^^^^^

.. java:method:: public Path goToEditModeInCell(String columnName, int rowNumber)
   :outertype: AgGridHighLevelOperations

   Find a cell, and doubleclick it

   :param columnName: column name
   :param rowNumber: row number
   :return: the cell

goToEditModeInCell
^^^^^^^^^^^^^^^^^^

.. java:method:: public Path goToEditModeInCell(String columnName, String value)
   :outertype: AgGridHighLevelOperations

   Find a cell, and doubleclick it

   :param columnName: column name
   :param value: value of cell
   :return: the cell

hoverOverCell
^^^^^^^^^^^^^

.. java:method:: public Path hoverOverCell(int rowNumber, String columnTitle)
   :outertype: AgGridHighLevelOperations

   Hover over speicic cell, after ensuring it is visible

   :param rowNumber: - row number
   :param columnTitle: - column
   :return: the cell

selectInCell
^^^^^^^^^^^^

.. java:method:: public void selectInCell(String columnName, int rowNumber, String option)
   :outertype: AgGridHighLevelOperations

   select an option from a dropdown in a cell

   :param columnName: column name
   :param rowNumber: row number
   :param option: option to choose

unorderedGrid
^^^^^^^^^^^^^

.. java:method:: public AgGrid unorderedGrid(List<Map<String, String>> rows)
   :outertype: AgGridHighLevelOperations

   define AgGrid with unordered rows

   :param rows: a list of the rows, ignoring the order
   :return: an AgGrid object

