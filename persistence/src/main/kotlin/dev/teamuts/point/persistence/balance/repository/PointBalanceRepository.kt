package dev.teamuts.point.persistence.balance.repository

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.persistence.balance.entity.PointBalanceJpaEntity
import dev.teamuts.shared.domain.constant.Currency
import org.springframework.data.jpa.repository.JpaRepository

interface PointBalanceRepository : JpaRepository<PointBalanceJpaEntity, Long> {
  fun findByMemberIdAndOwnerTypeAndCurrency(
    memberId: Long,
    ownerType: OwnerType,
    currency: Currency,
  ): PointBalanceJpaEntity?
}
