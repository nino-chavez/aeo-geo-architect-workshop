package com.workshop.aeogeo.service.embedding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Precomputed Embedding Provider - Default offline mode.
 *
 * Loads pre-generated embeddings from a JSON file, allowing the workshop
 * to start immediately without API keys or network access.
 *
 * This is the default provider for Day 1 exercises focused on AEO.
 */
@Service
@Profile("precomputed")
@Slf4j
public class PrecomputedEmbeddingProvider implements EmbeddingProvider {

    @Value("${embedding.precomputed.file}")
    private Resource embeddingsFile;

    private Map<String, List<Float>> embeddingCache;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final int EMBEDDING_DIMENSION = 1536; // OpenAI ada-002 dimension

    @PostConstruct
    public void loadEmbeddings() {
        try {
            if (embeddingsFile.exists()) {
                JsonNode root = objectMapper.readTree(embeddingsFile.getInputStream());
                embeddingCache = new HashMap<>();

                root.fields().forEachRemaining(entry -> {
                    String key = entry.getKey();
                    List<Float> embedding = StreamSupport.stream(
                            entry.getValue().spliterator(), false)
                            .map(JsonNode::floatValue)
                            .collect(Collectors.toList());
                    embeddingCache.put(key.toLowerCase(), embedding);
                });

                log.info("Loaded {} precomputed embeddings from {}",
                        embeddingCache.size(), embeddingsFile.getFilename());
            } else {
                log.warn("Precomputed embeddings file not found: {}. Using random fallback.",
                        embeddingsFile.getFilename());
                embeddingCache = new HashMap<>();
            }
        } catch (IOException e) {
            log.error("Failed to load precomputed embeddings", e);
            embeddingCache = new HashMap<>();
        }
    }

    @Override
    public List<Float> generateEmbedding(String text) {
        if (text == null || text.isBlank()) {
            return generateRandomEmbedding();
        }

        // Try exact match first
        String key = text.toLowerCase().trim();
        if (embeddingCache.containsKey(key)) {
            return embeddingCache.get(key);
        }

        // Try fuzzy match for partial keys
        Optional<String> fuzzyMatch = embeddingCache.keySet().stream()
                .filter(k -> k.contains(key) || key.contains(k))
                .findFirst();

        if (fuzzyMatch.isPresent()) {
            log.debug("Fuzzy match found for '{}': '{}'", text, fuzzyMatch.get());
            return embeddingCache.get(fuzzyMatch.get());
        }

        // Fallback: generate deterministic random embedding based on hash
        log.debug("No precomputed embedding found for '{}', using deterministic fallback", text);
        return generateDeterministicEmbedding(text);
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
        return "precomputed";
    }

    /**
     * Generate a deterministic embedding based on text hash.
     * This ensures consistent results for the same input text.
     */
    private List<Float> generateDeterministicEmbedding(String text) {
        Random random = new Random(text.hashCode());
        List<Float> embedding = new ArrayList<>(EMBEDDING_DIMENSION);
        for (int i = 0; i < EMBEDDING_DIMENSION; i++) {
            embedding.add(random.nextFloat() * 2 - 1); // Range: -1 to 1
        }
        return embedding;
    }

    /**
     * Generate a random embedding as ultimate fallback.
     */
    private List<Float> generateRandomEmbedding() {
        Random random = new Random();
        List<Float> embedding = new ArrayList<>(EMBEDDING_DIMENSION);
        for (int i = 0; i < EMBEDDING_DIMENSION; i++) {
            embedding.add(random.nextFloat() * 2 - 1);
        }
        return embedding;
    }
}
