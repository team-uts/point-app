package dev.teamuts.point.domain.exppoint.constant

import dev.teamuts.point.domain.exppoint.model.sub.IssuanceDetails
import dev.teamuts.shared.domain.constant.Currency
import dev.teamuts.shared.domain.model.Money
import dev.teamuts.shared.domain.model.Percentage
import dev.teamuts.shared.utils.BigDecimalUtils
import java.math.BigDecimal

enum class PointIssuanceAmountType(
  val validate: (IssuanceDetails) -> Boolean,
  val getAmountOrZero: (amount: BigDecimal, currency: Currency) -> Money,
  val getPercentageOrNull: (percent: BigDecimal?) -> Percentage?,
) {
  FIXED(
    // issuanceAmount > 0 && issuanceAmount < maxIssuanceAmount
    validate = { t ->
      BigDecimalUtils.isGreaterThanZero(t.getIssuanceAmount()) &&
        BigDecimalUtils.isLessThan(t.getIssuanceAmount(), t.getMaxIssuanceAmount())
    },
    getAmountOrZero = { a, c -> Money.of(a, c) },
    getPercentageOrNull = { _ -> null },
  ),
  PERCENTAGE(
    validate = { t ->
      // issuancePercentage > 0 && issuancePercentage < 100 (and != null)
      t.getIssuancePercentage() != null &&
        BigDecimalUtils.isGreaterThanZero(t.getIssuancePercentage()) &&
        BigDecimalUtils.isLessThan(
          t.getIssuancePercentage(),
          Percentage.HUNDRED.value,
        )
    },
    getAmountOrZero = { _, c -> Money.zero(c) },
    getPercentageOrNull = { p -> p?.let { Percentage.of(it) } },
  ),
}
