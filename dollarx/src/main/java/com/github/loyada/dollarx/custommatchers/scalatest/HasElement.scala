package com.github.loyada.dollarx.custommatchers.scalatest

import com.github.loyada.dollarx.{PathParsers, Path, Browser}
import org.scalatest.matchers.{Matcher, MatchResult}
import org.w3c.dom.Document

case class HasElement(path: Path) extends  Matcher[Browser] {
  def apply(browser: Browser) = MatchResult(
                   browser.isPresent(path),
                   path + " is expected to be present, but is absent",
                   path + " is expected to be absent, but is present")
  def apply(doc: Document) = MatchResult(
    PathParsers.findAllByPath(doc, path).getLength > 0 ,
    path + " is expected to be present, but is absent",
    path + " is expected to be absent, but is present")
}
