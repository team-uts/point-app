package dev.teamuts.point.domain.balance.model.sub

import dev.teamuts.point.domain.common.model.Point
import dev.teamuts.shared.domain.constant.Currency
import java.math.BigDecimal

class PointBalanceAmount
private constructor(
  private val cashAmount: Point,
  private val expAmount: Point,
) : PointBalanceAmountDetails {
  override val cashAmountValue: BigDecimal = cashAmount.value
  override val expAmountValue: BigDecimal = expAmount.value
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
}
