package com.yugyeong.iamstar.repository

import com.yugyeong.iamstar.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findByEmail(email: String): User?
}
