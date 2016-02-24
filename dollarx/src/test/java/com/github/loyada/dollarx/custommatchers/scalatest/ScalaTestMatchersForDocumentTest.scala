package com.github.loyada.dollarx.custommatchers.scalatest

import com.github.loyada.dollarx.{PathParsers, Path}
import com.github.loyada.dollarx.XpathUtils._
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, MustMatchers, FunSpec}
import org.scalatest.exceptions.TestFailedException
import org.w3c.dom.Document

class ScalaTestMatchersForDocumentTest extends FunSpec with MustMatchers with BeforeAndAfterEach with BeforeAndAfter{
  import Path._
  import CustomMatchers._
  var doc: Document = _

  describe("Browser") {
    doc = PathParsers.getDocumentFromString("<body><div><span class='foo'></span></div><div></div><div></div><span class='foo'></span></body>")

    describe("when element is not present") {
      val el = div withClass "foo" describedBy "Hulk"
      it("shows correct error for present") {
        try {
          el must be(present in doc)
          fail("should fail")
        } catch {
          case e: TestFailedException => e.getMessage() must equal("Hulk is expected to be present, but is absent")
        }
      }
    }

    describe("when element is not absent") {
      val el = span inside div describedBy "Hulk"
      val xpath = DoesNotExistInEntirePage(el.getXPath.get)
      it("shows correct error for absent") {
        try {
          el must be(absent in doc)
          fail("should fail")
        } catch {
          case e: TestFailedException => e.getMessage() must equal("Hulk is expected to be absent, but is present")
        }
      }
    }

    describe("when element is present") {
      val el = span inside div describedBy "Hulk"
      val xpath = el.getXPath.get
      it("haveElement is successful") {
        el must be(present in doc)
      }
    }

    describe("for element is appear n times") {
      it("functions for success") {
        div must (appear(3 times) in doc)
      }

      it(" shows error for presence n times correctly") {
        try {
          span withClass "foo" must (appear(5 times) in doc)
          fail("should fail")
        } catch {
          case e: TestFailedException => e.getMessage() must equal("(span, that has class \"foo\") should appear 5 times, but it appears 2 times")
        }
      }
    }

    describe("for element is appear at least n times") {
      it("functions for success") {
        div must (appear(3 timesOrMore ) in doc)
      }

      it(" shows error for presence n times correctly") {
        try {
          span withClass "foo" must (appear(5 timesOrMore) in doc)
          fail("should fail")
        } catch {
          case e: TestFailedException => e.getMessage() must equal("(span, that has class \"foo\") should appear at least 5 times, but it appears 2 times")
        }
      }
    }
    describe("for element is appear at most n times") {
      it("functions for success") {
        span withClass "foo" must (appear(3 timesOrLess ) in doc)
      }

      it(" shows error for presence n times correctly") {
        try {
          div must (appear(2 timesOrLess) in doc)
          fail("should fail")
        } catch {
          case e: TestFailedException => e.getMessage() must equal("div should appear at most 2 times, but it appears 3 times")
        }
      }
    }
  }
}
