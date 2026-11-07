package dev.teamuts.point.persistence.balance.mapper

import dev.teamuts.point.domain.balance.model.PointBalance
import dev.teamuts.point.persistence.balance.entity.PointBalanceJpaEntity
import org.springframework.stereotype.Component

@Component
class PointBalanceConverter {
  fun convertToJpaEntity(model: PointBalance): PointBalanceJpaEntity {
    return PointBalanceJpaEntity.of(model)
  }

  fun convertToDomainModel(entity: PointBalanceJpaEntity): PointBalance {
    return PointBalance.fromDatabase(
      id = entity.id,
      memberId = entity.memberId,
      ownerType = entity.ownerType,
      amount = entity.amount,
      currency = entity.currency,
      status = entity.status,
    )
  }
}
