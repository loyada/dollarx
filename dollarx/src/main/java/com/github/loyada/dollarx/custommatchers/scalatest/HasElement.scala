package com.github.loyada.dollarx.custommatchers.scalatest

import com.github.loyada.dollarx.custommatchers.CustomMatchersUtil
import com.github.loyada.dollarx.{PathParsers, Path, Browser}
import org.scalatest.matchers.{Matcher, MatchResult}
import CustomMatchersUtil.wrap

case class HasElement(path: Path) extends  Matcher[Browser] {
  def apply(browser: Browser) = MatchResult(
                   browser.isPresent(path),
                   wrap(path) + " is expected to be present, but is absent",
                   wrap(path) + " is expected to be absent, but is present")
}
