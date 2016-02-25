package com.github.loyada.dollarx.custommatchers.scalatest

import com.github.loyada.dollarx.util.StringUtil.wrap
import com.github.loyada.dollarx.{PathParsers, Path, Browser}
import org.scalatest.matchers.{Matcher, MatchResult}

case class HasElement(path: Path) extends  Matcher[Browser] {
  def apply(browser: Browser) = MatchResult(
                   browser.isPresent(path),
                   wrap(path) + " is expected to be present, but is absent",
                   wrap(path) + " is expected to be absent, but is present")
}
