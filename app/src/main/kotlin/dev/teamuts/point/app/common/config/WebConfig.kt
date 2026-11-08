package dev.teamuts.point.app.common.config

import dev.teamuts.shared.error.ProblemDetailGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class WebConfig {
  @Bean
  fun problemDetailGenerator(
    messageSource: MessageSource,
    @Value($$"${spring.mvc.problemdetails.base-url}") baseUrl: String,
  ): ProblemDetailGenerator {
    return ProblemDetailGenerator(messageSource, baseUrl)
  }
}
