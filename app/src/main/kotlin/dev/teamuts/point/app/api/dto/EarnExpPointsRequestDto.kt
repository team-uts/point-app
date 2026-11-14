package dev.teamuts.point.app.api.dto

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.shared.domain.constant.Currency
import java.math.BigDecimal

data class EarnExpPointsRequestDto(
  val ownerType: OwnerType,
  val pointTemplateId: String,
  val payAmount: BigDecimal,
  val currency: Currency,
  val paymentType: String,
  val payTransactionId: String,
)
