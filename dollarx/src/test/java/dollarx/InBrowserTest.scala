package dollarx

import dollarx.ElementProperties.{hasText, hasClass}
import org.scalatest.FunSpec
import CustomMatchers._
import org.scalatest._
import Matchers._

class InBrowserTest extends FunSpec with MustMatchers {

  def doubleYourPleasure(i: Int): Int = i * 2

  describe("Browser") {

    it("should pop values in last-in-first-out order") {
      val evenNum = 2
      evenNum must be(even)
      doubleYourPleasure(evenNum) must be(even)

      val oddNum = 3
      oddNum must be(odd)
      doubleYourPleasure(oddNum) must be(odd) // This will fail    }
    }
    it("should throw NoSuchElementException if an empty stack is popped") {
      List(1,2) should have size 20

    }
  }
}