package dev.teamuts.point.app.security

import org.springdoc.core.properties.SpringDocConfigProperties
import org.springdoc.core.properties.SwaggerUiConfigProperties
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties
import org.springframework.boot.security.autoconfigure.web.StaticResourceLocation
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component

@Component
class AppRequestMatcherHolder(
  actuatorProperties: WebEndpointProperties,
  swaggerUiProperties: SwaggerUiConfigProperties,
  springDocProperties: SpringDocConfigProperties,
) {
  private val actuatorEndpoints: AppRequestMatchers =
    AppRequestMatchers.of(
      AppRequestMatcher.noneRoleOf(
        urlPattern = "${actuatorProperties.basePath}/**",
        httpMethod = HttpMethod.GET,
      )
    )

  private val swaggerUrls: AppRequestMatchers =
    AppRequestMatchers.of(
      AppRequestMatcher.noneRoleOf(
        urlPattern = swaggerUiProperties.getPath(),
        httpMethod = HttpMethod.GET,
      ),
      AppRequestMatcher.noneRoleOf(urlPattern = "/swagger-ui/**", httpMethod = HttpMethod.GET),
      AppRequestMatcher.noneRoleOf(
        urlPattern = "${springDocProperties.apiDocs.path}/**",
        httpMethod = HttpMethod.GET,
      ),
    )

  companion object {
    private val STATIC_RESOURCE_URL_LIST: List<AppRequestMatcher> =
      StaticResourceLocation.entries
        .flatMap { it.patterns.toList() }
        .map {
          AppRequestMatcher.noneRoleOf(
            urlPattern = it,
            httpMethod = HttpMethod.GET,
          )
        }
  }

  fun getShouldNotLoggingFilterMatchers(): AppRequestMatchers = actuatorEndpoints.merge(swaggerUrls)
}
