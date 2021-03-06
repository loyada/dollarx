package com.github.loyada.dollarx.custommatchers.scalatest

import com.github.loyada.dollarx.{PathParsers, Browser, Path}
import com.github.loyada.dollarx.singlebrowser.SingleBrowser._
import org.scalatest.matchers.{BeMatcher, MatchResult, Matcher}
import org.w3c.dom.Document

import scala.language.implicitConversions


object CustomMatchers {

  // present in browser
  val present = new IsPresent
  def haveElement(path: Path) = HasElement(path)

  class IsAbsent {
    def in(browser: Browser) = new BeMatcher[Path] {
      def apply(left: Path) = getMatchResult(left, absentInBrowser(browser))
    }

    def in(doc: Document) = new BeMatcher[Path] {
      def apply(left: Path) = getMatchResult(left, absentInDocument(doc))
    }

    private def getMatchResult(path: Path, f: (Path => Boolean)) = MatchResult(
      f(path),
      path.toString + " is expected to be absent, but is present",
      path.toString + " is expected to be present, but is absent"
    )

    private def absentInBrowser(browser: Browser) = (path: Path) => browser.isPresent(!path)
    private def absentInDocument(doc: Document) = (path: Path) =>  PathParsers.findAllByPath(doc, !path).getLength > 0
  }

  case class HasNoElement(path: Path) extends  Matcher[Browser] {
    def apply(browser: Browser) = MatchResult(
      browser.isPresent(!path),
      path + " is expected to be absent, but is present",
      path + " is expected to be present, but is absent")
  }

  val absent = new IsAbsent
  def haveNoElement(path: Path) = HasNoElement(path)

  class IsEnabled {
    def in(browser: Browser) = new BeMatcher[Path] {
      def apply(left: Path) =
        MatchResult(
          browser.isEnabled(left),
          left.toString + " is expected to be enabled, but it is not",
          left.toString + " is not expected to be enabled, but it is"
        )
    }
  }

  class IsDisabled {
    def in(browser: Browser) = new BeMatcher[Path] {
      def apply(left: Path) =
        MatchResult(
          !browser.isEnabled(left),
          left.toString + " is expected to be disabled, but it is enabled",
          left.toString + " is not expected to be disabled, but it is"
        )
    }
  }

  val enabled = new IsEnabled
  val disabled = new IsDisabled

  class IsDisplayed {
    def in(browser: Browser) = new BeMatcher[Path] {
      def apply(left: Path) =
        MatchResult(
          browser.isDisplayed(left),
          left.toString + " is not displayed",
          left.toString + " is displayed"
        )
    }
  }

  class IsHidden {
    def in(browser: Browser) = new BeMatcher[Path] {
      def apply(left: Path) =
        MatchResult(
          browser.isDisplayed(left),
          left.toString + " is not hidden",
          left.toString + " is hidden"
        )
    }
  }

  val displayed = new IsDisplayed
  val hidden = new IsHidden

  class IsSelected {
    def in(broswer: Browser) = new BeMatcher[Path] {
      def apply(left: Path) =
        MatchResult(
          Predicates.isSelected(left),
          left.toString + " is not selected",
          left.toString + " is selected"
        )
    }
  }

  val selected = new IsSelected

  def appear(nTimes: NTimes) =  PresentNTimes(nTimes)
  implicit def intToTimesBuilder(n: Int): TimesBuilder = TimesBuilder(n)

}

