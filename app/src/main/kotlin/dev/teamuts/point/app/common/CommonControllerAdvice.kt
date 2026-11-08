package dev.teamuts.point.app.common

import dev.teamuts.shared.config.AppProfile
import dev.teamuts.shared.error.ProblemDetailGenerator
import dev.teamuts.shared.error.constant.CommonErrorCode
import dev.teamuts.shared.error.exception.ApiExternalException
import dev.teamuts.shared.error.exception.BusinessException
import dev.teamuts.shared.error.exception.SystemException
import dev.teamuts.shared.http.response.ApiCommonResponse
import dev.teamuts.shared.http.response.ErrorResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import java.util.*
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

private val log = KotlinLogging.logger {}

@RestControllerAdvice
class CommonControllerAdvice(
  private val appProfile: AppProfile,
  private val messageSource: MessageSource,
  private val problemDetailGenerator: ProblemDetailGenerator,
) {
  @ApiResponse(
    responseCode = "500",
    description = "Internal Server Error",
    content =
      [
        Content(
          mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
          schema = Schema(implementation = ProblemDetail::class),
          examples =
            [
              ExampleObject(
                value =
                  """
                  {
                  `"type": "https://api.payment.teamuts.dev/problem/internal-server-error",
                  "title": "INTERNAL_SERVER_ERROR",
                  "status": 500,
                  "code": "COM-0001",
                  "detail": "An unexpected error occurred. Please try again later.",
                  `"instance": "/v1/exp-points/12345"
                  }
                  """
              )
            ],
        )
      ],
  )
  @ExceptionHandler(Exception::class)
  fun handleException(
    e: Exception,
    request: HttpServletRequest,
    locale: Locale,
  ): ProblemDetail {
    val systemException =
      SystemException.fromOrDefault(
        /* cause = */ e,
        /* defaultCode = */ CommonErrorCode.INTERNAL_SERVER_ERROR,
      )

    logException(systemException)

    return problemDetailGenerator.generate(systemException, request, locale)
  }

  @ApiResponse(
    responseCode = "200",
    description = "Business Exception",
    content =
      [
        Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = Schema(implementation = ApiCommonResponse::class),
          examples =
            [
              ExampleObject(
                value =
                  """
                  {
                    "result": "FAILURE",
                    "error": {
                      "code": "EXP-0011",
                      "type": "MEMBER_MISMATCH",
                      "message": "Order (extOrderNum: {0}) does not belong to member (memberId: {1})."
                    }
                  }
                  """
              )
            ],
        )
      ],
  )
  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler(BusinessException::class)
  fun handleBusinessException(e: BusinessException, locale: Locale): ApiCommonResponse<Nothing> {
    logException(e)

    val errorResponse =
      ErrorResponse.of(
        /* errorCode = */ e.errorCode,
        /* message = */ messageSource.getMessage(e.errorCode.messageKey, e.args, locale),
      )

    return ApiCommonResponse.failure(errorResponse)
  }

  private fun logException(e: ApiExternalException) {
    val logMessage = if (e is SystemException) e.logMessage else e.message

    if (appProfile.isLocal) {
      log.error(e.cause) {
        "Code: ${e.errorCode.name()} / Exception: ${e.javaClass.getSimpleName()} / Msg: $logMessage /"
      }
      return
    }

    log.error {
      "Code: ${e.errorCode.name()} / Exception: ${e.javaClass.getSimpleName()} / Msg: $logMessage"
    }
  }
}
