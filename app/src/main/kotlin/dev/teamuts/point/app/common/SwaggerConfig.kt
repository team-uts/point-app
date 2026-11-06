package dev.teamuts.point.app.common

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.properties.SwaggerUiConfigProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration(proxyBeanMethods = false)
class SwaggerConfig {
  @Bean
  fun openApi(): OpenAPI {
    return OpenAPI()
      .info(Info().title("Point API").description("Point API documentation").version("v1"))
  }

  @Primary
  @Bean
  fun swaggerUiConfig(): SwaggerUiConfigProperties {
    return SwaggerUiConfigProperties().apply {
      this.setDefaultModelsExpandDepth(-1) // Disable model expansion by default
      this.setTagsSorter("alpha") // Sort tags alphabetically
      this.setOperationsSorter("alpha") // Sort operations alphabetically
    }
  }
}
