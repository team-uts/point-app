package dev.teamuts.point.app.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/exp-points")
class ExpPointApiController {
    @GetMapping("/test")
    fun test(): String {
        return "ExpPointApiController test()"
    }
}
