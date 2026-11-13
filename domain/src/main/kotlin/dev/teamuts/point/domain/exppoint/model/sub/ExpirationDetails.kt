package dev.teamuts.point.domain.exppoint.model.sub

import dev.teamuts.point.domain.exppoint.constant.PointExpirationType
import java.time.ZonedDateTime

interface ExpirationDetails {
  val expirationType: PointExpirationType
  val expirationPeriod: Int?
  val expiresAt: ZonedDateTime?
}
