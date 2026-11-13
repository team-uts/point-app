package dev.teamuts.point.domain.exppoint.model.sub

import dev.teamuts.point.domain.exppoint.constant.PointExpirationType
import dev.teamuts.point.domain.exppoint.error.ExpPointErrorCode
import dev.teamuts.point.domain.exppoint.error.ExpPointException
import dev.teamuts.shared.utils.AppTimeUtils
import java.time.ZonedDateTime
import java.util.stream.Stream
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class ExpirationTemplateTest {
  @ParameterizedTest
  @MethodSource("provideInvalidExpirationInputs")
  fun `throw exception when expiration setup is invalid`(
    expirationType: PointExpirationType,
    expirationPeriod: Int?,
    expiresAt: ZonedDateTime?,
  ) {
    // When & Then
    assertThatThrownBy {
        ExpirationTemplate.of(
          expirationType = expirationType,
          expirationPeriod = expirationPeriod,
          expiresAt = expiresAt,
        )
      }
      .isInstanceOf(ExpPointException::class.java)
      .hasMessageContaining(ExpPointErrorCode.POINT_TEMPLATE_EXPIRATION_INVALID.name)
  }

  @ParameterizedTest
  @MethodSource("provideValidExpirationInputs")
  fun `create expiration template instance`(testCase: ExpirationTemplateTestCase) {
    val expirationTemplate =
      ExpirationTemplate.of(
        expirationType = testCase.expirationType,
        expirationPeriod = testCase.expirationPeriod,
        expiresAt = testCase.expiresAt,
      )

    assertThat(expirationTemplate.expirationType).isEqualTo(testCase.expirationType)
    assertThat(expirationTemplate.expirationPeriod).isEqualTo(testCase.expectedExpPeriod)
    assertThat(expirationTemplate.expiresAt).isEqualTo(testCase.expectedExpiresAt)
  }

  companion object {
    @JvmStatic
    fun provideInvalidExpirationInputs(): Stream<Arguments> {
      return Stream.of(
        Arguments.arguments(PointExpirationType.FIXED, null, null),
        Arguments.arguments(
          PointExpirationType.FIXED,
          null,
          AppTimeUtils.getNowTime().minusDays(1),
        ),
        Arguments.arguments(PointExpirationType.YEAR, null, null),
        Arguments.arguments(PointExpirationType.YEAR, 0, null),
        Arguments.arguments(PointExpirationType.MONTH, null, null),
      )
    }

    @JvmStatic
    fun provideValidExpirationInputs(): Stream<Arguments> {
      val validExpiresAt = AppTimeUtils.getNowTime().plusDays(1)

      return Stream.of(
        Arguments.arguments(
          ExpirationTemplateTestCase(
            expirationType = PointExpirationType.FIXED,
            expirationPeriod = 3,
            expiresAt = validExpiresAt,
            expectedExpPeriod = null,
            expectedExpiresAt = validExpiresAt,
          )
        ),
        Arguments.arguments(
          ExpirationTemplateTestCase(
            expirationType = PointExpirationType.FIXED,
            expirationPeriod = -1,
            expiresAt = validExpiresAt,
            expectedExpPeriod = null,
            expectedExpiresAt = validExpiresAt,
          )
        ),
        Arguments.arguments(
          ExpirationTemplateTestCase(
            expirationType = PointExpirationType.YEAR,
            expirationPeriod = 3,
            expiresAt = AppTimeUtils.getNowTime().minusDays(3),
            expectedExpPeriod = 3,
            expectedExpiresAt = null,
          )
        ),
        Arguments.arguments(
          ExpirationTemplateTestCase(
            expirationType = PointExpirationType.YEAR,
            expirationPeriod = 1,
            expiresAt = null,
            expectedExpPeriod = 1,
            expectedExpiresAt = null,
          )
        ),
        Arguments.arguments(
          ExpirationTemplateTestCase(
            expirationType = PointExpirationType.MONTH,
            expirationPeriod = 6,
            expiresAt = null,
            expectedExpPeriod = 6,
            expectedExpiresAt = null,
          )
        ),
      )
    }
  }

  data class ExpirationTemplateTestCase(
    val expirationType: PointExpirationType,
    val expirationPeriod: Int?,
    val expiresAt: ZonedDateTime?,
    val expectedExpPeriod: Int?,
    val expectedExpiresAt: ZonedDateTime?,
  )
}
