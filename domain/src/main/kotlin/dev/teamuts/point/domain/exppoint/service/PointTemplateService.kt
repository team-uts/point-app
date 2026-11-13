package dev.teamuts.point.domain.exppoint.service

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.exppoint.model.PointTemplate
import dev.teamuts.point.domain.exppoint.port.PointTemplateReaderPort
import org.springframework.stereotype.Service

@Service
class PointTemplateService(private val pointTemplateReaderPort: PointTemplateReaderPort) {
  fun getPointTemplate(extTemplateId: String, ownerType: OwnerType): PointTemplate {
    return pointTemplateReaderPort.retrieveByUnique(
      extTemplateId = extTemplateId,
      ownerType = ownerType,
    )
  }
}
