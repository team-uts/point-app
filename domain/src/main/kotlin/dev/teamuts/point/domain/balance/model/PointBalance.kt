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
  private var balanceAmount: PointBalanceAmount,
  totalAmount: Point,
  status: PointBalanceStatus,
  lastTransactionId: Long?,
) : BaseIdentifiableModel<Long>(id), PointBalanceAmountDetails {
  var totalAmount: Point = totalAmount
    private set

  var status: PointBalanceStatus = status
    private set

  var lastTransactionId: Long? = lastTransactionId
    private set

  override val cashAmount: Point
    get() = balanceAmount.cashAmount

  override val expAmount: Point
    get() = balanceAmount.expAmount

  override val currency: Currency
    get() = balanceAmount.currency

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

  /** @return current balance total amount + new balance amount */
  fun getValidBalanceTotalAmount(
    newBalanceAmount: PointBalanceAmount,
    operationType: BalanceOperationType,
  ): Point {
    checkActivated()

    val error =
      PointBalanceException.of(
        errorCode = PointBalanceErrorCode.INVALID_BALANCE_OPERATION,
        totalAmount,
        newBalanceAmount,
        operationType,
      )

    // check currency consistency
    check(totalAmount.currency == newBalanceAmount.currency) {
      throw error
    }

    return try {
      operationType.calculate(totalAmount, newBalanceAmount.getTotalPoint())
    } catch (_: Exception) {
      throw error
    }
  }

  /**
   * Updates the balance amount and total amount based on the provided point transaction.
   *
   * @param pointTransaction The newly created point transaction to apply to the balance.
   */
  fun updateBalanceAfterTransaction(pointTransaction: PointBalanceTransaction) {
    checkActivated()

    // check the last transaction ID to ensure the transaction is applied in order
    checkPointTransactionApplicable(pointTransaction)

    // update the balance amount based on the transaction
    balanceAmount = pointTransaction.calculateNewBalanceAmount(balanceAmount)
    println("updated balanceAmount: $balanceAmount")
    // update the total amount based on the transaction
    totalAmount =
      balanceAmount.getTotalPoint().also {
        // check the after balance amount is consistent with the calculated total amount
        check(pointTransaction.isSameAfterBalanceAmount(it)) {
          throw PointBalanceException.of(
            errorCode = PointBalanceErrorCode.INVALID_POINT_TRANSACTION_FOR_CALCULATION,
            this.id,
            pointTransaction.id,
          )
        }
      }

    // update the last transaction ID
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

  private fun checkPointTransactionApplicable(pointTransaction: PointBalanceTransaction) {
    lastTransactionId.also {
      check(it == null || it < pointTransaction.id) {
        throw PointBalanceException.of(
          errorCode = PointBalanceErrorCode.INVALID_POINT_TRANSACTION_FOR_CALCULATION,
          this.id,
          pointTransaction.id,
        )
      }
    }
  }
}
