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
        // 여기서 다시 비밀번호를 해싱하지 않습니다.
        return userRepository.save(user)
   }
}
