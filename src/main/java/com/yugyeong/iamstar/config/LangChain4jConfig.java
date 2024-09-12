package com.yugyeong.iamstar.config;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChain4jConfig {

    @Value("${langchain4j.open-ai.api-key}")
    private String apiKey;

    @Value("${langchain4j.open-ai.model-name}")
    private String modelName;

    @Bean
    public OpenAiChatModel chatModel() { // 테스트용 chatModel
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .build();
    }

    @Bean
    public OpenAiEmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .build();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return ChromaEmbeddingStore.builder()
                .baseUrl("http://localhost:8000/") // Chroma 서버 URL
                .collectionName("iamstar_chroma")
                .build();
    }

    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                // maxResults: 검색 결과로 반환할 최대 문서 수 (검색 엔진은 사용자 질의와 가장 관련성이 높은 n개의 문서만 반환)
                // minScore: 검색된 문서가 반환되기 위해 필요한 최소 유사도 점수 (검색된 문서 중 유사도 점수가 n 이상인 문서들만 반환)
                .dynamicMaxResults(query -> 3) // 질의에 따라 최대 결과 수 동적 설정
                .dynamicMinScore(query -> 0.6) // 질의에 따라 최소 점수 동적 설정
                .build();
    }

    @Bean
    public RetrievalAugmentor retrievalAugmentor(ContentRetriever contentRetriever) {
        return DefaultRetrievalAugmentor.builder()
                .contentRetriever(contentRetriever)
                .build();
    }
}
