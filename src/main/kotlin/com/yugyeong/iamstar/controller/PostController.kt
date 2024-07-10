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

    @PostMapping("/{postId}/comment")
    fun addComment(@PathVariable postId: String, @RequestBody comment: Comment): ResponseEntity<Post> {
        val savedPost = postService.addComment(postId, comment)
        return ResponseEntity.ok(savedPost)
    }
}
