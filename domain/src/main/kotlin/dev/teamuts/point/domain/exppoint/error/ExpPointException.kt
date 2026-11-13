package dev.teamuts.point.domain.exppoint.error

import dev.teamuts.shared.error.exception.BusinessException
import dev.teamuts.shared.error.exception.ErrorArguments

class ExpPointException : BusinessException {
  constructor(errorCode: ExpPointErrorCode) : super(errorCode)

  constructor(
    errorCode: ExpPointErrorCode,
    vararg args: Any?,
  ) : super(errorCode, ErrorArguments.of(*args))

  companion object {
    fun of(errorCode: ExpPointErrorCode, vararg args: Any?): ExpPointException {
      return ExpPointException(errorCode, *args)
    }
  }
}
