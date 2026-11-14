package dev.teamuts.point.domain.exppoint.model.sub

import dev.teamuts.point.domain.exppoint.constant.PointIssuanceAmountType
import dev.teamuts.shared.domain.constant.Currency
import java.math.BigDecimal
import java.time.ZonedDateTime

interface IssuanceDetails {
  val issuanceStartsAt: ZonedDateTime

  val issuanceEndsAt: ZonedDateTime

  val issuanceAmountType: PointIssuanceAmountType

  val issuancePercentageValue: BigDecimal?

  val issuanceAmountValue: BigDecimal

  val issuanceCurrency: Currency

  val maxIssuanceAmountValue: BigDecimal
}
