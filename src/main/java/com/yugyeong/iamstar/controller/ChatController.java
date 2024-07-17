package com.yugyeong.iamstar.controller;

import com.yugyeong.iamstar.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/chatbot")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(value = "/chat", produces = "application/json")
    public ResponseEntity<String> chat(@RequestBody String message) {
        logger.info("Received chat message: {}", message);
        ResponseEntity<String> response = chatService.chat(message);
        logger.info("Chat response: {}", response);
        return response;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingestDocuments(@RequestParam String directory) {
        logger.info("Received request to ingest documents from directory: {}", directory);
        try {
            chatService.ingestDocuments(Path.of(directory));
            return ResponseEntity.ok("Documents ingested successfully");
        } catch (IOException e) {
            logger.error("Error ingesting documents", e);
            return ResponseEntity.status(500).body("Error ingesting documents");
        }
    }
}
