package dev.teamuts.point.domain.exppoint.usecase

import dev.teamuts.point.domain.balance.service.PointBalanceService
import dev.teamuts.point.domain.exppoint.dto.app.ExpPointCommand
import dev.teamuts.point.domain.exppoint.service.PointTemplateService
import dev.teamuts.shared.domain.annotation.UseCase

@UseCase
class GrantExpPointsUseCase(
  private val pointBalanceService: PointBalanceService,
  private val pointTemplateService: PointTemplateService,
) {
  fun execute(memberId: Long, command: ExpPointCommand.GrantExpPoints) {
    // 1. Retrieve point balance
    val pointBalance =
      pointBalanceService.getPointBalance(
        memberId = memberId,
        ownerType = command.ownerType,
        currency = command.currency,
      )

    // 2. Retrieve point template
    val pointTemplate =
      pointTemplateService.getPointTemplate(
        extTemplateId = command.pointTemplateId,
        ownerType = command.ownerType,
      )

    // 3. Calculate exp points and create new point transaction

    // 4. Create exp point record and mapping to point transaction

    // 5. Update point balance
  }
}
