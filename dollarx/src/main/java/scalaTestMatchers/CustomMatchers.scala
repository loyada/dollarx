package scalaTestMatchers

import dollarx.{InBrowser, WebEl}
import org.scalatest.Matchers._
import org.scalatest.matchers.{BeMatcher, MatchResult}

trait CustomMatchers {

  class OddMatcher extends BeMatcher[Int] {
    def apply(left: Int) =
      MatchResult(
        left % 2 == 1,
        left.toString + " was even",
        left.toString + " was odd"
      )
  }

  val odd = new OddMatcher
  val even = not(odd)

  class PresentMatcher extends BeMatcher[WebEl] {
    def apply(left: WebEl) =
      MatchResult(
        InBrowser.Predicates.isPresent(left),
        left.toString + " is not present",
        left.toString + " is present"
      )
  }

  val present = new PresentMatcher

  class AbsentMatcher extends BeMatcher[WebEl] {
    def apply(left: WebEl) =
      MatchResult(
        InBrowser.Predicates.isPresent(!left),
        left.toString + " is not present",
        left.toString + " is present"
      )
  }

  val absent = new AbsentMatcher

  class enabledMatcher extends BeMatcher[WebEl] {
    def apply(left: WebEl) =
      MatchResult(
        InBrowser.Predicates.isEnabled(left),
        left.toString + " is not enabled",
        left.toString + " is enabled"
      )
  }

  val enabled = new AbsentMatcher
  val disabled = not(enabled)

  class displayedMatcher extends BeMatcher[WebEl] {
    def apply(left: WebEl) =
      MatchResult(
        InBrowser.Predicates.isDisplayed(left),
        left.toString + " is not displayed",
        left.toString + " is displayed"
      )
  }

  val displayed = new AbsentMatcher
  val hidden = not(displayed)

  class selectedMatcher extends BeMatcher[WebEl] {
    def apply(left: WebEl) =
      MatchResult(
        InBrowser.Predicates.isSelected(left),
        left.toString + " is not selected",
        left.toString + " is selected"
      )
  }

  val selected = new AbsentMatcher

  object appear {

    private trait ApearsTimes {
      def times(): BeMatcher[WebEl]
    }

    def apply(nTimes: NTimes) = new ApearsTimes {
      def times() = new BeMatcher[WebEl] {
        def apply(left: WebEl) =
          MatchResult(
            InBrowser.Predicates.apearsNTimes(left, nTimes.n),
            left.toString + " is not selected",
            left.toString + " is selected"
          )
      }
    }
  }


  case class NTimes(n: Int)
  case class TimesBuilder(n: Int) {
    val times = NTimes(n)
  }

  implicit def intToTimes(n: Int) = TimesBuilder(n)
}

  // Make them easy to import with:
  // import CustomMatchers._
  object CustomMatchers extends CustomMatchers