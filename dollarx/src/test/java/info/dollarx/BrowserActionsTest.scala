package info.dollarx

import java.util
import java.util.Collections

import scala.collection.JavaConverters._

import org.junit.{Test, Before}
import org.mockito.Mockito._
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers._

import org.openqa.selenium._
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.interactions.internal.Coordinates
import org.openqa.selenium.interactions.{HasInputDevices, Mouse, Keyboard}
import org.openqa.selenium.remote.RemoteWebElement

import Path._

class BrowserActionsTest extends Browser{
  var driverMock: WebDriver = null
  var keyboardMock: Keyboard = null
  var mouseMock: Mouse = null
  var webElement: RemoteWebElement = null
  var webElement2: RemoteWebElement = null
  var coordinates: Coordinates = null
  var coordinates2: Coordinates = null
  protected var driver: WebDriver = _

  @Before def setup {
    driverMock = mock(classOf[ChromeDriver])
    mouseMock = mock(classOf[Mouse])
    keyboardMock = mock(classOf[Keyboard])
    webElement = mock(classOf[RemoteWebElement])
    webElement2 = mock(classOf[RemoteWebElement])
    coordinates = mock(classOf[Coordinates])
    coordinates2 = mock(classOf[Coordinates])
    when((driverMock.asInstanceOf[HasInputDevices]).getMouse).thenReturn(mouseMock)
    when((driverMock.asInstanceOf[HasInputDevices]).getKeyboard).thenReturn(keyboardMock)
    when(webElement.getCoordinates).thenReturn(coordinates)
    when(webElement2.getCoordinates).thenReturn(coordinates2)
    when(driverMock.findElement(By.xpath("//" + div.getXPath.get))).thenReturn(webElement)
    when(driverMock.findElement(By.xpath("//" + span.getXPath.get))).thenReturn(webElement2)
    driver = driverMock
  }

  @Test def clickElement {
    click at div
    verify(mouseMock).mouseMove(coordinates)
    verify(mouseMock).click(null)
  }

  @Test def clickOn {
    click on div
    verify(webElement).click
  }

  @Test def doubleclickElement {
    doubleClick on div
    verify(mouseMock).mouseMove(coordinates)
    verify(mouseMock).doubleClick(coordinates)
  }

  @Test def sendKeysToElement {
    sendKeys("x", "yz") to div
    verify(mouseMock).click(coordinates)
    verify(keyboardMock).sendKeys("x", "yz")
  }

  @Test def sendKeysToBrowser {
    sendKeys("x", "yz") toBrowser()
    verify(keyboardMock).sendKeys("x", "yz")
  }

  @Test def DragAndDropToElement {
    dragAndDrop(div) to span
    verify(mouseMock).mouseMove(coordinates)
    verify(mouseMock).mouseDown(coordinates)
    verify(mouseMock, atLeastOnce).mouseMove(coordinates2)
    verify(mouseMock).mouseUp(coordinates2)
  }

  @Test def DragAndDropToOffset {
    dragAndDrop(div) to (10, 10)
    verify(mouseMock).mouseMove(coordinates)
    verify(mouseMock).mouseDown(coordinates)
    verify(mouseMock).mouseMove(null, 10, 10)
    verify(mouseMock).mouseUp(null)
  }

  @Test def findTest {
    val el = find(div)
    assertThat(el, equalTo(webElement.asInstanceOf[WebElement]))
  }

  @Test def findAllTest {
    val result: java.util.List[WebElement] = Collections.singletonList(webElement)
    when(driverMock.findElements(By.xpath("//" + div.getXPath.get))).thenReturn(result)
    assertThat(findAll(div), is(equalTo(result.asScala.toList)))
  }

  @Test def scrollTest {
    scroll to div
    verify(mouseMock).mouseMove(coordinates)
  }

  @Test def hoverTest {
    hover over div
    verify(mouseMock).mouseMove(coordinates)
  }

  @Test def pressKeyDownInBrowser {
    pressKeyDown(Keys.SHIFT) inBrowser()
    sendKeys("x") toBrowser()
    verify(keyboardMock).pressKey(Keys.SHIFT)
    verify(keyboardMock).sendKeys("x")
  }

  @Test def releaseKey {
    releaseKey(Keys.SHIFT) inBrowser()
    verify(keyboardMock).releaseKey(Keys.SHIFT)
  }

  @Test def pressKeyDownOnElement {
    pressKeyDown(Keys.SHIFT).on(div)
    sendKeys("x") toBrowser()
    verify(keyboardMock).pressKey(Keys.SHIFT)
    verify(mouseMock).click(coordinates)
    verify(keyboardMock).sendKeys("x")
  }

  @Test def releaseKeyOnElement {
    releaseKey(Keys.SHIFT) on div
    verify(mouseMock).click(coordinates)
    verify(keyboardMock).releaseKey(Keys.SHIFT)
  }

  @Test def numberOfAppearances0 {
    when(driverMock.findElement(By.xpath("//" + listItem.getXPath.get))).thenThrow(new NoSuchElementException(""))
    assertThat(numberOfAppearances(listItem), equalTo(0))
  }

  @Test def numberOfAppearances2 {
    val result: java.util.List[WebElement] = util.Arrays.asList(webElement, webElement)
    when(driverMock.findElements(By.xpath("//" + listItem.getXPath.get))).thenReturn(result)
    assertThat(numberOfAppearances(listItem), is(2))
  }

  @Test def isPresentFalse {
    when(driverMock.findElement(By.xpath("//" + listItem.getXPath.get))).thenThrow(new NoSuchElementException(""))
    assertThat(isPresent(listItem), is(false))
  }

  @Test def isPresentTrue {
    assertThat(isPresent(div), is(true))
  }

  @Test def isDisplayedYes {
    when(webElement.isDisplayed).thenReturn(true)
    assertThat(isDisplayed(div), is(true))
  }

  @Test def isDisplayedNo {
    when(webElement.isDisplayed).thenReturn(false)
    assertThat(isDisplayed(div), is(false))
  }

  @Test def isDisplayedDoesNotExist {
    when(driverMock.findElement(By.xpath("//" + listItem.getXPath.get))).thenThrow(new NoSuchElementException(""))
    assertThat(isDisplayed(listItem), is(false))
  }

  @Test def isSelectedYes {
    when(webElement.isSelected).thenReturn(true)
    assertThat(isSelected(div), is(true))
  }

  @Test def isSelectedNo {
    when(webElement.isSelected).thenReturn(false)
    assertThat(isSelected(div), is(false))
  }

  @Test def isSelectedDoesNotExist {
    when(driverMock.findElement(By.xpath("//" + listItem.getXPath.get))).thenThrow(new NoSuchElementException(""))
    assertThat(isSelected(listItem), is(false))
  }

  @Test def isEnabledYes {
    when(webElement.isEnabled).thenReturn(true)
    assertThat(isEnabled(div), is(true))
  }

  @Test def isEnabledNo {
    when(webElement.isEnabled).thenReturn(false)
    assertThat(isEnabled(div), is(false))
  }

  @Test def isEnableDoesNotExist {
    when(driverMock.findElement(By.xpath("//" + listItem.getXPath.get))).thenThrow(new NoSuchElementException(""))
    assertThat(isEnabled(listItem), is(false))
  }
}
