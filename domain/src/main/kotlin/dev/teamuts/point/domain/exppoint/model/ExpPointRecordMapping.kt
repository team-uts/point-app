package dev.teamuts.point.domain.exppoint.model

import dev.teamuts.shared.domain.annotation.DomainModel
import dev.teamuts.shared.domain.constant.Currency
import dev.teamuts.shared.domain.model.BaseIdentifiableModel
import dev.teamuts.shared.domain.model.Money
import java.math.BigDecimal

@DomainModel
class ExpPointRecordMapping
private constructor(
  id: Long,
  pointTransactionId: Long,
  expPointRecordId: Long,
  amount: BigDecimal,
  currency: Currency,
) : BaseIdentifiableModel<Long>(id) {
  val pointTransactionId: Long = pointTransactionId

  val expPointRecordId: Long = expPointRecordId

  private val amount: Money = Money.of(amount, currency)

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

  fun getAmount(): BigDecimal {
    return amount.amount
  }

  fun getCurrency(): Currency {
    return amount.currency
  }
}
