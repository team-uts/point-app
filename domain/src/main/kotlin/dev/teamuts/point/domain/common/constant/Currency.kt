package dev.teamuts.point.domain.common.constant

enum class Currency(val scale: Int = 2) {
  AUD,
  USD,
  KRW(0),
}
