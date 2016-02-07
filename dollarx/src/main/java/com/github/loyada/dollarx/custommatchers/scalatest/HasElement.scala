package com.github.loyada.dollarx.custommatchers.scalatest

import com.github.loyada.dollarx.Path
import com.github.loyada.dollarx.Browser
import org.scalatest.matchers.{Matcher, MatchResult}

case class HasElement(path: Path) extends  Matcher[Browser] {
  def apply(browser: Browser) = MatchResult(
                   browser.isPresent(path),
                   path + " is expected to be present, but is absent",
                   path + " is expected to be absent, but is present")
}
