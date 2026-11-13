package dev.teamuts.point.domain.exppoint.constant

import dev.teamuts.point.domain.exppoint.model.sub.IssuanceDetails
import dev.teamuts.shared.utils.BigDecimalUtils
import java.math.BigDecimal

enum class PointIssuanceAmountType(val validate: (IssuanceDetails) -> Boolean) {
  FIXED({ t -> BigDecimalUtils.isGreaterThanZero(t.getIssuanceAmount()) }),
  PERCENTAGE({ t ->
    t.getIssuancePercentage() != null &&
      BigDecimalUtils.isGreaterThanZero(t.getIssuancePercentage()) &&
      BigDecimalUtils.isLessThan(
        t.getIssuancePercentage(),
        BigDecimal("100.00"),
      )
  }),
}
