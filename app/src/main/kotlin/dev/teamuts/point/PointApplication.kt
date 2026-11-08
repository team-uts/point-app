package dev.teamuts.point

import dev.teamuts.shared.config.AppModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication class PointApplication

fun main(args: Array<String>) {
  SpringApplicationBuilder(PointApplication::class.java)
    .properties("spring.config.name=${AppModule.getApplicationModuleNames()}")
    .run(*args)
}
