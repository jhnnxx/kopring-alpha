package org.jhnnx.jhnnxalpha.dto

class CommonDto {
    data class ApiResponse<T>(
        val success: Boolean,
        val message: String,
        val code: Int? = null,
        val data: T? = null,
    )
}
