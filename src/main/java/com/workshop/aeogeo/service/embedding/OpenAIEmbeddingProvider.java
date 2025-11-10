package com.workshop.aeogeo.service.embedding;

import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OpenAI Embedding Provider (BYOK - Bring Your Own Key).
 *
 * Direct integration with OpenAI API using your personal API key.
 * Good for individual use or small teams.
 *
 * Required environment variables:
 * - OPENAI_API_KEY: Your OpenAI API key
 * - OPENAI_MODEL: Model to use (default: text-embedding-ada-002)
 */
@Service
@Profile("openai")
@Slf4j
public class OpenAIEmbeddingProvider implements EmbeddingProvider {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    private OpenAiService service;
    private static final int EMBEDDING_DIMENSION = 1536; // ada-002 dimension

    @PostConstruct
    public void initialize() {
        try {
            this.service = new OpenAiService(apiKey, Duration.ofSeconds(30));
            log.info("OpenAI service initialized successfully with model: {}", model);
        } catch (Exception e) {
            log.error("Failed to initialize OpenAI service", e);
            throw new RuntimeException("OpenAI initialization failed", e);
        }
    }

    @Override
    public List<Float> generateEmbedding(String text) {
        try {
            EmbeddingRequest request = EmbeddingRequest.builder()
                    .model(model)
                    .input(List.of(text))
                    .build();

            List<Double> embedding = service.createEmbeddings(request)
                    .getData().get(0).getEmbedding();

            return embedding.stream()
                    .map(Double::floatValue)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to generate embedding for text: {}", text, e);
            throw new RuntimeException("Embedding generation failed", e);
        }
    }

    @Override
    public List<List<Float>> generateEmbeddings(List<String> texts) {
        try {
            EmbeddingRequest request = EmbeddingRequest.builder()
                    .model(model)
                    .input(texts)
                    .build();

            return service.createEmbeddings(request)
                    .getData().stream()
                    .map(data -> data.getEmbedding().stream()
                            .map(Double::floatValue)
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to generate embeddings for {} texts", texts.size(), e);
            throw new RuntimeException("Batch embedding generation failed", e);
        }
    }

    @Override
    public int getEmbeddingDimension() {
        return EMBEDDING_DIMENSION;
    }

    @Override
    public String getProviderName() {
        return "openai";
    }
}
