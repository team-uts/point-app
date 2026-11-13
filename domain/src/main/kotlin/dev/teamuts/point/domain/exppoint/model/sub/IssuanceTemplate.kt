package dev.teamuts.point.domain.exppoint.model.sub

import dev.teamuts.point.domain.exppoint.constant.PointIssuanceAmountType
import dev.teamuts.point.domain.exppoint.error.ExpPointErrorCode
import dev.teamuts.point.domain.exppoint.error.ExpPointException
import dev.teamuts.shared.domain.constant.Currency
import dev.teamuts.shared.domain.model.Money
import dev.teamuts.shared.domain.model.Percentage
import java.math.BigDecimal
import java.time.ZonedDateTime

class IssuanceTemplate
private constructor(
  override val issuanceStartsAt: ZonedDateTime,
  override val issuanceEndsAt: ZonedDateTime,
  override val issuanceAmountType: PointIssuanceAmountType,
  private val issuanceAmount: Money,
  private val issuancePercentage: Percentage?,
  private val maxIssuanceAmount: Money,
) : IssuanceDetails {
  companion object {
    fun of(
      issuanceStartsAt: ZonedDateTime,
      issuanceEndsAt: ZonedDateTime,
      issuanceAmountType: PointIssuanceAmountType,
      issuanceAmount: BigDecimal,
      issuancePercentage: BigDecimal?,
      maxIssuanceAmount: BigDecimal,
      issuanceCurrency: Currency,
    ): IssuanceTemplate {
      return IssuanceTemplate(
        issuanceStartsAt = issuanceStartsAt,
        issuanceEndsAt = issuanceEndsAt,
        issuanceAmountType = issuanceAmountType,
        issuanceAmount = Money.of(issuanceAmount, issuanceCurrency),
        issuancePercentage = issuancePercentage?.let { Percentage.of(it) },
        maxIssuanceAmount = Money.of(maxIssuanceAmount, issuanceCurrency),
      )
    }
  }

  override fun getIssuancePercentage(): BigDecimal? {
    return issuancePercentage?.value
  }

  override fun getIssuanceAmount(): BigDecimal {
    return issuanceAmount.amount
  }

  override fun getMaxIssuanceAmount(): BigDecimal {
    return maxIssuanceAmount.amount
  }

  override fun getIssuanceCurrency(): Currency {
    check(issuanceAmount.hasNotSameCurrency(maxIssuanceAmount)) {
      throw ExpPointException.of(
        errorCode = ExpPointErrorCode.POINT_TEMPLATE_ISSUANCE_CURRENCY_NOT_CONSISTENT,
        issuanceAmount.currency,
        maxIssuanceAmount.currency,
      )
    }

    return issuanceAmount.currency
  }
}
