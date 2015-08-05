package info.dollarx.custommatchers.hamcrest

import info.dollarx.custommatchers.hamcrest.CustomMatchers._
import info.dollarx.{Path, Browser}
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.{is, equalTo}
import org.junit.Assert._
import org.junit.Test
import org.junit.Before
import org.mockito.Matchers
import org.mockito.Mockito.{mock, when}
import org.openqa.selenium.{WebElement, NoSuchElementException}


class HamcrestMatchersTest {
  import Path._
  
  var browser: Browser = null

  @Before def setup {
    browser = mock(classOf[Browser])
  }

  @Test def isPresentFailed {
    when(browser.find(div)).thenThrow(new NoSuchElementException(""))
    try {
      assertThat(div, isPresentIn(browser))
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: browser page contains div\n     but: div is absent")))
      }
    }
  }

  @Test def isPresentVariationFailed {
    when(browser.find(div)).thenThrow(new NoSuchElementException(""))
    try {
      assertThat(div, IsPresent in browser)
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: browser page contains div\n     but: div is absent")))
      }
    }
  }

  @Test def isAbsentFailed {
    when(browser.find(div)).thenReturn(mock(classOf[WebElement]))
    assertThat(div, CoreMatchers.not(isPresentIn(browser)))
    try {
      assertThat(div, isAbsentFrom(browser))
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: browser page does not contain div\n     but: div is present")))
      }
    }
  }

  @Test def hasElementFailed {
    when(browser.find(div)).thenThrow(new NoSuchElementException(""))
    try {
      assertThat(browser, hasElement(div))
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: browser page contains div\n     but: div is absent")))
      }
    }
  }

  @Test def hasNoElementFailed {
    when(browser.find(div)).thenReturn(mock(classOf[WebElement]))
    try {
      assertThat(browser, hasNoElement(div))
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: browser page does not contain div\n     but: div is present")))
      }
    }
  }

  @Test def isPresentNTimesVariationFailed {
    val el = span inside div
    when(browser.findPageWithNumberOfOccurrences(Matchers.eq(el), Matchers.eq(5))).thenThrow(new NoSuchElementException(""))
    when(browser.numberOfAppearances(Matchers.eq(el))).thenReturn(1)
    try {
      assertThat(el, isPresent(5).timesIn(browser))
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: browser page contains (span, inside div) 5 times\n     but: (span, inside div) appears 1 time")))
      }
    }
  }

  @Test def isPresentNTimesVariationSuccess {
    when(browser.numberOfAppearances(Matchers.eq(span inside div))).thenReturn(5)
    assertThat(span inside div, isPresent(5).timesIn(browser))
  }

  @Test def hasElementNTimesFailed {
    when(browser.numberOfAppearances(div)).thenReturn(2)
    when(browser.findPageWithNumberOfOccurrences(div, 5)).thenThrow(new NoSuchElementException(""))
    try {
      assertThat(browser, hasElements(div).present(5).times)
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: browser page contains div 5 times\n     but: div appears 2 times")))
      }
    }
  }

  @Test def hasElementNTimesSuccess {
    when(browser.findPageWithNumberOfOccurrences(div, 2)).thenReturn(mock(classOf[WebElement]))
    assertThat(browser, hasElements(div).present(2).times)
  }

  @Test def isDisplayedFailed {
    when(browser.isDisplayed(div)).thenReturn(false)
    try {
      assertThat(div, CustomMatchers.isDisplayedIn(browser))
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: div is displayed\n     but: div is not displayed")))
      }
    }
  }

  @Test def isSelecteddFailed {
    when(browser.isSelected(div)).thenReturn(false)
    try {
      assertThat(div, CustomMatchers.isSelectedIn(browser))
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: div is selected\n     but: div is not selected")))
      }
    }
  }

  @Test def isEnabledFailed {
    when(browser.isEnabled(div)).thenReturn(false)
    try {
      assertThat(div, CustomMatchers.isEnabledIn(browser))
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: div is enabled\n     but: div is not enabled")))
      }
    }
  }
}
