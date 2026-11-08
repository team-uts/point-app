package dev.teamuts.point.persistence.common.config.properties

import dev.teamuts.point.persistence.common.constant.DataSourceConstants.SPRING_DATA_SOURCE_PREFIX
import dev.teamuts.shared.utils.ConfigUtils
import java.util.*
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = SPRING_DATA_SOURCE_PREFIX)
data class CustomDataSourceProperties(
  val common: HikariProperties,
  val source: HikariProperties,
  val replica: HikariProperties,
) {
  fun getSourceWithCommon(): Properties {
    return ConfigUtils.mergeProperties(
      common.hikari,
      source.hikari,
    )
  }

  fun getReplicaWithCommon(): Properties {
    return ConfigUtils.mergeProperties(
      common.hikari,
      replica.hikari,
    )
  }
}

data class HikariProperties(val hikari: Properties)
