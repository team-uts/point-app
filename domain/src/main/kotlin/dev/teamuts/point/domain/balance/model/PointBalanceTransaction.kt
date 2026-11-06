package dev.teamuts.point.domain.balance.model

import dev.teamuts.point.domain.balance.constant.BalanceOperationType
import dev.teamuts.point.domain.balance.constant.BalanceTransactionType
import dev.teamuts.point.domain.common.annotation.DomainModel
import dev.teamuts.point.domain.common.constant.Currency
import dev.teamuts.point.domain.common.model.BaseIdentifiableModel
import dev.teamuts.point.domain.common.model.Money
import java.math.BigDecimal

@DomainModel
class PointBalanceTransaction(
  id: Long = 0L,
  balanceId: Long,
  transactionType: BalanceTransactionType,
  operationType: BalanceOperationType,
  cashAmount: BigDecimal,
  expAmount: BigDecimal,
  afterBalanceAmount: BigDecimal,
  currency: Currency,
  paymentType: String?,
  payTransactionId: String?,
) : BaseIdentifiableModel<Long>(id) {
  val balanceId: Long = balanceId

  val transactionType: BalanceTransactionType = transactionType

  val operationType: BalanceOperationType = operationType

  val cashAmount: Money = Money.of(cashAmount, currency)

  val expAmount: Money = Money.of(expAmount, currency)

  val afterBalanceAmount: Money = Money.of(afterBalanceAmount, currency)

  val paymentType: String? = paymentType

  val payTransactionId: String? = payTransactionId

  companion object {
    fun fromDatabase(
      id: Long,
      balanceId: Long,
      transactionType: BalanceTransactionType,
      operationType: BalanceOperationType,
      cashAmount: BigDecimal,
      expAmount: BigDecimal,
      afterBalanceAmount: BigDecimal,
      currency: Currency,
      paymentType: String?,
      payTransactionId: String?,
    ): PointBalanceTransaction {
      return PointBalanceTransaction(
        id = id,
        balanceId = balanceId,
        transactionType = transactionType,
        operationType = operationType,
        cashAmount = cashAmount,
        expAmount = expAmount,
        afterBalanceAmount = afterBalanceAmount,
        currency = currency,
        paymentType = paymentType,
        payTransactionId = payTransactionId
      )
    }
  }
}
