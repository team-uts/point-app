package dev.teamuts.point.domain.balance.facade

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.balance.model.PointBalance
import dev.teamuts.point.domain.balance.service.PointBalanceService
import dev.teamuts.shared.data.AppTransactional
import dev.teamuts.shared.domain.constant.Currency
import org.springframework.stereotype.Service

@Service
class PointBalanceFacade(private val pointBalanceService: PointBalanceService) {
  @AppTransactional
  fun getOrCreatePointBalanceWithLock(
    memberId: Long,
    ownerType: OwnerType,
    currency: Currency,
  ): PointBalance {
    // 1. Try to find the point balance or create a new one in a separate transaction.
    val pointBalance =
      pointBalanceService.getOrCreateNewActiveBalanceIndependently(
        memberId = memberId,
        ownerType = ownerType,
        currency = currency,
      )

    // 2. get point balance with lock
    return pointBalanceService.getPointBalanceByIdWithLock(pointBalance.id)
  }
}
