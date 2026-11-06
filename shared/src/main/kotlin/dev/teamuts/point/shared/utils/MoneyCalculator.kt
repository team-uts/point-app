package dev.teamuts.point.shared.utils

import java.math.BigDecimal

object MoneyCalculator {
  fun isGreaterThan(amount1: BigDecimal, amount2: BigDecimal): Boolean {
    return amount1 > amount2
  }

  fun isLessThan(amount1: BigDecimal, amount2: BigDecimal): Boolean {
    return amount1 < amount2
  }

  fun isGreaterThanZero(amount: BigDecimal): Boolean {
    return amount > BigDecimal.ZERO
  }

  fun isLessThanZero(amount: BigDecimal): Boolean {
    return amount < BigDecimal.ZERO
  }

  fun isLessThanEqualToZero(amount: BigDecimal): Boolean {
    return amount <= BigDecimal.ZERO
  }

  fun isEqual(amount1: BigDecimal, amount2: BigDecimal): Boolean {
    return amount1.compareTo(amount2) == 0
  }

  fun isNotEqual(amount1: BigDecimal, amount2: BigDecimal): Boolean {
    return amount1.compareTo(amount2) != 0
  }

  fun subtract(amount1: BigDecimal, amount2: BigDecimal): BigDecimal {
    return amount1.subtract(amount2)
  }
}
