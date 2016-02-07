package com.github.loyada.jdollarx;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class BrowserActionsTest {
    WebDriver driverMock;
    Keyboard keyboardMock;
    Mouse mouseMock;
    RemoteWebElement webElement, webElement2;
    Coordinates coordinates, coordinates2;
    InBrowser browser;


    @Before
    public void setup() {
        driverMock = mock(ChromeDriver.class);
        mouseMock = mock(Mouse.class);
        keyboardMock = mock(Keyboard.class);
        webElement = mock(RemoteWebElement.class);
        webElement2 = mock(RemoteWebElement.class);
        coordinates = mock(Coordinates.class);
        coordinates2 = mock(Coordinates.class);
        when(((HasInputDevices) driverMock).getMouse()).thenReturn(mouseMock);
        when(((HasInputDevices) driverMock).getKeyboard()).thenReturn(keyboardMock);
        when(webElement.getCoordinates()).thenReturn(coordinates);
        when(webElement2.getCoordinates()).thenReturn(coordinates2);
        when(driverMock.findElement(By.xpath("//" + BasicPath.div.getXPath().get()))).thenReturn(webElement);
        when(driverMock.findElement(By.xpath("//" + BasicPath.span.getXPath().get()))).thenReturn(webElement2);
        browser = new InBrowser(driverMock);
    }

    @Test
    public void scrollToElementTest() {
       browser.scroll().to(BasicPath.div);
        verify(mouseMock).mouseMove(coordinates);
    }

    @Test
    public void scrollDirection() {
        browser.scroll().down(100);
        verify(((JavascriptExecutor) driverMock)).executeScript("scroll(0,100)");
        browser.scroll().up(100);
        verify(((JavascriptExecutor) driverMock)).executeScript("scroll(0,-100)");
        browser.scroll().left(100);
        verify(((JavascriptExecutor) driverMock)).executeScript("scroll(-100,0)");
        browser.scroll().right(100);
        verify(((JavascriptExecutor) driverMock)).executeScript("scroll(100,0)");
    }

    @Test
    public void clickElement() {
        browser.clickAt(BasicPath.div);
        verify(mouseMock).mouseMove(coordinates);
        verify(mouseMock).click(null);
    }

    @Test
    public void clickOn() {
        browser.clickOn(BasicPath.div);
        verify(webElement).click();
    }

    @Test
    public void doubleclickElement() {
        browser.doubleClickOn(BasicPath.div);
        verify(mouseMock).mouseMove(coordinates);
        verify(mouseMock).doubleClick(coordinates);
    }

    @Test
    public void sendKeysToElement() throws Operations.OperationFailedException {
        browser.sendKeys("x", "yz").to(BasicPath.div);
        verify(mouseMock).click(coordinates);
        verify(keyboardMock).sendKeys("x", "yz");
    }

    @Test
    public void sendKeysToBrowser() {
        browser.sendKeys("x", "yz").toBrowser();
        verify(keyboardMock).sendKeys("x", "yz");
    }

    @Test
    public void DragAndDropToElement() throws Operations.OperationFailedException {
        browser.dragAndDrop(BasicPath.div).to(BasicPath.span);
        verify(mouseMock).mouseMove(coordinates);
        verify(mouseMock).mouseDown(coordinates);
        verify(mouseMock, atLeastOnce()).mouseMove(coordinates2);
        verify(mouseMock).mouseUp(coordinates2);
    }

    @Test
    public void DragAndDropToOffset() throws Operations.OperationFailedException {
        browser.dragAndDrop(BasicPath.div).to(10, 10);
        verify(mouseMock).mouseMove(coordinates);
        verify(mouseMock).mouseDown(coordinates);
        verify(mouseMock).mouseMove(null, 10, 10);
        verify(mouseMock).mouseUp(null);
    }

    @Test
    public void find() {
        assertThat(browser.find(BasicPath.div), is(webElement));
    }

    @Test
    public void findInsideElement() {
        WebElement el = browser.find(BasicPath.div);
        Path newPath = BasicPath.builder().withUnderlying(el).withXpath("span").build();
        assertThat(newPath.toString(), org.hamcrest.Matchers.startsWith("under reference element Mock for RemoteWebElement"));
        assertThat(newPath.toString(), org.hamcrest.Matchers.endsWith("xpath: \"span\""));
        RemoteWebElement el2 = mock(RemoteWebElement.class);
        when(webElement.findElement(By.xpath("span"))).thenReturn(el2);
        assertThat(browser.find(newPath), is(el2));
    }

    @Test
    public void findAll() {
        List<WebElement> result = Collections.singletonList(webElement);
        when(driverMock.findElements(By.xpath("//" + BasicPath.div.getXPath().get()))).thenReturn(result);

        assertThat(browser.findAll(BasicPath.div), is(equalTo(result)));
    }

    @Test
    public void scroll() {
        browser.scrollTo(BasicPath.div);
        verify(mouseMock).mouseMove(coordinates);
    }

    @Test
    public void hover() {
        browser.hoverOver(BasicPath.div);
        verify(mouseMock).mouseMove(coordinates);
    }

    @Test
    public void pressKeyDownInBrowser() {
        browser.pressKeyDown(Keys.SHIFT).inBrowser();
        browser.sendKeys("x").toBrowser();
        verify(keyboardMock).pressKey(Keys.SHIFT);
        verify(keyboardMock).sendKeys("x");
    }

    @Test
    public void releaseKey() {
        browser.releaseKey(Keys.SHIFT).inBrowser();
        verify(keyboardMock).releaseKey(Keys.SHIFT);
    }

    @Test
    public void pressKeyDownOnElement() throws Operations.OperationFailedException {
        browser.pressKeyDown(Keys.SHIFT).on(BasicPath.div);
        browser.sendKeys("x").toBrowser();
        verify(keyboardMock).pressKey(Keys.SHIFT);
        verify(mouseMock).click(coordinates);
        verify(keyboardMock).sendKeys("x");
    }

    @Test
    public void releaseKeyOnElement() throws Operations.OperationFailedException {
        browser.releaseKey(Keys.SHIFT).on(BasicPath.div);
        verify(mouseMock).click(coordinates);
        verify(keyboardMock).releaseKey(Keys.SHIFT);
    }

    @Test
    public void numberOfAppearances0() {
        when(driverMock.findElement(By.xpath("//" + BasicPath.listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.numberOfAppearances(BasicPath.listItem), is(0));
    }

    @Test
    public void numberOfAppearances2() {
        List<WebElement> result = Arrays.asList(webElement, webElement);
        when(driverMock.findElements(By.xpath("//" + BasicPath.listItem.getXPath().get()))).thenReturn(result);
        assertThat( browser.numberOfAppearances(BasicPath.listItem), is(2));
    }

    @Test
    public void isPresentFalse() {
        when(driverMock.findElement(By.xpath("//" + BasicPath.listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.isPresent(BasicPath.listItem), is(false));
    }

    @Test
    public void isPresentTrue() {
        assertThat( browser.isPresent(BasicPath.div), is(true));
    }

    @Test
    public void isDisplayedYes() {
        when(webElement.isDisplayed()).thenReturn(true);
        assertThat( browser.isDisplayed(BasicPath.div), is(true));
    }

    @Test
    public void isDisplayedNo() {
        when(webElement.isDisplayed()).thenReturn(false);
        assertThat( browser.isDisplayed(BasicPath.div), is(false));
    }

    @Test
    public void isDisplayedDoesNotExist() {
        when(driverMock.findElement(By.xpath("//" + BasicPath.listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.isDisplayed(BasicPath.listItem), is(false));
    }

    @Test
    public void isSelectedYes() {
        when(webElement.isSelected()).thenReturn(true);
        assertThat( browser.isSelected(BasicPath.div), is(true));
    }

    @Test
    public void isSelectedNo() {
        when(webElement.isSelected()).thenReturn(false);
        assertThat( browser.isSelected(BasicPath.div), is(false));
    }

    @Test
    public void isSelectedDoesNotExist() {
        when(driverMock.findElement(By.xpath("//" + BasicPath.listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.isSelected(BasicPath.listItem), is(false));
    }

    @Test
    public void isEnabledYes() {
        when(webElement.isEnabled()).thenReturn(true);
        assertThat( browser.isEnabled(BasicPath.div), is(true));
    }

    @Test
    public void isEnabledNo() {
        when(webElement.isEnabled()).thenReturn(false);
        assertThat( browser.isEnabled(BasicPath.div), is(false));
    }

    @Test
    public void isEnableDoesNotExist() {
        when(driverMock.findElement(By.xpath("//" + BasicPath.listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.isEnabled(BasicPath.listItem), is(false));
    }

}
