package dev.teamuts.point.shared.extensions

import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

fun ContentCachingRequestWrapper.getRequestHeaders(): String {
    val request = this
    return mutableMapOf<String, String?>().apply {
        request.headerNames.toList().forEach {
            this@apply[it] = request.getHeader(it)
        }
    }.entries.joinToString("&")
}

fun ContentCachingRequestWrapper.getRequestParams(): String {
    return this.parameterMap.mapValues {
        it.value.joinToString(",")
    }.entries.joinToString("&")
}

fun ContentCachingRequestWrapper.getRequestBody(maxLength: Int): String {
    return this.contentAsByteArray.toStr(maxLength)
}

// ----------------------------------- //

fun ContentCachingResponseWrapper.getResponseBody(maxLength: Int): String {
    return this.contentAsByteArray.toStr(maxLength)
}

fun ContentCachingResponseWrapper.getResponseHeaders(): String {
    val response = this
    return mutableMapOf<String, String?>().apply {
        response.headerNames.toList().forEach {
            this@apply[it] = response.getHeader(it)
        }
    }.entries.joinToString("&")
}
