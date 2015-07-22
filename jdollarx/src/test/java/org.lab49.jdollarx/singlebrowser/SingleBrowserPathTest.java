package com.lab49.jdollarx.singlebrowser;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.Arrays;
import java.util.List;

import static org.lab49.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SingleBrowserPathTest {
    WebDriver driverMock;
    Keyboard keyboardMock;
    Mouse mouseMock;
    RemoteWebElement webElement, webElement2;
    Coordinates coordinates, coordinates2;

    @Before
    public void setup() {
        driverMock = mock(ChromeDriver.class);
        mouseMock = mock(Mouse.class);
        keyboardMock = mock(Keyboard.class);
        driver = driverMock;
        webElement = mock(RemoteWebElement.class);
        webElement2 = mock(RemoteWebElement.class);
        coordinates = mock(Coordinates.class);
        coordinates2 = mock(Coordinates.class);
        when(((HasInputDevices) driver).getMouse()).thenReturn(mouseMock);
        when(((HasInputDevices) driver).getKeyboard()).thenReturn(keyboardMock);
        when(webElement.getCoordinates()).thenReturn(coordinates);
        when(webElement2.getCoordinates()).thenReturn(coordinates2);
        when(driverMock.findElement(By.xpath("//" + div.getXPath().get()))).thenReturn(webElement);
        when(driverMock.findElement(By.xpath("//" + span.getXPath().get()))).thenReturn(webElement2);
    }

    @Test
    public void clickElement() {
        div.click();
        verify(mouseMock).mouseMove(coordinates);
        verify(mouseMock).click(null);
    }

    @Test
    public void doubleclickElement() {
        div.doubleClick();

        verify(mouseMock).mouseMove(coordinates);
        verify(mouseMock).doubleClick(coordinates);
    }

    @Test
    public void sendKeysToElement() {
        div.sendKeys("abc", "d");
        verify(mouseMock).click(coordinates);
        verify(keyboardMock).sendKeys("abc", "d");
    }

    @Test
    public void DragAndDropToElement() {
        div.dragAndDrop().to(span);
        verify(mouseMock).mouseMove(coordinates);
        verify(mouseMock).mouseDown(coordinates);
        verify(mouseMock, atLeastOnce()).mouseMove(coordinates2);
        verify(mouseMock).mouseUp(coordinates2);
    }

    @Test
    public void DragAndDropToOffset() {
        div.dragAndDrop().to(10, 10);
        verify(mouseMock).mouseMove(coordinates);
        verify(mouseMock).mouseDown(coordinates);
        verify(mouseMock).mouseMove(null, 10, 10);
        verify(mouseMock).mouseUp(null);
    }

    @Test
    public void find() {
        assertThat(div.find(), is(webElement));
    }

    @Test
    public void findAll() {
        List<WebElement> result =  Arrays.asList(webElement);
        when(driverMock.findElements(By.xpath("//" + div.getXPath().get()))).thenReturn(result);

        assertThat(div.findAll(), is(equalTo(result)));
    }

    @Test
    public void scroll() {
        div.scrollTo();
        verify(mouseMock).mouseMove(coordinates);
    }

    @Test
    public void hover() {
        div.hover();
        verify(mouseMock).mouseMove(coordinates);
    }
}
