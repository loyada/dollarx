package info.dollarx.custommatchers.scalatest

import info.dollarx.{Browser, Path}
import org.openqa.selenium.NoSuchElementException
import org.scalatest.matchers.{MatchResult, Matcher}


object PresentNTimes {
  implicit def intToTimesBuilder(n: Int) = TimesBuilder(n)

  def apply() = new IsPresent

  def apply(nTimes: NTimes) = new ApearsTimes {

    private def getByNumOfAppearances(el: Path, browser: Browser) = {
      try {
        browser.findPageWithNumberOfOccurrences(el, nTimes.n)
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
          left.toString + s" should appear ${nTimes.n} times, but it appears $actual times",
          left.toString + s" appears ${nTimes.n} even though it should not"
        )
      }
    }
  }
}

trait ApearsTimes {
  def in(browser: Browser): Matcher[Path]
}


case class NTimes(n: Int)
case class TimesBuilder(n: Int) {
  val times = NTimes(n)
}