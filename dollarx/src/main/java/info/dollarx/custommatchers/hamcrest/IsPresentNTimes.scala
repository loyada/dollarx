package info.dollarx.custommatchers.hamcrest

import info.dollarx.Browser
import info.dollarx.Path
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.openqa.selenium.NoSuchElementException

case class IsPresentNTimes(nTimes: Int) {


  def timesIn(browser: Browser): Matcher[Path] = {
     new TypeSafeMatcher[Path]() {
      private var foundNTimes: Int = 0
      private var el: Path = null

      def describeTo(description: Description) {
        description.appendText(s"browser page contains ${CustomMatchersUtil.wrap(el)} ${nTimes} time${if (nTimes != 1) "s" else ""}")
      }

      protected override def describeMismatchSafely(el: Path, mismatchDescription: Description) {
        mismatchDescription.appendText(CustomMatchersUtil.wrap(el) + " appears " + foundNTimes + " time" + (if (foundNTimes != 1) "s" else ""))
      }

      protected def matchesSafely(el: Path): Boolean = {
        this.el = el
        try {
          browser.findPageWithNumberOfOccurrences(el, nTimes)
           true
        }
        catch {
          case e: NoSuchElementException => {
            foundNTimes = browser.numberOfAppearances(el)
             foundNTimes == nTimes
          }
        }
      }
    }
  }
}
