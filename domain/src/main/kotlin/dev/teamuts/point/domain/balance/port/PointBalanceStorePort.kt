package dev.teamuts.point.domain.balance.port

import dev.teamuts.point.domain.balance.model.PointBalance

interface PointBalanceStorePort {
  fun store(pointBalance: PointBalance): PointBalance
}
