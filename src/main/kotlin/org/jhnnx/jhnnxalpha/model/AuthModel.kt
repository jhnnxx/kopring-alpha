package org.jhnnx.jhnnxalpha.model

import org.jhnnx.jhnnxalpha.entity.Role

class AuthModel {
    data class UserInfo(
        val id: Long,
        val username: String?,
        val email: String?,
        val profileImageUrl: String?,
        val role: Role,
    )

    data class SignUpRequest(
        val email: String,
    )

    data class VerifyEmailRequest(
        val token: String,
    )

    data class SetPasswordRequest(
        val email: String,
        val password: String,
    )

    data class LoginRequest(
        val email: String,
        val password: String,
    )
}
