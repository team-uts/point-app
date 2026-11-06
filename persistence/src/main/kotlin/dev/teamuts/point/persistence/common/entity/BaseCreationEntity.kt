package dev.teamuts.point.persistence.common.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import java.time.ZonedDateTime
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseCreationEntity {
  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  lateinit var createdAt: ZonedDateTime
    protected set

  @CreatedBy
  @Column(name = "created_by", nullable = false, updatable = false)
  lateinit var createdBy: String
    protected set
}
