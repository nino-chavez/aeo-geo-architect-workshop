package com.workshop.aeogeo.service.embedding;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Vertex AI Embedding Provider (GCP) - STUB IMPLEMENTATION.
 *
 * This is a placeholder for Day 2 advanced exercises where participants
 * will implement full Vertex AI integration.
 *
 * Current implementation returns mock embeddings for workshop progression.
 *
 * To implement:
 * 1. Add Google Cloud Vertex AI SDK dependency to pom.xml
 * 2. Implement authentication with service account
 * 3. Call Vertex AI text-embeddings-gecko API
 * 4. Handle rate limiting and retries
 *
 * Environment variables needed:
 * - VERTEXAI_PROJECT_ID: GCP project ID
 * - VERTEXAI_LOCATION: Region (default: us-central1)
 * - VERTEXAI_MODEL: Model name (default: textembedding-gecko@003)
 */
@Service
@Profile("vertexai")
@Slf4j
public class VertexAIEmbeddingProvider implements EmbeddingProvider {

    @Value("${vertexai.project-id:}")
    private String projectId;

    @Value("${vertexai.location:us-central1}")
    private String location;

    @Value("${vertexai.model:textembedding-gecko@003}")
    private String model;

    private static final int EMBEDDING_DIMENSION = 768; // gecko dimension

    @PostConstruct
    public void initialize() {
        log.warn("=".repeat(80));
        log.warn("Vertex AI Provider is a STUB implementation for advanced exercises.");
        log.warn("Current behavior: Returns deterministic mock embeddings.");
        log.warn("See exercises/day2/bonus-vertexai.md to implement full integration.");
        log.warn("=".repeat(80));

        if (projectId == null || projectId.isBlank()) {
            log.warn("VERTEXAI_PROJECT_ID not set. Using mock mode.");
        } else {
            log.info("Vertex AI configured for project: {} in {}", projectId, location);
        }
    }

    @Override
    public List<Float> generateEmbedding(String text) {
        log.debug("Generating MOCK embedding for text: {}", text);

        // TODO: Implement actual Vertex AI API call
        // For now, return deterministic mock embedding based on text hash
        return generateMockEmbedding(text);
    }

    @Override
    public List<List<Float>> generateEmbeddings(List<String> texts) {
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
        return "vertexai-stub";
    }

    /**
     * Generate a deterministic mock embedding for workshop exercises.
     * Participants will replace this with real Vertex AI calls.
     */
    private List<Float> generateMockEmbedding(String text) {
        Random random = new Random(text.hashCode());
        List<Float> embedding = new ArrayList<>(EMBEDDING_DIMENSION);

        for (int i = 0; i < EMBEDDING_DIMENSION; i++) {
            embedding.add(random.nextFloat() * 2 - 1); // Range: -1 to 1
        }

        return embedding;
    }
}
