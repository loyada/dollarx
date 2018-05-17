package com.github.loyada.jdollarx.singlebrowser.custommatchers;


import com.github.loyada.jdollarx.BasicPath;

import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.*;
import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SingleBrowserCustomMatchersTest {

    WebDriver driver;

    @Before
    public void setup() {
        driver = mock(WebDriver.class);
        InBrowserSinglton.driver = driver;
    }

    @Test
    public void isPresentFailed() {
        when(driver.findElement(any())).thenThrow(new NoSuchElementException(""));
        try {
            assertThat(BasicPath.div, isPresent());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div\n     but: div is absent")));
        }
    }

    @Test
    public void hasTextFailed() {
        when(driver.findElement(any())).thenThrow(new NoSuchElementException(""));
        try {
            assertThat(BasicPath.div, hasText("foo"));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\n" +
                    "Expected: browser page contains div, that has the text \"foo\"\n" +
                    "     but: (div, that has the text \"foo\") is absent")));
        }
    }


    @Test
    public void isAbsentFailed() {
        when(driver.findElement(eq(By.xpath("/html[not(descendant::div)]")))).thenThrow(new NoSuchElementException(""));
        try {
            assertThat(BasicPath.div, isAbsent());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page does not contain div\n     but: div is present")));
        }
    }

    @Test
    public void isAbsentSuccess() {
        when(driver.findElement(eq(By.xpath("/html[not(descendant::div)]")))).thenReturn(mock(WebElement.class));
        assertThat(BasicPath.div, isAbsent());
    }

    @Test
    public void isPresentNTimesVariationFailed() {
        when(driver.findElement(any())).thenThrow(new NoSuchElementException(""));
        List<WebElement> mockedResult = Collections.singletonList(mock(WebElement.class));
        when(driver.findElements(any())).thenReturn(mockedResult);
        try {
            assertThat(BasicPath.span.inside(BasicPath.div), isPresent(5).times());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains (span, inside div) 5 times\n     but: (span, inside div) appears 1 time")));
        }
    }

    @Test
    public void isPresentNTimesVariationSuccess() {
        when(driver.findElement(any())).thenReturn(mock(WebElement.class));
        assertThat(BasicPath.span.inside(BasicPath.div), isPresent(5).times());
    }


    @Test
    public void isDisplayedFailed() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenReturn(mockedElement);
        when(mockedElement.isDisplayed()).thenReturn(false);
        try {
            assertThat(BasicPath.div, isDisplayed());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is displayed\n     but: div is not displayed")));
        }
    }

    @Test
    public void isSelecteddFailed() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenReturn(mockedElement);
        when(mockedElement.isSelected()).thenReturn(false);
        try {
            assertThat(BasicPath.div, isSelected());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is selected\n     but: div is not selected")));
        }
    }

    @Test
    public void isEnabledFailed() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenReturn(mockedElement);
        when(mockedElement.isEnabled()).thenReturn(false);
        try {
            assertThat(BasicPath.div, isEnabled());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is enabled\n     but: div is not enabled")));
        }
    }

}
