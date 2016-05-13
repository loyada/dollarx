package com.github.loyada.dollarx.custommatchers.hamcrest

import javax.xml.xpath.XPathExpressionException

import com.github.loyada.dollarx.util.StringUtil
import com.github.loyada.dollarx.util.StringUtil._
import com.github.loyada.dollarx.{PathParsers, Path, Browser}
import org.hamcrest.{Description, TypeSafeMatcher, Matcher}
import org.w3c.dom.Document


object IsAbsent {
  def apply = new IsAbsent
  def from(browser: Browser) = new IsAbsent from browser
  def from(document: Document) = new IsAbsent from document
}

class IsAbsent {
  def from(browser: Browser): Matcher[Path] =
    new TypeSafeMatcher[Path]() {
      private var el: Path = null

      def describeTo(description: Description) {
        description.appendText("browser page does not contain " + wrap(el))
      }

      protected override def describeMismatchSafely(el: Path, mismatchDescription: Description) {
        mismatchDescription.appendText(wrap(el) + " is present")
      }

      protected def matchesSafely(el: Path): Boolean = {
        this.el = el
        browser.isPresent(!el)
      }
  }

  def from(document: Document): Matcher[Path] = {
    new TypeSafeMatcher[Path]() {
      private var el: Path = null

      def describeTo(description: Description) {
        description.appendText("document does not contain " + StringUtil.wrap(el))
      }

      protected override def describeMismatchSafely(el: Path, mismatchDescription: Description) {
        mismatchDescription.appendText(StringUtil.wrap(el) + " is present")
      }

      protected def matchesSafely(el: Path): Boolean = {
        this.el = el
        try {
          PathParsers.findAllByPath(document, el).getLength == 0
        }
        catch {
          case e: XPathExpressionException =>
            throw new RuntimeException("could not parse")
        }
      }
    }
  }
}
