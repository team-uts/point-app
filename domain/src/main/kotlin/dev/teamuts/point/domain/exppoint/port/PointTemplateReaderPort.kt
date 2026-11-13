package dev.teamuts.point.domain.exppoint.port

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.exppoint.model.PointTemplate

interface PointTemplateReaderPort {
  fun retrieveByUnique(extTemplateId: String, ownerType: OwnerType): PointTemplate
}
