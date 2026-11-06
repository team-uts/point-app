package dev.teamuts.point.persistence.balance.repository

import dev.teamuts.point.persistence.balance.entity.PointBalanceTransactionJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PointBalanceTransactionRepository : JpaRepository<PointBalanceTransactionJpaEntity, Long>
