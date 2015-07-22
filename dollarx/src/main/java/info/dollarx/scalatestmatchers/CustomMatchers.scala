package info.dollarx.scalatestmatchers

import dollarx.InBrowser
import info.dollarx.{Path, InBrowser}
import info.dollarx.InBrowser.Predicates
import org.openqa.selenium.NoSuchElementException
import org.scalatest.Matchers._
import org.scalatest.matchers.{BeMatcher, MatchResult, Matcher}



trait CustomMatchers {
  class PresentMatcher extends BeMatcher[Path] {
    def apply(left: Path) =
      MatchResult(
        Predicates.isPresent(left),
        left.toString + " is expected to be present, but is absent",
          left.toString + " is expected to be absent, but is present"
      )
  }

  val present = new PresentMatcher

  class AbsentMatcher extends BeMatcher[Path] {
    def apply(left: Path) =
      MatchResult(
        InBrowser.Predicates.isPresent(!left),
        left.toString + " is expected to be absent, but is present",
        left.toString + " is expected to be present, but is absent"
      )
  }

  val absent = new AbsentMatcher

  class enabledMatcher extends BeMatcher[Path] {
    def apply(left: Path) =
      MatchResult(
        InBrowser.Predicates.isEnabled(left),
        left.toString + " is expected to be enabled, but it is not",
        left.toString + " is not expected to be enabled, but it is"
      )
  }

  val enabled = new AbsentMatcher
  val disabled = not(enabled)

  class displayedMatcher extends BeMatcher[Path] {
    def apply(left: Path) =
      MatchResult(
        InBrowser.Predicates.isDisplayed(left),
        left.toString + " is not displayed",
        left.toString + " is displayed"
      )
  }

  val displayed = new AbsentMatcher
  val hidden = not(displayed)

  class selectedMatcher extends BeMatcher[Path] {
    def apply(left: Path) =
      MatchResult(
        InBrowser.Predicates.isSelected(left),
        left.toString + " is not selected",
        left.toString + " is selected"
      )
  }

  val selected = new AbsentMatcher

  object appear {

    trait ApearsTimes extends Matcher[Path]

    def apply(nTimes: NTimes) = new ApearsTimes {
        private def getNumOfAppearances(el: Path) = {
          try {
            InBrowser.findAll(el).size
          } catch {
            case _: NoSuchElementException => 0
          }
        }

        def apply(left: Path) = {
          val actual = getNumOfAppearances(left)
          MatchResult(
            actual == nTimes.n,
            left.toString + s" should appear ${nTimes.n} times, but it appears $actual times",
            left.toString + s" appears ${nTimes.n} even though it should not"
          )
        }
    }
  }


  case class NTimes(n: Int)
  case class TimesBuilder(n: Int) {
    val times = NTimes(n)
  }

  implicit def intToTimesBuilder(n: Int) = TimesBuilder(n)
}

  // Make them easy to import with:
  // import CustomMatchers._
  object CustomMatchers extends CustomMatchers