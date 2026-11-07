package dev.teamuts.point.domain.exppoint.model

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.common.annotation.DomainModel
import dev.teamuts.point.domain.common.constant.Currency
import dev.teamuts.point.domain.common.model.BaseIdentifiableModel
import dev.teamuts.point.domain.common.model.Money
import dev.teamuts.point.domain.exppoint.constant.ExpPointRecordStatus
import java.math.BigDecimal
import java.time.ZonedDateTime

@DomainModel
class ExpPointRecord(
  id: Long,
  balanceId: Long,
  ownerType: OwnerType,
  pointTransactionId: Long,
  totalAmount: BigDecimal,
  remainingAmount: BigDecimal,
  currency: Currency,
  expiresAt: ZonedDateTime,
  expiredAt: ZonedDateTime?,
  status: ExpPointRecordStatus,
) : BaseIdentifiableModel<Long>(id) {
  val balanceId: Long = balanceId

  val ownerType: OwnerType = ownerType

  val pointTransactionId: Long = pointTransactionId

  val totalAmount: Money = Money.of(totalAmount, currency)

  var remainingAmount: Money = Money.of(remainingAmount, currency)
    private set

  val expiresAt: ZonedDateTime = expiresAt

  var expiredAt: ZonedDateTime? = expiredAt
    private set

  var status: ExpPointRecordStatus = status
    private set

  companion object {
    fun fromDatabase(
      id: Long,
      balanceId: Long,
      ownerType: OwnerType,
      pointTransactionId: Long,
      totalAmount: BigDecimal,
      remainingAmount: BigDecimal,
      currency: Currency,
      expiresAt: ZonedDateTime,
      expiredAt: ZonedDateTime?,
      status: ExpPointRecordStatus,
    ): ExpPointRecord {
      return ExpPointRecord(
        id = id,
        balanceId = balanceId,
        ownerType = ownerType,
        pointTransactionId = pointTransactionId,
        totalAmount = totalAmount,
        remainingAmount = remainingAmount,
        currency = currency,
        expiresAt = expiresAt,
        expiredAt = expiredAt,
        status = status
      )
    }
  }
}
