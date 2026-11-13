package dev.teamuts.point.domain.balance.port

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.balance.model.PointBalance
import dev.teamuts.shared.domain.constant.Currency

interface PointBalanceReaderPort {
  fun retrieveByUnique(memberId: Long, ownerType: OwnerType, currency: Currency): PointBalance
}
