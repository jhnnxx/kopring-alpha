package org.jhnnx.jhnnxalpha.controller

import org.jhnnx.jhnnxalpha.dto.AuthDto
import org.jhnnx.jhnnxalpha.dto.CommonDto
import org.jhnnx.jhnnxalpha.model.AuthModel
import org.jhnnx.jhnnxalpha.repository.UserRepository
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
    private val userRepository: UserRepository,
) {
    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody request: AuthModel.SignUpRequest,
    ): ResponseEntity<CommonDto.ApiResponse<String>> =
        try {
            authService.signUp(request.email)

            ResponseEntity.ok(
                CommonDto.ApiResponse(
                    success = true,
                    message = "이메일이 전송되었습니다. 메일을 확인하여 인증을 완료해 주세요.",
                ),
            )
        } catch (e: Exception) {
            ResponseEntity.status(500).body(
                CommonDto.ApiResponse(
                    success = false,
                    message = "이메일 전송에 실패했습니다. (${e.message})",
                ),
            )
        }

    @PostMapping("/verify-email")
    fun verifyEmail(
        @RequestBody request: AuthModel.VerifyEmailRequest,
    ): ResponseEntity<CommonDto.ApiResponse<String>> =
        try {
            authService.verifyEmail(request.token)
            ResponseEntity.ok(CommonDto.ApiResponse(success = true, message = "이메일이 인증되었습니다."))
        } catch (e: Exception) {
            ResponseEntity
                .status(400)
                .body(CommonDto.ApiResponse(success = false, message = "이메일 인증에 실패했습니다. (${e.message})"))
        }

    @PostMapping("/set-password")
    fun setPassword(
        @RequestBody request: AuthModel.SetPasswordRequest,
    ): ResponseEntity<CommonDto.ApiResponse<String>> =
        try {
            authService.setPassword(request.email, request.password)
            ResponseEntity.ok(CommonDto.ApiResponse(success = true, message = "비밀번호가 설정되었습니다."))
        } catch (e: Exception) {
            ResponseEntity
                .status(400)
                .body(CommonDto.ApiResponse(success = false, message = "비밀번호 설정에 실패했습니다. (${e.message})"))
        }

    @PostMapping("/sign-in")
    fun signIn(
        @RequestBody request: AuthModel.LoginRequest,
    ): ResponseEntity<CommonDto.ApiResponse<AuthDto.LoginResponse>> =
        try {
            val (userInfo, token) = authService.signIn(request.email, request.password)

            ResponseEntity.ok(
                CommonDto.ApiResponse(
                    success = true,
                    message = "로그인에 성공했습니다.",
                    data =
                        AuthDto.LoginResponse(
                            userInfo = userInfo,
                            token = token,
                        ),
                ),
            )
        } catch (e: Exception) {
            ResponseEntity
                .status(401)
                .body(CommonDto.ApiResponse(success = false, message = "로그인에 실패했습니다. (${e.message})"))
        }
}
