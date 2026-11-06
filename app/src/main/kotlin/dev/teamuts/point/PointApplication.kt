package dev.teamuts.point

import dev.teamuts.point.shared.config.AppModule.APPLICATION_MODULES
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication class PointApplication

fun main(args: Array<String>) {
  SpringApplicationBuilder(PointApplication::class.java)
    .properties("spring.config.name=${APPLICATION_MODULES}")
    .run(*args)
}
