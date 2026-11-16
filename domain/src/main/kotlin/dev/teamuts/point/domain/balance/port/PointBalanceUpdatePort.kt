package dev.teamuts.point.domain.balance.port

import dev.teamuts.point.domain.balance.model.PointBalance

interface PointBalanceUpdatePort {
  fun updateBalanceAmountAndTransactionId(pointBalance: PointBalance)
}
