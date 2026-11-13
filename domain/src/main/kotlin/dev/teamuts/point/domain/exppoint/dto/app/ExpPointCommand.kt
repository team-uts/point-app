package dev.teamuts.point.domain.exppoint.dto.app

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.shared.domain.constant.Currency
import java.math.BigDecimal

class ExpPointCommand {
  data class GrantExpPoints(
    val ownerType: OwnerType,
    val pointTemplateId: String,
    val payAmount: BigDecimal,
    val currency: Currency,
    val serviceType: String,
    val paymentType: String,
    val payTransactionId: String,
  )
}
