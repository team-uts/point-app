package dev.teamuts.point.domain.balance.model.sub

import dev.teamuts.point.domain.common.model.Point
import dev.teamuts.shared.domain.constant.Currency

interface PointBalanceAmountDetails {
  val cashAmount: Point
  val expAmount: Point
  val currency: Currency
}
