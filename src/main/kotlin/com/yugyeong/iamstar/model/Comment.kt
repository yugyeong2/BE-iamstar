package com.yugyeong.iamstar.model

import java.time.LocalDateTime

data class Comment(
    val username: String,
    val comment: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
