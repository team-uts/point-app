package dev.teamuts.point.domain.balance.service

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.balance.model.PointBalance
import dev.teamuts.point.domain.balance.port.PointBalanceReaderPort
import dev.teamuts.shared.domain.constant.Currency
import org.springframework.stereotype.Service

@Service
class PointBalanceService(private val pointBalanceReader: PointBalanceReaderPort) {
  fun getPointBalance(memberId: Long, ownerType: OwnerType, currency: Currency): PointBalance {
    return pointBalanceReader.retrieveByUnique(
      memberId = memberId,
      ownerType = ownerType,
      currency = currency,
    )
  }
}
