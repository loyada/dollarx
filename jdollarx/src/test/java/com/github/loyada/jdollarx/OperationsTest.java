package com.github.loyada.jdollarx;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;

import static com.github.loyada.jdollarx.BasicPath.div;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OperationsTest {
    WebDriver driver;
    InBrowser browser;
    WebElement webel;

    @Before
    public void setup() {
        driver = mock(WebDriver.class);
        browser = mock(InBrowser.class);
        webel = mock(WebElement.class);
    }

    @Test
    public void clickWithRetriesSuccess() throws Exception {
        when(browser.clickOn(any())).thenThrow(NoSuchElementException.class).thenThrow(NoSuchElementException.class).thenReturn(webel);
        Operations.doWithRetries(() -> browser.clickOn(div), 5, 10);
        verify(browser, times(3)).clickOn(div);

    }

    @Test
    public void clickWithRetriesFailure() throws Exception {
        when(browser.clickOn(any())).thenThrow(Exception.class).thenThrow(IOException.class).thenThrow(NoSuchElementException.class);
        try {
            Operations.doWithRetries(() -> browser.clickOn(div), 3, 10);
        } catch ( NoSuchElementException e) {
            verify(browser, times(3)).clickOn(div);
            return;
        }
        fail("expected to throw NoSuchElementException");
    }

    @Test(expected = TimeoutException.class)
    public void clickInvisibleFailure() throws Exception {
        browser = new InBrowser(driver);
        when(driver.findElement(any())).thenReturn(webel);
        when(webel.isDisplayed()).thenReturn(false);
        browser.clickOn(div);
    }

    @Test(expected = TimeoutException.class)
    public void clickDisabledFailure() throws Exception {
        browser = new InBrowser(driver);
        when(driver.findElement(any())).thenReturn(webel);
        when(webel.isDisplayed()).thenReturn(true);
        when(webel.isEnabled()).thenReturn(false);
        browser.clickOn(div);
    }

    @Test
    public void clickSuccess() throws Exception {
        browser = new InBrowser(driver);
        when(driver.findElement(any())).thenReturn(webel);
        when(webel.isDisplayed()).thenReturn(true);
        when(webel.isEnabled()).thenReturn(true);
        browser.clickOn(div);
    }

}
