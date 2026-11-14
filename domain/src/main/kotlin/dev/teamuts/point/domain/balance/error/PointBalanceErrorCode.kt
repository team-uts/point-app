package dev.teamuts.point.domain.balance.error

import dev.teamuts.shared.error.constant.KtErrorCode
import org.springframework.http.HttpStatus

enum class PointBalanceErrorCode(
  override val code: String,
  override val httpStatus: HttpStatus,
) : KtErrorCode {
  INVALID_BALANCE_OPERATION("PBL-100", HttpStatus.BAD_REQUEST),
  INVALID_STATUS("PBL-101", HttpStatus.BAD_REQUEST),
  INVALID_POINT_TRANSACTION_FOR_CALCULATION("PBL-102", HttpStatus.BAD_REQUEST);

  override val messageKey: String = "point_balance.$name"
}
