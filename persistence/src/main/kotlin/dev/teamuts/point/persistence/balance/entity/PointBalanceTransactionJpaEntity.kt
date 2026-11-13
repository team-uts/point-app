package dev.teamuts.point.persistence.balance.entity

import dev.teamuts.point.domain.balance.constant.BalanceOperationType
import dev.teamuts.point.domain.balance.constant.BalanceTransactionType
import dev.teamuts.point.domain.balance.model.PointBalanceTransaction
import dev.teamuts.point.persistence.common.entity.BaseAuditableEntity
import dev.teamuts.point.persistence.common.entity.EntityMetadata.Companion.PREFIX_INDEX
import dev.teamuts.shared.domain.constant.Currency
import jakarta.persistence.*
import java.math.BigDecimal

private const val TABLE_ABBR = "pbt"

@Entity
@Table(
  catalog = "point",
  name = "point_balance_transactions",
  indexes =
    [
      Index(
        name = "${PREFIX_INDEX}_${TABLE_ABBR}_balanceid_createdat",
        columnList = "balance_id, created_at",
      ),
      Index(
        name = "${PREFIX_INDEX}_${TABLE_ABBR}_balanceid_transactiontype_createdat",
        columnList = "balance_id, transaction_type, created_at",
      ),
    ],
)
class PointBalanceTransactionJpaEntity(
  balance: PointBalanceJpaEntity,
  transactionType: BalanceTransactionType,
  operationType: BalanceOperationType,
  cashAmount: BigDecimal,
  expAmount: BigDecimal,
  afterBalanceAmount: BigDecimal,
  currency: Currency,
  paymentType: String?,
  payTransactionId: String?,
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
  @Column(name = "transaction_type", nullable = false, comment = "transaction type")
  var transactionType: BalanceTransactionType = transactionType
    protected set

  @Enumerated(EnumType.STRING)
  @Column(name = "operation_type", nullable = false, comment = "operation type")
  var operationType: BalanceOperationType = operationType
    protected set

  @Column(name = "cash_amount", nullable = false, comment = "cashable point amount")
  var cashAmount: BigDecimal = cashAmount
    protected set

  @Column(name = "exp_amount", nullable = false, comment = "expirable point amount")
  var expAmount: BigDecimal = expAmount
    protected set

  @Column(
    name = "after_balance_amount",
    nullable = false,
    comment = "point balance amount after transaction",
  )
  var afterBalanceAmount: BigDecimal = afterBalanceAmount
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

  @Column(
    name = "pay_transaction_id",
    nullable = true,
    length = 50,
    comment = "payment transaction ID",
  )
  var payTransactionId: String? = payTransactionId
    protected set

  @Column(
    name = "payment_type",
    nullable = true,
    length = 50,
    comment = "payment type",
  )
  var paymentType: String? = paymentType
    protected set

  companion object {
    fun of(
      model: PointBalanceTransaction,
      balance: PointBalanceJpaEntity,
    ): PointBalanceTransactionJpaEntity {
      return PointBalanceTransactionJpaEntity(
        balance = balance,
        transactionType = model.transactionType,
        operationType = model.operationType,
        cashAmount = model.getCashAmount(),
        expAmount = model.getExpAmount(),
        afterBalanceAmount = model.getAfterBalanceAmount(),
        currency = model.getCurrency(),
        paymentType = model.paymentType,
        payTransactionId = model.payTransactionId,
      )
    }
  }
}
