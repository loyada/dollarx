package com.github.loyada.jdollarx.CustomMatchers;

import com.github.loyada.jdollarx.ElementProperties;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.custommatchers.CustomMatchers;
import com.github.loyada.jdollarx.BasicPath;
import com.github.loyada.jdollarx.RelationOperator;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import static com.github.loyada.jdollarx.BasicPath.html;
import static com.github.loyada.jdollarx.ElementProperties.contains;
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
            assertThat(BasicPath.div, CustomMatchers.isPresent().in(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div\n     but: div is absent")));
        }
    }

    @Test
    public void isPresentSuccess() {
        when(browser.isPresent(any())).thenReturn(true);
        assertThat(BasicPath.div, CustomMatchers.isPresentIn(browser));
    }

    @Test
    public void isAbsentFailed() {
        when(browser.isPresent(eq(html.that(ElementProperties.not(contains(BasicPath.div)))))).thenThrow(new NoSuchElementException(""));
        try {
            assertThat(BasicPath.div, CustomMatchers.isAbsentFrom(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page does not contain div\n     but: div is present")));
        }
    }

    @Test
    public void isAbsentSuccess() {
        when(browser.isPresent(any())).thenReturn(true);
        assertThat(BasicPath.div, CustomMatchers.isAbsentFrom(browser));
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
    public void isPresentNTimesVariationFailed() {
        when(browser.findPageWithNumberOfOccurrences(any(), eq(5), eq(RelationOperator.exactly))).thenThrow(new NoSuchElementException(""));
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
        when(browser.findPageWithNumberOfOccurrences(any(), eq(5), eq(RelationOperator.exactly))).thenReturn(mock(WebElement.class));

        when(browser.numberOfAppearances(any())).thenReturn(5);
        assertThat(BasicPath.span.inside(BasicPath.div), CustomMatchers.isPresent(5).timesIn(browser));
    }

    @Test(expected = IllegalArgumentException.class)
    public void isPresentNTimesInvalidInput() {
            assertThat(BasicPath.span, CustomMatchers.isPresent(0).timesIn(browser));
    }

    @Test
    public void isPresentNTimesOrMoreFailed() {
        when(browser.findPageWithNumberOfOccurrences(any(), eq(5), eq(RelationOperator.orMore))).thenThrow(new NoSuchElementException(""));
        when(browser.numberOfAppearances(any())).thenReturn(1);
        try {
            assertThat(BasicPath.span.inside(BasicPath.div), CustomMatchers.isPresent(5).timesOrMoreIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains (span, inside div) at least 5 times\n     but: (span, inside div) appears 1 time")));
        }
    }

    @Test
    public void isPresentNTimesOrMoreSuccess() {
        when(browser.findPageWithNumberOfOccurrences(any(), eq(5), eq(RelationOperator.orMore))).thenReturn(mock(WebElement.class));
        assertThat(BasicPath.span.inside(BasicPath.div), CustomMatchers.isPresent(5).timesOrMoreIn(browser));
    }


    @Test
    public void isPresentNTimesOrLessFailed() {
        when(browser.findPageWithNumberOfOccurrences(any(), eq(5), eq(RelationOperator.orLess))).thenThrow(new NoSuchElementException(""));
        when(browser.numberOfAppearances(any())).thenReturn(1);
        try {
            assertThat(BasicPath.span.inside(BasicPath.div), CustomMatchers.isPresent(5).timesOrLessIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains (span, inside div) at most 5 times\n     but: (span, inside div) appears 1 time")));
        }
    }

    @Test
    public void isPresentNTimesOrLessSuccess() {
        when(browser.findPageWithNumberOfOccurrences(any(), eq(5), eq(RelationOperator.orLess))).thenReturn(mock(WebElement.class));
        assertThat(BasicPath.span.inside(BasicPath.div), CustomMatchers.isPresent(5).timesOrLessIn(browser));
    }

    @Test
    public void hasElementNTimesFailed() {
        when(browser.numberOfAppearances(BasicPath.div)).thenReturn(2);
        when(browser.findPageWithNumberOfOccurrences(eq(BasicPath.div), eq(5), eq(RelationOperator.exactly))).thenThrow(new NoSuchElementException(""));

        try {
            assertThat(browser, CustomMatchers.hasElements(BasicPath.div).present(5).times());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div 5 times\n     but: div appears 2 times")));
        }
    }

    @Test
    public void hasElementNTimesSuccess() {
        when(browser.findPageWithNumberOfOccurrences(eq(BasicPath.div), eq(2), eq(RelationOperator.exactly))).thenReturn(mock(WebElement.class));
        assertThat(browser, CustomMatchers.hasElements(BasicPath.div).present(2).times());
    }

    @Test
    public void hasElementNTimesOrMoreFailed() {
        when(browser.numberOfAppearances(BasicPath.div)).thenReturn(2);
        when(browser.findPageWithNumberOfOccurrences(eq(BasicPath.div), eq(5), eq(RelationOperator.orMore))).thenThrow(new NoSuchElementException(""));

        try {
            assertThat(browser, CustomMatchers.hasElements(BasicPath.div).present(5).timesOrMore());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains at least div 5 times\n     but: div appears 2 times")));
        }
    }

    @Test
    public void hasElementNTimesOrMoreSuccess() {
        when(browser.findPageWithNumberOfOccurrences(eq(BasicPath.div), eq(2), eq(RelationOperator.orMore))).thenReturn(mock(WebElement.class));
        assertThat(browser, CustomMatchers.hasElements(BasicPath.div).present(2).timesOrMore());
    }

    @Test
    public void hasElementNTimesOrLessFailed() {
        when(browser.numberOfAppearances(BasicPath.div)).thenReturn(2);
        when(browser.findPageWithNumberOfOccurrences(eq(BasicPath.div), eq(5), eq(RelationOperator.orLess))).thenThrow(new NoSuchElementException(""));

        try {
            assertThat(browser, CustomMatchers.hasElements(BasicPath.div).present(5).timesOrLess());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains at most div 5 times\n     but: div appears 2 times")));
        }
    }

    @Test
    public void hasElementNTimesOrLessSuccess() {
        when(browser.findPageWithNumberOfOccurrences(eq(BasicPath.div), eq(2), eq(RelationOperator.orLess))).thenReturn(mock(WebElement.class));
        assertThat(browser, CustomMatchers.hasElements(BasicPath.div).present(2).timesOrLess());
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
