package dev.teamuts.point.shared.utils

object TimeUtils {
  private const val MILLI_SEC_UNIT: Int = 1000

  fun convertSecToMillis(seconds: Int): Int {
    return seconds * MILLI_SEC_UNIT
  }

  fun convertMillisToSecWithPrecision(millis: Long): Double {
    return millis / MILLI_SEC_UNIT.toDouble()
  }
}
