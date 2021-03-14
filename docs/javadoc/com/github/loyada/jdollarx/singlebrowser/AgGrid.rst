.. java:import:: com.github.loyada.jdollarx BasicPath

.. java:import:: com.github.loyada.jdollarx ElementProperty

.. java:import:: com.github.loyada.jdollarx Operations

.. java:import:: com.github.loyada.jdollarx Operations.OperationFailedException

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: com.github.loyada.jdollarx.singlebrowser.sizing ElementResizer

.. java:import:: com.google.common.collect ImmutableList

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.openqa.selenium NotFoundException

.. java:import:: org.openqa.selenium StaleElementReferenceException

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

AgGrid
======

.. java:package:: com.github.loyada.jdollarx.singlebrowser
   :noindex:

.. java:type:: public class AgGrid

   Custom class to validate the presence of an AgGrid, and interact with it, since it can be tricky. It supports virtualized and non-virtualized tables. It should be used like other custom matchers in the package.

Fields
------
CELL
^^^^

.. java:field:: public static final Path CELL
   :outertype: AgGrid

COL_ID
^^^^^^

.. java:field:: public static final String COL_ID
   :outertype: AgGrid

HEADER_CELL
^^^^^^^^^^^

.. java:field:: public static final Path HEADER_CELL
   :outertype: AgGrid

HEADER_MENU
^^^^^^^^^^^

.. java:field:: public static final Path HEADER_MENU
   :outertype: AgGrid

HEADER_TXT
^^^^^^^^^^

.. java:field:: public static final Path HEADER_TXT
   :outertype: AgGrid

ROW
^^^

.. java:field:: public static final Path ROW
   :outertype: AgGrid

Methods
-------
clickMenuOfHeader
^^^^^^^^^^^^^^^^^

.. java:method:: public void clickMenuOfHeader(String headerText)
   :outertype: AgGrid

   Click on the menu of a the column with the given header

   :param headerText: - the header text, or the column ID. A string wrapped with curly braces is interpreted as the column ID.

clickOnSort
^^^^^^^^^^^

.. java:method:: public void clickOnSort(String headerText)
   :outertype: AgGrid

   Click on the 'sort' column with the given header

   :param headerText: - the header text, or the column ID. A string wrapped with curly braces is interpreted as the column ID.

ensureVisibilityOfCellInColumn
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public Path ensureVisibilityOfCellInColumn(String columnTitle, ElementProperty cellContent)
   :outertype: AgGrid

   Find a specific cell under a column, without knowing the row, ensure it is displayed, and return its Path

   :param columnTitle: the title of the column to look under
   :param cellContent: a property that describes the content of the expect cell
   :return: the Path of the found cell. allows to interact with it

ensureVisibilityOfRowWithIndex
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public Path ensureVisibilityOfRowWithIndex(int n)
   :outertype: AgGrid

   Scroll until the row with the given index is visible, and return a Path element that matches it. Useful for performing operations or accessing fields in the wanted row.

   :param n: the number of row in the table, as visible to the user
   :return: a Path element that allows to access the row

ensureVisibilityOfRowWithIndexAndColumn
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public Path ensureVisibilityOfRowWithIndexAndColumn(int index, String columnTitle)
   :outertype: AgGrid

   Scroll until the row with the given index, as well as the given column, is visible. It return a Path element that matches the wanted cell in row. Useful for performing operations or accessing fields in the wanted cell (for example: edit it)

   :param index: the number of row in the table, as visible to the user
   :param columnTitle: the header title of the wanted cell in the row
   :return: the Path element to access the wanted cell in the wanted row

findRowIndex
^^^^^^^^^^^^

.. java:method:: public int findRowIndex(Map<String, ElementProperty> row)
   :outertype: AgGrid

   Find internal index of row within table. This method typically will make sure the row is also visible if it exists, in case the user needs to interact with it, but in some cases ensureVisiblityOfRow will be required.

   :param row: - the definition of the row content
   :return: the internal index of the row, if it was found

findTableInBrowser
^^^^^^^^^^^^^^^^^^

.. java:method:: public void findTableInBrowser()
   :outertype: AgGrid

getBuilder
^^^^^^^^^^

.. java:method:: public static AgGridBuilder getBuilder()
   :outertype: AgGrid

getRowIndex
^^^^^^^^^^^

.. java:method:: public int getRowIndex(Path row)
   :outertype: AgGrid

   assuming the row is already present in the DOM, get its internal index in the table.

   :param row: the row we are interested in. Should be the value returned from findRowInBrowser() or ensureVisibilityOfRowWithIndex()
   :return: the internal index of the row in the table

getRowIndexOfCell
^^^^^^^^^^^^^^^^^

.. java:method:: public int getRowIndexOfCell(Path cell)
   :outertype: AgGrid

   assuming the row is already present in the DOM, get its internal index in the table.

   :param cell: - the cell in the row we are interested in. Should be the return value of ensureVisibilityOfRowWithIndexAndColumn()
   :return: the internal index of the row in the table

getVisibleHeaderPath
^^^^^^^^^^^^^^^^^^^^

.. java:method:: public Path getVisibleHeaderPath(String headerText)
   :outertype: AgGrid

   Make sure the given column header is visible, and returns a Path element to access it. This is useful to perform direct operations on that element or access other DOM elements contained in the header.

   :param headerText: - the header text, or the column ID. A string wrapped with curly braces is interpreted as the column ID.
   :return: the Path element to access the column header

isVirtualized
^^^^^^^^^^^^^

.. java:method:: public boolean isVirtualized()
   :outertype: AgGrid

openColumnFilterTabAndGetMenu
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public Path openColumnFilterTabAndGetMenu(String headerText)
   :outertype: AgGrid

   open the popup filter for the column

   :param headerText: - the header text, or the column ID, to open the popup menu from. A string wrapped with curly braces is interpreted as the column ID.
   :return: the Path to the popup menu

openColumnMenuTabAndGetMenu
^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public Path openColumnMenuTabAndGetMenu(String headerText)
   :outertype: AgGrid

   open the popup menu for the column

   :param headerText: - the header text, or the column ID, to open the popup menu from. A string wrapped with curly braces is interpreted as the column ID.
   :return: the Path to the popup menu

openColumnsSelectionMenuAndGetMenu
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public Path openColumnsSelectionMenuAndGetMenu(String headerText)
   :outertype: AgGrid

   open the popup columns show/hide selection by using a popup of the given column

   :param headerText: - the header text, or the column ID, to open the popup menu from. A string wrapped with curly braces is interpreted as the column ID.
   :return: the Path to the popup menu

openColumnsSelectionMenuAndGetMenu
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public Path openColumnsSelectionMenuAndGetMenu()
   :outertype: AgGrid

   open the popup columns show/hide selection by using a popup of the first column (assumes it is active)

   :return: the Path to the popup menu

overrideTimeoutDuringOperation
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void overrideTimeoutDuringOperation(int millisecs)
   :outertype: AgGrid

   Override the default timeout threshold for finding elements while scrolling the table. The default is 5 milliseconds

   :param millisecs: - the timeout in milliseconds

overrideTimeoutWhenDone
^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void overrideTimeoutWhenDone(int millisecs)
   :outertype: AgGrid

   Override the default timeout threshold it reverts to when table operations are done. The default is 5000 milliseconds

   :param millisecs: - the timeout in milliseconds

setFinalTimeout
^^^^^^^^^^^^^^^

.. java:method:: public void setFinalTimeout()
   :outertype: AgGrid

setScrollStep
^^^^^^^^^^^^^

.. java:method:: public void setScrollStep(int size)
   :outertype: AgGrid

   Override the default step size of scrolling when moving through a grid

   :param size: step size in pixels

showAllColumnsUsingFirstColumn
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void showAllColumnsUsingFirstColumn()
   :outertype: AgGrid

   Show all columns, by opening the popup menu of the first column. Assumes that the first column has the popup menu.

showAllColumnsUsingMenuOfColumn
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void showAllColumnsUsingMenuOfColumn(String headerText)
   :outertype: AgGrid

   * Show all columns by using the popup menu of the given header.

   :param headerText: - the header text, or the column ID, to open the popup menu from. A string wrapped with curly braces is interpreted as the column ID.

showSpecificColumnsUsingMenuOfColumn
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void showSpecificColumnsUsingMenuOfColumn(String headerText, List<String> columns)
   :outertype: AgGrid

   Show only specific columns, by opening the popup menu of the given column

   :param headerText: - the header text, or the column ID, to open the popup menu from. A string wrapped with curly braces is interpreted as the column ID.
   :param columns: - the columns to show

showSpecificColumnsUsingMenuOfColumn
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void showSpecificColumnsUsingMenuOfColumn(List<String> columns)
   :outertype: AgGrid

   Show only specific columns, by opening the popup menu of the first column. Assumes that the first column has the popup menu.

   :param columns: - the columns to show

sortBy
^^^^^^

.. java:method:: public void sortBy(String headerText, SortDirection direction) throws OperationFailedException
   :outertype: AgGrid

   Click on 'sort' so that the given column is sorted in the direction provided.

   :param headerText: - the header text, or the column ID. A string wrapped with curly braces is interpreted as the column ID.
   :param direction: - wanted direction. either descending or ascending.
   :throws OperationFailedException: operation failed - typically the configuration of the grid does not allow to sort as wanted.

toString
^^^^^^^^

.. java:method:: @Override public String toString()
   :outertype: AgGrid

