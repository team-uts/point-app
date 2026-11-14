package dev.teamuts.point.persistence.exppoint.adapter

import dev.teamuts.point.domain.balance.model.PointBalance
import dev.teamuts.point.domain.balance.model.PointBalanceTransaction
import dev.teamuts.point.domain.exppoint.model.ExpPointRecord
import dev.teamuts.point.domain.exppoint.port.ExpPointRecordStorePort
import dev.teamuts.point.persistence.balance.repository.PointBalanceRepository
import dev.teamuts.point.persistence.balance.repository.PointBalanceTransactionRepository
import dev.teamuts.point.persistence.common.annotation.PersistenceAdapter
import dev.teamuts.point.persistence.exppoint.mapper.ExpPointRecordConverter
import dev.teamuts.point.persistence.exppoint.repository.ExpPointRecordRepository
import dev.teamuts.shared.data.PersistenceException
import org.springframework.data.repository.findByIdOrNull

@PersistenceAdapter
class ExpPointRecordStoreAdapter(
  private val expPointRecordRepository: ExpPointRecordRepository,
  private val pointBalanceRepository: PointBalanceRepository,
  private val pointBalanceTransactionRepository: PointBalanceTransactionRepository,
  private val expPointRecordConverter: ExpPointRecordConverter,
) : ExpPointRecordStorePort {
  override fun store(expPointRecord: ExpPointRecord): ExpPointRecord {
    val balanceEntity =
      pointBalanceRepository.findByIdOrNull(expPointRecord.balanceId)
        ?: throw PersistenceException.notFound(
          PointBalance::class.java,
          expPointRecord.balanceId,
        )

    val pointTransactionEntity =
      pointBalanceTransactionRepository.findByIdOrNull(expPointRecord.pointTransactionId)
        ?: throw PersistenceException.notFound(
          PointBalanceTransaction::class.java,
          expPointRecord.pointTransactionId,
        )

    val newRecordEntity =
      expPointRecordConverter.covertToJpaEntity(
        model = expPointRecord,
        balance = balanceEntity,
        pointTransaction = pointTransactionEntity,
      )

    return expPointRecordRepository.save(newRecordEntity).let {
      expPointRecordConverter.convertToDomainModel(it)
    }
  }
}
