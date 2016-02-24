package com.github.loyada.dollarx.custommatchers.hamcrest

import com.github.loyada.dollarx.RelationOperator._
import com.github.loyada.dollarx.custommatchers.CustomMatchersUtil
import com.github.loyada.dollarx.{PathParsers, RelationOperator, Path}
import org.hamcrest.{Matcher, Description, TypeSafeMatcher}
import org.w3c.dom.Document


class HasElementNTimesForDocument(path: Path, nTimes: Int) {

    case class NTimesMatcherForDocument(relationOperator: RelationOperator = exactly) extends TypeSafeMatcher[Document] {
      private var foundNTimes: Int = 0

      def describeTo(description: Description) {
        description.appendText(s"document page contains ${CustomMatchersUtil.wrap(path)}${opAsEnglish(relationOperator)}${nTimes} time${if (nTimes != 1) "s" else ""}")
      }

      protected override def describeMismatchSafely(document: Document, mismatchDescription: Description) {
        mismatchDescription.appendText(CustomMatchersUtil.wrap(path) + " appears " + foundNTimes + " time" + (if (foundNTimes != 1) "s" else ""))
      }

      protected def matchesSafely(document: Document): Boolean = {
        foundNTimes = PathParsers.findAllByPath(document, path).getLength
        relationOperator match {
          case RelationOperator.orLess => foundNTimes <= nTimes
          case RelationOperator.orMore => foundNTimes >= nTimes
          case RelationOperator.exactly => foundNTimes == nTimes
        }
      }
    }

    def timesOrMore: Matcher[Document] = NTimesMatcherForDocument(orMore)
    def timesOrLess: Matcher[Document] = NTimesMatcherForDocument(orLess)
    def times: Matcher[Document] = NTimesMatcherForDocument()

}
