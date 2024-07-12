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
    var likes: MutableList<String> = mutableListOf(),
    val comments: MutableList<Comment> = mutableListOf(),
    var timestamp: LocalDateTime = LocalDateTime.now()
)
