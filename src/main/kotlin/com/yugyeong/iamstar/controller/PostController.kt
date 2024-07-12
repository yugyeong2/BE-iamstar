package com.yugyeong.iamstar.controller

import com.yugyeong.iamstar.dto.PostRequest
import com.yugyeong.iamstar.dto.PostResponse
import com.yugyeong.iamstar.model.Comment
import com.yugyeong.iamstar.model.Post
import com.yugyeong.iamstar.service.PostService
import com.yugyeong.iamstar.service.UserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/post")
class PostController @Autowired constructor(
    private val postService: PostService
) {

    @PostMapping
    fun createPost(@RequestBody postRequest: PostRequest) {
        val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails
        val userId = userDetails.id
        postService.createPost(userId, postRequest)
    }

    @GetMapping
    fun getAllPosts(): List<PostResponse> {
        return postService.getAllPosts()
    }

    @PostMapping("/{postId}/like")
    fun likePost(@PathVariable postId: String) {
        val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails
        val userId = userDetails.id
        postService.likePost(postId, userId)
    }

    @PostMapping("/{postId}/unlike")
    fun unlikePost(@PathVariable postId: String) {
        val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails
        val userId = userDetails.id
        postService.unlikePost(postId, userId)
    }

    @GetMapping("/{postId}/isLiked")
    fun isLiked(@PathVariable postId: String): Map<String, Boolean> {
        val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails
        val userId = userDetails.id
        val isLiked = postService.isLiked(postId, userId)
        return mapOf("isLiked" to isLiked)
    }

    @PostMapping("/{postId}/comment")
    fun addComment(@PathVariable postId: String, @RequestBody commentRequest: Map<String, String>): ResponseEntity<Post> {
        val userDetails = SecurityContextHolder.getContext().authentication.principal as CustomUserDetails
        val username = userDetails.username
        val commentText = commentRequest["comment"] ?: throw IllegalArgumentException("Comment text is required")
        val comment = Comment(username = username, comment = commentText)
        val savedPost = postService.addComment(postId, comment)
        return ResponseEntity.ok(savedPost)
    }
}
