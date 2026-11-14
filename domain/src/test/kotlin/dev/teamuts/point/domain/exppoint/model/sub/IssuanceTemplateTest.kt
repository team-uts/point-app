package dev.teamuts.point.domain.exppoint.model.sub

import dev.teamuts.point.domain.exppoint.constant.PointIssuanceAmountType
import dev.teamuts.point.domain.exppoint.error.ExpPointErrorCode
import dev.teamuts.point.domain.exppoint.error.ExpPointException
import dev.teamuts.shared.domain.constant.Currency
import dev.teamuts.shared.domain.model.Percentage
import dev.teamuts.shared.utils.AppTimeUtils
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.stream.Stream
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class IssuanceTemplateTest {
  @ParameterizedTest
  @MethodSource("provideInvalidIssuanceCases")
  fun `throw exception when issuance setup is invalid`(testCase: IssuanceTemplateTestCase) {
    // When & Then
    assertThatThrownBy {
        IssuanceTemplate.of(
          issuanceStartsAt = testCase.issuanceStartsAt,
          issuanceEndsAt = testCase.issuanceEndsAt,
          issuanceAmountType = testCase.issuanceAmountType,
          issuanceAmount = testCase.issuanceAmount,
          issuancePercentage = testCase.issuancePercentage,
          maxIssuanceAmount = testCase.maxIssuanceAmount,
          issuanceCurrency = testCase.issuanceCurrency,
        )
      }
      .isInstanceOf(ExpPointException::class.java)
      .hasMessageContaining(ExpPointErrorCode.POINT_TEMPLATE_ISSUANCE_INVALID.name)
  }

  @ParameterizedTest
  @MethodSource("provideValidIssuanceCases")
  fun `create issuance template instance`(testCase: IssuanceTemplateTestCase) {
    val issuanceStartsAt = testCase.issuanceStartsAt
    val issuanceEndsAt = testCase.issuanceEndsAt
    val issuanceAmountType = testCase.issuanceAmountType
    val issuanceAmount = testCase.issuanceAmount
    val issuancePercentage = testCase.issuancePercentage
    val maxIssuanceAmount = testCase.maxIssuanceAmount
    val issuanceCurrency = testCase.issuanceCurrency

    val issuanceTemplate =
      IssuanceTemplate.of(
        issuanceStartsAt = issuanceStartsAt,
        issuanceEndsAt = issuanceEndsAt,
        issuanceAmountType = issuanceAmountType,
        issuanceAmount = issuanceAmount,
        issuancePercentage = issuancePercentage,
        maxIssuanceAmount = maxIssuanceAmount,
        issuanceCurrency = issuanceCurrency,
      )

    assertThat(issuanceTemplate.issuanceStartsAt).isEqualTo(issuanceStartsAt)
    assertThat(issuanceTemplate.issuanceEndsAt).isEqualTo(issuanceEndsAt)
    assertThat(issuanceTemplate.issuanceAmountType).isEqualTo(issuanceAmountType)
    assertThat(issuanceTemplate.issuanceAmountValue)
      .isEqualTo(issuanceAmountType.getAmountOrZero(issuanceAmount, issuanceCurrency).value)
    assertThat(issuanceTemplate.issuancePercentageValue)
      .isEqualTo(issuanceAmountType.getPercentageOrNull(issuancePercentage)?.value)
  }

  companion object {
    @JvmStatic
    fun provideInvalidIssuanceCases(): Stream<Arguments> {
      return Stream.of(
        Arguments.arguments(
          IssuanceTemplateTestCase(
            issuanceStartsAt = AppTimeUtils.getNowTime().plusDays(1),
            issuanceEndsAt = AppTimeUtils.getNowTime(),
          )
        ),
        Arguments.arguments(
          IssuanceTemplateTestCase(issuanceEndsAt = AppTimeUtils.getNowTime().minusDays(2))
        ),
        Arguments.arguments(
          IssuanceTemplateTestCase(
            issuanceAmountType = PointIssuanceAmountType.FIXED,
            issuanceAmount = BigDecimal.ZERO,
          )
        ),
        Arguments.arguments(
          IssuanceTemplateTestCase(
            issuanceAmountType = PointIssuanceAmountType.FIXED,
            issuanceAmount = BigDecimal("100"),
            maxIssuanceAmount = BigDecimal("50"),
          )
        ),
        Arguments.arguments(
          IssuanceTemplateTestCase(
            issuanceAmountType = PointIssuanceAmountType.PERCENTAGE,
            issuancePercentage = null,
          )
        ),
        Arguments.arguments(
          IssuanceTemplateTestCase(
            issuanceAmountType = PointIssuanceAmountType.PERCENTAGE,
            issuancePercentage = BigDecimal.ZERO,
          )
        ),
        Arguments.arguments(
          IssuanceTemplateTestCase(
            issuanceAmountType = PointIssuanceAmountType.PERCENTAGE,
            issuancePercentage = Percentage.ONE_HUNDRED.value,
          )
        ),
        Arguments.arguments(IssuanceTemplateTestCase(maxIssuanceAmount = BigDecimal.ZERO)),
      )
    }

    @JvmStatic
    fun provideValidIssuanceCases(): Stream<Arguments> {
      return Stream.of(
        Arguments.arguments(
          IssuanceTemplateTestCase(
            issuanceStartsAt = AppTimeUtils.getNowTime().plusDays(1),
            issuanceEndsAt = AppTimeUtils.getNowTime().plusDays(2),
          )
        ),
        Arguments.arguments(
          IssuanceTemplateTestCase(
            issuanceAmountType = PointIssuanceAmountType.FIXED,
            issuanceAmount = BigDecimal("100"),
            maxIssuanceAmount = BigDecimal("150"),
          )
        ),
        Arguments.arguments(
          IssuanceTemplateTestCase(
            issuanceAmountType = PointIssuanceAmountType.PERCENTAGE,
            issuancePercentage = BigDecimal("50"),
          )
        ),
        Arguments.arguments(IssuanceTemplateTestCase(maxIssuanceAmount = BigDecimal("1000"))),
      )
    }
  }

  data class IssuanceTemplateTestCase(
    val issuanceStartsAt: ZonedDateTime = AppTimeUtils.getNowTime().minusDays(1),
    val issuanceEndsAt: ZonedDateTime = AppTimeUtils.getNowTime().plusDays(1),
    val issuanceAmountType: PointIssuanceAmountType = PointIssuanceAmountType.FIXED,
    val issuanceAmount: BigDecimal = BigDecimal.valueOf(100),
    val issuancePercentage: BigDecimal? = null,
    val maxIssuanceAmount: BigDecimal = BigDecimal.valueOf(1000),
    val issuanceCurrency: Currency = Currency.AUD,
  )
}
