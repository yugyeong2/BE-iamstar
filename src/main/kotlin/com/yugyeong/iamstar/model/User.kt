package com.yugyeong.iamstar.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user")
data class User(
    @Id val id: String? = null,
    val email: String,
    val password: String,
    val username: String, // 닉네임
    val fullName: String, // 실제 이름
    val phoneNumber: String? = null,
    val profilePicture: String? = null,
    val bio: String? = null
)
