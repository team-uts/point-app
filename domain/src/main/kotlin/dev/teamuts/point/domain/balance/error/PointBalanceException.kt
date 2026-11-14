package dev.teamuts.point.domain.balance.error

import dev.teamuts.shared.error.exception.BusinessException
import dev.teamuts.shared.error.exception.ErrorArguments

class PointBalanceException : BusinessException {
  constructor(
    errorCode: PointBalanceErrorCode,
    vararg args: Any?,
  ) : super(errorCode, ErrorArguments.of(*args))

  companion object {
    fun of(errorCode: PointBalanceErrorCode, vararg args: Any?): PointBalanceException {
      return PointBalanceException(errorCode, *args)
    }
  }
}
