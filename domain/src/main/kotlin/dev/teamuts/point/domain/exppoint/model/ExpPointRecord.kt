package dev.teamuts.point.domain.exppoint.model

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.balance.model.PointBalanceTransaction
import dev.teamuts.point.domain.common.model.Point
import dev.teamuts.point.domain.exppoint.constant.ExpPointRecordStatus
import dev.teamuts.shared.domain.annotation.DomainModel
import dev.teamuts.shared.domain.constant.Currency
import dev.teamuts.shared.domain.model.BaseIdentifiableModel
import dev.teamuts.shared.utils.AppTimeUtils
import java.math.BigDecimal
import java.time.ZonedDateTime

@DomainModel
class ExpPointRecord
private constructor(
  id: Long = 0L,
  val balanceId: Long,
  val ownerType: OwnerType,
  val pointTransactionId: Long,
  private val totalAmount: Point,
  private val remainingAmount: Point,
  val expiresAt: ZonedDateTime,
  expiredAt: ZonedDateTime?,
  status: ExpPointRecordStatus,
) : BaseIdentifiableModel<Long>(id) {
  var expiredAt: ZonedDateTime? = expiredAt
    private set

  var status: ExpPointRecordStatus = status
    private set

  val totalAmountValue: BigDecimal = totalAmount.value
  val remainingAmountValue: BigDecimal = remainingAmount.value
  val currency: Currency = totalAmount.currency

  companion object {
    fun newRecord(
      pointTransaction: PointBalanceTransaction,
      pointTemplate: PointTemplate,
    ): ExpPointRecord {
      return ExpPointRecord(
        balanceId = pointTransaction.balanceId,
        ownerType = pointTransaction.ownerType,
        pointTransactionId = pointTransaction.id,
        totalAmount =
          Point.of(
            amount = pointTransaction.expAmountValue,
            currency = pointTransaction.currency,
          ),
        remainingAmount =
          Point.of(
            amount = pointTransaction.expAmountValue,
            currency = pointTransaction.currency,
          ),
        expiresAt = pointTemplate.calculateExpirationDate(baseDateTime = AppTimeUtils.getNowTime()),
        expiredAt = null,
        status = ExpPointRecordStatus.ACTIVE,
      )
    }

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
        totalAmount = Point.of(amount = totalAmount, currency = currency),
        remainingAmount = Point.of(amount = remainingAmount, currency = currency),
        expiresAt = expiresAt,
        expiredAt = expiredAt,
        status = status,
      )
    }
  }
}
