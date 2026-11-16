package dev.teamuts.point.domain.exppoint.dto.app

import dev.teamuts.point.domain.balance.constant.BalanceOperationType
import dev.teamuts.point.domain.balance.constant.BalanceTransactionType
import dev.teamuts.point.domain.balance.model.PointBalance
import dev.teamuts.point.domain.balance.model.PointBalanceTransaction
import dev.teamuts.shared.domain.constant.Currency
import java.math.BigDecimal

class ExpPointInfo {
  data class EarnedExpPointInfo(
    val balanceId: Long,
    val pointTransactionId: Long,
    val operationType: BalanceOperationType,
    val transactionType: BalanceTransactionType,
    val cashAmount: BigDecimal,
    val expAmount: BigDecimal,
    val afterBalanceAmount: BigDecimal,
    val currency: Currency,
  ) {
    companion object {
      fun succeeded(
        balance: PointBalance,
        pointTransaction: PointBalanceTransaction,
      ): EarnedExpPointInfo {
        return EarnedExpPointInfo(
          balanceId = balance.id,
          pointTransactionId = pointTransaction.id,
          operationType = pointTransaction.operationType,
          transactionType = pointTransaction.transactionType,
          cashAmount = pointTransaction.cashAmount.value,
          expAmount = pointTransaction.expAmount.value,
          afterBalanceAmount = pointTransaction.afterBalanceAmount.value,
          currency = pointTransaction.currency,
        )
      }
    }
  }
}
