@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.jhnnx.jhnnxalpha.service

import org.jhnnx.jhnnxalpha.entity.Role
import org.jhnnx.jhnnxalpha.entity.User
import org.jhnnx.jhnnxalpha.model.AuthModel
import org.jhnnx.jhnnxalpha.repository.UserRepository
import org.jhnnx.jhnnxalpha.security.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val emailService: EmailService,
) {
    fun sendVerificationEmail(
        email: String,
        token: String,
    ) {
        val verificationLink = "http://localhost:3000/verify-email?token=$token"
        val subject = "[Connect] 이메일 인증을 완료해 주세요."
        val content = "아래 링크를 클릭하여 이메일 인증을 완료하세요: <a href='$verificationLink'>$verificationLink</a>"

        emailService.sendEmail(email, subject, content)
    }

    fun signUp(email: String): User {
        if (userRepository.existsByEmail(email)) {
            throw IllegalArgumentException("이미 사용 중인 이메일입니다.")
        }

        val verificationToken = UUID.randomUUID().toString()

        val user =
            User(
                email = email,
                username = null,
                password = null,
                profileImageUrl = null,
                emailVerified = false,
                provider = null,
                providerId = null,
                role = Role.USER,
                verificationToken = verificationToken,
            )

        val savedUser = userRepository.save(user)

        sendVerificationEmail(email, verificationToken)

        return savedUser
    }

    fun verifyEmail(token: String) {
        val user =
            userRepository.findByVerificationToken(token)
                ?: throw IllegalArgumentException("만료되었거나, 유효하지 않은 토큰입니다.")

        userRepository.save(user.copy(emailVerified = true, verificationToken = null))
    }

    fun setPassword(
        email: String,
        password: String,
    ): User {
        val user = userRepository.findByEmail(email) ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        if (!user.emailVerified) {
            throw IllegalArgumentException("이메일이 인증되지 않았습니다.")
        }

        val encodedPassword = passwordEncoder.encode(password)

        return userRepository.save(user.copy(password = encodedPassword))
    }

    fun signIn(
        email: String,
        password: String,
    ): Pair<AuthModel.UserInfo, String> {
        val user = userRepository.findByEmail(email) ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")
        if (!passwordEncoder.matches(password, user.password)) {
            throw IllegalArgumentException("비밀번호를 확인해 주세요.")
        }

        val token = jwtTokenProvider.generateToken(user)
        val userInfo =
            AuthModel.UserInfo(
                id = user.id,
                username = user.username,
                email = user.email,
                profileImageUrl = user.profileImageUrl,
                role = user.role,
            )
        return Pair(userInfo, token)
    }
}
