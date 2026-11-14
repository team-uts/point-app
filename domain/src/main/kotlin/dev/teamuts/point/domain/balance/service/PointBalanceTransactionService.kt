package dev.teamuts.point.domain.balance.service

import dev.teamuts.point.domain.balance.model.PointBalance
import dev.teamuts.point.domain.balance.model.PointBalanceTransaction
import dev.teamuts.point.domain.balance.port.PointBalanceTransactionStorePort
import dev.teamuts.point.domain.exppoint.model.PointTemplate
import dev.teamuts.shared.data.AppTransactional
import dev.teamuts.shared.domain.model.Money
import org.springframework.stereotype.Service

@Service
@AppTransactional
class PointBalanceTransactionService(
  private val pointBalanceTransactionStorePort: PointBalanceTransactionStorePort
) {
  fun createEarnTransaction(
    pointBalance: PointBalance,
    pointTemplate: PointTemplate,
    payAmount: Money,
    paymentType: String?,
    payTransactionId: String?,
  ): PointBalanceTransaction {
    // Calculate exp points
    val issuancePoint = pointTemplate.calculateIssuancePoint(baseAmount = payAmount)

    val earnPointTransaction =
      PointBalanceTransaction.earnExpPoints(
        pointBalance = pointBalance,
        earningPoint = issuancePoint,
        paymentType = paymentType,
        payTransactionId = payTransactionId,
      )

    return pointBalanceTransactionStorePort.store(earnPointTransaction)
  }
}
