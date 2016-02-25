package com.github.loyada.dollarx.custommatchers.hamcrest

import com.github.loyada.dollarx.util.StringUtil
import com.github.loyada.dollarx.{PathOperators, Path, Browser}
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import StringUtil.wrap

object CustomMatchers {

  def hasElement(el: Path): Matcher[Browser] = {
     new TypeSafeMatcher[Browser]() {
      def describeTo(description: Description) {
        description.appendText("browser page contains " + wrap(el))
      }

      protected override def describeMismatchSafely(browser: Browser, mismatchDescription: Description) {
        mismatchDescription.appendText(wrap(el) + " is absent")
      }

      protected def matchesSafely(browser: Browser): Boolean = {
         browser.isPresent(el)
      }
    }
  }

  def isPresent(nTimes: Int): IsPresentNTimes = {
      IsPresentNTimes(nTimes)
  }

  def isPresent: IsPresent = {
     new IsPresent
  }

  def hasElements(path: Path): HasElements = {
      HasElements(path)
  }

  def isPresentIn(browser: Browser): Matcher[Path] = {
     new TypeSafeMatcher[Path]() {
      private var el: Path = null

      def describeTo(description: Description) {
        description.appendText("browser page contains " + wrap(el))
      }

      protected override def describeMismatchSafely(el: Path, mismatchDescription: Description) {
        mismatchDescription.appendText(wrap(el) + " is absent")
      }

      protected def matchesSafely(el: Path): Boolean = {
        this.el = el
         browser.isPresent(el)
      }
    }
  }

  def isDisplayedIn(browser: Browser): Matcher[Path] = {
     new TypeSafeMatcher[Path]() {
      private var el: Path = null

      def describeTo(description: Description) {
        description.appendText(el + " is displayed")
      }

      protected override def describeMismatchSafely(el: Path, mismatchDescription: Description) {
        mismatchDescription.appendText(wrap(el) + " is not displayed")
      }

      protected def matchesSafely(el: Path): Boolean = {
        this.el = el
         browser.isDisplayed(el)
      }
    }
  }

  def isSelectedIn(browser: Browser): Matcher[Path] = {
     new TypeSafeMatcher[Path]() {
      private var el: Path = null

      def describeTo(description: Description) {
        description.appendText(el + " is selected")
      }

      protected override def describeMismatchSafely(el: Path, mismatchDescription: Description) {
        mismatchDescription.appendText(el + " is not selected")
      }

      protected def matchesSafely(el: Path): Boolean = {
        this.el = el
         browser.isSelected(el)
      }
    }
  }

  def isEnabledIn(browser: Browser): Matcher[Path] = {
     new TypeSafeMatcher[Path]() {
      private var el: Path = null

      def describeTo(description: Description) {
        description.appendText(wrap(el) + " is enabled")
      }

      protected override def describeMismatchSafely(el: Path, mismatchDescription: Description) {
        mismatchDescription.appendText(wrap(el) + " is not enabled")
      }

      protected def matchesSafely(el: Path): Boolean = {
        this.el = el
         browser.isEnabled(el)
      }
    }
  }

  def hasNoElement(el: Path): Matcher[Browser] = {
     new TypeSafeMatcher[Browser]() {
      def describeTo(description: Description) {
        description.appendText("browser page does not contain " + StringUtil.wrap(el))
      }

      protected override def describeMismatchSafely(browser: Browser, mismatchDescription: Description) {
        mismatchDescription.appendText(el.toString + " is present")
      }

      protected def matchesSafely(browser: Browser): Boolean = {
         browser.isPresent(PathOperators.not(el))
      }
    }
  }

  def isAbsentFrom(browser: Browser): Matcher[Path] = {
     new TypeSafeMatcher[Path]() {
      private var el: Path = null

      def describeTo(description: Description) {
        description.appendText("browser page does not contain " + wrap(el))
      }

      protected override def describeMismatchSafely(el: Path, mismatchDescription: Description) {
        mismatchDescription.appendText(wrap(el) + " is present")
      }

      protected def matchesSafely(el: Path): Boolean = {
        this.el = el
         browser.isPresent(!el)
      }
    }
  }
}
