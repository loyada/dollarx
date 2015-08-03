package info.dollarx.custommatchers.scalatest

import info.dollarx.{Browser, Path}
import org.scalatest.matchers.{Matcher, MatchResult}

case class HasElement(path: Path) extends  Matcher[Browser] {
  def apply(browser: Browser) = MatchResult(
                   browser.isPresent(path),
                   path + " is expected to be present, but is absent",
                   path + " is expected to be absent, but is present")
}
