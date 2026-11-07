package dev.teamuts.point.domain.exppoint.model

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.common.annotation.DomainModel
import dev.teamuts.point.domain.common.constant.Currency
import dev.teamuts.point.domain.common.model.BaseIdentifiableModel
import dev.teamuts.point.domain.common.model.Money
import dev.teamuts.point.domain.exppoint.constant.PointExpirationType
import dev.teamuts.point.domain.exppoint.constant.PointIssuanceAmountType
import java.math.BigDecimal
import java.time.ZonedDateTime

@DomainModel
class PointTemplate(
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
) : BaseIdentifiableModel<Long>(id) {
  var templateName: String = templateName
    private set

  val extTemplateId: String = extTemplateId

  val ownerType: OwnerType = ownerType

  var active: Boolean = active
    private set

  val issuanceStartsAt: ZonedDateTime = issuanceStartsAt

  val issuanceEndsAt: ZonedDateTime = issuanceEndsAt

  val issuanceAmountType: PointIssuanceAmountType = issuanceAmountType

  val issuanceAmount: Money = Money.of(issuanceAmount, issuanceCurrency)

  val issuancePercentage: BigDecimal? = issuancePercentage

  val maxIssuanceAmount: Money = Money.of(maxIssuanceAmount, issuanceCurrency)

  val expirationType: PointExpirationType = expirationType

  val expirationPeriod: Int? = expirationPeriod

  val expiresAt: ZonedDateTime? = expiresAt

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
        issuanceStartsAt = issuanceStartsAt,
        issuanceEndsAt = issuanceEndsAt,
        issuanceAmountType = issuanceAmountType,
        issuanceAmount = issuanceAmount,
        issuancePercentage = issuancePercentage,
        maxIssuanceAmount = maxIssuanceAmount,
        issuanceCurrency = issuanceCurrency,
        expirationType = expirationType,
        expirationPeriod = expirationPeriod,
        expiresAt = expiresAt,
      )
    }
  }
}
