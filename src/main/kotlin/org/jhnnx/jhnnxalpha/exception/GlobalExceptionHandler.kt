package org.jhnnx.jhnnxalpha.exception

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_BAD_REQUEST

        val errorResponse =
            mutableMapOf<String, Any>(
                "timestamp" to LocalDateTime.now().toString(),
                "status" to HttpServletResponse.SC_BAD_REQUEST,
                "error" to "Bad Request",
                "message" to "Validation Failed",
                "path" to request.requestURI,
            )

        val errors =
            ex.bindingResult.fieldErrors.map { fieldError ->
                mapOf(
                    "field" to fieldError.field,
                    "message" to fieldError.defaultMessage,
                )
            }
        errorResponse["errors"] = errors

        val objectMapper = ObjectMapper()
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        ex: HttpMessageNotReadableException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_BAD_REQUEST

        val errorResponse =
            mapOf(
                "timestamp" to LocalDateTime.now().toString(),
                "status" to HttpServletResponse.SC_BAD_REQUEST,
                "error" to "Bad Request",
                "message" to "Malformed JSON request",
                "path" to request.requestURI,
            )

        val objectMapper = ObjectMapper()
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(
        ex: NoSuchElementException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_NOT_FOUND

        val errorResponse =
            mapOf(
                "timestamp" to LocalDateTime.now().toString(),
                "status" to HttpServletResponse.SC_NOT_FOUND,
                "error" to "Not Found",
                "message" to ex.message,
                "path" to request.requestURI,
            )

        val objectMapper = ObjectMapper()
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(
        ex: RuntimeException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR

        val errorResponse =
            mapOf(
                "timestamp" to LocalDateTime.now().toString(),
                "status" to HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "error" to "Internal Server Error",
                "message" to ex.message,
                "path" to request.requestURI,
            )

        val objectMapper = ObjectMapper()
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}
