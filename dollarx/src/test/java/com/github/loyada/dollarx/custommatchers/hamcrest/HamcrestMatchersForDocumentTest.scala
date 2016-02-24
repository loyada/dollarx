package com.github.loyada.dollarx.custommatchers.hamcrest

import java.io.IOException
import javax.xml.parsers.ParserConfigurationException

import com.github.loyada.dollarx.PathParsers
import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.junit.Test
import org.mockito.Mockito._
import org.openqa.selenium.NoSuchElementException
import org.w3c.dom.Document
import org.xml.sax.SAXException
import com.github.loyada.dollarx.Path._
import com.github.loyada.dollarx.custommatchers.hamcrest.CustomMatchersForDocument._
import com.github.loyada.dollarx.custommatchers.hamcrest.CustomMatchers.isPresent

class HamcrestMatchersForDocumentTest {
  @Test
  def isPresentSuccess {
    val doc: Document = PathParsers.getDocumentFromString("<div><span></span></div>")
    assertThat(span.inside(div), isPresentIn(doc))
  }

  @Test
  def isPresentFailure {
    val doc: Document = PathParsers.getDocumentFromString("<div><span></span></div>")
    try {
      assertThat(div.inside(span), isPresentIn(doc))
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: document contains (div, inside span)\n     but: (div, inside span) is absent")))
      }
    }
  }

  @Test def isPresentVariationFailed {
    val doc: Document = PathParsers.getDocumentFromString("<div><span></span></div>")
    try {
      assertThat(div inside span, IsPresent in doc)
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: document contains (div, inside span)\n     but: (div, inside span) is absent")))
      }
    }
  }
  @Test
  def isAbsentSuccess {
    val doc: Document = PathParsers.getDocumentFromString("<div><span></span></div>")
    assertThat(div.inside(span), isAbsentFrom(doc))
  }

  @Test
  def isAbsentFailure {
    val doc: Document = PathParsers.getDocumentFromString("<div><span></span></div>")
    try {
      assertThat(span.inside(div), isAbsentFrom(doc))
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: document does not contain (span, inside div)\n     but: (span, inside div) is present")))
      }
    }
  }

  @Test
  def isPresentNTimesVariationFailed {
    val doc: Document = PathParsers.getDocumentFromString("<div><span><div/></span></div>")
    try {
      assertThat(div, isPresent(5).timesIn(doc))
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: document contains div 5 times\n     but: div appears 2 times")))
      }
    }
  }

  @Test
  def isPresentNTimesVariationSuccess {
    val doc: Document = PathParsers.getDocumentFromString("<div><span><div></div></span></div>")
    assertThat(div, CustomMatchers.isPresent(2).timesIn(doc))
  }

  @Test
  def isPresentNTimesOrMoreFailed {
    val doc: Document = PathParsers.getDocumentFromString("<div><span><div/></span></div>")
    try {
      assertThat(div, CustomMatchers.isPresent(5).timesOrMoreIn(doc))
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: document contains div at least 5 times\n     but: div appears 2 times")))
      }
    }
  }

  @Test
  def isPresentNTimesOrMoreSuccess {
    val doc: Document = PathParsers.getDocumentFromString("<div><span><div/></span></div>")
    assertThat(div, CustomMatchers.isPresent(2).timesOrMoreIn(doc))
  }

  @Test
  def isPresentNTimesOrLessFailed {
    val doc: Document = PathParsers.getDocumentFromString("<div><span><div/></span></div>")
    try {
      assertThat(div, CustomMatchers.isPresent(1).timesOrLessIn(doc))
      fail("should fail")
    }
    catch {
      case e: AssertionError => {
        assertThat(e.getMessage, is(equalTo("\nExpected: document contains div at most 1 time\n     but: div appears 2 times")))
      }
    }
  }

  @Test
  def isPresentNTimesOrLessSuccess {
    val doc: Document = PathParsers.getDocumentFromString("<div><span><div/></span></div>")
    assertThat(div, CustomMatchers.isPresent(5).timesOrLessIn(doc))
  }
}
