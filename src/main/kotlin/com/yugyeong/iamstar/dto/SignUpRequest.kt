package com.yugyeong.iamstar.dto

data class SignUpRequest(
    val email: String,
    val password: String,
    val username: String,
    val fullName: String
)