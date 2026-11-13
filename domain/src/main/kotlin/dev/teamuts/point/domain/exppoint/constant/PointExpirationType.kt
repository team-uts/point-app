package dev.teamuts.point.domain.exppoint.constant

import dev.teamuts.point.domain.exppoint.model.sub.ExpirationTemplate
import dev.teamuts.shared.utils.AppTimeUtils
import java.time.ZonedDateTime

enum class PointExpirationType(
  val validate: (ExpirationTemplate) -> Boolean = { t ->
    t.expirationPeriod != null && t.expirationPeriod > 0
  },
  val getExpPeriodOrNull: (Int?) -> Int? = { t -> t },
  val getExpiresAtOrNull: (ZonedDateTime?) -> ZonedDateTime? = { _ -> null },
) {
  FIXED(
    validate = { t -> t.expiresAt != null && t.expiresAt.isAfter(AppTimeUtils.getNowTime()) },
    getExpPeriodOrNull = { _ -> null },
    getExpiresAtOrNull = { t -> t },
  ),
  YEAR,
  MONTH,
  DAY,
  HOUR,
}
