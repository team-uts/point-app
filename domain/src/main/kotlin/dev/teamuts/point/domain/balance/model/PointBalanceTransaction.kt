package dev.teamuts.point.domain.balance.model

import dev.teamuts.point.domain.balance.constant.BalanceOperationType
import dev.teamuts.point.domain.balance.constant.BalanceTransactionType
import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.balance.model.sub.PointBalanceAmount
import dev.teamuts.point.domain.balance.model.sub.PointBalanceAmountDetails
import dev.teamuts.point.domain.common.model.Point
import dev.teamuts.shared.domain.annotation.DomainModel
import dev.teamuts.shared.domain.constant.Currency
import dev.teamuts.shared.domain.model.BaseIdentifiableModel
import java.math.BigDecimal

@DomainModel
class PointBalanceTransaction
private constructor(
  id: Long = 0L,
  val balanceId: Long,
  val ownerType: OwnerType,
  val transactionType: BalanceTransactionType,
  val operationType: BalanceOperationType,
  private val balanceAmount: PointBalanceAmount,
  private val afterBalanceAmount: Point,
  val paymentType: String?,
  val payTransactionId: String?,
) : BaseIdentifiableModel<Long>(id), PointBalanceAmountDetails by balanceAmount {
  val afterBalanceAmountValue: BigDecimal = afterBalanceAmount.value

  companion object {
    fun earnExpPoints(
      pointBalance: PointBalance,
      earningPoint: Point,
      paymentType: String?,
      payTransactionId: String?,
    ): PointBalanceTransaction {
      val operationType = BalanceOperationType.CREDIT

      return PointBalanceTransaction(
        balanceId = pointBalance.id,
        ownerType = pointBalance.ownerType,
        transactionType = BalanceTransactionType.EARN,
        operationType = operationType,
        balanceAmount =
          PointBalanceAmount.of(
            cashAmount = BigDecimal.ZERO,
            expAmount = earningPoint.value,
            currency = earningPoint.currency,
          ),
        afterBalanceAmount =
          pointBalance.getValidAfterBalanceTotalAmount(
            pointAmount = earningPoint,
            operationType = operationType,
          ),
        paymentType = paymentType,
        payTransactionId = payTransactionId,
      )
    }

    fun fromDatabase(
      id: Long,
      balanceId: Long,
      ownerType: OwnerType,
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
        ownerType = ownerType,
        transactionType = transactionType,
        operationType = operationType,
        balanceAmount =
          PointBalanceAmount.of(
            cashAmount = cashAmount,
            expAmount = expAmount,
            currency = currency,
          ),
        afterBalanceAmount = Point.of(afterBalanceAmount, currency),
        paymentType = paymentType,
        payTransactionId = payTransactionId,
      )
    }
  }

  fun calculateNewBalanceAmount(balanceAmount: PointBalanceAmount): PointBalanceAmount {
    return when (operationType) {
      BalanceOperationType.CREDIT -> balanceAmount.credit(this.balanceAmount)
      BalanceOperationType.DEBIT -> balanceAmount.debit(this.balanceAmount)
    }
  }
}
