package dev.teamuts.point.app.security

import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher

class AppRequestMatchers(
    private val matchers: List<AppRequestMatcher>
) {
    companion object {
        fun of(vararg matchers: AppRequestMatcher): AppRequestMatchers {
            return AppRequestMatchers(matchers.toList())
        }
    }

    fun merge(other: AppRequestMatchers): AppRequestMatchers {
        return AppRequestMatchers(this.matchers + other.matchers)
    }

    fun toOrRequestMatcher(): OrRequestMatcher {
        return OrRequestMatcher(
            matchers.map {
                PathPatternRequestMatcher
                    .withDefaults()
                    .matcher(it.httpMethod, it.urlPattern)
            }.toList()
        )
    }
}
