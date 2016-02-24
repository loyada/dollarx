package com.github.loyada.dollarx.custommatchers.hamcrest

import com.github.loyada.dollarx.custommatchers.CustomMatchersUtil
import com.github.loyada.dollarx.{PathParsers, RelationOperator, Path, Browser}
import org.hamcrest.{TypeSafeMatcher, Description, Matcher}
import org.openqa.selenium.NoSuchElementException
import org.w3c.dom.Document

case class IsPresentNTimes(nTimes: Int) {
  import RelationOperator._
  case class PresentNTimesMatcher(browser: Browser, relationOperator: RelationOperator = exactly) extends TypeSafeMatcher[Path]() {
    private var foundNTimes: Int = 0
    private var el: Path = null

    def describeTo(description: Description) {
      description.appendText(s"browser page contains ${CustomMatchersUtil.wrap(el)}${opAsEnglish(relationOperator)}$nTimes time${if (nTimes != 1) "s" else ""}")
    }

    protected override def describeMismatchSafely(el: Path, mismatchDescription: Description) {
      mismatchDescription.appendText(CustomMatchersUtil.wrap(el) + " appears " + foundNTimes + " time" + (if (foundNTimes != 1) "s" else ""))
    }

    protected def matchesSafely(el: Path): Boolean = {
      this.el = el
      try {
        browser.findPageWithNumberOfOccurrences(el, nTimes, relationOperator)
        true
      }
      catch {
        case e: NoSuchElementException => {
          foundNTimes = browser.numberOfAppearances(el)
          false
        }
      }
    }
  }

  case class PresentNTimesMatcherForDocument(document: Document, relationOperator: RelationOperator = exactly) extends TypeSafeMatcher[Path]() {
    private var foundNTimes: Int = 0
    private var el: Path = null

    def describeTo(description: Description) {
      description.appendText(s"document contains ${CustomMatchersUtil.wrap(el)}${opAsEnglish(relationOperator)}$nTimes time${if (nTimes != 1) "s" else ""}")
    }

    protected override def describeMismatchSafely(el: Path, mismatchDescription: Description) {
      mismatchDescription.appendText(CustomMatchersUtil.wrap(el) + " appears " + foundNTimes + " time" + (if (foundNTimes != 1) "s" else ""))
    }

    protected def matchesSafely(el: Path): Boolean = {
      this.el = el
      foundNTimes = PathParsers.findAllByPath(document, el).getLength
      relationOperator match {
          case RelationOperator.orLess =>  foundNTimes<=nTimes
          case RelationOperator.orMore => foundNTimes>=nTimes
          case RelationOperator.exactly => foundNTimes==nTimes
      }
    }
  }

  def timesIn(browser: Browser): Matcher[Path] = PresentNTimesMatcher(browser)
  def timesOrMoreIn(browser: Browser): Matcher[Path] = PresentNTimesMatcher(browser, orMore)
  def timesOrLessIn(browser: Browser): Matcher[Path] = PresentNTimesMatcher(browser, orLess)

  def timesIn(document: Document): Matcher[Path] = PresentNTimesMatcherForDocument(document)
  def timesOrMoreIn(document: Document): Matcher[Path] = PresentNTimesMatcherForDocument(document, orMore)
  def timesOrLessIn(document: Document): Matcher[Path] = PresentNTimesMatcherForDocument(document, orLess)

}
