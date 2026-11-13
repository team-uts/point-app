package dev.teamuts.point.persistence.exppoint.entity

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.exppoint.constant.ExpPointRecordStatus
import dev.teamuts.point.domain.exppoint.model.ExpPointRecord
import dev.teamuts.point.persistence.balance.entity.PointBalanceJpaEntity
import dev.teamuts.point.persistence.balance.entity.PointBalanceTransactionJpaEntity
import dev.teamuts.point.persistence.common.entity.BaseAuditableEntity
import dev.teamuts.point.persistence.common.entity.EntityMetadata.Companion.PREFIX_INDEX
import dev.teamuts.shared.domain.constant.Currency
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.ZonedDateTime

private const val TABLE_ABBR = "epr"

@Entity
@Table(
  catalog = "point",
  name = "exp_point_records",
  indexes =
    [
      Index(
        name = "${PREFIX_INDEX}_${TABLE_ABBR}_balanceid",
        columnList = "balance_id",
      )
    ],
)
class ExpPointRecordJpaEntity(
  balance: PointBalanceJpaEntity,
  ownerType: OwnerType,
  pointTransaction: PointBalanceTransactionJpaEntity,
  totalAmount: BigDecimal,
  remainingAmount: BigDecimal,
  currency: Currency,
  expiresAt: ZonedDateTime,
  expiredAt: ZonedDateTime?,
  status: ExpPointRecordStatus,
) : BaseAuditableEntity() {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  var id: Long = 0L
    protected set

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "balance_id", nullable = false, comment = "point balance ID")
  var balance: PointBalanceJpaEntity = balance
    protected set

  @Enumerated(EnumType.STRING)
  @Column(name = "owner_type", nullable = false, comment = "owner type")
  var ownerType: OwnerType = ownerType
    protected set

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "point_transaction_id",
    nullable = false,
    comment = "point balance transaction ID",
  )
  var pointTransaction: PointBalanceTransactionJpaEntity = pointTransaction
    protected set

  @Column(
    name = "total_amount",
    nullable = false,
    precision = 15,
    scale = 2,
    comment = "total point amount",
  )
  var totalAmount: BigDecimal = totalAmount
    protected set

  @Column(
    name = "remaining_amount",
    nullable = false,
    precision = 15,
    scale = 2,
    comment = "remaining point amount after usage",
  )
  var remainingAmount: BigDecimal = remainingAmount
    protected set

  @Enumerated(EnumType.STRING)
  @Column(
    name = "currency",
    nullable = false,
    columnDefinition = "CHAR(3)",
    comment = "currency type",
  )
  var currency: Currency = currency
    protected set

  @Column(name = "expires_at", nullable = false, comment = "point expiration date")
  var expiresAt: ZonedDateTime = expiresAt
    protected set

  @Column(name = "expired_at", nullable = true, comment = "point expired date")
  var expiredAt: ZonedDateTime? = expiredAt
    protected set

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, comment = "point record status")
  var status: ExpPointRecordStatus = status
    protected set

  companion object {
    fun of(
      model: ExpPointRecord,
      balance: PointBalanceJpaEntity,
      pointTransaction: PointBalanceTransactionJpaEntity,
    ): ExpPointRecordJpaEntity {
      return ExpPointRecordJpaEntity(
        balance = balance,
        ownerType = model.ownerType,
        pointTransaction = pointTransaction,
        totalAmount = model.totalAmount.amount,
        remainingAmount = model.remainingAmount.amount,
        currency = model.totalAmount.currency,
        expiresAt = model.expiresAt,
        expiredAt = model.expiredAt,
        status = model.status,
      )
    }
  }
}
