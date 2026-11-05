package dev.teamuts.point.app.logging.constant

import jakarta.servlet.http.HttpServletRequest

enum class HttpClientIpType(
    private val headerName: String,
    private val priority: Int = 0
) {
    X_FORWARDED_FOR("X-Forwarded-For"),
    PROXY_CLIENT_IP("Proxy-Client-IP"),
    WL_PROXY_CLIENT_IP("WL-Proxy-Client-IP"),
    HTTP_X_FORWARDED(""),
    HTTP_X_FORWARDED_FOR(""),
    HTTP_CLIENT_IP(""),
    X_REAL_IP("X-Real-IP", Int.MAX_VALUE);

    companion object {
        private const val UNKNOWN_CLIENT = "unknown"

        private val SORTED_PRIORITY_CLIENT_IP_TYPES: List<HttpClientIpType> =
            entries.toTypedArray().sortedByDescending { it.priority }

        fun getClientIpInHeader(request: HttpServletRequest): String {
            SORTED_PRIORITY_CLIENT_IP_TYPES.forEach {
                request.getHeader(it.headerName).also { headerValue: String? ->
                    if (headerValue.isNullOrBlank().not() && UNKNOWN_CLIENT.equals(
                            headerValue,
                            ignoreCase = true
                        ).not()
                    ) {
                        return headerValue
                    }
                }
            }

            return request.remoteAddr ?: UNKNOWN_CLIENT
        }
    }
}
