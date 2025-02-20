package org.jhnnx.jhnnxalpha.service

import org.jhnnx.jhnnxalpha.entity.User
import org.jhnnx.jhnnxalpha.repository.UserRepository
import org.jhnnx.jhnnxalpha.util.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun register(username: String, email: String, password: String): User {
        if (userRepository.existsByEmail(email)) {
            throw IllegalArgumentException("Email already in use")
        }
        val encodedPassword = passwordEncoder.encode(password)
        return userRepository.save(User(username = username, email = email, password = encodedPassword))
    }

    fun login(email: String, password: String): String {
        val user = userRepository.findByEmail(email) ?: throw IllegalArgumentException("User not found")
        if (!passwordEncoder.matches(password, user.password)) {
            throw IllegalArgumentException("Invalid credentials")
        }
        return jwtTokenProvider.generateToken(user.username)
    }
}
