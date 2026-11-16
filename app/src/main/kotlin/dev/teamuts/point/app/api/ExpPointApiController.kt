package dev.teamuts.point.app.api

import dev.teamuts.point.app.api.dto.EarnExpPointsRequestDto
import dev.teamuts.point.app.api.dto.ExpPointDtoMapper
import dev.teamuts.point.domain.exppoint.dto.app.ExpPointInfo
import dev.teamuts.point.domain.exppoint.usecase.EarnExpPointsUseCase
import dev.teamuts.shared.http.response.ApiCommonResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Expirable Points API", description = "Expirable Points Domain")
@RestController
@RequestMapping("/v1/exp-points")
class ExpPointApiController(
  private val expPointDtoMapper: ExpPointDtoMapper,
  private val earnExpPointsUseCase: EarnExpPointsUseCase,
) {
  @Operation(
    summary = "Earn Expirable Points",
    description = "Grant Earning Expirable Points to a member",
  )
  @PostMapping("/earn")
  fun grantExpirablePoints(
    @RequestParam memberId: Long,
    @RequestBody request: EarnExpPointsRequestDto,
  ): ApiCommonResponse<ExpPointInfo.EarnedExpPointInfo> {
    val command = expPointDtoMapper.of(request)

    val result =
      earnExpPointsUseCase.execute(
        memberId = memberId,
        command = command,
      )

    return ApiCommonResponse.success(result)
  }
}
