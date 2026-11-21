package dev.teamuts.point.domain.exppoint.usecase

import dev.teamuts.point.domain.balance.facade.PointBalanceFacade
import dev.teamuts.point.domain.balance.service.PointBalanceService
import dev.teamuts.point.domain.exppoint.dto.app.ExpPointCommand
import dev.teamuts.point.domain.exppoint.dto.app.ExpPointInfo
import dev.teamuts.point.domain.exppoint.facade.ExpPointFacade
import dev.teamuts.shared.data.annotation.AppTransactional
import dev.teamuts.shared.domain.annotation.UseCase

@UseCase
@AppTransactional
class EarnExpPointsUseCase(
  private val pointBalanceService: PointBalanceService,
  private val pointBalanceFacade: PointBalanceFacade,
  private val expPointFacade: ExpPointFacade,
) {
  fun execute(
    memberId: Long,
    command: ExpPointCommand.EarnExpPoints,
  ): ExpPointInfo.EarnedExpPointInfo {
    // 1. Retrieve point balance
    val pointBalance =
      pointBalanceFacade.getOrCreatePointBalanceWithLock(
        memberId = memberId,
        ownerType = command.ownerType,
        currency = command.payAmount.currency,
      )

    // 2. issue expirable earned points.
    val pointTransaction =
      expPointFacade.issueEarningPoints(
        pointBalance = pointBalance,
        command = command,
      )

    // 3. Update point balance
    pointBalanceService.updateBalanceAfterPointProcess(
      pointBalance = pointBalance,
      pointTransaction = pointTransaction,
    )

    return ExpPointInfo.EarnedExpPointInfo.succeeded(
      balance = pointBalance,
      pointTransaction = pointTransaction,
    )
  }
}
