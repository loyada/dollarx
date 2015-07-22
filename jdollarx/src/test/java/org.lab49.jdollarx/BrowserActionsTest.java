package com.lab49.jdollarx;

import org.junit.Before;
import org.junit.Test;
import org.lab49.jdollarx.InBrowser;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.Arrays;
import java.util.List;

import static org.lab49.jdollarx.BasicPath.div;
import static org.lab49.jdollarx.BasicPath.listItem;
import static org.lab49.jdollarx.BasicPath.span;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
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
        when(driverMock.findElement(By.xpath("//" + div.getXPath().get()))).thenReturn(webElement);
        when(driverMock.findElement(By.xpath("//" + span.getXPath().get()))).thenReturn(webElement2);
        browser = new InBrowser(driverMock);
    }

    @Test
    public void clickElement() {
        browser.clickAt(div);
        verify(mouseMock).mouseMove(coordinates);
        verify(mouseMock).click(null);
    }

    @Test
    public void clickOn() {
        browser.clickOn(div);
        verify(webElement).click();
    }

    @Test
    public void doubleclickElement() {
        browser.doubleClickOn(div);
        verify(mouseMock).mouseMove(coordinates);
        verify(mouseMock).doubleClick(coordinates);
    }

    @Test
    public void sendKeysToElement() {
        browser.sendKeys("x", "yz").to(div);
        verify(mouseMock).click(coordinates);
        verify(keyboardMock).sendKeys("x", "yz");
    }

    @Test
    public void sendKeysToBrowser() {
        browser.sendKeys("x", "yz").toBrowser();
        verify(keyboardMock).sendKeys("x", "yz");
    }

    @Test
    public void DragAndDropToElement() {
        browser.dragAndDrop(div).to(span);
        verify(mouseMock).mouseMove(coordinates);
        verify(mouseMock).mouseDown(coordinates);
        verify(mouseMock, atLeastOnce()).mouseMove(coordinates2);
        verify(mouseMock).mouseUp(coordinates2);
    }

    @Test
    public void DragAndDropToOffset() {
        browser.dragAndDrop(div).to(10, 10);
        verify(mouseMock).mouseMove(coordinates);
        verify(mouseMock).mouseDown(coordinates);
        verify(mouseMock).mouseMove(null, 10, 10);
        verify(mouseMock).mouseUp(null);
    }

    @Test
    public void find() {
        assertThat(browser.find(div), is(webElement));
    }

    @Test
    public void findAll() {
        List<WebElement> result =  Arrays.asList(webElement);
        when(driverMock.findElements(By.xpath("//" + div.getXPath().get()))).thenReturn(result);

        assertThat(browser.findAll(div), is(equalTo(result)));
    }

    @Test
    public void scroll() {
        browser.scrollTo(div);
        verify(mouseMock).mouseMove(coordinates);
    }

    @Test
    public void hover() {
        browser.hoverOver(div);
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
    public void pressKeyDownOnElement() {
        browser.pressKeyDown(Keys.SHIFT).on(div);
        browser.sendKeys("x").toBrowser();
        verify(keyboardMock).pressKey(Keys.SHIFT);
        verify(mouseMock).click(coordinates);
        verify(keyboardMock).sendKeys("x");
    }

    @Test
    public void releaseKeyOnElement() {
        browser.releaseKey(Keys.SHIFT).on(div);
        verify(mouseMock).click(coordinates);
        verify(keyboardMock).releaseKey(Keys.SHIFT);
    }

    @Test
    public void numberOfAppearances0() {
        when(driverMock.findElement(By.xpath("//" + listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.numberOfAppearances(listItem), is(0));
    }

    @Test
    public void numberOfAppearances2() {
        List<WebElement> result = Arrays.asList(webElement, webElement);
        when(driverMock.findElements(By.xpath("//" + listItem.getXPath().get()))).thenReturn(result);
        assertThat( browser.numberOfAppearances(listItem), is(2));
    }

    @Test
    public void isPresentFalse() {
        when(driverMock.findElement(By.xpath("//" + listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.isPresent(listItem), is(false));
    }

    @Test
    public void isPresentTrue() {
        assertThat( browser.isPresent(div), is(true));
    }

    @Test
    public void isDisplayedYes() {
        when(webElement.isDisplayed()).thenReturn(true);
        assertThat( browser.isDisplayed(div), is(true));
    }

    @Test
    public void isDisplayedNo() {
        when(webElement.isDisplayed()).thenReturn(false);
        assertThat( browser.isDisplayed(div), is(false));
    }

    @Test
    public void isDisplayedDoesNotExist() {
        when(driverMock.findElement(By.xpath("//" + listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.isDisplayed(listItem), is(false));
    }

    @Test
    public void isSelectedYes() {
        when(webElement.isSelected()).thenReturn(true);
        assertThat( browser.isSelected(div), is(true));
    }

    @Test
    public void isSelectedNo() {
        when(webElement.isSelected()).thenReturn(false);
        assertThat( browser.isSelected(div), is(false));
    }

    @Test
    public void isSelectedDoesNotExist() {
        when(driverMock.findElement(By.xpath("//" + listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.isSelected(listItem), is(false));
    }

    @Test
    public void isEnabledYes() {
        when(webElement.isEnabled()).thenReturn(true);
        assertThat( browser.isEnabled(div), is(true));
    }

    @Test
    public void isEnabledNo() {
        when(webElement.isEnabled()).thenReturn(false);
        assertThat( browser.isEnabled(div), is(false));
    }

    @Test
    public void isEnableDoesNotExist() {
        when(driverMock.findElement(By.xpath("//" + listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.isEnabled(listItem), is(false));
    }

}
