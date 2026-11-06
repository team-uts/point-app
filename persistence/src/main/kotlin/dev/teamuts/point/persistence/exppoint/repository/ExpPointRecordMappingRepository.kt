package dev.teamuts.point.persistence.exppoint.repository

import dev.teamuts.point.persistence.exppoint.entity.ExpPointRecordMappingJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ExpPointRecordMappingRepository : JpaRepository<ExpPointRecordMappingJpaEntity, Long> {}
