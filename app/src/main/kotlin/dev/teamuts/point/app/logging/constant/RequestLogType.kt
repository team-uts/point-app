package dev.teamuts.point.app.logging.constant

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.MDC
import java.util.*

enum class RequestLogType(
    private val key: String,
    private val valueExtractor: (HttpServletRequest) -> String?
) {
    REQUEST_ID("request_id", { UUID.randomUUID().toString().substring(0, 8) }),
    REQUEST_URI("request_uri", { it.requestURI }),
    HTTP_METHOD("http_method", { it.method }),
    REQUEST_QUERY_PARAM("request_query_param", { it.queryString }),
    ;

    companion object {
        fun putAllLogsToMdc(request: HttpServletRequest) {
            entries
                .forEach {
                    MDC.put(it.key, it.valueExtractor.invoke(request))
                }
        }

        fun removeAllMdcLogs() {
            entries.forEach {
                MDC.remove(it.key)
            }
        }
    }
}
