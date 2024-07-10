package com.yugyeong.iamstar.service

import com.yugyeong.iamstar.dto.PostRequest
import com.yugyeong.iamstar.dto.PostResponse
import com.yugyeong.iamstar.model.Comment
import com.yugyeong.iamstar.model.Post
import com.yugyeong.iamstar.repository.PostRepository
import com.yugyeong.iamstar.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PostService @Autowired constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    fun createPost(userId: String, postRequest: PostRequest): Post {
        val post = Post(
            userId = userId,
            content = postRequest.content,
            postUrl = postRequest.postUrl,
            timestamp = LocalDateTime.now()
        )

        return postRepository.save(post)
    }

    fun getAllPosts(): List<PostResponse> {
        return postRepository.findAllByOrderByTimestampDesc().map { post ->
            val user = userRepository.findById(post.userId).orElseThrow { RuntimeException("User not found") }
            PostResponse(
                id = post.id!!,
                username = user.username,
                fullName = user.fullName,
                profileUrl = user.profileUrl,
                content = post.content,
                postUrl = post.postUrl,
                likes = post.likes,
                comments = post.comments,
                timestamp = post.timestamp
            )
        }
    }

    fun addComment(postId: String, comment: Comment): Post {
        val post = postRepository.findById(postId).orElseThrow { RuntimeException("Post not found") }
        val user = userRepository.findById(comment.userId).orElseThrow { RuntimeException("User not found") }
        val updatedComment = comment.copy(userId = user.id!!)
        val updatedPost = post.copy(comments = post.comments + updatedComment)
        return postRepository.save(updatedPost)
    }
}
