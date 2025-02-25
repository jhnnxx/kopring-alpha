@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.jhnnx.jhnnxalpha.util

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.expiration}") private val expirationTime: Long,
) {
    fun generateToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationTime)
        val key: Key = SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS512.jcaName)

        return Jwts
            .builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean =
        try {
            val key: Key = SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS512.jcaName)
            Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)

            true
        } catch (e: JwtException) {
            throw IllegalArgumentException("Invalid JWT token", e)
        }

    fun getUsernameFromToken(token: String): String {
        val key: Key = SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS512.jcaName)

        return Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body.subject
    }
}
