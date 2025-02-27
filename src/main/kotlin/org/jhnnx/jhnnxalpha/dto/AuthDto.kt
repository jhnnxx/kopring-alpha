package org.jhnnx.jhnnxalpha.dto

import org.jhnnx.jhnnxalpha.model.AuthModel

class AuthDto {
    data class LoginResponse(
        val userInfo: AuthModel.UserInfo,
        val token: String,
    )
}
