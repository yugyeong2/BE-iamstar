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
    private val userRepository: UserRepository,
    private val chatService: ChatService
) {

    fun createPost(userId: String, postRequest: PostRequest): Post {
        val post = Post(
            userId = userId,
            content = postRequest.content,
            postUrl = postRequest.postUrl,
            timestamp = LocalDateTime.now()
        )

        val savedPost = postRepository.save(post)
        val user = userRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val postResponse = PostResponse(
            id = savedPost.id!!,
            username = user.username,
            fullName = user.fullName,
            profileUrl = user.profileUrl,
            content = savedPost.content,
            postUrl = savedPost.postUrl,
            likes = savedPost.likes.size,
            comments = savedPost.comments,
            timestamp = savedPost.timestamp
        )
        chatService.ingestPost(postResponse) // 단일 게시글을 Chroma에 삽입
        return savedPost
    }

    fun getAllPosts(): List<PostResponse> {
        return postRepository.findAllByOrderByTimestampDesc().map { post ->
            val user = userRepository.findById(post.userId).orElseThrow { RuntimeException("User with ID ${post.userId} not found") }
            PostResponse(
                id = post.id!!,  // null이 아님을 보장
                username = user.username,
                fullName = user.fullName,
                profileUrl = user.profileUrl,
                content = post.content,
                postUrl = post.postUrl,
                likes = post.likes.size, // 게시글을 좋아요한 사용자 수를 반환
                comments = post.comments,
                timestamp = post.timestamp
            )
        }
    }

    fun likePost(postId: String, userId: String) {
        val post = postRepository.findById(postId).orElseThrow { RuntimeException("Post not found") }
        if (!post.likes.contains(userId)) {
            post.likes.add(userId)
            postRepository.save(post)
        }
    }

    fun unlikePost(postId: String, userId: String) {
        val post = postRepository.findById(postId).orElseThrow { RuntimeException("Post not found") }
        if (post.likes.contains(userId)) {
            post.likes.remove(userId)
            postRepository.save(post)
        }
    }

    fun isLiked(postId: String, userId: String): Boolean {
        val post = postRepository.findById(postId).orElseThrow { RuntimeException("Post not found") }
        return post.likes.contains(userId)
    }

    fun addComment(postId: String, comment: Comment): Post {
        val post = postRepository.findById(postId).orElseThrow { RuntimeException("Post not found") }
        post.comments.add(comment)
        return postRepository.save(post)
    }

    fun getComments(postId: String): List<Comment> {
        val post: Post = postRepository.findById(postId).orElseThrow {
            IllegalArgumentException("Invalid post ID: $postId")
        }
        return post.comments
    }
}
