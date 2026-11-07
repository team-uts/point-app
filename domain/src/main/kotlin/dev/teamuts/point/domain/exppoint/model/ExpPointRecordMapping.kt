package dev.teamuts.point.domain.exppoint.model

import dev.teamuts.point.domain.common.annotation.DomainModel
import dev.teamuts.point.domain.common.constant.Currency
import dev.teamuts.point.domain.common.model.BaseIdentifiableModel
import dev.teamuts.point.domain.common.model.Money
import java.math.BigDecimal

@DomainModel
class ExpPointRecordMapping(
  id: Long,
  pointTransactionId: Long,
  expPointRecordId: Long,
  amount: BigDecimal,
  currency: Currency,
) : BaseIdentifiableModel<Long>(id) {
  val pointTransactionId: Long = pointTransactionId

  val expPointRecordId: Long = expPointRecordId

  val amount: Money = Money.of(amount, currency)

  companion object {
    fun fromDatabase(
      id: Long,
      pointTransactionId: Long,
      expPointRecordId: Long,
      amount: BigDecimal,
      currency: Currency,
    ): ExpPointRecordMapping {
      return ExpPointRecordMapping(
        id = id,
        pointTransactionId = pointTransactionId,
        expPointRecordId = expPointRecordId,
        amount = amount,
        currency = currency,
      )
    }
  }
}
