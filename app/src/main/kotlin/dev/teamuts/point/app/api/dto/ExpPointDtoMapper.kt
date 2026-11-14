package dev.teamuts.point.app.api.dto

import dev.teamuts.point.domain.exppoint.dto.app.ExpPointCommand
import dev.teamuts.shared.domain.model.Money
import org.mapstruct.*

@Mapper(
  componentModel = MappingConstants.ComponentModel.SPRING,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  unmappedTargetPolicy = ReportingPolicy.ERROR,
  imports = [Money::class],
)
interface ExpPointDtoMapper {
  @Mapping(
    target = "payAmount",
    expression = "java(Money.of(request.getPayAmount(), request.getCurrency()))",
  )
  fun of(request: EarnExpPointsRequestDto): ExpPointCommand.EarnExpPoints
}
