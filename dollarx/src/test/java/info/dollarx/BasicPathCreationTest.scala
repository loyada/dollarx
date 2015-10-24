package info.dollarx

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.equalTo
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
    assertThat(el.toString, equalTo("""div, that has the text "John", after span"""))
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

  @Test def withClassAndIndex {
    val el: Path = (div that hasClass("foo"))(5)
    val xpath: String = el.getXPath.get
    assertThat(xpath, equalTo("(//div[contains(concat(' ', normalize-space(@class), ' '), ' foo ')])[6]"))
    assertThat(el.toString, equalTo("""occurrence number 6 of (div, that has class "foo")"""))
  }

  @Test def withClassAndInside {
    val dialog: Path = div withClass "ui-dialog"
    val el: Path = span that(has cssClass "foo", is inside dialog)
    val xpath: String = el.getXPath.get
    assertThat(xpath, equalTo("span[contains(concat(' ', normalize-space(@class), ' '), ' foo ')][ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' ui-dialog ')]]"))
    assertThat(el.toString, equalTo("""span, that has class "foo", and has ancestor: (div, that has class "ui-dialog")"""))
  }

  @Test def withClassAndInsidedialogWithDescription {
    val dialog: Path = (div withClass "ui-dialog") describedBy "sumbission form"
    val el: Path =  span that(has cssClass "foo" , is inside dialog)
    val xpath: String = el.getXPath.get
    assertThat(xpath, equalTo("span[contains(concat(' ', normalize-space(@class), ' '), ' foo ')][ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' ui-dialog ')]]"))
    assertThat(el.toString, equalTo("""span, that has class "foo", and has ancestor: (sumbission form)"""))
  }

  @Test def divOrSpan {
    val el: Path = div or span
    val xpath: String = el.getXPath.get
    assertThat(xpath, equalTo("*[self::div | self::span]"))
    assertThat(el.toString, equalTo("div or span"))
  }

  @Test def divWithClassOrSpan {
    val el: Path = (div withClass "foo") or span
    val xpath: String = el.getXPath.get
    assertThat(xpath, equalTo("*[self::div[contains(concat(' ', normalize-space(@class), ' '), ' foo ')] | self::span]"))
    assertThat(el.toString,  equalTo("""(div, that has class "foo") or span"""))
  }
}
