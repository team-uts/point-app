package dev.teamuts.point.persistence.balance.entity

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.balance.constant.PointBalanceStatus
import dev.teamuts.point.domain.common.constant.Currency
import dev.teamuts.point.persistence.common.entity.BaseAuditableEntity
import dev.teamuts.point.persistence.common.entity.EntityMetadata.Companion.PREFIX_UNIQUE
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
        name = "${PREFIX_UNIQUE}_${TABLE_ABBR}_ownerid_ownertype_currency",
        columnNames = ["owner_id", "owner_type", "currency"],
      )
    ],
)
class PointBalanceJpaEntity(
  ownerType: OwnerType,
  amount: BigDecimal,
  currency: Currency,
  status: PointBalanceStatus,
) : BaseAuditableEntity() {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  var id: Long = 0L
    protected set

  @Column(name = "member_id", nullable = false, comment = "owner member ID")
  var memberId: Long = 0L
    protected set

  @Enumerated(EnumType.STRING)
  @Column(name = "owner_type", nullable = false, comment = "owner type")
  var ownerType: OwnerType = ownerType
    protected set

  @Column(
    name = "amount",
    nullable = false,
    precision = 15,
    scale = 2,
    comment = "point balance amount",
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

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, comment = "point balance status")
  var status: PointBalanceStatus = status
    protected set
}
