package dev.teamuts.point.domain.exppoint.facade

import dev.teamuts.point.domain.balance.model.PointBalance
import dev.teamuts.point.domain.balance.model.PointBalanceTransaction
import dev.teamuts.point.domain.balance.service.PointBalanceTransactionService
import dev.teamuts.point.domain.exppoint.dto.app.ExpPointCommand
import dev.teamuts.point.domain.exppoint.service.ExpPointRecordService
import dev.teamuts.point.domain.exppoint.service.PointTemplateService
import dev.teamuts.shared.data.AppTransactional
import org.springframework.stereotype.Service

@Service
class ExpPointFacade(
  private val pointTemplateService: PointTemplateService,
  private val pointBalanceTransactionService: PointBalanceTransactionService,
  private val expPointRecordService: ExpPointRecordService,
) {
  @AppTransactional
  fun issueEarningPoints(
    pointBalance: PointBalance,
    command: ExpPointCommand.EarnExpPoints,
  ): PointBalanceTransaction {
    // 1. Retrieve point template
    val pointTemplate =
      pointTemplateService.getPointTemplate(
        extTemplateId = command.pointTemplateId,
        ownerType = pointBalance.ownerType,
      )

    // 2. Create new point transaction (EARN)
    val pointTransaction =
      pointBalanceTransactionService.createEarnTransaction(
        pointBalance = pointBalance,
        pointTemplate = pointTemplate,
        payAmount = command.payAmount,
        paymentType = command.paymentType,
        payTransactionId = command.payTransactionId,
      )

    // 3. Create exp point record.
    expPointRecordService.createNewPointRecord(
      pointTransaction = pointTransaction,
      pointTemplate = pointTemplate,
    )

    return pointTransaction
  }
}
