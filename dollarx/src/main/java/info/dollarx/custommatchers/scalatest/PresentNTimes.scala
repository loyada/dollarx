package info.dollarx.custommatchers.scalatest

import info.dollarx.{RelationOperator, Browser, Path}
import org.openqa.selenium.NoSuchElementException
import org.scalatest.matchers.{MatchResult, Matcher}


object PresentNTimes {
  implicit def intToTimesBuilder(n: Int) = TimesBuilder(n)

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
    def in(browser: Browser): Matcher[Path] = new Matcher[Path] {
      def apply(left: Path) = {
        val actual: Int = getByNumOfAppearances(left, browser)
        MatchResult(
          actual == nTimes.n,
          left.toString + s" should appear${RelationOperator.opAsEnglish(nTimes.relationOperator)}${nTimes.n} times, but it appears $actual times",
          left.toString + s" appears${RelationOperator.opAsEnglish(nTimes.relationOperator)}${nTimes.n} even though it should not"
        )
      }
    }
  }
}

trait ApearsTimes {
  def in(browser: Browser): Matcher[Path]
}


import info.dollarx.RelationOperator._

case class NTimes(n: Int, relationOperator: RelationOperator = exactly) {
}



case class TimesBuilder(n: Int) {
  val times = NTimes(n, exactly)
  val timesOrMore = NTimes(n, orMore)
  val timesOrLess = NTimes(n, orLess)

}
