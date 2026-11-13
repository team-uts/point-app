package dev.teamuts.point.domain.exppoint.error

import dev.teamuts.shared.error.constant.KtErrorCode
import org.springframework.http.HttpStatus

enum class ExpPointErrorCode(
  override val code: String,
  override val httpStatus: HttpStatus,
) : KtErrorCode {
  POINT_TEMPLATE_ISSUANCE_CURRENCY_NOT_CONSISTENT("EXPP-020", HttpStatus.BAD_REQUEST),
  POINT_TEMPLATE_EXPIRATION_INVALID("EXPP-0201", HttpStatus.BAD_REQUEST);

  override val messageKey: String = "exp_point.$name"
}
