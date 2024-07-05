package com.yugyeong.iamstar.dto

import com.yugyeong.iamstar.model.Comment
import java.time.LocalDateTime

data class PostResponse(
    val id: String,
    val username: String,
    val fullName: String,
    val profileUrl: String,
    val content: String,
    val postUrl: String,
    val likes: Int,
    val comments: List<Comment>,
    val timestamp: LocalDateTime
)
