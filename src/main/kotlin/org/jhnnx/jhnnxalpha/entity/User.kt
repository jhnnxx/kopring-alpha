@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.jhnnx.jhnnxalpha.entity

import jakarta.persistence.*

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = true, unique = true)
    val email: String?,
    @Column(nullable = true)
    var password: String?,
    @Column(nullable = true, unique = true)
    val providerId: String?,
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    val provider: AuthProvider?,
    @Column(nullable = true)
    val username: String?,
    @Column(nullable = true)
    val profileImageUrl: String?,
    @Column(nullable = false)
    val emailVerified: Boolean = false,
    @Column(nullable = true, unique = true)
    var verificationToken: String?,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role = Role.USER,
)

enum class AuthProvider {
    GOOGLE,
    KAKAO,
    NAVER,
}

enum class Role {
    USER,
    ADMIN,
}
