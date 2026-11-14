package dev.teamuts.point.domain.balance.constant

import dev.teamuts.point.domain.common.model.Point

enum class BalanceOperationType(
  val calculate: (currentBalance: Point, pointAmount: Point) -> Point
) {
  CREDIT({ c, p -> c.plus(p) }),
  DEBIT({ c, p -> c.minus(p) }),
}
