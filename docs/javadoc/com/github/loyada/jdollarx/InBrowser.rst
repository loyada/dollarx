.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.openqa.selenium WebDriver

.. java:import:: org.openqa.selenium WebElement

.. java:import:: org.openqa.selenium.interactions Actions

.. java:import:: org.openqa.selenium.support.ui ExpectedConditions

.. java:import:: org.openqa.selenium.support.ui FluentWait

.. java:import:: org.openqa.selenium.support.ui Wait

.. java:import:: java.util List

.. java:import:: java.util.concurrent TimeUnit

.. java:import:: java.util.function UnaryOperator

InBrowser
=========

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public class InBrowser

   A wrapper around Selenium WebDriver, used for interaction with the browser. In case only a single instance of the browser is used, \ :java:ref:`com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton`\  offers a simpler API.

Constructors
------------
InBrowser
^^^^^^^^^

.. java:constructor:: public InBrowser(WebDriver driver)
   :outertype: InBrowser

   Creates a connection to a browser, using the given driver

   :param driver: a WebDriver instance

Methods
-------
clickAt
^^^^^^^

.. java:method:: public WebElement clickAt(Path el)
   :outertype: InBrowser

   Click at the location the first element that fits the given path. Does not require a clickable element.

   :param el: the element
   :return: the clicked on WebElement

clickOn
^^^^^^^

.. java:method:: public WebElement clickOn(Path el)
   :outertype: InBrowser

   Click on the first element that fits the given path. Only works for clickable elements. If the element is currently not clickable, will wait up to a second for it to be clickable.

   :param el: the element
   :return: the clicked on WebElement

doubleClickOn
^^^^^^^^^^^^^

.. java:method:: public void doubleClickOn(Path el)
   :outertype: InBrowser

   Doubleclick the location of the first element that fits the given path.

   :param el: the element

dragAndDrop
^^^^^^^^^^^

.. java:method:: public Operations.DragAndDrop dragAndDrop(Path path)
   :outertype: InBrowser

   Drag and drop in the browser. Several flavors of use: browser.dragAndDrop(source).to(target); browser.dragAndDrop(source).to(xCor, yCor);

   :param path: the source element
   :return: a DragAndDrop instance, that allows to drag and drop to a location or to another DOM element

find
^^^^

.. java:method:: public WebElement find(Path el)
   :outertype: InBrowser

   Finds an element in the browser, based on the xpath representing el. It is similar to WebDriver.findElement(), If el also has a WebElement (ie: getUnderlyingSource() is not empty), then it looks inside that WebElement. This is useful also to integrate with existing WebDriver code.

   :param el: - the path to find
   :return: - A WebElement instance from selenium, or throws NoSuchElementException exception

findAll
^^^^^^^

.. java:method:: public List<WebElement> findAll(Path el)
   :outertype: InBrowser

   Finds all elements in the browser, based on the xpath representing el. It is similar to WebDriver.findElements(), If el also has a WebElement (ie: getUnderlyingSource() is not empty), then it looks inside that WebElement. This is useful also to integrate with existing WebDriver code.

   :param el: - the path to find
   :return: - A list of WebElement from selenium, or throws NoSuchElementException exception

findPageWithNumberOfOccurrences
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public WebElement findPageWithNumberOfOccurrences(Path el, int numberOfOccurrences, RelationOperator relationOperator)
   :outertype: InBrowser

   Don't use this directly. There are better ways to do equivalent operation.

   :param el: the path to find
   :param numberOfOccurrences: the base number to find
   :param relationOperator: whether we look for exactly the numberOfOccurrences, at least, or at most occurrences
   :return: the first WebElement found

findPageWithout
^^^^^^^^^^^^^^^

.. java:method:: public WebElement findPageWithout(Path el)
   :outertype: InBrowser

   Finds an page in the browser, that does not contain the given path

   :param el: - the path that must not appear in the page
   :return: returns the page element or raises NoSuchElementException

getDriver
^^^^^^^^^

.. java:method:: public WebDriver getDriver()
   :outertype: InBrowser

   :return: the underlying WebDriver instance

hoverOver
^^^^^^^^^

.. java:method:: public WebElement hoverOver(Path el)
   :outertype: InBrowser

   Hover over the location of the first element that fits the given path

   :param el: the element
   :return: the clicked on WebElement

isDisplayed
^^^^^^^^^^^

.. java:method:: public boolean isDisplayed(Path el)
   :outertype: InBrowser

   is the element present and displayed? Typically you should not use this method directly. Instead, use CustomMatchers. Also, this is limited to checking the inlined css style, so it is quite limited.

   :param el: the element
   :return: true if it is present and selected

isEnabled
^^^^^^^^^

.. java:method:: public boolean isEnabled(Path el)
   :outertype: InBrowser

   is the element present and enabled? Typically you should not use this method directly. Instead, use CustomMatchers.

   :param el: the element
   :return: true if it is present and enabled

isNotPresent
^^^^^^^^^^^^

.. java:method:: public boolean isNotPresent(Path el)
   :outertype: InBrowser

   is the element present? Typically you should not use this method directly. Instead, use CustomMatchers.

   :param el: the path to find
   :return: true if it is not present

isPresent
^^^^^^^^^

.. java:method:: public boolean isPresent(Path el)
   :outertype: InBrowser

   is the element present? Typically you should not use this method directly. Instead, use CustomMatchers.

   :param el: the path to find
   :return: true if the element is present

isSelected
^^^^^^^^^^

.. java:method:: public boolean isSelected(Path el)
   :outertype: InBrowser

   is the element present and selected? Typically you should not use this method directly. Instead, use CustomMatchers.

   :param el: the element
   :return: true if it is present and selected

numberOfAppearances
^^^^^^^^^^^^^^^^^^^

.. java:method:: public Integer numberOfAppearances(Path el)
   :outertype: InBrowser

   Returns the number of elements in the browser that match the given path. Typically you should not use this method directly. Instead, use CustomMatchers.

   :param el: the element to find
   :return: the number of elements in the browser that match the given path

pressKeyDown
^^^^^^^^^^^^

.. java:method:: public Operations.KeysDown pressKeyDown(CharSequence thekey)
   :outertype: InBrowser

   Press key down in the browser, or on a specific element. Two flavors of use: browser.pressKeyDown(Keys.TAB).inBrowser(); browser.pressKeyDown(Keys.TAB).on(path);

   :param thekey: a key to press
   :return: returns a KeysDown instance that allows to press a key on the browser in general or on a specific DOM element

releaseKey
^^^^^^^^^^

.. java:method:: public Operations.ReleaseKey releaseKey(CharSequence thekey)
   :outertype: InBrowser

   Release key down in the browser, or on a specific element. Two flavors of use:

   .. parsed-literal::

      browser.releaseKey(Keys.TAB).inBrowser();
         browser.releaseKey(Keys.TAB).on(path);

   :param thekey: a key to release
   :return: returns a ReleaseKey instance that allows to release on the browser in general or on a specific DOM element

scroll
^^^^^^

.. java:method:: public Operations.Scroll scroll()
   :outertype: InBrowser

   scroll the browser. Several flavors of use:

   .. parsed-literal::

      browser.scroll().to(path);
         browser.scroll().left(50);
         browser.scroll().right(50);
         browser.scroll().up(50);
         browser.scroll().down(50);

   :return: a Scroll instance that allows to scroll by offset or to a location of a DOM element

scrollElement
^^^^^^^^^^^^^

.. java:method:: public Operations.ScrollElement scrollElement(Path wrapper)
   :outertype: InBrowser

scrollElementWithStepOverride
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public Operations.ScrollElement scrollElementWithStepOverride(Path wrapper, int step)
   :outertype: InBrowser

scrollTo
^^^^^^^^

.. java:method:: public WebElement scrollTo(Path el)
   :outertype: InBrowser

   Scroll to the location of the first element that fits the given path

   :param el: the element
   :return: the clicked on WebElement

sendKeys
^^^^^^^^

.. java:method:: public Operations.KeysSender sendKeys(CharSequence... charsToSend)
   :outertype: InBrowser

   send keys to the browser, or to a specific element. Two flavors of use: browser.sendKeys("abc").toBrowser(); browser.sendKeys("abc").to(path);

   :param charsToSend: The characters to send. Can be "abc" or "a", "b", "c"
   :return: a KeySender instance that allows to send keys to the browser in general, or to a specific DOM element

