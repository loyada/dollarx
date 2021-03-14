.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: java.util List

AgGridHighLevelOperations
=========================

.. java:package:: com.github.loyada.jdollarx.singlebrowser
   :noindex:

.. java:type:: public final class AgGridHighLevelOperations

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

ensureCellValueIsPresent
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void ensureCellValueIsPresent(int rowNumber, String columnTitle, String expectedValue)
   :outertype: AgGridHighLevelOperations

hoverOverCell
^^^^^^^^^^^^^

.. java:method:: public Path hoverOverCell(int rowNumber, String columnTitle)
   :outertype: AgGridHighLevelOperations

