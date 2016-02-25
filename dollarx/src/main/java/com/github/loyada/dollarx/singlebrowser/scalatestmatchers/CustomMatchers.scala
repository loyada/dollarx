package com.github.loyada.dollarx.singlebrowser.scalatestmatchers

import com.github.loyada.dollarx.util.StringUtil.wrap
import com.github.loyada.dollarx.{RelationOperator, Path}
import com.github.loyada.dollarx.singlebrowser.SingleBrowser
import com.github.loyada.dollarx.singlebrowser.SingleBrowser._

import org.openqa.selenium.NoSuchElementException
import org.scalatest.Matchers._
import org.scalatest.matchers.{BeMatcher, MatchResult, Matcher}


trait CustomMatchers extends SingleBrowser{
  class PresentMatcher extends BeMatcher[Path] {
    def apply(left: Path) =
      MatchResult(
        Predicates.isPresent(left),
        wrap(left) + " is expected to be present, but is absent",
        wrap(left) + " is expected to be absent, but is present"
      )
  }

  val present = new PresentMatcher

  class AbsentMatcher extends BeMatcher[Path] {
    def apply(left: Path) =
      MatchResult(
        Predicates.isPresent(!left),
        wrap(left) + " is expected to be absent, but is present",
        wrap(left) + " is expected to be present, but is absent"
      )
  }

  val absent = new AbsentMatcher

  class enabledMatcher extends BeMatcher[Path] {
    def apply(left: Path) =
      MatchResult(
        Predicates.isEnabled(left),
        wrap(left) + " is expected to be enabled, but it is not",
        wrap(left) + " is not expected to be enabled, but it is"
      )
  }

  val enabled = new enabledMatcher
  val disabled = not(enabled)

  class displayedMatcher extends BeMatcher[Path] {
    def apply(left: Path) =
      MatchResult(
        Predicates.isDisplayed(left),
        wrap(left) + " is not displayed",
        wrap(left) + " is displayed"
      )
  }

  val displayed = new displayedMatcher
  val hidden = not(displayed)

  class selectedMatcher extends BeMatcher[Path] {
    def apply(left: Path) =
      MatchResult(
        Predicates.isSelected(left),
        wrap(left) + " is not selected",
        wrap(left) + " is selected"
      )
  }

  val selected = new selectedMatcher

  object appear {

    trait ApearsTimes extends Matcher[Path]

    def apply(nTimes: NTimes) = new ApearsTimes {
        private def getNumOfAppearances(el: Path) = {
          try {
            findAll(el).size
          } catch {
            case _: NoSuchElementException => 0
          }
        }

      private def getByNumOfAppearances(el: Path) = {
        try {
          findPageWithNumberOfOccurrences(el, nTimes.n, nTimes.relationOperator)
          nTimes.n
        }
        catch {
          case e: NoSuchElementException => {
            val foundNTimes = numberOfAppearances(el)
            foundNTimes
          }
        }
      }

        def apply(left: Path) = {
          val actual = getByNumOfAppearances(left)
          MatchResult(
            actual == nTimes.n,
            wrap(left) + s" should appear${RelationOperator.opAsEnglish(nTimes.relationOperator)}${nTimes.n} times, but it appears $actual times",
            wrap(left) + s" appears${RelationOperator.opAsEnglish(nTimes.relationOperator)}${nTimes.n} times, even though it must not"
          )
        }
    }
  }


  implicit def intToTimesBuilder(n: Int) = TimesBuilder(n)

  import RelationOperator._

  case class NTimes(n: Int, relationOperator: RelationOperator = exactly) {
  }



  case class TimesBuilder(n: Int) {
    val times = NTimes(n, exactly)
    val timesOrMore = NTimes(n, orMore)
    val timesOrLess = NTimes(n, orLess)

  }
}

  // Make them easy to import with:
  // import CustomMatchers._
  object CustomMatchers extends CustomMatchers