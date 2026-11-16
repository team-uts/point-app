package dev.teamuts.point.domain.balance.model.sub

import dev.teamuts.point.domain.common.model.Point
import dev.teamuts.shared.domain.constant.Currency
import java.math.BigDecimal

/** Immutable class representing the amount of points. */
class PointBalanceAmount
private constructor(
  override val cashAmount: Point,
  override val expAmount: Point,
) : PointBalanceAmountDetails {
  override val currency: Currency = cashAmount.currency

  companion object {
    fun zero(currency: Currency): PointBalanceAmount {
      return PointBalanceAmount(
        cashAmount = Point.zero(currency),
        expAmount = Point.zero(currency),
      )
    }

    fun of(
      cashAmount: BigDecimal,
      expAmount: BigDecimal,
      currency: Currency,
    ): PointBalanceAmount {
      return PointBalanceAmount(
        cashAmount = Point.of(cashAmount, currency),
        expAmount = Point.of(expAmount, currency),
      )
    }
  }

  fun credit(amount: PointBalanceAmount): PointBalanceAmount {
    return PointBalanceAmount(
      cashAmount = this.cashAmount.plus(amount.cashAmount),
      expAmount = this.expAmount.plus(amount.expAmount),
    )
  }

  fun debit(amount: PointBalanceAmount): PointBalanceAmount {
    return PointBalanceAmount(
      cashAmount = this.cashAmount.minus(amount.cashAmount),
      expAmount = this.expAmount.minus(amount.expAmount),
    )
  }

  fun getTotalPoint(): Point {
    return cashAmount.plus(expAmount)
  }

  /** Overrides PointBalanceAmount for comparison based on cashAmount and expAmount values. */
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is PointBalanceAmount) return false

    if (cashAmount != other.cashAmount) return false
    if (expAmount != other.expAmount) return false

    return true
  }

  override fun hashCode(): Int {
    var result = cashAmount.hashCode()
    result = 31 * result + expAmount.hashCode()
    return result
  }

  override fun toString(): String {
    return "PointBalanceAmount(cashAmount=$cashAmount, expAmount=$expAmount)"
  }
}
