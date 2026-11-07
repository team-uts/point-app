package dev.teamuts.point.persistence.exppoint.entity

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.common.constant.Currency
import dev.teamuts.point.domain.exppoint.constant.PointExpirationType
import dev.teamuts.point.domain.exppoint.constant.PointIssuanceAmountType
import dev.teamuts.point.domain.exppoint.model.PointTemplate
import dev.teamuts.point.persistence.common.entity.BaseAuditableEntity
import dev.teamuts.point.persistence.common.entity.EntityMetadata.Companion.PREFIX_UNIQUE
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.ZonedDateTime

private const val TABLE_ABBR = "pt"

@Entity
@Table(
  catalog = "point",
  name = "point_templates",
  uniqueConstraints =
    [
      UniqueConstraint(
        name = "${PREFIX_UNIQUE}_${TABLE_ABBR}_exttemplateid_ownertype",
        columnNames = ["ext_template_id", "owner_type"],
      )
    ],
)
class PointTemplateJpaEntity(
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
) : BaseAuditableEntity() {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  var id: Long = 0L
    protected set

  @Column(
    name = "template_name",
    nullable = false,
    length = 50,
    comment = "external point template name",
  )
  var templateName: String = templateName
    protected set

  @Column(
    name = "ext_template_id",
    nullable = false,
    length = 50,
    comment = "external point template ID",
  )
  var extTemplateId: String = extTemplateId
    protected set

  @Enumerated(EnumType.STRING)
  @Column(name = "owner_type", nullable = false, comment = "owner type")
  var ownerType: OwnerType = ownerType
    protected set

  @Column(name = "is_active", nullable = false, comment = "is active (true, false)")
  var active: Boolean = active
    protected set

  @Column(name = "issuance_starts_at", nullable = false, comment = "issuance start date")
  var issuanceStartsAt: ZonedDateTime = issuanceStartsAt
    protected set

  @Column(name = "issuance_ends_at", nullable = false, comment = "issuance end date")
  var issuanceEndsAt: ZonedDateTime = issuanceEndsAt
    protected set

  @Enumerated(EnumType.STRING)
  @Column(name = "issuance_amount_type", nullable = false, comment = "issuance amount type")
  var issuanceAmountType: PointIssuanceAmountType = issuanceAmountType
    protected set

  @Column(
    name = "issuance_amount",
    nullable = false,
    precision = 15,
    scale = 2,
    comment = "issuance amount (when FIXED)",
  )
  var issuanceAmount: BigDecimal = issuanceAmount
    protected set

  @Column(
    name = "issuance_percentage",
    nullable = true,
    precision = 5,
    scale = 2,
    comment = "issuance percentage (when PERCENTAGE)",
  )
  var issuancePercentage: BigDecimal? = issuancePercentage
    protected set

  @Column(
    name = "max_issuance_amount",
    nullable = false,
    precision = 15,
    scale = 2,
    comment = "maximum issuance amount",
  )
  var maxIssuanceAmount: BigDecimal = maxIssuanceAmount
    protected set

  @Enumerated(EnumType.STRING)
  @Column(
    name = "issuance_currency",
    nullable = false,
    columnDefinition = "CHAR(3)",
    comment = "currency type",
  )
  var issuanceCurrency: Currency = issuanceCurrency
    protected set

  @Enumerated(EnumType.STRING)
  @Column(name = "expiration_type", nullable = false, comment = "expiration type")
  var expirationType: PointExpirationType = expirationType
    protected set

  @Column(
    name = "expiration_period",
    nullable = true,
    comment = "expiration period (when other than FIXED)",
  )
  var expirationPeriod: Int? = expirationPeriod
    protected set

  @Column(name = "expires_at", nullable = true, comment = "expiration date (when FIXED)")
  var expiresAt: ZonedDateTime? = expiresAt
    protected set

  companion object {
    fun of(model: PointTemplate): PointTemplateJpaEntity {
      return PointTemplateJpaEntity(
        templateName = model.templateName,
        extTemplateId = model.extTemplateId,
        ownerType = model.ownerType,
        active = model.active,
        issuanceStartsAt = model.issuanceStartsAt,
        issuanceEndsAt = model.issuanceEndsAt,
        issuanceAmountType = model.issuanceAmountType,
        issuanceAmount = model.getIssuanceAmount(),
        issuancePercentage = model.issuancePercentage,
        maxIssuanceAmount = model.getMaxIssuanceAmount(),
        issuanceCurrency = model.getIssuanceCurrency(),
        expirationType = model.expirationType,
        expirationPeriod = model.expirationPeriod,
        expiresAt = model.expiresAt,
      )
    }
  }
}
