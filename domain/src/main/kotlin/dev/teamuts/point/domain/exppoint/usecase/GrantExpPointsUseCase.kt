package dev.teamuts.point.domain.exppoint.usecase

import dev.teamuts.point.domain.balance.service.PointBalanceService
import dev.teamuts.point.domain.exppoint.dto.app.ExpPointCommand
import dev.teamuts.point.domain.exppoint.facade.ExpPointFacade
import dev.teamuts.shared.data.AppTransactional
import dev.teamuts.shared.domain.annotation.UseCase

@UseCase
@AppTransactional
class GrantExpPointsUseCase(
  private val pointBalanceService: PointBalanceService,
  private val expPointFacade: ExpPointFacade,
) {
  fun execute(memberId: Long, command: ExpPointCommand.EarnExpPoints) {
    // 1. Retrieve point balance
    val pointBalance =
      pointBalanceService.getPointBalance(
        memberId = memberId,
        ownerType = command.ownerType,
        currency = command.payAmount.currency,
      )

    // 2. issue expirable points for reward.
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
  }
}
