package info.dollarx

import info.dollarx.RelationOperator._


case class NCount(n: Int, relationOperator: RelationOperator = exactly) {
}

case class NCountBuilder(n: Int) {
  val orMore = NCount(n, RelationOperator.orMore)
  val orLess = NCount(n, RelationOperator.orLess)
}
