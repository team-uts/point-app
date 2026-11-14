package dev.teamuts.point.domain.common.model

import dev.teamuts.shared.domain.constant.Currency
import dev.teamuts.shared.domain.model.Money
import dev.teamuts.shared.domain.model.Percentage
import java.math.BigDecimal

/** Money with minus allowable (forced to be created with minus allowable) */
class Point private constructor(private val amount: Money) {
  val value: BigDecimal = amount.amount
  val currency: Currency = amount.currency

  companion object {
    fun zero(currency: Currency): Point {
      return Point(Money.of(BigDecimal.ZERO, currency, true))
    }

    fun of(amount: BigDecimal, currency: Currency): Point {
      return Point(Money.of(amount, currency, true))
    }

    fun fromMoney(money: Money): Point {
      if (money.isAllowsNegative) {
        return Point(money)
      }

      return Point(Money.of(money.amount, money.currency, true))
    }
  }

  fun hasSameCurrency(other: Point): Boolean {
    return amount.hasSameCurrency(other.amount)
  }

  fun hasSameCurrency(money: Money): Boolean {
    return amount.hasSameCurrency(money)
  }

  fun hasNotSameCurrency(other: Point): Boolean {
    return amount.hasNotSameCurrency(other.amount)
  }

  fun isGreaterThanZero(): Boolean {
    return amount.isGreaterThanZero
  }

  fun isLessThanOrEqualTo(other: Point): Boolean {
    return amount.isLessThanOrEqualTo(other.amount)
  }

  fun plus(other: Point): Point {
    return Point(amount.plus(other.amount))
  }

  fun minus(other: Point): Point {
    return Point(amount.minus(other.amount))
  }

  fun multiplyByPercentage(percentage: Percentage): Point {
    return Point(amount.multiplyByPercentage(percentage))
  }

  fun copy(): Point {
    return Point(amount.copy())
  }
}
