package dev.teamuts.point.domain.balance.service

import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.balance.model.PointBalance
import dev.teamuts.point.domain.balance.model.PointBalanceTransaction
import dev.teamuts.point.domain.balance.port.PointBalanceReaderPort
import dev.teamuts.point.domain.balance.port.PointBalanceStorePort
import dev.teamuts.point.domain.balance.port.PointBalanceUpdatePort
import dev.teamuts.shared.data.annotation.AppTransactional
import dev.teamuts.shared.domain.constant.Currency
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation

@Service
@AppTransactional
class PointBalanceService(
  private val pointBalanceReaderPort: PointBalanceReaderPort,
  private val pointBalanceUpdatePort: PointBalanceUpdatePort,
  private val pointBalanceStorePort: PointBalanceStorePort,
) {
  fun getPointBalanceByIdWithLock(balanceId: Long): PointBalance {
    return pointBalanceReaderPort.retrieveByIdWithLock(balanceId)
  }

  /**
   * Creates a new point balance in a separate transaction, independent of the caller's transaction.
   * trying to get lock on the balance before creating a new one could cause deadlock because of gap
   * lock in mysql.
   *
   * (if trying to get lock on non-existing balance, mysql could create a gap lock for it.)
   *
   * Therefore, it should check first if the balance exists or not before locking.
   */
  @AppTransactional(propagation = Propagation.REQUIRES_NEW)
  fun getOrCreateNewActiveBalanceIndependently(
    memberId: Long,
    ownerType: OwnerType,
    currency: Currency,
  ): PointBalance {
    return pointBalanceReaderPort.retrieveOrNullByUnique(
      memberId = memberId,
      ownerType = ownerType,
      currency = currency,
    )
      ?: pointBalanceStorePort.store(
        PointBalance.newActive(
          memberId = memberId,
          ownerType = ownerType,
          currency = currency,
        ),
      )
  }

  fun updateBalanceAfterPointProcess(
    pointBalance: PointBalance,
    pointTransaction: PointBalanceTransaction,
  ) {
    pointBalance.updateBalanceAfterTransaction(pointTransaction)

    pointBalanceUpdatePort.updateBalanceAmountAndTransactionId(pointBalance)
  }
}
