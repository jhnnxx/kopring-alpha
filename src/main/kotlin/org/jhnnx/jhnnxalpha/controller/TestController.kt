package org.jhnnx.jhnnxalpha.controller

import org.jhnnx.jhnnxalpha.security.CustomUserDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController {
    @GetMapping("/public")
    fun publicEndpoint(): String = "🔓 누구나 접근 가능(JWT NOT NEEDED)"

    @GetMapping("/protected")
    fun protectedEndpoint(
        @AuthenticationPrincipal user: CustomUserDetails,
    ): String = "🔐 인증된 사용자: ${user.username}"
}
