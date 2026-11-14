package dev.teamuts.point.domain.exppoint.service

import dev.teamuts.point.domain.balance.model.PointBalanceTransaction
import dev.teamuts.point.domain.exppoint.model.ExpPointRecord
import dev.teamuts.point.domain.exppoint.model.PointTemplate
import dev.teamuts.point.domain.exppoint.port.ExpPointRecordStorePort
import org.springframework.stereotype.Service

@Service
class ExpPointRecordService(private val expPointRecordStorePort: ExpPointRecordStorePort) {
  fun createNewPointRecord(
    pointTransaction: PointBalanceTransaction,
    pointTemplate: PointTemplate,
  ): ExpPointRecord {
    val newRecord =
      ExpPointRecord.newRecord(
        pointTransaction = pointTransaction,
        pointTemplate = pointTemplate,
      )

    return expPointRecordStorePort.store(newRecord)
  }
}
