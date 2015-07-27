package info.dollarx

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers._
import Path._
import ElementProperties._
class BasicPathCreationTest {

   @Test def divBeforeSpan {
    val el = div before span
    val xpath: String = el.getXPath.get
    assertThat(xpath, equalTo("span/preceding::div"))
    assertThat(el.toString(), equalTo("div, before span"))
  }

  @Test def divAfterSpan {
    val el = div that hasText("John") after span
    val xpath: String = el.getXPath.get
    assertThat(xpath, equalTo("span/following::div[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = 'john']"))
    assertThat(el.toString, equalTo("div, with the text \"John\", after span"))
  }

  @Test def divBeforeSiblingSpan {
    val el = div beforeSibling span
    val xpath: String = el.getXPath.get
    assertThat(xpath, equalTo("span/preceding-sibling::div"))
    assertThat(el.toString, equalTo("div, before the sibling span"))
  }

  @Test def complexBeforeAndAfter {
    val el: Path = (div that isBefore(span)).before(span that isAfter(div that isBefore(span)))
    val xpath: String = el.getXPath.get
    assertThat(xpath, equalTo("span[preceding::div[following::span]]/preceding::div[following::span]"))
    assertThat(el.toString, equalTo("div, that is before: span, before (span, that is after: (div, that is before: span))"))
  }
}
