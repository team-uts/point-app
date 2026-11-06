package dev.teamuts.point.shared.utils

import java.util.*

object ConfigUtils {
  fun mergeProperties(first: Properties, second: Properties): Properties {
    return Properties().apply {
      this.putAll(first)
      this.putAll(second)
    }
  }
}
