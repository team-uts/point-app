package dev.teamuts.point.persistence.common.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import java.time.ZonedDateTime
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseAuditableEntity : BaseCreationEntity() {
  @LastModifiedDate
  @Column(name = "updated_at")
  lateinit var updatedAt: ZonedDateTime
    protected set

  @LastModifiedBy
  @Column(name = "updated_by")
  lateinit var updatedBy: String
    protected set
}
