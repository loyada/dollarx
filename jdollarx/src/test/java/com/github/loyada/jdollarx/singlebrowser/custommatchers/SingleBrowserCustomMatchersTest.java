package com.github.loyada.jdollarx.singlebrowser.custommatchers;


import com.github.loyada.jdollarx.BasicPath;
import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;

import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.hasText;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isAbsent;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isDisplayed;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isEnabled;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isNotDisplayed;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isNotSelected;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isPresent;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isSelected;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
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
    public void isDisplayedSuccess() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenReturn(mockedElement);
        when(mockedElement.isDisplayed()).thenReturn(false).thenReturn(true);
        assertThat(BasicPath.div, isDisplayed());
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
    public void isDisplayedNotInDOMFailed() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenThrow(NoSuchElementException.class);
        try {
            assertThat(BasicPath.div, isDisplayed());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is displayed\n     but: div is not displayed")));
        }
    }

    @Test
    public void isNotDisplayedSuccess() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenReturn(mockedElement);
        when(mockedElement.isDisplayed()).thenReturn(true).thenReturn(false);
        assertThat(BasicPath.div, isNotDisplayed());
    }

    @Test
    public void isNotDisplayedFailed() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenReturn(mockedElement);
        when(mockedElement.isDisplayed()).thenReturn(true);
        try {
            assertThat(BasicPath.div, isNotDisplayed());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is not displayed\n     but: div is displayed")));
        }
    }

    @Test
    public void isNotDisplayedNotPresent() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenThrow(NoSuchElementException.class);
        assertThat(BasicPath.div, isNotDisplayed());
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
    public void isNotSelecteddFailed() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenReturn(mockedElement);
        when(mockedElement.isSelected()).thenReturn(true);
        try {
            assertThat(BasicPath.div, isNotSelected());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(startsWith("\nExpected: div is not selected\n     but: div is selected")));
        }
    }

    @Test
    public void isNotSelecteddNotInDOMFailed() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenThrow(NoSuchElementException.class);
        try {
            assertThat(BasicPath.div, isNotSelected());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(startsWith("\nExpected: div is not selected\n     but: div is selected")));
        }
    }

    @Test
    public void isSelectedSuccess() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenReturn(mockedElement);
        when(mockedElement.isSelected()).thenReturn(false).thenReturn(false).thenReturn(true);
        assertThat(BasicPath.div, isSelected());
    }

    @Test
    public void isNotSelectedSuccess() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenReturn(mockedElement);
        when(mockedElement.isSelected()).thenReturn(true).thenReturn(true).thenReturn(false);
        assertThat(BasicPath.div, isNotSelected());
    }


    @Test
    public void isNotSelecteddNotInDOM() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenThrow(NoSuchElementException.class);
        try {
            assertThat(BasicPath.div, isNotSelected());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(startsWith("\nExpected: div is not selected\n     but: div is selected")));
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
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is enabled\n     but: div is not enabled, or is not in the DOM")));
        }
    }

    @Test
    public void isEnabledSuccess() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenReturn(mockedElement);
        when(mockedElement.isEnabled()).thenReturn(true);
        assertThat(BasicPath.div, isEnabled());
    }

    @Test
    public void isEnabledNotInDOMFailed() {
        WebElement mockedElement = mock(WebElement.class);
        when(driver.findElement(any())).thenThrow(NoSuchElementException.class);
        try {
            assertThat(BasicPath.div, isEnabled());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is enabled\n     but: div is not enabled, or is not in the DOM")));
        }
    }

}
