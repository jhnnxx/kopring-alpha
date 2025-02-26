package org.jhnnx.jhnnxalpha.request

class AuthRequest {
    data class SignUp(
        val email: String,
    )

    data class VerifyEmail(
        val token: String,
    )

    data class SetPassword(
        val email: String,
        val password: String,
    )

    data class Login(
        val email: String,
        val password: String,
    )
}
