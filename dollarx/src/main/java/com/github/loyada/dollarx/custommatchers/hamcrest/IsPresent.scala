package com.github.loyada.dollarx.custommatchers.hamcrest

import javax.xml.xpath.XPathExpressionException

import com.github.loyada.dollarx.util.StringUtil
import com.github.loyada.dollarx.{Path, Browser, PathParsers}
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.w3c.dom.Document

object IsPresent {
  def apply = new IsPresent
  def in(browser: Browser) = new IsPresent in browser
  def in(document: Document) = new IsPresent in document
}

class IsPresent {
  def in(browser: Browser): Matcher[Path] = {
     new TypeSafeMatcher[Path]() {
      private var el: Path = null

      def describeTo(description: Description) {
        description.appendText("browser page contains " + el.toString)
      }

      protected override def describeMismatchSafely(el: Path, mismatchDescription: Description) {
        mismatchDescription.appendText(StringUtil.wrap(el) + " is absent")
      }

      protected def matchesSafely(el: Path): Boolean = {
        this.el = el
         browser.isPresent(el)
      }
    }
  }

  def in(document: Document): Matcher[Path] = {
    new TypeSafeMatcher[Path]() {
      private var el: Path = null

      def describeTo(description: Description) {
        description.appendText("document contains " + StringUtil.wrap(el))
      }

      protected override def describeMismatchSafely(el: Path, mismatchDescription: Description) {
        mismatchDescription.appendText(StringUtil.wrap(el) + " is absent")
      }

      protected def matchesSafely(el: Path): Boolean = {
        this.el = el
        try {
           PathParsers.findAllByPath(document, el).getLength > 0
        }
        catch {
          case e: XPathExpressionException =>
            throw new RuntimeException("could not parse")
        }
      }
    }
  }
}
