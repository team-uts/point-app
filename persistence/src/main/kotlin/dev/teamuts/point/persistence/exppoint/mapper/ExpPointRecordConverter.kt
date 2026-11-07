package dev.teamuts.point.persistence.exppoint.mapper

import dev.teamuts.point.domain.exppoint.model.ExpPointRecord
import dev.teamuts.point.persistence.balance.entity.PointBalanceJpaEntity
import dev.teamuts.point.persistence.balance.entity.PointBalanceTransactionJpaEntity
import dev.teamuts.point.persistence.exppoint.entity.ExpPointRecordJpaEntity
import org.springframework.stereotype.Component

@Component
class ExpPointRecordConverter {
  fun covertToJpaEntity(
    model: ExpPointRecord,
    balance: PointBalanceJpaEntity,
    pointTransaction: PointBalanceTransactionJpaEntity,
  ): ExpPointRecordJpaEntity {
    return ExpPointRecordJpaEntity.of(
      model = model,
      balance = balance,
      pointTransaction = pointTransaction,
    )
  }

  fun convertToDomainModel(entity: ExpPointRecordJpaEntity): ExpPointRecord {
    return ExpPointRecord.fromDatabase(
      id = entity.id,
      balanceId = entity.balance.id,
      ownerType = entity.ownerType,
      pointTransactionId = entity.pointTransaction.id,
      totalAmount = entity.totalAmount,
      remainingAmount = entity.remainingAmount,
      currency = entity.currency,
      expiresAt = entity.expiresAt,
      expiredAt = entity.expiredAt,
      status = entity.status,
    )
  }
}
