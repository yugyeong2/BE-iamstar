package com.yugyeong.iamstar.service;

import com.yugyeong.iamstar.dto.PostResponse;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ChatService {

    public interface Assistant {
        String chat(String userMessage);
    }

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private final OpenAiChatModel chatModel;
    private final OpenAiEmbeddingModel embeddingModel;
    private final RetrievalAugmentor retrievalAugmentor;
    private final MessageWindowChatMemory chatMemory;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final Assistant assistant;

    @Autowired
    public ChatService(OpenAiChatModel chatModel,
                       OpenAiEmbeddingModel embeddingModel,
                       RetrievalAugmentor retrievalAugmentor,
                       EmbeddingStore<TextSegment> embeddingStore) {
        this.chatModel = chatModel;
        this.embeddingModel = embeddingModel;
        this.retrievalAugmentor = retrievalAugmentor;
        this.embeddingStore = embeddingStore;
        this.chatMemory = MessageWindowChatMemory.withMaxMessages(100); // 최대로 기억하는 메시지 수

        this.assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatModel)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemory(chatMemory)
                .build();
    }

    public ResponseEntity<String> chat(String message) {
        logger.info("Processing chat message: {}", message);
        String response = assistant.chat(message);
        logger.info("Generated chat response: {}", response);
        return ResponseEntity.ok(response);
    }

    public void ingestPost(PostResponse post) {
        Metadata metadata = new Metadata();
        metadata.put("postId", post.getId());
        metadata.put("username", post.getUsername());
        metadata.put("fullName", post.getFullName());
        metadata.put("profileUrl", post.getProfileUrl());
        metadata.put("postUrl", post.getPostUrl());
        metadata.put("likes", String.valueOf(post.getLikes()));
        metadata.put("comments", post.getComments().toString());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        metadata.put("timestamp", post.getTimestamp().format(formatter)); // LocalDateTime을 문자열로 변환

        Document document = new Document(post.getContent(), metadata);

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        logger.info("Starting ingestion for post: {}", post.getId());
        ingestor.ingest(List.of(document));
        logger.info("Ingested post: {}", post.getId());
    }

    public void ingestAllPosts(List<PostResponse> posts) {
        for (PostResponse post : posts) {
            ingestPost(post);
        }
    }
}