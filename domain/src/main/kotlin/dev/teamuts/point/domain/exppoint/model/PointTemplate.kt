package dev.teamuts.point.domain.exppoint.model

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.common.model.Point
import dev.teamuts.point.domain.exppoint.constant.PointExpirationType
import dev.teamuts.point.domain.exppoint.constant.PointIssuanceAmountType
import dev.teamuts.point.domain.exppoint.error.ExpPointErrorCode
import dev.teamuts.point.domain.exppoint.error.ExpPointException
import dev.teamuts.point.domain.exppoint.error.ExpPointIssuanceException
import dev.teamuts.point.domain.exppoint.model.sub.ExpirationDetails
import dev.teamuts.point.domain.exppoint.model.sub.ExpirationTemplate
import dev.teamuts.point.domain.exppoint.model.sub.IssuanceDetails
import dev.teamuts.point.domain.exppoint.model.sub.IssuanceTemplate
import dev.teamuts.shared.domain.annotation.DomainModel
import dev.teamuts.shared.domain.constant.Currency
import dev.teamuts.shared.domain.model.BaseIdentifiableModel
import dev.teamuts.shared.domain.model.Money
import java.math.BigDecimal
import java.time.ZonedDateTime

@DomainModel
class PointTemplate
private constructor(
  id: Long,
  templateName: String,
  active: Boolean,
  val extTemplateId: String,
  val ownerType: OwnerType,
  private val issuanceTemplate: IssuanceTemplate,
  private val expirationTemplate: ExpirationTemplate,
) :
  BaseIdentifiableModel<Long>(id),
  IssuanceDetails by issuanceTemplate,
  ExpirationDetails by expirationTemplate {

  var templateName: String = templateName
    private set

  var active: Boolean = active
    private set

  companion object {
    fun fromDatabase(
      id: Long,
      templateName: String,
      extTemplateId: String,
      ownerType: OwnerType,
      active: Boolean,
      issuanceStartsAt: ZonedDateTime,
      issuanceEndsAt: ZonedDateTime,
      issuanceAmountType: PointIssuanceAmountType,
      issuanceAmount: BigDecimal,
      issuancePercentage: BigDecimal?,
      maxIssuanceAmount: BigDecimal,
      issuanceCurrency: Currency,
      expirationType: PointExpirationType,
      expirationPeriod: Int?,
      expiresAt: ZonedDateTime?,
    ): PointTemplate {
      return PointTemplate(
        id = id,
        templateName = templateName,
        extTemplateId = extTemplateId,
        ownerType = ownerType,
        active = active,
        issuanceTemplate =
          IssuanceTemplate.of(
            issuanceStartsAt = issuanceStartsAt,
            issuanceEndsAt = issuanceEndsAt,
            issuanceAmountType = issuanceAmountType,
            issuanceAmount = issuanceAmount,
            issuancePercentage = issuancePercentage,
            maxIssuanceAmount = maxIssuanceAmount,
            issuanceCurrency = issuanceCurrency,
          ),
        expirationTemplate =
          ExpirationTemplate.of(
            expirationType = expirationType,
            expirationPeriod = expirationPeriod,
            expiresAt = expiresAt,
          ),
      )
    }
  }

  fun calculateIssuancePoint(baseAmount: Money): Point {
    checkValidStatus()

    return try {
      issuanceTemplate.calculatePoint(baseAmount)
    } catch (_: ExpPointIssuanceException) {
      throw ExpPointException.of(
        errorCode = ExpPointErrorCode.POINT_ISSUANCE_FAILED,
        templateName,
        ownerType,
        baseAmount,
      )
    }
  }

  fun calculateExpirationDate(baseDateTime: ZonedDateTime): ZonedDateTime {
    checkValidStatus()

    return expirationTemplate.calculateExpirationDate(baseDateTime)
  }

  private fun checkValidStatus() {
    checkPersisted()

    check(active) {
      throw ExpPointException.of(
        errorCode = ExpPointErrorCode.POINT_TEMPLATE_INACTIVE,
        templateName,
        ownerType,
      )
    }
  }
}
