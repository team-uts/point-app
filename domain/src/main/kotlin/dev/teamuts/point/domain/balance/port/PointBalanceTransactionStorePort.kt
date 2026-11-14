package dev.teamuts.point.domain.balance.port

import dev.teamuts.point.domain.balance.model.PointBalanceTransaction

interface PointBalanceTransactionStorePort {
  fun store(pointBalanceTransaction: PointBalanceTransaction): PointBalanceTransaction
}
