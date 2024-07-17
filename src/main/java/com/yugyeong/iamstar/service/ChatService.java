package com.yugyeong.iamstar.service;

import com.yugyeong.iamstar.util.FileSystemDocumentLoader;
import dev.langchain4j.data.document.Document;
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

import java.io.IOException;
import java.nio.file.Path;
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
        this.chatMemory = MessageWindowChatMemory.withMaxMessages(50);

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

    public void ingestDocuments(Path directory) throws IOException {
        logger.info("Loading documents from directory: {}", directory);
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(directory);
        logger.info("Loaded {} documents", documents.size());

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        logger.info("Starting document ingestion");
        ingestor.ingest(documents);
        logger.info("Documents ingested successfully");
    }
}
