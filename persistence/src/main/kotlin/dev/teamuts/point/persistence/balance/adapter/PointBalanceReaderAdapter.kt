package dev.teamuts.point.persistence.balance.adapter

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.balance.model.PointBalance
import dev.teamuts.point.domain.balance.port.PointBalanceReaderPort
import dev.teamuts.point.persistence.balance.mapper.PointBalanceConverter
import dev.teamuts.point.persistence.balance.repository.PointBalanceRepository
import dev.teamuts.point.persistence.common.annotation.PersistenceAdapter
import dev.teamuts.shared.data.PersistenceErrorCode
import dev.teamuts.shared.data.PersistenceException
import dev.teamuts.shared.domain.constant.Currency

@PersistenceAdapter
class PointBalanceReaderAdapter(
  private val pointBalanceRepository: PointBalanceRepository,
  private val pointBalanceConverter: PointBalanceConverter,
) : PointBalanceReaderPort {
  override fun retrieveByUnique(
    memberId: Long,
    ownerType: OwnerType,
    currency: Currency,
  ): PointBalance {
    return pointBalanceRepository
      .findByMemberIdAndOwnerTypeAndCurrency(
        memberId = memberId,
        ownerType = ownerType,
        currency = currency,
      )
      ?.let { entity ->
        pointBalanceConverter.convertToDomainModel(entity)
      }
      ?: throw PersistenceException.of(
        PersistenceErrorCode.NOT_FOUND,
        "PointBalance not found for memberId: $memberId, ownerType: $ownerType, currency: $currency",
      )
  }
}
