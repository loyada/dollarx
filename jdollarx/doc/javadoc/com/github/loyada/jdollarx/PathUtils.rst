.. java:import:: java.util AbstractMap.SimpleEntry

.. java:import:: java.util Collections

.. java:import:: java.util Map

.. java:import:: java.util Optional

.. java:import:: java.util.regex Matcher

.. java:import:: java.util.regex Pattern

.. java:import:: java.util.stream Collectors

.. java:import:: java.util.stream Stream

PathUtils
=========

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public final class PathUtils

Methods
-------
hasHeirarchy
^^^^^^^^^^^^

.. java:method:: static boolean hasHeirarchy(String xpath)
   :outertype: PathUtils

oppositeRelation
^^^^^^^^^^^^^^^^

.. java:method:: static String oppositeRelation(String relation)
   :outertype: PathUtils

transformXpathToCorrectAxis
^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: static Optional<String> transformXpathToCorrectAxis(Path sourcePath)
   :outertype: PathUtils

