package org.jhnnx.jhnnxalpha.exception

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_UNAUTHORIZED

        val errorResponse =
            mapOf(
                "timestamp" to LocalDateTime.now().toString(),
                "status" to 401,
                "error" to "Unauthorized",
                "message" to authException.message,
                "path" to request.requestURI,
            )

        val objectMapper = ObjectMapper()
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}
