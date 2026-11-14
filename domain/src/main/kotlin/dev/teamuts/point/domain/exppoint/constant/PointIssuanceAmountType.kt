package dev.teamuts.point.domain.exppoint.constant

import dev.teamuts.point.domain.common.model.Point
import dev.teamuts.point.domain.exppoint.model.sub.IssuanceDetails
import dev.teamuts.shared.domain.constant.Currency
import dev.teamuts.shared.domain.model.Percentage
import dev.teamuts.shared.utils.BigDecimalUtils
import java.math.BigDecimal

enum class PointIssuanceAmountType(
  val validate: (template: IssuanceDetails) -> Boolean,
  val getAmountOrZero: (amount: BigDecimal, currency: Currency) -> Point,
  val getPercentageOrNull: (percent: BigDecimal?) -> Percentage?,
) {
  FIXED(
    // issuanceAmount > 0 && issuanceAmount < maxIssuanceAmount
    validate = { t ->
      BigDecimalUtils.isGreaterThanZero(t.issuanceAmountValue) &&
        BigDecimalUtils.isLessThan(t.issuanceAmountValue, t.maxIssuanceAmountValue)
    },
    getAmountOrZero = { a, c -> Point.of(a, c) },
    getPercentageOrNull = { _ -> null },
  ),
  PERCENTAGE(
    validate = { t ->
      // issuancePercentage > 0 && issuancePercentage < 100 (and != null)
      t.issuancePercentageValue != null &&
        BigDecimalUtils.isGreaterThanZero(t.issuancePercentageValue) &&
        BigDecimalUtils.isLessThan(
          t.issuancePercentageValue,
          Percentage.ONE_HUNDRED.value,
        )
    },
    getAmountOrZero = { _, c -> Point.zero(c) },
    getPercentageOrNull = { p -> p?.let { Percentage.of(it) } },
  ),
}
