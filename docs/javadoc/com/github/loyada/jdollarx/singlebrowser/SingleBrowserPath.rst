.. java:import:: com.github.loyada.jdollarx BasicPath

.. java:import:: com.github.loyada.jdollarx ElementProperty

.. java:import:: com.github.loyada.jdollarx Operations

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: org.openqa.selenium WebElement

.. java:import:: java.util List

.. java:import:: java.util Optional

SingleBrowserPath
=================

.. java:package:: com.github.loyada.jdollarx.singlebrowser
   :noindex:

.. java:type:: public final class SingleBrowserPath implements Path

   An implementation of \ :java:ref:`Path`\  that is tailored to a singleton browser, thus allows some additional API's for actions (for those who favor object-oriented API style)

Fields
------
anchor
^^^^^^

.. java:field:: public static final SingleBrowserPath anchor
   :outertype: SingleBrowserPath

body
^^^^

.. java:field:: public static final SingleBrowserPath body
   :outertype: SingleBrowserPath

button
^^^^^^

.. java:field:: public static final SingleBrowserPath button
   :outertype: SingleBrowserPath

div
^^^

.. java:field:: public static final SingleBrowserPath div
   :outertype: SingleBrowserPath

element
^^^^^^^

.. java:field:: public static final SingleBrowserPath element
   :outertype: SingleBrowserPath

form
^^^^

.. java:field:: public static final SingleBrowserPath form
   :outertype: SingleBrowserPath

header
^^^^^^

.. java:field:: public static final SingleBrowserPath header
   :outertype: SingleBrowserPath

header1
^^^^^^^

.. java:field:: public static final SingleBrowserPath header1
   :outertype: SingleBrowserPath

header2
^^^^^^^

.. java:field:: public static final SingleBrowserPath header2
   :outertype: SingleBrowserPath

header3
^^^^^^^

.. java:field:: public static final SingleBrowserPath header3
   :outertype: SingleBrowserPath

header4
^^^^^^^

.. java:field:: public static final SingleBrowserPath header4
   :outertype: SingleBrowserPath

header5
^^^^^^^

.. java:field:: public static final SingleBrowserPath header5
   :outertype: SingleBrowserPath

header6
^^^^^^^

.. java:field:: public static final SingleBrowserPath header6
   :outertype: SingleBrowserPath

html
^^^^

.. java:field:: public static final SingleBrowserPath html
   :outertype: SingleBrowserPath

input
^^^^^

.. java:field:: public static final SingleBrowserPath input
   :outertype: SingleBrowserPath

listItem
^^^^^^^^

.. java:field:: public static final SingleBrowserPath listItem
   :outertype: SingleBrowserPath

section
^^^^^^^

.. java:field:: public static final SingleBrowserPath section
   :outertype: SingleBrowserPath

span
^^^^

.. java:field:: public static final SingleBrowserPath span
   :outertype: SingleBrowserPath

svg
^^^

.. java:field:: public static final SingleBrowserPath svg
   :outertype: SingleBrowserPath

unorderedList
^^^^^^^^^^^^^

.. java:field:: public static final SingleBrowserPath unorderedList
   :outertype: SingleBrowserPath

Constructors
------------
SingleBrowserPath
^^^^^^^^^^^^^^^^^

.. java:constructor:: public SingleBrowserPath(BasicPath path)
   :outertype: SingleBrowserPath

Methods
-------
after
^^^^^

.. java:method:: @Override public Path after(Path another)
   :outertype: SingleBrowserPath

afterSibling
^^^^^^^^^^^^

.. java:method:: @Override public Path afterSibling(Path another)
   :outertype: SingleBrowserPath

ancestorOf
^^^^^^^^^^

.. java:method:: @Override public Path ancestorOf(Path another)
   :outertype: SingleBrowserPath

and
^^^

.. java:method:: @Override public Path and(ElementProperty... prop)
   :outertype: SingleBrowserPath

before
^^^^^^

.. java:method:: @Override public Path before(Path another)
   :outertype: SingleBrowserPath

beforeSibling
^^^^^^^^^^^^^

.. java:method:: @Override public Path beforeSibling(Path another)
   :outertype: SingleBrowserPath

childOf
^^^^^^^

.. java:method:: @Override public Path childOf(Path another)
   :outertype: SingleBrowserPath

click
^^^^^

.. java:method:: public void click()
   :outertype: SingleBrowserPath

   click at the location of this element

containing
^^^^^^^^^^

.. java:method:: @Override public Path containing(Path another)
   :outertype: SingleBrowserPath

contains
^^^^^^^^

.. java:method:: @Override public Path contains(Path another)
   :outertype: SingleBrowserPath

descendantOf
^^^^^^^^^^^^

.. java:method:: @Override public Path descendantOf(Path another)
   :outertype: SingleBrowserPath

describedBy
^^^^^^^^^^^

.. java:method:: @Override public Path describedBy(String description)
   :outertype: SingleBrowserPath

doubleClick
^^^^^^^^^^^

.. java:method:: public void doubleClick()
   :outertype: SingleBrowserPath

   doubleclick at the location of this element

dragAndDrop
^^^^^^^^^^^

.. java:method:: public Operations.DragAndDrop dragAndDrop()
   :outertype: SingleBrowserPath

   drag and drop this element, to another element or another location. Examples:

   .. parsed-literal::

      element.dragAndDrop().to(anotherElement);
         element.dragAndDrop().to(50, 50);

   :return: DragAndDrop instance. See examples for usage.

find
^^^^

.. java:method:: public WebElement find()
   :outertype: SingleBrowserPath

   Find the (first) element in the browser for this path

   :return: the WebElement

findAll
^^^^^^^

.. java:method:: public List<WebElement> findAll()
   :outertype: SingleBrowserPath

   Find all elements in the browser with this path

   :return: a list of all WebElements with this path

getAlternateXPath
^^^^^^^^^^^^^^^^^

.. java:method:: @Override public Optional<String> getAlternateXPath()
   :outertype: SingleBrowserPath

getDescribedBy
^^^^^^^^^^^^^^

.. java:method:: @Override public Optional<String> getDescribedBy()
   :outertype: SingleBrowserPath

getElementProperties
^^^^^^^^^^^^^^^^^^^^

.. java:method:: @Override public List<ElementProperty> getElementProperties()
   :outertype: SingleBrowserPath

getUnderlyingSource
^^^^^^^^^^^^^^^^^^^

.. java:method:: @Override public Optional<WebElement> getUnderlyingSource()
   :outertype: SingleBrowserPath

getXPath
^^^^^^^^

.. java:method:: @Override public Optional<String> getXPath()
   :outertype: SingleBrowserPath

getXpathExplanation
^^^^^^^^^^^^^^^^^^^

.. java:method:: @Override public Optional<String> getXpathExplanation()
   :outertype: SingleBrowserPath

hover
^^^^^

.. java:method:: public void hover()
   :outertype: SingleBrowserPath

   hover over the element with this path in the browser

immediatelyAfterSibling
^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: @Override public Path immediatelyAfterSibling(Path another)
   :outertype: SingleBrowserPath

immediatelyBeforeSibling
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: @Override public Path immediatelyBeforeSibling(Path another)
   :outertype: SingleBrowserPath

inside
^^^^^^

.. java:method:: @Override public Path inside(Path another)
   :outertype: SingleBrowserPath

insideTopLevel
^^^^^^^^^^^^^^

.. java:method:: @Override public Path insideTopLevel()
   :outertype: SingleBrowserPath

or
^^

.. java:method:: @Override public Path or(Path another)
   :outertype: SingleBrowserPath

parentOf
^^^^^^^^

.. java:method:: @Override public Path parentOf(Path another)
   :outertype: SingleBrowserPath

rightClick
^^^^^^^^^^

.. java:method:: public void rightClick()
   :outertype: SingleBrowserPath

   right click at the location of this element

scrollTo
^^^^^^^^

.. java:method:: public WebElement scrollTo()
   :outertype: SingleBrowserPath

   scroll the browser until this element is visible

   :return: the WebElement that was scrolled to

sendKeys
^^^^^^^^

.. java:method:: public void sendKeys(CharSequence... charsToSend) throws Operations.OperationFailedException
   :outertype: SingleBrowserPath

   send keys to element

   :param charsToSend: the keys to send. Examples:

   .. parsed-literal::

      input.sendKeys("abc"); input.sendKeys("a", "bc");
   :throws Operations.OperationFailedException: - operation failed. Includes root cause.

that
^^^^

.. java:method:: @Override public Path that(ElementProperty... prop)
   :outertype: SingleBrowserPath

toString
^^^^^^^^

.. java:method:: @Override public String toString()
   :outertype: SingleBrowserPath

withClass
^^^^^^^^^

.. java:method:: @Override public Path withClass(String cssClass)
   :outertype: SingleBrowserPath

withClasses
^^^^^^^^^^^

.. java:method:: @Override public Path withClasses(String... cssClasses)
   :outertype: SingleBrowserPath

withGlobalIndex
^^^^^^^^^^^^^^^

.. java:method:: @Override public Path withGlobalIndex(Integer index)
   :outertype: SingleBrowserPath

withText
^^^^^^^^

.. java:method:: @Override public Path withText(String txt)
   :outertype: SingleBrowserPath

withTextContaining
^^^^^^^^^^^^^^^^^^

.. java:method:: @Override public Path withTextContaining(String txt)
   :outertype: SingleBrowserPath

