package dollarx

import org.scalatest.matchers.{BeMatcher, MatchResult}
import org.scalatest._
import Matchers._

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

  class Present extends BeMatcher[Int] {
    def apply(left: Int) =
      MatchResult(
        left % 2 == 1,
        left.toString + " was even",
        left.toString + " was odd"
      )
  }
}

// Make them easy to import with:
// import CustomMatchers._
object CustomMatchers extends CustomMatchers