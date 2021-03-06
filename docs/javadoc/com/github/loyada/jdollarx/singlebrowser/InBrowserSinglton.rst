.. java:import:: com.github.loyada.jdollarx InBrowser

.. java:import:: com.github.loyada.jdollarx Operations

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: org.openqa.selenium WebDriver

.. java:import:: org.openqa.selenium WebElement

.. java:import:: org.openqa.selenium.support.ui Select

.. java:import:: java.util List

InBrowserSinglton
=================

.. java:package:: com.github.loyada.jdollarx.singlebrowser
   :noindex:

.. java:type:: public final class InBrowserSinglton

   A simplified API built to interact with a single instance of a running browser. See \ :java:ref:`com.github.loyada.jdollarx.InBrowser`\  for an API that supports multiple browser instances.

Fields
------
driver
^^^^^^

.. java:field:: public static WebDriver driver
   :outertype: InBrowserSinglton

Methods
-------
clickAt
^^^^^^^

.. java:method:: public static WebElement clickAt(Path el)
   :outertype: InBrowserSinglton

   Click on the location of the element that corresponds to the given path.

   :param el: a Path instance
   :return: the WebElement clicked at

clickOn
^^^^^^^

.. java:method:: public static WebElement clickOn(Path el)
   :outertype: InBrowserSinglton

   Click on the element that corresponds to the given path. Requires the element to be clickable.

   :param el: a Path instance
   :return: the WebElement clicked on

contextClick
^^^^^^^^^^^^

.. java:method:: public static WebElement contextClick(Path el)
   :outertype: InBrowserSinglton

   Context click (right click) on the location of the element that corresponds to the given path.

   :param el: a Path instance
   :return: the WebElement clicked at

doubleClickOn
^^^^^^^^^^^^^

.. java:method:: public static void doubleClickOn(Path el)
   :outertype: InBrowserSinglton

   Doubleclick on the element that corresponds to the given path. Requires the element to be clickable.

   :param el: a Path instance

dragAndDrop
^^^^^^^^^^^

.. java:method:: public static Operations.DragAndDrop dragAndDrop(Path path)
   :outertype: InBrowserSinglton

   Drag and drop in the browser. Several flavors of use:

   .. parsed-literal::

      dragAndDrop(source).to(target);
      dragAndDrop(source).to(xCor, yCor);

   :param path: the path of the element to drag and drop
   :return: a DragAndDrop instance that allows to drag and drop to another element or to another location

find
^^^^

.. java:method:: public static WebElement find(Path el)
   :outertype: InBrowserSinglton

   Equivalent to WebDriver.findElement(). If the Path contains a WebElement than it will look for an element inside that WebElement. Otherwise it looks starting at the top level. It also alters the xpath if needed to search from top level correctly.

   :param el: a Path instance
   :return: returns a WebElement or throws an ElementNotFoundException

findAll
^^^^^^^

.. java:method:: public static List<WebElement> findAll(Path el)
   :outertype: InBrowserSinglton

   Equivalent to WebDriver.findElements(). If the Path contains a WebElement than it will look for an element inside that WebElement. Otherwise it looks starting at the top level. It also alters the xpath if needed to search from top level correctly.

   :param el: a Path instance
   :return: a list of WebElements.

getCssClasses
^^^^^^^^^^^^^

.. java:method:: public static List<String> getCssClasses(Path el)
   :outertype: InBrowserSinglton

   Get all classes of given Path element.

   :param el: the element to look for
   :return: a list of classes

getSelect
^^^^^^^^^

.. java:method:: public static Select getSelect(Path el)
   :outertype: InBrowserSinglton

   Get a Selenium select element, which provides a high level API to interacting with a "select" menu. Since the Selenium API is good enough, there was no need to create a specialized dollarx version.

   :param el: - must be a "select" path, with "option" elements for the various selectable options.
   :return: org.openqa.selenium.support.ui.Select instance

hoverOver
^^^^^^^^^

.. java:method:: public static WebElement hoverOver(Path el)
   :outertype: InBrowserSinglton

   Hover over on the location of the element that corresponds to the given path.

   :param el: a Path instance
   :return: the WebElement found

isDisplayed
^^^^^^^^^^^

.. java:method:: public static boolean isDisplayed(Path el)
   :outertype: InBrowserSinglton

   Relies on Selenium WebElement::isDisplayed, thus non-atomic.

   :param el: the path of the element to find
   :return: true if the element is present and displayed

isEnabled
^^^^^^^^^

.. java:method:: public static boolean isEnabled(Path el)
   :outertype: InBrowserSinglton

   Relies on Selenium WebElement::isEnabled, thus non-atomic.

   :param el: the path of the element to find
   :return: true if the element is present and enabled

isPresent
^^^^^^^^^

.. java:method:: public static boolean isPresent(Path el)
   :outertype: InBrowserSinglton

   :param el: a Path instance
   :return: true if the element is present.

isSelected
^^^^^^^^^^

.. java:method:: public static boolean isSelected(Path el)
   :outertype: InBrowserSinglton

   Relies on Selenium WebElement::isSelected, thus non-atomic.

   :param el: the path of the element to find
   :return: true if the element is present and selected

numberOfAppearances
^^^^^^^^^^^^^^^^^^^

.. java:method:: public static Integer numberOfAppearances(Path el)
   :outertype: InBrowserSinglton

   Typically should not be used directly. There are usually better options.

   :param el: a Path instance
   :return: tbe number of appearances of an element.

pressKeyDown
^^^^^^^^^^^^

.. java:method:: public static Operations.KeysDown pressKeyDown(CharSequence thekey)
   :outertype: InBrowserSinglton

   Press key down in the browser, or on a specific element. Two flavors of use:

   .. parsed-literal::

      pressKeyDown(Keys.TAB).inBrowser();
      pressKeyDown(Keys.TAB).on(path);

   :param thekey: the key to press
   :return: a KeysDown instance that allows to send to the browser in general or to a specific element in the DOM. See example.

releaseKey
^^^^^^^^^^

.. java:method:: public static Operations.ReleaseKey releaseKey(CharSequence thekey)
   :outertype: InBrowserSinglton

   Release key in the browser, or on a specific element. Two flavors of use:

   .. parsed-literal::

      releaseKey(Keys.TAB).inBrowser();
      releaseKey(Keys.TAB).on(path);

   :param thekey: the key to release
   :return: a ReleaseKey instance that allows to send to the browser in general or to a specific element in the DOM. See example.

rightClick
^^^^^^^^^^

.. java:method:: public static WebElement rightClick(Path el)
   :outertype: InBrowserSinglton

   Context click (right click) on the location of the element that corresponds to the given path.

   :param el: a Path instance
   :return: the WebElement clicked at

scroll
^^^^^^

.. java:method:: public static Operations.Scroll scroll()
   :outertype: InBrowserSinglton

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

.. java:method:: public static Operations.ScrollElement scrollElement(Path el)
   :outertype: InBrowserSinglton

   scroll within the given element. Useful especially when working with grids.

   :param el: a Path instance
   :return: the WebElement found

scrollElementWithStepOverride
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static Operations.ScrollElement scrollElementWithStepOverride(Path el, int step)
   :outertype: InBrowserSinglton

   scroll within the given element. Useful especially when working with grids.

   :param el: a Path instance
   :param step: step size override
   :return: the WebElement found

scrollTo
^^^^^^^^

.. java:method:: public static WebElement scrollTo(Path el)
   :outertype: InBrowserSinglton

   scroll to the location of the element that corresponds to the given path.

   :param el: a Path instance
   :return: the WebElement found

sendKeys
^^^^^^^^

.. java:method:: public static Operations.KeysSender sendKeys(CharSequence... charsToSend)
   :outertype: InBrowserSinglton

   send keys to the browser, or to a specific element. Two flavors of use:

   .. parsed-literal::

      sendKeys("abc").toBrowser();
           sendKeys("abc").to(path);

   :param charsToSend: the keys to send. Can be "abc", or "a", "b", "c"
   :return: a KeySender instance that allows to send to the browser in general or to a specific element in the DOM

