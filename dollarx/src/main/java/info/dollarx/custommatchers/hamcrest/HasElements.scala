package info.dollarx.custommatchers.hamcrest

import info.dollarx.Path

case class HasElements(path: Path) {
  def present(nTimes: Int): HasElementNTimes = {
     new HasElementNTimes(path, nTimes)
  }
}
