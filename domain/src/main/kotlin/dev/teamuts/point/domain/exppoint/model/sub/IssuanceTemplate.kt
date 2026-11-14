package dev.teamuts.point.domain.exppoint.model.sub

import dev.teamuts.point.domain.common.model.Point
import dev.teamuts.point.domain.exppoint.constant.PointIssuanceAmountType
import dev.teamuts.point.domain.exppoint.error.ExpPointErrorCode
import dev.teamuts.point.domain.exppoint.error.ExpPointException
import dev.teamuts.point.domain.exppoint.error.ExpPointIssuanceException
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
  private val issuancePercentage: Percentage?,
  private val issuanceAmount: Point,
  private val maxIssuanceAmount: Point,
) : IssuanceDetails {
  override val issuancePercentageValue: BigDecimal? = issuancePercentage?.value
  override val issuanceAmountValue: BigDecimal = issuanceAmount.value
  override val issuanceCurrency: Currency = issuanceAmount.currency
  override val maxIssuanceAmountValue: BigDecimal = maxIssuanceAmount.value

  init {
    // validate instance
    check(
      issuanceEndsAt.isAfter(AppTimeUtils.getNowTime()) &&
        issuanceStartsAt.isBefore(issuanceEndsAt) &&
        issuanceAmountType.validate(this) &&
        this.maxIssuanceAmount.isGreaterThanZero()
    ) {
      throw ExpPointException.of(
        errorCode = ExpPointErrorCode.POINT_TEMPLATE_ISSUANCE_INVALID,
        issuanceStartsAt,
        issuanceEndsAt,
        issuanceAmountType,
        this.issuanceAmount.value,
        this.issuancePercentage?.value,
        this.maxIssuanceAmount.value,
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
        issuanceAmount = issuanceAmountType.getAmountOrZero(issuanceAmount, issuanceCurrency),
        issuancePercentage = issuanceAmountType.getPercentageOrNull(issuancePercentage),
        maxIssuanceAmount = Point.of(maxIssuanceAmount, issuanceCurrency),
      )
    }
  }

  /**
   * Calculates the issuance point amount based on the baseAmount and the issuance template
   * configuration.
   */
  fun calculatePoint(baseAmount: Money): Point {
    // validate baseAmount and currency consistency
    check(
      baseAmount.isGreaterThanZero &&
        issuanceAmount.hasSameCurrency(baseAmount) &&
        maxIssuanceAmount.hasSameCurrency(baseAmount)
    ) {
      throw ExpPointIssuanceException()
    }

    // calculate issuance amount based on issuanceAmountType
    return when (issuanceAmountType) {
      PointIssuanceAmountType.FIXED -> issuanceAmount.copy()
      PointIssuanceAmountType.PERCENTAGE -> {
        Point.fromMoney(
          baseAmount.multiplyByPercentage(
            requireNotNull(issuancePercentage) {
              throw ExpPointIssuanceException()
            }
          )
        )
      }
    }.also { result ->
      // check the maximum issuance amount constraint
      check(result.isLessThanOrEqualTo(maxIssuanceAmount)) {
        throw ExpPointIssuanceException()
      }
    }
  }
}
