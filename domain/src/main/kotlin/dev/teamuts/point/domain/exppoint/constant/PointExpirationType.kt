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
  val calculatePeriod: (baseDateTime: ZonedDateTime, period: Int) -> ZonedDateTime,
) {
  FIXED(
    validate = { t -> t.expiresAt != null && t.expiresAt.isAfter(AppTimeUtils.getNowTime()) },
    getExpPeriodOrNull = { _ -> null },
    getExpiresAtOrNull = { t -> t },
    calculatePeriod = { _, _ ->
      throw IllegalStateException("FIXED expiration type does not support period calculation")
    },
  ),
  YEAR(calculatePeriod = { b, p -> b.plusYears(p.toLong()) }),
  MONTH(calculatePeriod = { b, p -> b.plusMonths(p.toLong()) }),
  WEEK(calculatePeriod = { b, p -> b.plusWeeks(p.toLong()) }),
  DAY(calculatePeriod = { b, p -> b.plusDays(p.toLong()) }),
  HOUR(calculatePeriod = { b, p -> b.plusHours(p.toLong()) });

  fun isFixed(): Boolean {
    return this == FIXED
  }
}
