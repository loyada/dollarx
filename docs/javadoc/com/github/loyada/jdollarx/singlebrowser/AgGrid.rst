.. java:import:: com.github.loyada.jdollarx ElementProperty

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: com.github.loyada.jdollarx.singlebrowser.sizing ElementResizer

.. java:import:: com.google.common.collect ImmutableList

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.openqa.selenium NotFoundException

.. java:import:: org.openqa.selenium WebElement

.. java:import:: java.util.function Function

.. java:import:: java.util.function Predicate

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

   :param headerText: - the header text

clickOnSort
^^^^^^^^^^^

.. java:method:: public void clickOnSort(String headerText)
   :outertype: AgGrid

   Click on the 'sort' column with the given header

   :param headerText: - the header text

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

findTableInBrowser
^^^^^^^^^^^^^^^^^^

.. java:method:: public void findTableInBrowser()
   :outertype: AgGrid

getBuilder
^^^^^^^^^^

.. java:method:: public static AgGridBuilder getBuilder()
   :outertype: AgGrid

getVisibleHeaderPath
^^^^^^^^^^^^^^^^^^^^

.. java:method:: public Path getVisibleHeaderPath(String headerText)
   :outertype: AgGrid

   Make sure the given column header is visible, and returns a Path element to access it. This is useful to perform direct operations on that element or access other DOM elements contained in the header.

   :param headerText: - the header text
   :return: the Path element to access the column header

isVirtualized
^^^^^^^^^^^^^

.. java:method:: public boolean isVirtualized()
   :outertype: AgGrid

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

toString
^^^^^^^^

.. java:method:: @Override public String toString()
   :outertype: AgGrid

