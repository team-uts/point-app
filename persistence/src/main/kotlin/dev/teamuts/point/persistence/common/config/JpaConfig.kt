package dev.teamuts.point.persistence.common.config

import com.querydsl.jpa.impl.JPAQueryFactory
import dev.teamuts.point.persistence.common.constant.DataSourceConstants.APPLICATION_DATA_SOURCE
import dev.teamuts.point.persistence.common.constant.JpaConstants.APP_ENTITY_MANAGER
import dev.teamuts.point.persistence.common.constant.JpaConstants.APP_JPA_QUERY_FACTORY
import dev.teamuts.point.shared.data.TransactionConstants.APP_TRANSACTION_MANAGER
import jakarta.persistence.EntityManager
import java.util.*
import javax.sql.DataSource
import org.hibernate.cfg.SchemaToolingSettings
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.hibernate.autoconfigure.HibernateProperties
import org.springframework.boot.jpa.autoconfigure.JpaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter

@Configuration(proxyBeanMethods = false)
@EnableJpaAuditing
@EnableJpaRepositories(
  basePackages = ["dev.teamuts.point.persistence.**.repository"],
  entityManagerFactoryRef = APP_ENTITY_MANAGER,
  transactionManagerRef = APP_TRANSACTION_MANAGER,
)
class JpaConfig(
  private val jpaProperties: JpaProperties,
  private val hibernateProperties: HibernateProperties,
) {
  companion object {
    const val SYSTEM = "system"
  }

  @Primary
  @Bean(APP_ENTITY_MANAGER)
  fun appEntityManager(
    @Qualifier(APPLICATION_DATA_SOURCE) applicationDataSource: DataSource
  ): LocalContainerEntityManagerFactoryBean {
    val mergedJpaProperties =
      Properties().apply {
        this.putAll(jpaProperties.properties)
        this[SchemaToolingSettings.HBM2DDL_AUTO] = hibernateProperties.ddlAuto
      }

    return LocalContainerEntityManagerFactoryBean().apply {
      this.jpaVendorAdapter = HibernateJpaVendorAdapter()
      this.setJpaProperties(mergedJpaProperties)
      this.dataSource = applicationDataSource
      this.persistenceUnitName = "pointApp"
      this.setPackagesToScan("dev.teamuts.point.persistence.*.entity")
    }
  }

  @Bean(APP_JPA_QUERY_FACTORY)
  fun jpaQueryFactory(
    @Qualifier(APP_ENTITY_MANAGER) appEntityManager: EntityManager
  ): JPAQueryFactory {
    return JPAQueryFactory(appEntityManager)
  }

  @Bean
  fun auditorProvider(): AuditorAware<String> {
    return AuditorAware {
      Optional.of(SYSTEM)
    }
  }
}
