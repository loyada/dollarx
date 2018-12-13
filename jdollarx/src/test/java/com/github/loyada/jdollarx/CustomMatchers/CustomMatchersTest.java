package com.github.loyada.jdollarx.CustomMatchers;

import com.github.loyada.jdollarx.ElementProperties;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.custommatchers.CustomMatchers;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.BasicPath.html;
import static com.github.loyada.jdollarx.BasicPath.span;
import static com.github.loyada.jdollarx.ElementProperties.contains;
import static com.github.loyada.jdollarx.RelationOperator.exactly;
import static com.github.loyada.jdollarx.RelationOperator.orLess;
import static com.github.loyada.jdollarx.RelationOperator.orMore;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomMatchersTest {
    InBrowser browser;

    @Before
    public void setup() {
        browser = mock(InBrowser.class);
    }

    @Test
    public void isPresentFailed() {
        when(browser.isPresent(any())).thenReturn(false);
        try {
            assertThat(div, CustomMatchers.isPresentIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div\n     but: div is absent")));
        }
    }

    @Test
    public void isPresentWithRetriesFailure() {
        when(browser.isPresent(any())).thenReturn(false);
        try {
            Operations.doWithRetries(
                    () -> assertThat(div, CustomMatchers.isPresentIn(browser)),
                    5,
                    10

            );
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div\n     but: div is absent")));
            verify(browser, times(5)).isPresent(div);
        }
    }

    @Test
    public void isPresentWithRetriesSuccess() {
        when(browser.isPresent(any())).thenReturn(false,false, true);
        Operations.doWithRetries(
                    () -> assertThat(div, CustomMatchers.isPresentIn(browser)),
                    5,
                    10

        );
        verify(browser, times(3)).isPresent(div);
    }

    @Test
    public void isPresentVariationFailed() {
        when(browser.isPresent(any())).thenReturn(false);
        try {
            assertThat(div, CustomMatchers.isPresent().in(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div\n     but: div is absent")));
        }
    }

    @Test
    public void isPresentSuccess() {
        when(browser.isPresent(any())).thenReturn(true);
        assertThat(div, CustomMatchers.isPresentIn(browser));
    }

    @Test
    public void isAbsentFailed() {
        when(browser.isPresent(eq(html.that(ElementProperties.not(contains(div)))))).thenThrow(new NoSuchElementException(""));
        try {
            assertThat(div, CustomMatchers.isAbsentFrom(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page does not contain div\n     but: div is present")));
        }
    }

    @Test
    public void isAbsentSuccess() {
        when(browser.isPresent(any())).thenReturn(true);
        assertThat(div, CustomMatchers.isAbsentFrom(browser));
    }

    @Test
    public void hasElementFailed() {
        when(browser.find(any())).thenThrow(new NoSuchElementException(""));
        try {
            assertThat(browser, CustomMatchers.hasElement(div));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div\n     but: div is absent")));
        }
    }

    @Test
    public void hasNoElementFailed() {
        when(browser.find(any())).thenReturn(mock(WebElement.class));
        try {
            assertThat(browser, CustomMatchers.hasNoElement(div));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page does not contain div\n     but: div is present")));
        }
    }

    @Test
    public void isPresentNTimesVariationFailed() {
        when(browser.findPageWithNumberOfOccurrences(any(), eq(5), eq(exactly))).thenThrow(new NoSuchElementException(""));
        when(browser.numberOfAppearances(any())).thenReturn(1);
        try {
            assertThat(span.inside(div), CustomMatchers.isPresent(5).timesIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains (span, inside div) 5 times\n     but: (span, inside div) appears 1 time")));
        }
    }

    @Test
    public void isPresentNTimesVariationSuccess() {
        when(browser.findPageWithNumberOfOccurrences(any(), eq(5), eq(exactly))).thenReturn(mock(WebElement.class));

        when(browser.numberOfAppearances(any())).thenReturn(5);
        assertThat(span.inside(div), CustomMatchers.isPresent(5).timesIn(browser));
    }

    @Test(expected = IllegalArgumentException.class)
    public void isPresentNTimesInvalidInput() {
            assertThat(span, CustomMatchers.isPresent(0).timesIn(browser));
    }

    @Test
    public void isPresentNTimesOrMoreFailed() {
        when(browser.findPageWithNumberOfOccurrences(any(), eq(5), eq(orMore))).thenThrow(new NoSuchElementException(""));
        when(browser.numberOfAppearances(any())).thenReturn(1);
        try {
            assertThat(span.inside(div), CustomMatchers.isPresent(5).timesOrMoreIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains (span, inside div) at least 5 times\n     but: (span, inside div) appears 1 time")));
        }
    }

    @Test
    public void isPresentNTimesOrMoreSuccess() {
        when(browser.findPageWithNumberOfOccurrences(any(), eq(5), eq(orMore))).thenReturn(mock(WebElement.class));
        assertThat(span.inside(div), CustomMatchers.isPresent(5).timesOrMoreIn(browser));
    }


    @Test
    public void isPresentNTimesOrLessFailed() {
        when(browser.findPageWithNumberOfOccurrences(any(), eq(5), eq(orLess))).thenThrow(new NoSuchElementException(""));
        when(browser.numberOfAppearances(any())).thenReturn(1);
        try {
            assertThat(span.inside(div), CustomMatchers.isPresent(5).timesOrLessIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains (span, inside div) at most 5 times\n     but: (span, inside div) appears 1 time")));
        }
    }

    @Test
    public void isPresentNTimesOrLessSuccess() {
        when(browser.findPageWithNumberOfOccurrences(any(), eq(5), eq(orLess))).thenReturn(mock(WebElement.class));
        assertThat(span.inside(div), CustomMatchers.isPresent(5).timesOrLessIn(browser));
    }

    @Test
    public void hasElementNTimesFailed() {
        when(browser.numberOfAppearances(div)).thenReturn(2);
        when(browser.findPageWithNumberOfOccurrences(eq(div), eq(5), eq(exactly))).thenThrow(new NoSuchElementException(""));

        try {
            assertThat(browser, CustomMatchers.hasElements(div).present(5).times());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains div 5 times\n     but: div appears 2 times")));
        }
    }

    @Test
    public void hasElementNTimesSuccess() {
        when(browser.findPageWithNumberOfOccurrences(eq(div), eq(2), eq(exactly))).thenReturn(mock(WebElement.class));
        assertThat(browser, CustomMatchers.hasElements(div).present(2).times());
    }

    @Test
    public void hasElementNTimesOrMoreFailed() {
        when(browser.numberOfAppearances(div)).thenReturn(2);
        when(browser.findPageWithNumberOfOccurrences(eq(div), eq(5), eq(orMore))).thenThrow(new NoSuchElementException(""));

        try {
            assertThat(browser, CustomMatchers.hasElements(div).present(5).timesOrMore());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains at least div 5 times\n     but: div appears 2 times")));
        }
    }

    @Test
    public void hasElementNTimesOrMoreSuccess() {
        when(browser.findPageWithNumberOfOccurrences(eq(div), eq(2), eq(orMore))).thenReturn(mock(WebElement.class));
        assertThat(browser, CustomMatchers.hasElements(div).present(2).timesOrMore());
    }

    @Test
    public void hasElementNTimesOrLessFailed() {
        when(browser.numberOfAppearances(div)).thenReturn(2);
        when(browser.findPageWithNumberOfOccurrences(eq(div), eq(5), eq(orLess))).thenThrow(new NoSuchElementException(""));

        try {
            assertThat(browser, CustomMatchers.hasElements(div).present(5).timesOrLess());
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: browser page contains at most div 5 times\n     but: div appears 2 times")));
        }
    }

    @Test
    public void hasElementNTimesOrLessSuccess() {
        when(browser.findPageWithNumberOfOccurrences(eq(div), eq(2), eq(orLess))).thenReturn(mock(WebElement.class));
        assertThat(browser, CustomMatchers.hasElements(div).present(2).timesOrLess());
    }

    @Test
    public void isDisplayedFailed() {
        when(browser.isDisplayed(div)).thenReturn(false);
        try {
            assertThat(div, CustomMatchers.isDisplayedIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is displayed\n     but: div is not displayed")));
        }
    }

    @Test
    public void isSelecteddFailed() {
        when(browser.isSelected(div)).thenReturn(false);
        try {
            assertThat(div, CustomMatchers.isSelectedIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is selected\n     but: div is not selected")));
        }
    }

    @Test
    public void isEnabledFailed() {
        when(browser.isEnabled(div)).thenReturn(false);
        try {
            assertThat(div, CustomMatchers.isEnabledIn(browser));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: div is enabled\n     but: div is not enabled")));
        }
    }

}
