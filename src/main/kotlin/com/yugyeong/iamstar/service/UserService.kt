package com.yugyeong.iamstar.service

import com.yugyeong.iamstar.model.User
import com.yugyeong.iamstar.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun save(user: User): User {
        val hashedPassword = passwordEncoder.encode(user.password)
        val userWithHashedPassword = user.copy(password = hashedPassword)
        return userRepository.save(userWithHashedPassword)
    }
}
