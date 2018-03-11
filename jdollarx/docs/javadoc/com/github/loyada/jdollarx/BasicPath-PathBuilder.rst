.. java:import:: com.google.common.collect ImmutableList

.. java:import:: org.openqa.selenium WebElement

.. java:import:: java.util Arrays

.. java:import:: java.util Collections

.. java:import:: java.util List

.. java:import:: java.util Optional

.. java:import:: java.util.stream Collectors

.. java:import:: java.util.stream Stream

BasicPath.PathBuilder
=====================

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public static final class PathBuilder
   :outertype: BasicPath

   A builder for BasicPath. Usually \ :java:ref:`customElement(String)`\  is simpler and sufficient.

Constructors
------------
PathBuilder
^^^^^^^^^^^

.. java:constructor:: public PathBuilder()
   :outertype: BasicPath.PathBuilder

PathBuilder
^^^^^^^^^^^

.. java:constructor:: public PathBuilder(Optional<String> insideXpath, Optional<String> xpath, Optional<String> xpathExplanation, Optional<String> describedBy, Optional<WebElement> underlying, List<ElementProperty> elementProperties, Optional<String> alternateXpath)
   :outertype: BasicPath.PathBuilder

Methods
-------
build
^^^^^

.. java:method:: public BasicPath build()
   :outertype: BasicPath.PathBuilder

withAlternateXpath
^^^^^^^^^^^^^^^^^^

.. java:method:: public PathBuilder withAlternateXpath(String alternateXpath)
   :outertype: BasicPath.PathBuilder

withAlternateXpathOptional
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public PathBuilder withAlternateXpathOptional(Optional<String> alternateXpath)
   :outertype: BasicPath.PathBuilder

withDescribedBy
^^^^^^^^^^^^^^^

.. java:method:: public PathBuilder withDescribedBy(String describedBy)
   :outertype: BasicPath.PathBuilder

withDescribedByOptional
^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public PathBuilder withDescribedByOptional(Optional<String> describedBy)
   :outertype: BasicPath.PathBuilder

withElementProperties
^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public PathBuilder withElementProperties(List<ElementProperty> elementProperties)
   :outertype: BasicPath.PathBuilder

withInsideXpath
^^^^^^^^^^^^^^^

.. java:method:: public PathBuilder withInsideXpath(String insideXpath)
   :outertype: BasicPath.PathBuilder

withInsideXpathOptional
^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public PathBuilder withInsideXpathOptional(Optional<String> insideXpath)
   :outertype: BasicPath.PathBuilder

withUnderlying
^^^^^^^^^^^^^^

.. java:method:: public PathBuilder withUnderlying(WebElement underlying)
   :outertype: BasicPath.PathBuilder

withUnderlyingOptional
^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public PathBuilder withUnderlyingOptional(Optional<WebElement> underlying)
   :outertype: BasicPath.PathBuilder

withXpath
^^^^^^^^^

.. java:method:: public PathBuilder withXpath(String xpath)
   :outertype: BasicPath.PathBuilder

withXpathExplanation
^^^^^^^^^^^^^^^^^^^^

.. java:method:: public PathBuilder withXpathExplanation(String xpathExplanation)
   :outertype: BasicPath.PathBuilder

withXpathExplanationOptional
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public PathBuilder withXpathExplanationOptional(Optional<String> xpathExplanation)
   :outertype: BasicPath.PathBuilder

withXpathOptional
^^^^^^^^^^^^^^^^^

.. java:method:: public PathBuilder withXpathOptional(Optional<String> xpath)
   :outertype: BasicPath.PathBuilder

