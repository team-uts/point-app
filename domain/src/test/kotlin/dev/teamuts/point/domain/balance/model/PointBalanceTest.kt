package dev.teamuts.point.domain.balance.model

import dev.teamuts.point.domain.balance.constant.BalanceOperationType
import dev.teamuts.point.domain.balance.constant.BalanceTransactionType
import dev.teamuts.point.domain.balance.constant.OwnerType
import dev.teamuts.point.domain.balance.constant.PointBalanceStatus
import dev.teamuts.point.domain.balance.model.sub.PointBalanceAmount
import dev.teamuts.point.domain.common.model.Point
import dev.teamuts.shared.domain.constant.Currency
import java.math.BigDecimal
import java.util.stream.Stream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class PointBalanceTest {
  private lateinit var givenBalance: PointBalance

  @BeforeEach
  fun setup() {
    givenBalance =
      PointBalance.fromDatabase(
        id = 100L,
        memberId = 1L,
        ownerType = OwnerType.CUSTOMER,
        cashAmount = balanceCashAmount,
        expAmount = balanceExpAmount,
        totalAmount = balanceTotalAmount,
        currency = Currency.AUD,
        status = PointBalanceStatus.ACTIVE,
        lastTransactionId = null,
      )
  }

  @ParameterizedTest
  @MethodSource("providesNewBalanceAmountTestCases")
  fun `get valid balance total amount`(
    balanceAmount: PointBalanceAmount,
    operationType: BalanceOperationType,
    expectedAmount: Point,
  ) {
    val result =
      givenBalance.getValidBalanceTotalAmount(
        newBalanceAmount = balanceAmount,
        operationType = operationType,
      )

    assertThat(result).isEqualTo(expectedAmount)
  }

  @ParameterizedTest
  @MethodSource("providesTransactionAmountTestCases")
  fun `update balance amount test based on transaction`(
    operationType: BalanceOperationType,
    cashAmount: BigDecimal,
    expAmount: BigDecimal,
    expectedCashAmount: BigDecimal,
    expectedExpAmount: BigDecimal,
    expectedTotalAmount: BigDecimal,
  ) {
    val givenTransactionId = 1500L
    val transaction =
      PointBalanceTransaction.fromDatabase(
        id = givenTransactionId,
        balanceId = givenBalance.id,
        ownerType = givenBalance.ownerType,
        transactionType = BalanceTransactionType.EARN,
        operationType = operationType,
        cashAmount = cashAmount,
        expAmount = expAmount,
        afterBalanceAmount = expectedTotalAmount,
        currency = givenBalance.currency,
        paymentType = "TAXI_FARE",
        payTransactionId = "1234567",
      )

    givenBalance.updateBalanceAfterTransaction(transaction)

    assertThat(givenBalance.cashAmount.value).isEqualTo(expectedCashAmount)
    assertThat(givenBalance.expAmount.value).isEqualTo(expectedExpAmount)
    assertThat(givenBalance.totalAmount.value).isEqualTo(expectedTotalAmount)
    assertThat(givenBalance.lastTransactionId).isEqualTo(transaction.id)
  }

  companion object {
    private val balanceCashAmount: BigDecimal = BigDecimal("100.00")
    private val balanceExpAmount: BigDecimal = BigDecimal("1500.00")
    private val balanceTotalAmount: BigDecimal = balanceCashAmount.plus(balanceExpAmount)

    @JvmStatic
    fun providesNewBalanceAmountTestCases(): Stream<Arguments> {
      return Stream.of(
        Arguments.of(
          PointBalanceAmount.of(
            cashAmount = BigDecimal.ZERO,
            expAmount = BigDecimal("100.00"),
            currency = Currency.AUD,
          ),
          BalanceOperationType.CREDIT,
          Point.of(balanceTotalAmount.plus(BigDecimal("100.00")), Currency.AUD),
        ),
        Arguments.of(
          PointBalanceAmount.of(
            cashAmount = BigDecimal("50.00"),
            expAmount = BigDecimal("100.00"),
            currency = Currency.AUD,
          ),
          BalanceOperationType.DEBIT,
          Point.of(balanceTotalAmount.minus(BigDecimal("150.00")), Currency.AUD),
        ),
      )
    }

    @JvmStatic
    fun providesTransactionAmountTestCases(): Stream<Arguments> {
      return Stream.of(
        Arguments.of(
          BalanceOperationType.CREDIT,
          BigDecimal("100.00"),
          BigDecimal("200.00"),
          balanceCashAmount.plus(BigDecimal("100.00")),
          balanceExpAmount.plus(BigDecimal("200.00")),
          balanceTotalAmount.plus(BigDecimal("300.00")),
        ),
        Arguments.of(
          BalanceOperationType.DEBIT,
          BigDecimal("50.00"),
          BigDecimal("100.00"),
          balanceCashAmount.minus(BigDecimal("50.00")),
          balanceExpAmount.minus(BigDecimal("100.00")),
          balanceTotalAmount.minus(BigDecimal("150.00")),
        ),
      )
    }
  }
}
