package dev.teamuts.point.app.api

import dev.teamuts.point.app.api.dto.ExpPointDtoMapper
import dev.teamuts.point.app.api.dto.GrantExpPointsRequestDto
import dev.teamuts.point.domain.exppoint.usecase.GrantExpPointsUseCase
import dev.teamuts.shared.http.response.ApiCommonResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Expirable Points API", description = "Expirable Points Domain")
@RestController
@RequestMapping("/v1/exp-points")
class ExpPointApiController(
  private val expPointDtoMapper: ExpPointDtoMapper,
  private val grantExpPointsUseCase: GrantExpPointsUseCase,
) {

  @Operation(summary = "Grant Expirable Points", description = "Grant Expirable Points to a member")
  @GetMapping("/grant")
  fun grantExpirablePoints(
    @RequestParam memberId: Long,
    @RequestBody request: GrantExpPointsRequestDto,
  ): ApiCommonResponse<String> {
    val command = expPointDtoMapper.of(request)

    grantExpPointsUseCase.execute(memberId, command)

    return ApiCommonResponse.success("ExpPointApiController test()")
  }
}
