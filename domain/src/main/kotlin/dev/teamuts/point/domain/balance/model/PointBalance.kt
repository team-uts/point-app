package dev.teamuts.point.domain.balance.model

import dev.teamuts.point.domain.balance.constant.BalanceOperationType
import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.balance.constant.PointBalanceStatus
import dev.teamuts.point.domain.balance.error.PointBalanceErrorCode
import dev.teamuts.point.domain.balance.error.PointBalanceException
import dev.teamuts.point.domain.balance.model.sub.PointBalanceAmount
import dev.teamuts.point.domain.balance.model.sub.PointBalanceAmountDetails
import dev.teamuts.point.domain.common.model.Point
import dev.teamuts.shared.domain.annotation.DomainModel
import dev.teamuts.shared.domain.constant.Currency
import dev.teamuts.shared.domain.model.BaseIdentifiableModel
import java.math.BigDecimal

@DomainModel
class PointBalance
private constructor(
  id: Long = 0L,
  val memberId: Long,
  val ownerType: OwnerType,
  private val balanceAmount: PointBalanceAmount,
  private var totalAmount: Point,
  status: PointBalanceStatus,
  lastTransactionId: Long?,
) : BaseIdentifiableModel<Long>(id), PointBalanceAmountDetails by balanceAmount {

  var lastTransactionId: Long? = lastTransactionId
    private set

  var status: PointBalanceStatus = status
    private set

  val totalAmountValue: BigDecimal = totalAmount.value

  companion object {
    fun newActive(
      memberId: Long,
      ownerType: OwnerType,
      currency: Currency,
    ): PointBalance {
      return PointBalance(
        memberId = memberId,
        ownerType = ownerType,
        balanceAmount = PointBalanceAmount.zero(currency),
        totalAmount = Point.zero(currency),
        status = PointBalanceStatus.ACTIVE,
        lastTransactionId = null,
      )
    }

    fun fromDatabase(
      id: Long,
      memberId: Long,
      ownerType: OwnerType,
      cashAmount: BigDecimal,
      expAmount: BigDecimal,
      totalAmount: BigDecimal,
      currency: Currency,
      status: PointBalanceStatus,
      lastTransactionId: Long?,
    ): PointBalance {
      return PointBalance(
        id = id,
        memberId = memberId,
        ownerType = ownerType,
        balanceAmount =
          PointBalanceAmount.of(
            cashAmount = cashAmount,
            expAmount = expAmount,
            currency = currency,
          ),
        totalAmount = Point.of(totalAmount, currency),
        status = status,
        lastTransactionId = lastTransactionId,
      )
    }
  }

  fun getValidAfterBalanceTotalAmount(
    pointAmount: Point,
    operationType: BalanceOperationType,
  ): Point {
    checkActivated()

    val error =
      PointBalanceException.of(
        errorCode = PointBalanceErrorCode.INVALID_BALANCE_OPERATION,
        totalAmount,
        pointAmount,
        operationType,
      )

    // check currency consistency
    check(totalAmount.hasSameCurrency(pointAmount)) {
      throw error
    }

    return try {
      operationType.calculate(totalAmount, pointAmount)
    } catch (_: Exception) {
      throw error
    }
  }

  fun updateBalanceAfterTransaction(pointTransaction: PointBalanceTransaction) {
    checkActivated()
    check(lastTransactionId == null || requireNotNull(lastTransactionId) < pointTransaction.id) {
      throw PointBalanceException.of(
        errorCode = PointBalanceErrorCode.INVALID_POINT_TRANSACTION_FOR_CALCULATION,
        this.id,
        pointTransaction.id,
      )
    }

    lastTransactionId = pointTransaction.id
  }

  private fun checkActivated() {
    checkPersisted()

    check(status == PointBalanceStatus.ACTIVE) {
      throw PointBalanceException.of(
        errorCode = PointBalanceErrorCode.INVALID_STATUS,
        status,
      )
    }
  }
}
