package org.jhnnx.jhnnxalpha.controller

import org.jhnnx.jhnnxalpha.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequest,
    ): ResponseEntity<String> {
        authService.register(request.username, request.email, request.password)
        return ResponseEntity.ok("User Registered!")
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): ResponseEntity<String> {
        val token = authService.login(request.email, request.password)
        return ResponseEntity.ok(token)
    }
}

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
)

data class LoginRequest(
    val email: String,
    val password: String,
)
