package dev.teamuts.point.persistence.balance.repository

import dev.teamuts.point.persistence.balance.entity.PointBalanceJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PointBalanceRepository : JpaRepository<PointBalanceJpaEntity, Long> {}
