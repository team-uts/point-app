package dev.teamuts.point.persistence.exppoint.entity

import dev.teamuts.point.domain.exppoint.model.ExpPointRecordMapping
import dev.teamuts.point.persistence.balance.entity.PointBalanceTransactionJpaEntity
import dev.teamuts.point.persistence.common.entity.BaseCreationEntity
import dev.teamuts.point.persistence.common.entity.EntityMetadata.Companion.PREFIX_INDEX
import dev.teamuts.shared.domain.constant.Currency
import jakarta.persistence.*
import java.math.BigDecimal

private const val TABLE_ABBR = "eprm"

@Entity
@Table(
  catalog = "point",
  name = "exp_point_record_mappings",
  indexes =
    [
      Index(
        name = "${PREFIX_INDEX}_${TABLE_ABBR}_pointtransactionid",
        columnList = "point_transaction_id",
      ),
      Index(
        name = "${PREFIX_INDEX}_${TABLE_ABBR}_exppointrecordid",
        columnList = "exp_point_record_id",
      ),
    ],
)
class ExpPointRecordMappingJpaEntity(
  pointTransaction: PointBalanceTransactionJpaEntity,
  expPointRecord: ExpPointRecordJpaEntity,
  amount: BigDecimal,
  currency: Currency,
) : BaseCreationEntity() {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  var id: Long = 0L
    protected set

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "point_transaction_id",
    nullable = false,
    comment = "point balance transaction ID",
  )
  var pointTransaction: PointBalanceTransactionJpaEntity = pointTransaction
    protected set

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "exp_point_record_id",
    nullable = false,
    comment = "exp point record ID",
  )
  var expPointRecord: ExpPointRecordJpaEntity = expPointRecord
    protected set

  @Column(
    name = "amount",
    nullable = false,
    precision = 15,
    scale = 2,
    comment = "point amount",
  )
  var amount: BigDecimal = amount
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

  companion object {
    fun of(
      model: ExpPointRecordMapping,
      pointTransaction: PointBalanceTransactionJpaEntity,
      expPointRecord: ExpPointRecordJpaEntity,
    ): ExpPointRecordMappingJpaEntity {
      return ExpPointRecordMappingJpaEntity(
        pointTransaction = pointTransaction,
        expPointRecord = expPointRecord,
        amount = model.getAmount(),
        currency = model.getCurrency(),
      )
    }
  }
}
