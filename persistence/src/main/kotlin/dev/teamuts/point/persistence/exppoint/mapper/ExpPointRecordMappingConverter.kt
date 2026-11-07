package dev.teamuts.point.persistence.exppoint.mapper

import dev.teamuts.point.domain.exppoint.model.ExpPointRecordMapping
import dev.teamuts.point.persistence.balance.entity.PointBalanceTransactionJpaEntity
import dev.teamuts.point.persistence.exppoint.entity.ExpPointRecordJpaEntity
import dev.teamuts.point.persistence.exppoint.entity.ExpPointRecordMappingJpaEntity
import org.springframework.stereotype.Component

@Component
class ExpPointRecordMappingConverter {
  fun convertToJpaEntity(
    model: ExpPointRecordMapping,
    pointTransaction: PointBalanceTransactionJpaEntity,
    expPointRecord: ExpPointRecordJpaEntity,
  ): ExpPointRecordMappingJpaEntity {
    return ExpPointRecordMappingJpaEntity.of(
      model = model,
      pointTransaction = pointTransaction,
      expPointRecord = expPointRecord,
    )
  }

  fun convertToDomainModel(entity: ExpPointRecordMappingJpaEntity): ExpPointRecordMapping {
    return ExpPointRecordMapping.fromDatabase(
      id = entity.id,
      pointTransactionId = entity.pointTransaction.id,
      expPointRecordId = entity.expPointRecord.id,
      amount = entity.amount,
      currency = entity.currency,
    )
  }
}
