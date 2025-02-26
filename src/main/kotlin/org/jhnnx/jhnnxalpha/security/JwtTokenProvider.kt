@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.jhnnx.jhnnxalpha.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.jhnnx.jhnnxalpha.entity.User
import org.jhnnx.jhnnxalpha.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.expiration}") private val expirationTime: Long,
    private val userRepository: UserRepository,
) {
    private val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun generateToken(user: User): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationTime)

        return Jwts
            .builder()
            .setSubject(user.email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun validateToken(token: String): Boolean =
        try {
            Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)

            true
        } catch (e: JwtException) {
            throw IllegalArgumentException("Invalid JWT token", e)
        }

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)
        val email = claims.subject

        val user: User = userRepository.findByEmail(email) ?: throw UsernameNotFoundException("사용자를 찾을 수 없습니다.")

        val userDetails = CustomUserDetails(user)
        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }

    private fun getClaims(token: String): Claims =
        Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

    fun getUsernameFromToken(token: String): String = getClaims(token).subject
}
