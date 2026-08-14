package dev.teamuts.point

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PointAppApplication

fun main(args: Array<String>) {
    runApplication<PointAppApplication>(*args)
}
