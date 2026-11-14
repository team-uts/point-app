package dev.teamuts.point.persistence.balance.adapter

import dev.teamuts.point.domain.balance.model.PointBalance
import dev.teamuts.point.domain.balance.model.PointBalanceTransaction
import dev.teamuts.point.domain.balance.port.PointBalanceTransactionStorePort
import dev.teamuts.point.persistence.balance.mapper.PointBalanceTransactionConverter
import dev.teamuts.point.persistence.balance.repository.PointBalanceRepository
import dev.teamuts.point.persistence.balance.repository.PointBalanceTransactionRepository
import dev.teamuts.point.persistence.common.annotation.PersistenceAdapter
import dev.teamuts.shared.data.AppTransactional
import dev.teamuts.shared.data.PersistenceException
import org.springframework.data.repository.findByIdOrNull

@PersistenceAdapter
@AppTransactional
class PointBalanceTransactionStoreAdapter(
  private val pointBalanceTransactionRepository: PointBalanceTransactionRepository,
  private val pointBalanceRepository: PointBalanceRepository,
  private val pointBalanceTransactionConverter: PointBalanceTransactionConverter,
) : PointBalanceTransactionStorePort {
  override fun store(pointBalanceTransaction: PointBalanceTransaction): PointBalanceTransaction {
    val pointBalanceEntity =
      pointBalanceRepository.findByIdOrNull(pointBalanceTransaction.balanceId)
        ?: throw PersistenceException.notFound(
          PointBalance::class.java,
          pointBalanceTransaction.balanceId,
        )

    val entity =
      pointBalanceTransactionConverter.convertToJpaEntity(
        model = pointBalanceTransaction,
        balance = pointBalanceEntity,
      )

    return pointBalanceTransactionRepository.save(entity).let {
      pointBalanceTransactionConverter.convertToDomainModel(it)
    }
  }
}
