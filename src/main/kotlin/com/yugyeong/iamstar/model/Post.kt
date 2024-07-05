package com.yugyeong.iamstar.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "post")
data class Post(
    @Id val id: String? = null,
    val userId: String,
    val content: String,
    val postUrl: String,
    val likes: Int = 0,
    val comments: List<Comment> = listOf(),
    var timestamp: LocalDateTime = LocalDateTime.now()
)