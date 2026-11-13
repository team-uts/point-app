package dev.teamuts.point.domain.exppoint.model.sub

import dev.teamuts.point.domain.exppoint.constant.PointIssuanceAmountType
import dev.teamuts.point.domain.exppoint.error.ExpPointErrorCode
import dev.teamuts.point.domain.exppoint.error.ExpPointException
import dev.teamuts.shared.domain.constant.Currency
import dev.teamuts.shared.domain.model.Money
import dev.teamuts.shared.domain.model.Percentage
import dev.teamuts.shared.utils.AppTimeUtils
import java.math.BigDecimal
import java.time.ZonedDateTime

class IssuanceTemplate
private constructor(
  override val issuanceStartsAt: ZonedDateTime,
  override val issuanceEndsAt: ZonedDateTime,
  override val issuanceAmountType: PointIssuanceAmountType,
  issuanceAmount: BigDecimal,
  issuancePercentage: BigDecimal?,
  maxIssuanceAmount: BigDecimal,
  issuanceCurrency: Currency,
) : IssuanceDetails {
  private val issuanceAmount: Money =
    issuanceAmountType.getAmountOrZero(issuanceAmount, issuanceCurrency)

  private val issuancePercentage: Percentage? =
    issuanceAmountType.getPercentageOrNull(issuancePercentage)

  private val maxIssuanceAmount: Money = Money.of(maxIssuanceAmount, issuanceCurrency)

  init {
    check(
      issuanceEndsAt.isAfter(AppTimeUtils.getNowTime()) &&
        issuanceStartsAt.isBefore(issuanceEndsAt) &&
        issuanceAmountType.validate(this) &&
        this.maxIssuanceAmount.isGreaterThanZero
    ) {
      throw ExpPointException.of(
        errorCode = ExpPointErrorCode.POINT_TEMPLATE_ISSUANCE_INVALID,
        issuanceStartsAt,
        issuanceEndsAt,
        issuanceAmountType,
        this.issuanceAmount.amount,
        this.issuancePercentage?.value,
        this.maxIssuanceAmount.amount,
      )
    }
  }

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
        issuanceAmount = issuanceAmount,
        issuancePercentage = issuancePercentage,
        maxIssuanceAmount = maxIssuanceAmount,
        issuanceCurrency = issuanceCurrency,
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
