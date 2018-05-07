.. java:import:: java.util ArrayList

.. java:import:: java.util Arrays

.. java:import:: java.util List

.. java:import:: java.util.function BiFunction

.. java:import:: java.util.function BinaryOperator

.. java:import:: java.util.stream Collectors

.. java:import:: java.util.stream Stream

XpathUtils
==========

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public final class XpathUtils

   Internal implementation.

Fields
------
hasSomeText
^^^^^^^^^^^

.. java:field:: public static final String hasSomeText
   :outertype: XpathUtils

isHidden
^^^^^^^^

.. java:field:: public static final String isHidden
   :outertype: XpathUtils

Methods
-------
aggregatedTextContains
^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static String aggregatedTextContains(String text)
   :outertype: XpathUtils

aggregatedTextEndsWith
^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static String aggregatedTextEndsWith(String text)
   :outertype: XpathUtils

aggregatedTextEquals
^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static String aggregatedTextEquals(String text)
   :outertype: XpathUtils

aggregatedTextStartsWith
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static String aggregatedTextStartsWith(String text)
   :outertype: XpathUtils

doesNotExist
^^^^^^^^^^^^

.. java:method:: public static String doesNotExist(String path)
   :outertype: XpathUtils

doesNotExistInEntirePage
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static String doesNotExistInEntirePage(String path)
   :outertype: XpathUtils

hasAnyOfClasses
^^^^^^^^^^^^^^^

.. java:method:: public static String hasAnyOfClasses(String... classNames)
   :outertype: XpathUtils

hasAttribute
^^^^^^^^^^^^

.. java:method:: public static String hasAttribute(String attribute, String value)
   :outertype: XpathUtils

hasClass
^^^^^^^^

.. java:method:: public static String hasClass(String className)
   :outertype: XpathUtils

hasClassContaining
^^^^^^^^^^^^^^^^^^

.. java:method:: public static String hasClassContaining(String className)
   :outertype: XpathUtils

hasClasses
^^^^^^^^^^

.. java:method:: public static String hasClasses(String... classNames)
   :outertype: XpathUtils

hasId
^^^^^

.. java:method:: public static String hasId(String id)
   :outertype: XpathUtils

insideTopLevel
^^^^^^^^^^^^^^

.. java:method:: public static String insideTopLevel(String xpath)
   :outertype: XpathUtils

nOccurances
^^^^^^^^^^^

.. java:method:: public static String nOccurances(String xpath, int numberOfOccurrences, RelationOperator relationOperator)
   :outertype: XpathUtils

processTextForXpath
^^^^^^^^^^^^^^^^^^^

.. java:method:: public static String processTextForXpath(String txt)
   :outertype: XpathUtils

textContains
^^^^^^^^^^^^

.. java:method:: public static String textContains(String text)
   :outertype: XpathUtils

textEndsWith
^^^^^^^^^^^^

.. java:method:: public static String textEndsWith(String text)
   :outertype: XpathUtils

textEquals
^^^^^^^^^^

.. java:method:: public static String textEquals(String text)
   :outertype: XpathUtils

textStartsWith
^^^^^^^^^^^^^^

.. java:method:: public static String textStartsWith(String text)
   :outertype: XpathUtils

translateTextForPath
^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static String translateTextForPath(String txt)
   :outertype: XpathUtils

