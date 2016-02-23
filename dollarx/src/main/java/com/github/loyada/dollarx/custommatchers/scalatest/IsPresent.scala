package com.github.loyada.dollarx.custommatchers.scalatest

import com.github.loyada.dollarx.{PathParsers, Path, Browser}
import org.scalatest.matchers.{MatchResult, BeMatcher}
import org.w3c.dom.Document


class IsPresent {
  def in(browser: Browser): BeMatcher[Path] = new BeMatcher[Path]{
    def apply(left: Path) =
      getMatchResult(left, existsInBrowser(browser))
  }

  def in(doc: Document): BeMatcher[Path] = new BeMatcher[Path]{
    def apply(left: Path) =
      getMatchResult(left, existsInDocument(doc))
  }

  private def getMatchResult(path: Path, f: (Path => Boolean)) = MatchResult(
    f(path),
    path.toString + " is expected to be present, but is absent",
    path.toString + " is expected to be absent, but is present"
  )

  private def existsInBrowser(browser: Browser) = (path: Path) => browser.isPresent(path)
  private def existsInDocument(doc: Document) = (path: Path) =>  PathParsers.findAllByPath(doc, path).getLength > 0

}

