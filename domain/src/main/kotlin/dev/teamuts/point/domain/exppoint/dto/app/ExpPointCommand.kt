package dev.teamuts.point.domain.exppoint.dto.app

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.shared.domain.model.Money

class ExpPointCommand {
  data class EarnExpPoints(
    val ownerType: OwnerType,
    val pointTemplateId: String,
    val payAmount: Money,
    val paymentType: String,
    val payTransactionId: String,
  )
}
