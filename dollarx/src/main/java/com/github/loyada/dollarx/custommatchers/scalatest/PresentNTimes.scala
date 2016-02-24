package com.github.loyada.dollarx.custommatchers.scalatest

import com.github.loyada.dollarx.custommatchers.CustomMatchersUtil
import CustomMatchersUtil.wrap
import com.github.loyada.dollarx.{PathParsers, RelationOperator, Path, Browser}
import org.openqa.selenium.NoSuchElementException
import org.scalatest.matchers.{MatchResult, Matcher}
import org.w3c.dom.Document

import scala.language.implicitConversions


object PresentNTimes {
  implicit def intToTimesBuilder(n: Int): TimesBuilder = TimesBuilder(n)

  def apply() = new IsPresent

  def apply(nTimes: NTimes) = new ApearsTimes {

    private def getByNumOfAppearances(el: Path, browser: Browser) = {
      try {
        browser.findPageWithNumberOfOccurrences(el, nTimes.n, nTimes.relationOperator)
        nTimes.n
      }
      catch {
        case e: NoSuchElementException => {
          val foundNTimes = browser.numberOfAppearances(el)
          foundNTimes
        }
      }
    }

    private def getByNumOfAppearances(el: Path, doc: Document) = {
      val found = PathParsers.findAllByPath(doc, el).getLength
      nTimes.relationOperator match {
        case RelationOperator.orLess => if (found<=nTimes.n) nTimes.n else found
        case RelationOperator.orMore => if (found>=nTimes.n) nTimes.n else found
        case RelationOperator.exactly => if (found==nTimes.n) nTimes.n else found
      }
    }

    def in(browser: Browser): Matcher[Path] = new Matcher[Path] {
      def apply(left: Path) = getMatcher(left, (path: Path) => getByNumOfAppearances(path, browser))
    }

    def in(doc: Document): Matcher[Path] = new Matcher[Path] {
      def apply(left: Path) = getMatcher(left, (path: Path) => getByNumOfAppearances(path, doc))
    }

    private def getMatcher(path: Path, findNumberOfOccurrencesFunc: (Path => Int)) = {
      val actual: Int = findNumberOfOccurrencesFunc(path)
      MatchResult(
        actual == nTimes.n,
        wrap(path) + s" should appear${RelationOperator.opAsEnglish(nTimes.relationOperator)}${nTimes.n} times, but it appears $actual times",
        wrap(path) + s" appears${RelationOperator.opAsEnglish(nTimes.relationOperator)}${nTimes.n} even though it should not"
      )
    }
  }
}

trait ApearsTimes {
  def in(browser: Browser): Matcher[Path]
}


import RelationOperator._

case class NTimes(n: Int, relationOperator: RelationOperator = exactly) {
}



case class TimesBuilder(n: Int) {
  val times = NTimes(n, exactly)
  val timesOrMore = NTimes(n, orMore)
  val timesOrLess = NTimes(n, orLess)

}
