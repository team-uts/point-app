package dev.teamuts.point.domain.common.model

import dev.teamuts.point.domain.common.constant.Currency
import dev.teamuts.point.shared.utils.MoneyCalculator
import java.math.BigDecimal
import java.math.RoundingMode

class Money(
  val amount: BigDecimal,
  val currency: Currency,
) {

  companion object {
    fun zero(currency: Currency): Money {
      return of(BigDecimal.ZERO, currency)
    }

    fun of(amount: BigDecimal, currency: Currency): Money {
      if (MoneyCalculator.isLessThanZero(amount)) {
        throw RuntimeException("amount must be greater than or equal to zero")
      }

      return Money(amount.setScale(currency.scale, RoundingMode.UNNECESSARY), currency)
    }
  }

  private fun checkIfSameCurrency(other: Money) {
    if (this.currency != other.currency) {
      throw RuntimeException(
        "currency must be same. this.currency=${this.currency}, other.currency=${other.currency}"
      )
    }
  }

  fun isEqual(other: Money): Boolean {
    checkIfSameCurrency(other)

    return MoneyCalculator.isEqual(this.amount, other.amount)
  }

  fun isGreaterThanZero(): Boolean {
    return MoneyCalculator.isGreaterThan(
      this.amount,
      BigDecimal.ZERO.setScale(currency.scale, RoundingMode.UNNECESSARY),
    )
  }

  fun isZero(): Boolean {
    return MoneyCalculator.isEqual(
      this.amount,
      BigDecimal.ZERO.setScale(currency.scale, RoundingMode.UNNECESSARY),
    )
  }

  fun isLessThan(other: Money): Boolean {
    checkIfSameCurrency(other)

    return MoneyCalculator.isLessThan(this.amount, other.amount)
  }

  fun minus(other: Money): Money {
    checkIfSameCurrency(other)

    return of(MoneyCalculator.subtract(this.amount, other.amount), this.currency)
  }

  fun copy(): Money {
    return of(this.amount, this.currency)
  }

  fun hasSameCurrency(other: Money): Boolean {
    return this.currency == other.currency
  }

  fun hasNotSameCurrency(other: Money): Boolean {
    return !hasSameCurrency(other)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Money) return false

    if (amount != other.amount) return false
    if (currency != other.currency) return false

    return true
  }

  override fun hashCode(): Int {
    return 31 * amount.hashCode() + currency.hashCode()
  }
}
