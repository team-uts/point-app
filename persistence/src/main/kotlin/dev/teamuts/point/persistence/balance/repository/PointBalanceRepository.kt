package dev.teamuts.point.persistence.balance.repository

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.persistence.balance.entity.PointBalanceJpaEntity
import dev.teamuts.shared.domain.constant.Currency
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface PointBalanceRepository : JpaRepository<PointBalanceJpaEntity, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  fun findWithLockById(balanceId: Long): PointBalanceJpaEntity?

  fun findByMemberIdAndOwnerTypeAndCurrency(
    memberId: Long,
    ownerType: OwnerType,
    currency: Currency,
  ): PointBalanceJpaEntity?
}
