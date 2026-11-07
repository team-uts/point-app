package dev.teamuts.point.persistence.exppoint.mapper

import dev.teamuts.point.domain.exppoint.model.PointTemplate
import dev.teamuts.point.persistence.exppoint.entity.PointTemplateJpaEntity
import org.springframework.stereotype.Component

@Component
class PointTemplateConverter {
  fun convertToJpaEntity(model: PointTemplate): PointTemplateJpaEntity {
    return PointTemplateJpaEntity.of(model)
  }

  fun convertToDomainModel(entity: PointTemplateJpaEntity): PointTemplate {
    return PointTemplate.fromDatabase(
      id = entity.id,
      templateName = entity.templateName,
      extTemplateId = entity.extTemplateId,
      ownerType = entity.ownerType,
      active = entity.active,
      issuanceStartsAt = entity.issuanceStartsAt,
      issuanceEndsAt = entity.issuanceEndsAt,
      issuanceAmountType = entity.issuanceAmountType,
      issuanceAmount = entity.issuanceAmount,
      issuancePercentage = entity.issuancePercentage,
      maxIssuanceAmount = entity.maxIssuanceAmount,
      issuanceCurrency = entity.issuanceCurrency,
      expirationType = entity.expirationType,
      expirationPeriod = entity.expirationPeriod,
      expiresAt = entity.expiresAt,
    )
  }
}
