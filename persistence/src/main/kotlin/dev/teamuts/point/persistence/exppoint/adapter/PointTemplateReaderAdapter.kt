package dev.teamuts.point.persistence.exppoint.adapter

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.exppoint.model.PointTemplate
import dev.teamuts.point.domain.exppoint.port.PointTemplateReaderPort
import dev.teamuts.point.persistence.common.annotation.PersistenceAdapter
import dev.teamuts.point.persistence.exppoint.mapper.PointTemplateConverter
import dev.teamuts.point.persistence.exppoint.repository.PointTemplateRepository
import dev.teamuts.shared.data.PersistenceErrorCode
import dev.teamuts.shared.data.PersistenceException

@PersistenceAdapter
class PointTemplateReaderAdapter(
  private val pointTemplateRepository: PointTemplateRepository,
  private val pointTemplateConverter: PointTemplateConverter,
) : PointTemplateReaderPort {
  override fun retrieveByUnique(
    extTemplateId: String,
    ownerType: OwnerType,
  ): PointTemplate {
    return pointTemplateRepository
      .findByExtTemplateIdAndOwnerType(
        extTemplateId = extTemplateId,
        ownerType = ownerType,
      )
      ?.let { entity ->
        pointTemplateConverter.convertToDomainModel(entity)
      }
      ?: throw PersistenceException.of(
        PersistenceErrorCode.NOT_FOUND,
        "PointTemplate not found for extTemplateId: $extTemplateId, ownerType: $ownerType",
      )
  }
}
