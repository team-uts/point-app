package dev.teamuts.point.persistence.common.config

import dev.teamuts.point.persistence.common.constant.JpaConstants.APP_ENTITY_MANAGER
import dev.teamuts.point.shared.data.TransactionConstants.APP_TRANSACTION_MANAGER
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement
class TransactionConfig {
  @Primary
  @Bean(APP_TRANSACTION_MANAGER)
  fun appTransactionManager(
    @Qualifier(APP_ENTITY_MANAGER) appEntityManager: LocalContainerEntityManagerFactoryBean
  ): PlatformTransactionManager {
    return JpaTransactionManager().apply {
      this.entityManagerFactory = appEntityManager.`object`
    }
  }
}
