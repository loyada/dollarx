package info.dollarx.custommatchers.hamcrest

import info.dollarx.Browser
import info.dollarx.Path
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.openqa.selenium.NoSuchElementException

/**
 * This matcher is optimized for the success use-case. In that case it match for a single element
 * with exact number of elements wanted.
 * In case of failure, it will make another call to get the actual number of elements on
 * the page, in order to provide a detailed error message.
 * So the trade off is: In case of success it's faster, In case of failure it's slower. It makes sense since most
 * of the time we expect success.
 */
case class HasElementNTimes(path: Path, nTimes: Int)  {

  def times: Matcher[Browser] = {
     new TypeSafeMatcher[Browser]() {
      private[hamcrest] var foundNTimes: Int = 0

      def describeTo(description: Description) {
        description.appendText(s"browser page contains ${CustomMatchersUtil.wrap(path)} ${nTimes} time${if (nTimes != 1) "s" else ""}")
      }

      protected override def describeMismatchSafely(browser: Browser, mismatchDescription: Description) {
        mismatchDescription.appendText(CustomMatchersUtil.wrap(path) + " appears " + foundNTimes + " time" + (if (foundNTimes != 1) "s" else ""))
      }

      protected def matchesSafely(browser: Browser): Boolean = {
        foundNTimes = browser.numberOfAppearances(path)
        try {
          browser.findPageWithNumberOfOccurrences(path, nTimes)
           true
        }
        catch {
          case e: NoSuchElementException => {
            foundNTimes = browser.numberOfAppearances(path)
             foundNTimes == nTimes
          }
        }
      }
    }
  }
}
