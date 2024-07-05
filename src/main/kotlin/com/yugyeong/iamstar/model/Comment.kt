package com.yugyeong.iamstar.model

import java.time.LocalDateTime

data class Comment(
    val userId: String,
    val comment: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
