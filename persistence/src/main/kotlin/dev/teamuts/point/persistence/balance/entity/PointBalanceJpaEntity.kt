package dev.teamuts.point.persistence.balance.entity

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.balance.constant.PointBalanceStatus
import dev.teamuts.point.domain.balance.model.PointBalance
import dev.teamuts.point.persistence.common.entity.BaseAuditableEntity
import dev.teamuts.point.persistence.common.entity.EntityMetadata.Companion.PREFIX_UNIQUE
import dev.teamuts.shared.domain.constant.Currency
import jakarta.persistence.*
import java.math.BigDecimal

private const val TABLE_ABBR = "pb"

@Entity
@Table(
  catalog = "point",
  name = "point_balances",
  uniqueConstraints =
    [
      UniqueConstraint(
        name = "${PREFIX_UNIQUE}_${TABLE_ABBR}_memberid_ownertype_currency",
        columnNames = ["member_id", "owner_type", "currency"],
      )
    ],
)
class PointBalanceJpaEntity(
  memberId: Long,
  ownerType: OwnerType,
  cashAmount: BigDecimal,
  expAmount: BigDecimal,
  totalAmount: BigDecimal,
  currency: Currency,
  status: PointBalanceStatus,
  lastTransactionId: Long?,
) : BaseAuditableEntity() {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  var id: Long = 0L
    protected set

  @Column(name = "member_id", nullable = false, comment = "owner member ID")
  var memberId: Long = memberId
    protected set

  @Enumerated(EnumType.STRING)
  @Column(name = "owner_type", nullable = false, comment = "owner type")
  var ownerType: OwnerType = ownerType
    protected set

  @Column(
    name = "cash_amount",
    nullable = false,
    precision = 15,
    scale = 2,
    comment = "cashable point amount",
  )
  var cashAmount: BigDecimal = cashAmount
    protected set

  @Column(
    name = "exp_amount",
    nullable = false,
    precision = 15,
    scale = 2,
    comment = "expirable point amount",
  )
  var expAmount: BigDecimal = expAmount
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

  @Enumerated(EnumType.STRING)
  @Column(
    name = "currency",
    nullable = false,
    columnDefinition = "CHAR(3)",
    comment = "currency type",
  )
  var currency: Currency = currency
    protected set

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, comment = "point balance status")
  var status: PointBalanceStatus = status
    protected set

  @Column(name = "last_transaction_id", nullable = true, comment = "last transaction ID")
  var lastTransactionId: Long? = null
    protected set

  companion object {
    fun of(model: PointBalance): PointBalanceJpaEntity {
      return PointBalanceJpaEntity(
        memberId = model.memberId,
        ownerType = model.ownerType,
        cashAmount = model.cashAmountValue,
        expAmount = model.expAmountValue,
        totalAmount = model.totalAmountValue,
        currency = model.currency,
        status = model.status,
        lastTransactionId = model.lastTransactionId,
      )
    }
  }
}
