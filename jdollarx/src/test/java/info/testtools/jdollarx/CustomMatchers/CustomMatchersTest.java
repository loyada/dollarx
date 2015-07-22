package info.testtools.jdollarx.CustomMatchers;

import info.testtools.jdollarx.BasicPath;
import info.testtools.jdollarx.custommatchers.IsPresent;
import info.testtools.jdollarx.InBrowser;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import info.testtools.jdollarx.custommatchers.CustomMatchers;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

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
            assertThat(BasicPath.div, CustomMatchers.isPresentIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div\n     but: div is absent")));
        }
    }

    @Test
    public void isPresentVariationFailed() {
        when(browser.find(any())).thenThrow(new NoSuchElementException(""));
        try {
            assertThat(BasicPath.div, IsPresent.in(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div\n     but: div is absent")));
        }
    }

    @Test
    public void isAbsentFailed() {
        when(browser.find(any())).thenReturn(mock(WebElement.class));
        assertThat(BasicPath.div, CoreMatchers.not(CustomMatchers.isPresentIn(browser)));

        try {
            assertThat(BasicPath.div, CustomMatchers.isAbsentFrom(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page does not contain div\n     but: div is present")));
        }
    }

    @Test
    public void hasElementFailed() {
        when(browser.find(any())).thenThrow(new NoSuchElementException(""));
        try {
            assertThat(browser, CustomMatchers.hasElement(BasicPath.div));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div\n     but: div is absent")));
        }
    }

    @Test
    public void hasNoElementFailed() {
        when(browser.find(any())).thenReturn(mock(WebElement.class));
        try {
            assertThat(browser, CustomMatchers.hasNoElement(BasicPath.div));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page does not contain div\n     but: div is present")));
        }
    }

    @Test
    public void isPresentNTimesFailed() {
        when(browser.numberOfAppearances(any())).thenReturn(1);
        try {
            assertThat(BasicPath.span.inside(BasicPath.div), CustomMatchers.isPresentNTimes(5, browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains (span, inside div) 5 times\n     but: (span, inside div) appears 1 time")));
        }
    }

    @Test
    public void isPresentNTimesVariationFailed() {
        when(browser.numberOfAppearances(any())).thenReturn(1);
        try {
            assertThat(BasicPath.span.inside(BasicPath.div), CustomMatchers.isPresent(5).timesIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains (span, inside div) 5 times\n     but: (span, inside div) appears 1 time")));
        }
    }

    @Test
    public void isPresentNTimesVariationSuccess() {
        when(browser.numberOfAppearances(any())).thenReturn(5);
        assertThat(BasicPath.span.inside(BasicPath.div), CustomMatchers.isPresent(5).timesIn(browser));
    }

    @Test
    public void hasElementNTimesFailed() {
        when(browser.numberOfAppearances(BasicPath.div)).thenReturn(2);
        try {
            assertThat(browser, CustomMatchers.hasElements(BasicPath.div).present(5).times());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div 5 times\n     but: div appears 2 times")));
        }
    }

    @Test
    public void hasElementNTimesSuccess() {
        when(browser.numberOfAppearances(BasicPath.div)).thenReturn(2);
        assertThat(browser, CustomMatchers.hasElements(BasicPath.div).present(2).times());
    }

    @Test
    public void isDisplayedFailed() {
        when(browser.isDisplayed(BasicPath.div)).thenReturn(false);
        try {
            assertThat(BasicPath.div, CustomMatchers.isDisplayedIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is displayed\n     but: div is not displayed")));
        }
    }

    @Test
    public void isSelecteddFailed() {
        when(browser.isSelected(BasicPath.div)).thenReturn(false);
        try {
            assertThat(BasicPath.div, CustomMatchers.isSelectedIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is selected\n     but: div is not selected")));
        }
    }

    @Test
    public void isEnabledFailed() {
        when(browser.isEnabled(BasicPath.div)).thenReturn(false);
        try {
            assertThat(BasicPath.div, CustomMatchers.isEnabledIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is enabled\n     but: div is not enabled")));
        }
    }

}
