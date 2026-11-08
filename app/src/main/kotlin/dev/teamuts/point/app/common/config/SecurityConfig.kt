package dev.teamuts.point.app.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
class SecurityConfig {
  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    return http
      .csrf { it.disable() }
      .formLogin { it.disable() }
      .cors(withDefaults())
      .authorizeHttpRequests {
        it.anyRequest().permitAll()
      }
      .sessionManagement {
        it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      }
      .build()
  }
}
