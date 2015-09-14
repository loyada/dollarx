package info.dollarx.custommatchers.hamcrest

import info.dollarx.RelationOperator.RelationOperator
import info.dollarx.{RelationOperator, Browser, Path}
import info.dollarx.RelationOperator.RelationOperator
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
  import RelationOperator._
  case class NTimesMatcher(relationOperator: RelationOperator = exactly) extends TypeSafeMatcher[Browser] {
    private var foundNTimes: Int = 0

    def describeTo(description: Description) {
      description.appendText(s"browser page contains ${CustomMatchersUtil.wrap(path)}${opAsEnglish(relationOperator)}${nTimes} time${if (nTimes != 1) "s" else ""}")
    }

    protected override def describeMismatchSafely(browser: Browser, mismatchDescription: Description) {
      mismatchDescription.appendText(CustomMatchersUtil.wrap(path) + " appears " + foundNTimes + " time" + (if (foundNTimes != 1) "s" else ""))
    }

    protected def matchesSafely(browser: Browser): Boolean = {
      try {
        browser.findPageWithNumberOfOccurrences(path, nTimes, relationOperator)
        true
      }
      catch {
        case e: NoSuchElementException => {
          foundNTimes = browser.numberOfAppearances(path)
          false
        }
      }
    }
  }

  def timesOrMore: Matcher[Browser] = NTimesMatcher(orMore)
  def timesOrLess: Matcher[Browser] = NTimesMatcher(orLess)
  def times: Matcher[Browser] = NTimesMatcher()
}
