package dev.teamuts.point.domain.balance.model.sub

import dev.teamuts.shared.domain.constant.Currency
import java.math.BigDecimal

interface PointBalanceAmountDetails {
  val cashAmountValue: BigDecimal

  val expAmountValue: BigDecimal

  val currency: Currency
}
