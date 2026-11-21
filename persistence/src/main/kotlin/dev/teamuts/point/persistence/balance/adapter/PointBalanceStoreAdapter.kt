package dev.teamuts.point.persistence.balance.adapter

import dev.teamuts.point.domain.balance.model.PointBalance
import dev.teamuts.point.domain.balance.port.PointBalanceStorePort
import dev.teamuts.point.persistence.balance.mapper.PointBalanceConverter
import dev.teamuts.point.persistence.balance.repository.PointBalanceRepository
import dev.teamuts.point.persistence.common.annotation.PersistenceAdapter
import dev.teamuts.shared.data.annotation.AppTransactional

@PersistenceAdapter
@AppTransactional
class PointBalanceStoreAdapter(
  private val pointBalanceRepository: PointBalanceRepository,
  private val pointBalanceConverter: PointBalanceConverter,
) : PointBalanceStorePort {
  override fun store(pointBalance: PointBalance): PointBalance {
    val entity = pointBalanceConverter.convertToJpaEntity(pointBalance)

    val savedEntity = pointBalanceRepository.save(entity)

    return pointBalanceConverter.convertToDomainModel(savedEntity)
  }
}
