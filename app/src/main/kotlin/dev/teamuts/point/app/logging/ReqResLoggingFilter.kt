package dev.teamuts.point.app.logging

import dev.teamuts.point.app.logging.constant.RequestLogType
import dev.teamuts.point.app.security.AppRequestMatcherHolder
import dev.teamuts.point.shared.config.AppProfile
import dev.teamuts.point.shared.utils.TimeUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.IOException

private val log = KotlinLogging.logger {}

@Component
@Order(org.springframework.core.Ordered.HIGHEST_PRECEDENCE)
class ReqResLoggingFilter(
    private val appRequestMatcherHolder: AppRequestMatcherHolder,
    private val appProfile: AppProfile
) : OncePerRequestFilter() {
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return appRequestMatcherHolder
            .getShouldNotLoggingFilterMatchers()
            .toOrRequestMatcher()
            .matches(request)
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestWrapper = ContentCachingRequestWrapper(request, -1)
        val responseWrapper = ContentCachingResponseWrapper(response)

        RequestLogType.putAllLogsToMdc(requestWrapper)

        val startTime: Long = System.currentTimeMillis()
        filterChain.doFilter(requestWrapper, responseWrapper)
        val endTime: Long = System.currentTimeMillis()

        val elapsedTime: Double =
            TimeUtils.convertMillisToSecWithPrecision(endTime - startTime)

        try {
            log.info {
                HttpLogMessage.newInstance(
                    requestWrapper = requestWrapper,
                    responseWrapper = responseWrapper,
                    elapsedTime = elapsedTime
                ).getLogMsg(appProfile)
            }

            responseWrapper.copyBodyToResponse()
        } catch (e: Exception) {
            log.error(e) { "[${this.javaClass.getSimpleName()}] Logging failed" }
        }

        RequestLogType.removeAllMdcLogs()
    }
}
