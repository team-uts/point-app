package dev.teamuts.point.domain.balance.model

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.balance.constant.PointBalanceStatus
import dev.teamuts.point.domain.common.annotation.DomainModel
import dev.teamuts.point.domain.common.constant.Currency
import dev.teamuts.point.domain.common.model.BaseIdentifiableModel
import dev.teamuts.point.domain.common.model.Money
import java.math.BigDecimal

@DomainModel
class PointBalance(
  id: Long = 0L,
  memberId: Long,
  ownerType: OwnerType,
  amount: BigDecimal,
  currency: Currency,
  status: PointBalanceStatus,
) : BaseIdentifiableModel<Long>(id) {
  val memberId: Long = memberId

  val ownerType: OwnerType = ownerType

  var amount: Money = Money.of(amount, currency)
    private set

  var status: PointBalanceStatus = status
    private set

  companion object {
    fun newActive(
      memberId: Long,
      ownerType: OwnerType,
      currency: Currency,
    ): PointBalance {
      return PointBalance(
        memberId = memberId,
        ownerType = ownerType,
        amount = BigDecimal.ZERO,
        currency = currency,
        status = PointBalanceStatus.ACTIVE,
      )
    }

    fun fromDatabase(
      id: Long,
      memberId: Long,
      ownerType: OwnerType,
      amount: BigDecimal,
      currency: Currency,
      status: PointBalanceStatus,
    ): PointBalance {
      return PointBalance(
        id = id,
        memberId = memberId,
        ownerType = ownerType,
        amount = amount,
        currency = currency,
        status = status,
      )
    }
  }
}
