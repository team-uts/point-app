package dev.teamuts.point.domain.balance.constant

import dev.teamuts.point.domain.balance.model.sub.PointBalanceAmount
import dev.teamuts.point.domain.common.model.Point

enum class BalanceOperationType(
  val calculate: (currentBalance: Point, pointAmount: Point) -> Point,
  val calculateBalanceAmount:
    (amount1: PointBalanceAmount, amount2: PointBalanceAmount) -> PointBalanceAmount,
) {
  CREDIT(
    calculate = { c, p -> c.plus(p) },
    calculateBalanceAmount = { a1, a2 -> a1.credit(a2) },
  ),
  DEBIT(
    calculate = { c, p -> c.minus(p) },
    calculateBalanceAmount = { a1, a2 -> a1.debit(a2) },
  ),
}
