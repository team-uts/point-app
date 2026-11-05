package dev.teamuts.point.persistence.common.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.teamuts.point.persistence.common.config.properties.CustomDataSourceProperties
import dev.teamuts.point.persistence.common.constant.DataSourceConstants.APPLICATION_DATA_SOURCE
import dev.teamuts.point.persistence.common.constant.DataSourceConstants.REPLICA_DATA_SOURCE
import dev.teamuts.point.persistence.common.constant.DataSourceConstants.ROUTING_DATA_SOURCE
import dev.teamuts.point.persistence.common.constant.DataSourceConstants.SOURCE_DATA_SOURCE
import dev.teamuts.point.persistence.common.exception.DatabaseException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.sql.DataSource

private val log = KotlinLogging.logger {}

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CustomDataSourceProperties::class)
class DataSourceConfig(
    private val dataSourceProperties: CustomDataSourceProperties
) {
    @Primary
    @Bean(APPLICATION_DATA_SOURCE)
    fun applicationDataSource(
        @Qualifier(ROUTING_DATA_SOURCE) routingDataSource: DataSource,
    ): DataSource {
        return LazyConnectionDataSourceProxy(routingDataSource)
    }

    @Bean(ROUTING_DATA_SOURCE)
    fun routingDataSource(
        @Qualifier(SOURCE_DATA_SOURCE) sourceDataSource: DataSource,
        @Qualifier(REPLICA_DATA_SOURCE) replicaDataSource: DataSource
    ): DataSource {
        val dataSourceMap = mapOf(
            DbType.SOURCE to sourceDataSource,
            DbType.REPLICA to replicaDataSource
        )

        return RoutingDataSource().apply {
            this.setTargetDataSources(dataSourceMap.toMap())
            this.setDefaultTargetDataSource(sourceDataSource)
        }
    }

    @Bean(SOURCE_DATA_SOURCE)
    fun sourceDataSource(): DataSource {
        val sourceConfig = HikariConfig(dataSourceProperties.getSourceWithCommon())

        return HikariDataSource(sourceConfig)
    }

    @Bean(REPLICA_DATA_SOURCE)
    fun replicaDataSource(): DataSource {
        val replicaConfig = HikariConfig(dataSourceProperties.getReplicaWithCommon())

        return HikariDataSource(replicaConfig)
    }

    class RoutingDataSource : AbstractRoutingDataSource() {
        override fun determineCurrentLookupKey(): Any {
            return DbType.valueOfReadOnly(
                readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly()
            ).also {
                log.debug { "Routing DataSource: $it" }
            }
        }
    }

    private enum class DbType(
        private val readOnly: Boolean
    ) {
        SOURCE(false),
        REPLICA(true);

        companion object {
            fun valueOfReadOnly(readOnly: Boolean): DbType {
                return entries.find { it.readOnly == readOnly }
                    ?: throw DatabaseException("Not Found DB(${entries.joinToString { it.name }}) type")
            }
        }
    }
}
