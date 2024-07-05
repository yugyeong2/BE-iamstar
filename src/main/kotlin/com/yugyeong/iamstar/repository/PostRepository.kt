package com.yugyeong.iamstar.repository

import com.yugyeong.iamstar.model.Post
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : MongoRepository<Post, String> {
    fun findAllByOrderByTimestampDesc(): List<Post>
}