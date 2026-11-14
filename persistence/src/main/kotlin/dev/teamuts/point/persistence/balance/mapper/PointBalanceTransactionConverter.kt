package dev.teamuts.point.persistence.balance.mapper

import dev.teamuts.point.domain.balance.model.PointBalanceTransaction
import dev.teamuts.point.persistence.balance.entity.PointBalanceJpaEntity
import dev.teamuts.point.persistence.balance.entity.PointBalanceTransactionJpaEntity
import org.springframework.stereotype.Component

@Component
class PointBalanceTransactionConverter {
  fun convertToJpaEntity(
    model: PointBalanceTransaction,
    balance: PointBalanceJpaEntity,
  ): PointBalanceTransactionJpaEntity {
    return PointBalanceTransactionJpaEntity.of(model, balance)
  }

  fun convertToDomainModel(entity: PointBalanceTransactionJpaEntity): PointBalanceTransaction {
    return PointBalanceTransaction.fromDatabase(
      id = entity.id,
      balanceId = entity.balance.id,
      ownerType = entity.ownerType,
      transactionType = entity.transactionType,
      operationType = entity.operationType,
      cashAmount = entity.cashAmount,
      expAmount = entity.expAmount,
      afterBalanceAmount = entity.afterBalanceAmount,
      currency = entity.currency,
      paymentType = entity.paymentType,
      payTransactionId = entity.payTransactionId,
    )
  }
}
