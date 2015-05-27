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
  val even = not (odd)

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


}

// Make them easy to import with:
// import CustomMatchers._
object CustomMatchers extends CustomMatchers