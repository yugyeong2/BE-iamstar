package com.yugyeong.iamstar.controller;

import com.yugyeong.iamstar.dto.PostResponse;
import com.yugyeong.iamstar.service.ChatService;
import com.yugyeong.iamstar.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatbot")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatService chatService;
    private final PostService postService;

    @Autowired
    public ChatController(ChatService chatService, PostService postService) {
        this.chatService = chatService;
        this.postService = postService;
    }

    @PostMapping(value = "/chat", produces = "application/json")
    public ResponseEntity<String> chat(@RequestBody String message) {
        logger.info("Received chat message: {}", message);
        ResponseEntity<String> response = chatService.chat(message);
        logger.info("Chat response: {}", response);
        return response;
    }

    @PostMapping("/ingestPosts")
    public ResponseEntity<String> ingestPosts() {
        logger.info("Received request to ingest all posts");
        try {
            List<PostResponse> allPosts = postService.getAllPosts();
            chatService.ingestAllPosts(allPosts);
            return ResponseEntity.ok("Posts ingested successfully");
        } catch (Exception e) {
            logger.error("Error ingesting posts", e);
            return ResponseEntity.status(500).body("Error ingesting posts");
        }
    }
}
