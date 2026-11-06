package dev.teamuts.point.app.logging

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.teamuts.point.app.logging.constant.HttpClientIpType
import dev.teamuts.point.shared.config.AppProfile
import dev.teamuts.point.shared.extensions.*
import org.springframework.http.HttpStatus
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import org.yaml.snakeyaml.util.UriEncoder

class HttpLogMessage(
  val httpMethod: String,
  val requestUri: String,
  val httpStatus: Int,
  val clientIp: String,
  val elapsedTime: Double = 0.0,
  val requestHeaders: String?,
  val requestParam: String?,
  val requestBody: String?,
  val responseHeaders: String?,
  val responseBody: String?,
) {
  companion object {
    private const val INPUT = "INPUT"
    private const val OUTPUT = "OUTPUT"
    private const val MAXIMUM_BYTE_LENGTH = 1024

    private val gson: Gson =
      GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    fun newInstance(
      requestWrapper: ContentCachingRequestWrapper,
      responseWrapper: ContentCachingResponseWrapper,
      elapsedTime: Double,
    ): HttpLogMessage {
      return HttpLogMessage(
        httpMethod = requestWrapper.method,
        requestUri = requestWrapper.requestURI,
        httpStatus = HttpStatus.valueOf(responseWrapper.status).value(),
        clientIp = HttpClientIpType.getClientIpInHeader(requestWrapper),
        elapsedTime = elapsedTime,
        requestHeaders = requestWrapper.getRequestHeaders(),
        requestParam = requestWrapper.getRequestParams(),
        requestBody = requestWrapper.getRequestBody(MAXIMUM_BYTE_LENGTH),
        responseHeaders = responseWrapper.getResponseHeaders(),
        responseBody = responseWrapper.getResponseBody(MAXIMUM_BYTE_LENGTH),
      )
    }
  }

  fun getLogMsg(appProfile: AppProfile): String {
    if (appProfile.isLocal()) {
      return this.toPrettierLog()
    }

    return this.toJson()
  }

  private fun toJson(): String {
    return gson.toJson(this)
  }

  private fun toPrettierLog(): String {
    return """
      |
      |[REQUEST] ${this.httpMethod} ${UriEncoder.decode(this.requestUri)} ${this.httpStatus} (${this.elapsedTime}s)
      |>> CLIENT_IP: ${this.clientIp}
      |>> [$INPUT] REQUEST_HEADERS: ${this.requestHeaders}
      |>> [$INPUT] REQUEST_PARAM: ${this.requestParam}
      |>> [$INPUT] REQUEST_BODY: ${this.requestBody}
      |>> [$OUTPUT] RESPONSE_HEADERS: ${this.responseHeaders}
      |>> [$OUTPUT] RESPONSE_BODY: ${this.responseBody}
      """
      .trimMargin()
  }
}
