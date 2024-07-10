package com.yugyeong.iamstar.controller

import com.yugyeong.iamstar.dto.PostRequest
import com.yugyeong.iamstar.dto.PostResponse
import com.yugyeong.iamstar.model.Comment
import com.yugyeong.iamstar.model.Post
import com.yugyeong.iamstar.service.PostService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/post")
class PostController @Autowired constructor(
    private val postService: PostService
) {

    @PostMapping
    fun createPost(@RequestBody postRequest: PostRequest): ResponseEntity<Post> {
        val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails
        val userId = userDetails.username
        val savedPost = postService.createPost(userId, postRequest)
        return ResponseEntity.ok(savedPost)
    }

    @GetMapping
    fun getAllPosts(): List<PostResponse> {
        return postService.getAllPosts()
    }

    @PostMapping("/{postId}/comment")
    fun addComment(@PathVariable postId: String, @RequestBody comment: Comment): ResponseEntity<Post> {
        val savedPost = postService.addComment(postId, comment)
        return ResponseEntity.ok(savedPost)
    }
}
