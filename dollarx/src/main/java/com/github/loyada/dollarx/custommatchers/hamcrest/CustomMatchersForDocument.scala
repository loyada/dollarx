package com.github.loyada.dollarx.custommatchers.hamcrest

import com.github.loyada.dollarx.util.StringUtil
import com.github.loyada.dollarx.{PathOperators, PathParsers, Path}
import org.hamcrest.{Description, TypeSafeMatcher, Matcher}
import org.w3c.dom.Document


object CustomMatchersForDocument {
  def isPresentIn(document: Document): Matcher[Path] = {
    new TypeSafeMatcher[Path]() {
      private var el: Path = null

      def describeTo(description: Description) {
        description.appendText("document contains " + StringUtil.wrap(el))
      }

      protected override def describeMismatchSafely(el: Path, mismatchDescription: Description) {
        mismatchDescription.appendText(StringUtil.wrap(el) + " is absent")
      }

      protected def matchesSafely(el: Path): Boolean = {
        this.el = el
        PathParsers.findAllByPath(document, el).getLength > 0
      }
    }
  }

  def hasElement(el: Path): Matcher[Document] = {
    new TypeSafeMatcher[Document]() {
      def describeTo(description: Description) {
        description.appendText("document contains " + el)
      }

      protected override def describeMismatchSafely(document: Document, mismatchDescription: Description) {
        mismatchDescription.appendText(StringUtil.wrap(el) + " is absent")
      }

      protected def matchesSafely(document: Document): Boolean = {
        PathParsers.findAllByPath(document, el).getLength > 0
      }
    }
  }

  def hasElements(path: Path): HasElementsForDocument = {
    HasElementsForDocument(path)
  }


  def hasNoElement(el: Path): Matcher[Document] = {
    new TypeSafeMatcher[Document]() {
      def describeTo(description: Description) {
        description.appendText("document does not contain " + StringUtil.wrap(el))
      }

      protected override def describeMismatchSafely(document: Document, mismatchDescription: Description) {
        mismatchDescription.appendText(el.toString + " is present")
      }

      protected def matchesSafely(document: Document): Boolean = {
        PathParsers.findAllByPath(document, PathOperators.not(el)).getLength > 0
      }
    }
  }

  def isAbsentFrom(doc: Document): Matcher[Path] = IsAbsent from doc
  def isAbsent: IsAbsent = new IsAbsent
}
