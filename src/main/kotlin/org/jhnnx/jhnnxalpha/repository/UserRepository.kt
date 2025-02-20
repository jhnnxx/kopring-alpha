package org.jhnnx.jhnnxalpha.repository

import org.jhnnx.jhnnxalpha.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}
