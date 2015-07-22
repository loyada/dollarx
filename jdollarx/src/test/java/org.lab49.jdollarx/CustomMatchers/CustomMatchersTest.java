package com.lab49.jdollarx.CustomMatchers;

import org.lab49.jdollarx.InBrowser;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.lab49.jdollarx.BasicPath.*;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;


public class CustomMatchersTest {
    InBrowser browser;

    @Before
    public void setup() {
        browser = mock(InBrowser.class);
    }

    @Test
    public void isPresentFailed() {
        when(browser.find(any())).thenThrow(new NoSuchElementException(""));
        try {
            assertThat(div, isPresentIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div\n     but: div is absent")));
        }
    }

    @Test
    public void isPresentVariationFailed() {
        when(browser.find(any())).thenThrow(new NoSuchElementException(""));
        try {
            assertThat(div, isPresent().in(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div\n     but: div is absent")));
        }
    }

    @Test
    public void isAbsentFailed() {
        when(browser.find(any())).thenReturn(mock(WebElement.class));
        assertThat(div, not(isPresentIn(browser)));

        try {
            assertThat(div, isAbsentFrom(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page does not contain div\n     but: div is present")));
        }
    }

    @Test
    public void hasElementFailed() {
        when(browser.find(any())).thenThrow(new NoSuchElementException(""));
        try {
            assertThat(browser, hasElement(div));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div\n     but: div is absent")));
        }
    }

    @Test
    public void hasNoElementFailed() {
        when(browser.find(any())).thenReturn(mock(WebElement.class));
        try {
            assertThat(browser, hasNoElement(div));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page does not contain div\n     but: div is present")));
        }
    }

    @Test
    public void isPresentNTimesFailed() {
        when(browser.numberOfAppearances(any())).thenReturn(1);
        try {
            assertThat(span.inside(div), isPresentNTimes(5, browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains (span, inside div) 5 times\n     but: (span, inside div) appears 1 time")));
        }
    }

    @Test
    public void isPresentNTimesVariationFailed() {
        when(browser.numberOfAppearances(any())).thenReturn(1);
        try {
            assertThat(span.inside(div), isPresent(5).timesIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains (span, inside div) 5 times\n     but: (span, inside div) appears 1 time")));
        }
    }

    @Test
    public void isPresentNTimesVariationSuccess() {
        when(browser.numberOfAppearances(any())).thenReturn(5);
        assertThat(span.inside(div), isPresent(5).timesIn(browser));
    }

    @Test
    public void hasElementNTimesFailed() {
        when(browser.numberOfAppearances(div)).thenReturn(2);
        try {
            assertThat(browser, hasElements(div).present(5).times());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div 5 times\n     but: div appears 2 times")));
        }
    }

    @Test
    public void hasElementNTimesSuccess() {
        when(browser.numberOfAppearances(div)).thenReturn(2);
        assertThat(browser, hasElements(div).present(2).times());
    }

    @Test
    public void isDisplayedFailed() {
        when(browser.isDisplayed(div)).thenReturn(false);
        try {
            assertThat(div, isDisplayedIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is displayed\n     but: div is not displayed")));
        }
    }

    @Test
    public void isSelecteddFailed() {
        when(browser.isSelected(div)).thenReturn(false);
        try {
            assertThat(div, isSelectedIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is selected\n     but: div is not selected")));
        }
    }

    @Test
    public void isEnabledFailed() {
        when(browser.isEnabled(div)).thenReturn(false);
        try {
            assertThat(div, isEnabledIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is enabled\n     but: div is not enabled")));
        }
    }

}
