package info.dollarx.custommatchers.scalatest

import info.dollarx.{Path, Browser}
import org.scalatest.matchers.{MatchResult, BeMatcher}


class IsPresent {
  def in(browser: Browser): BeMatcher[Path] = new BeMatcher[Path]{
    def apply(left: Path) =
      MatchResult(
        browser.isPresent(left),
        left.toString + " is expected to be present, but is absent",
        left.toString + " is expected to be absent, but is present"
      )
  }
}

