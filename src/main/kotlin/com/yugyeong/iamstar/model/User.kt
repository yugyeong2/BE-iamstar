package com.yugyeong.iamstar.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

const val DEFAULT_PROFILE_IMAGE_URL = "https://iamstar.s3.ap-northeast-2.amazonaws.com/profiles/default_profile.png"

@Document(collection = "user")
data class User(
    @Id val id: String? = null,
    val email: String,
    val password: String,
    val username: String, // 닉네임
    val fullName: String, // 실제 이름
    val profileUrl: String = DEFAULT_PROFILE_IMAGE_URL
)
