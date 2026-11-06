package dev.teamuts.point.persistence.exppoint.repository

import dev.teamuts.point.persistence.exppoint.entity.PointTemplateJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PointTemplateRepository : JpaRepository<PointTemplateJpaEntity, Long> {}
