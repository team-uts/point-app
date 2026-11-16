package dev.teamuts.point.persistence.balance.adapter

import dev.teamuts.point.domain.balance.model.PointBalance
import dev.teamuts.point.domain.balance.port.PointBalanceUpdatePort
import dev.teamuts.point.persistence.balance.entity.PointBalanceJpaEntity
import dev.teamuts.point.persistence.balance.repository.PointBalanceRepository
import dev.teamuts.point.persistence.common.annotation.PersistenceAdapter
import dev.teamuts.shared.data.AppTransactional
import dev.teamuts.shared.data.PersistenceException
import dev.teamuts.shared.data.lock.RequireEntityLock
import org.springframework.data.repository.findByIdOrNull

@PersistenceAdapter
@AppTransactional
class PointBalanceUpdateAdapter(private val pointBalanceRepository: PointBalanceRepository) :
  PointBalanceUpdatePort {

  @RequireEntityLock(entity = PointBalanceJpaEntity::class)
  override fun updateBalanceAmountAndTransactionId(pointBalance: PointBalance) {
    val balanceEntity =
      pointBalanceRepository.findByIdOrNull(pointBalance.id)
        ?: throw PersistenceException.notFound(pointBalance)

    balanceEntity.updateBalanceAmountAndTransactionId(pointBalance)
  }
}
