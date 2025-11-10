package com.workshop.aeogeo.service.embedding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Ollama Embedding Provider (Fully Local).
 *
 * Uses locally-running Ollama instance for completely offline embeddings.
 * No external API calls, no data leaves your machine.
 *
 * Requires:
 * - Ollama installed and running (docker-compose includes it)
 * - Model pulled: ollama pull nomic-embed-text
 *
 * Environment variables:
 * - OLLAMA_BASE_URL: Ollama server URL (default: http://localhost:11434)
 * - OLLAMA_MODEL: Model to use (default: nomic-embed-text)
 */
@Service
@Profile("ollama")
@Slf4j
public class OllamaEmbeddingProvider implements EmbeddingProvider {

    @Value("${ollama.base-url}")
    private String baseUrl;

    @Value("${ollama.model}")
    private String model;

    private WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final int EMBEDDING_DIMENSION = 768; // nomic-embed-text dimension

    @PostConstruct
    public void initialize() {
        try {
            this.webClient = WebClient.builder()
                    .baseUrl(baseUrl)
                    .build();

            log.info("Ollama client initialized for {} at {}", model, baseUrl);

            // Test connection
            testConnection();
        } catch (Exception e) {
            log.error("Failed to initialize Ollama client", e);
            throw new RuntimeException("Ollama initialization failed. Is Ollama running?", e);
        }
    }

    private void testConnection() {
        try {
            webClient.get()
                    .uri("/api/tags")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Successfully connected to Ollama server");
        } catch (Exception e) {
            log.warn("Could not connect to Ollama. Make sure it's running: docker-compose up -d");
        }
    }

    @Override
    public List<Float> generateEmbedding(String text) {
        try {
            Map<String, Object> request = Map.of(
                    "model", model,
                    "prompt", text
            );

            String response = webClient.post()
                    .uri("/api/embeddings")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);
            JsonNode embeddingNode = root.get("embedding");

            return StreamSupport.stream(embeddingNode.spliterator(), false)
                    .map(JsonNode::floatValue)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to generate embedding with Ollama: {}", text, e);
            throw new RuntimeException("Ollama embedding generation failed", e);
        }
    }

    @Override
    public List<List<Float>> generateEmbeddings(List<String> texts) {
        // Ollama doesn't support batch embeddings, process sequentially
        return texts.stream()
                .map(this::generateEmbedding)
                .collect(Collectors.toList());
    }

    @Override
    public int getEmbeddingDimension() {
        return EMBEDDING_DIMENSION;
    }

    @Override
    public String getProviderName() {
        return "ollama";
    }
}
