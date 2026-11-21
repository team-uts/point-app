package dev.teamuts.point

import dev.teamuts.shared.config.AppModule
import dev.teamuts.shared.utils.AppTimeUtils
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class PointApplication {
  @PostConstruct
  fun init() {
    AppTimeUtils.setDefaultTimezone()
  }
}

fun main(args: Array<String>) {
  SpringApplicationBuilder(PointApplication::class.java)
    .properties("spring.config.name=${AppModule.getApplicationModuleNames()}")
    .run(*args)
}
