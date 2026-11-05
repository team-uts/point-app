package dev.teamuts.point.app.security

import dev.teamuts.point.domain.auth.constant.RoleType
import org.springframework.http.HttpMethod

class AppRequestMatcher private constructor(
    val urlPattern: String,
    val httpMethod: HttpMethod? = null,
    val role: RoleType
) {
    companion object {
        fun noneRoleOf(urlPattern: String, httpMethod: HttpMethod): AppRequestMatcher {
            return AppRequestMatcher(
                urlPattern = urlPattern,
                httpMethod = httpMethod,
                role = RoleType.NONE
            )
        }

        fun of(urlPattern: String, httpMethod: HttpMethod, role: RoleType): AppRequestMatcher {
            return AppRequestMatcher(
                urlPattern = urlPattern,
                httpMethod = httpMethod,
                role = role
            )
        }
    }
}
