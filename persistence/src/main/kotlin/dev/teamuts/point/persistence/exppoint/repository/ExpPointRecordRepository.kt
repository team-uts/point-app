package dev.teamuts.point.persistence.exppoint.repository

import dev.teamuts.point.persistence.exppoint.entity.ExpPointRecordJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ExpPointRecordRepository : JpaRepository<ExpPointRecordJpaEntity, Long> {}
