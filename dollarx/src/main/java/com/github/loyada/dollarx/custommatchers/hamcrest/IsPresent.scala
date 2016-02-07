package com.github.loyada.dollarx.custommatchers.hamcrest

import com.github.loyada.dollarx.Path
import com.github.loyada.dollarx.Browser
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object IsPresent {
  def apply = new IsPresent
  def in(browser: Browser) = new IsPresent in browser
}

class IsPresent {
  def in(browser: Browser): Matcher[Path] = {
     new TypeSafeMatcher[Path]() {
      private var el: Path = null

      def describeTo(description: Description) {
        description.appendText("browser page contains " + el.toString)
      }

      protected override def describeMismatchSafely(el: Path, mismatchDescription: Description) {
        mismatchDescription.appendText(CustomMatchersUtil.wrap(el) + " is absent")
      }

      protected def matchesSafely(el: Path): Boolean = {
        this.el = el
         browser.isPresent(el)
      }
    }
  }
}
