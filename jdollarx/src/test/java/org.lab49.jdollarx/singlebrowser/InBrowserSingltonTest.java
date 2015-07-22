package com.lab49.jdollarx.singlebrowser;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;


import static org.lab49.jdollarx.BasicPath.div;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InBrowserSingltonTest {
    WebDriver driverMock;
    WebElement webElement;

    @Before
    public void setup() {
        driverMock = mock(WebDriver.class);
        driver = driverMock;
        webElement = mock(WebElement.class);

        when(driverMock.findElement(By.xpath("//div"))).thenReturn(webElement);
        when(driverMock.findElements(By.xpath("//div"))).thenReturn(Arrays.asList(webElement, webElement));

    }

    @Test
    public void basicFindElement() {
        assertThat(find(div), is(equalTo(webElement)));
    }

    @Test
    public void basicFindElements() {
        assertThat(findAll(div), is(equalTo(Arrays.asList(webElement, webElement))));
    }

    @Test
    public void numberOfAppearancesTest() {
        assertThat(numberOfAppearances(div), is(2));
    }

    @Test
    public void isPresentTest() {
        assertThat(isPresent(div), is(equalTo(true)));
    }

    @Test
    public void clickTest() {
        clickOn(div);
        verify(webElement).click();
    }

    @Test
    public void clickAtTest() {
        clickOn(div);
        verify(webElement).click();
    }

    @Test
    public void isSelectedTest() {
        when(webElement.isSelected()).thenReturn(true);
        assertThat(isSelected(div), is(equalTo(true)));
        when(webElement.isSelected()).thenReturn(false);
        assertThat(isSelected(div), is(equalTo(false)));
    }

    @Test
    public void isDisplayedTest() {
        when(webElement.isDisplayed()).thenReturn(true);
        assertThat(isDisplayed(div), is(equalTo(true)));
        when(webElement.isDisplayed()).thenReturn(false);
        assertThat(isDisplayed(div), is(equalTo(false)));
    }

    @Test
    public void isEnabledTest() {
        when(webElement.isEnabled()).thenReturn(true);
        assertThat(isEnabled(div), is(equalTo(true)));
        when(webElement.isEnabled()).thenReturn(false);
        assertThat(isEnabled(div), is(equalTo(false)));
    }
}
