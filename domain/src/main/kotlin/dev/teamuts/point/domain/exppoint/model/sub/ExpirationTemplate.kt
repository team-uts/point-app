package dev.teamuts.point.domain.exppoint.model.sub

import dev.teamuts.point.domain.exppoint.constant.PointExpirationType
import dev.teamuts.point.domain.exppoint.error.ExpPointErrorCode
import dev.teamuts.point.domain.exppoint.error.ExpPointException
import java.time.ZonedDateTime

class ExpirationTemplate
private constructor(
  override val expirationType: PointExpirationType,
  override val expirationPeriod: Int?,
  override val expiresAt: ZonedDateTime?,
) : ExpirationDetails {
  init {
    check(expirationType.validate(this)) {
      throw ExpPointException.of(
        errorCode = ExpPointErrorCode.POINT_TEMPLATE_EXPIRATION_INVALID,
        expirationType,
        expirationPeriod,
        expiresAt,
      )
    }
  }

  companion object {
    fun of(
      expirationType: PointExpirationType,
      expirationPeriod: Int?,
      expiresAt: ZonedDateTime?,
    ): ExpirationTemplate {
      return ExpirationTemplate(
        expirationType = expirationType,
        expirationPeriod = expirationType.getExpPeriodOrNull(expirationPeriod),
        expiresAt = expirationType.getExpiresAtOrNull(expiresAt),
      )
    }
  }
}
