package dev.teamuts.point.app.api.dto

import dev.teamuts.point.domain.exppoint.dto.app.ExpPointCommand
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.mapstruct.ReportingPolicy

@Mapper(
  componentModel = MappingConstants.ComponentModel.SPRING,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  unmappedTargetPolicy = ReportingPolicy.ERROR,
)
interface ExpPointDtoMapper {
  fun of(request: GrantExpPointsRequestDto): ExpPointCommand.GrantExpPoints
}
