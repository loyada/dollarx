package com.github.loyada.dollarx.custommatchers.hamcrest

import com.github.loyada.dollarx.{RelationOperator, Path}
import com.github.loyada.dollarx.Browser
import org.hamcrest.{TypeSafeMatcher, Description, Matcher}
import org.openqa.selenium.NoSuchElementException

case class IsPresentNTimes(nTimes: Int) {
  import RelationOperator._
  case class PresentNTimesMatcher(browser: Browser, relationOperator: RelationOperator = exactly) extends TypeSafeMatcher[Path]() {
    private var foundNTimes: Int = 0
    private var el: Path = null

    def describeTo(description: Description) {
      description.appendText(s"browser page contains ${CustomMatchersUtil.wrap(el)}${opAsEnglish(relationOperator)}${nTimes} time${if (nTimes != 1) "s" else ""}")
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

  def timesIn(browser: Browser): Matcher[Path] = PresentNTimesMatcher(browser)
  def timesOrMoreIn(browser: Browser): Matcher[Path] = PresentNTimesMatcher(browser, orMore)
  def timesOrLessIn(browser: Browser): Matcher[Path] = PresentNTimesMatcher(browser, orLess)

}
