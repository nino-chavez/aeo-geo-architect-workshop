package com.workshop.aeogeo.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * PrecomputedEmbeddingProvider
 *
 * Default provider that uses pre-computed embeddings.
 *
 * Benefits:
 * - No API keys required
 * - Works offline
 * - Zero latency (lookup only)
 * - Perfect for Day 1 workshop
 * - Great for testing/CI environments
 *
 * Limitations:
 * - Only works for texts in the pre-computed database
 * - Cannot generate embeddings for new text
 *
 * Use Case:
 * - Workshop Day 1: Everyone uses this
 * - Production: Fallback when live providers fail
 * - Testing: Fast, deterministic results
 */
@Service
@ConditionalOnProperty(
    name = "embedding.provider",
    havingValue = "precomputed",
    matchIfMissing = true // This is the default
)
public class PrecomputedEmbeddingProvider implements EmbeddingProvider {

    private static final Logger logger = LoggerFactory.getLogger(PrecomputedEmbeddingProvider.class);

    /**
     * In-memory cache of pre-computed embeddings
     * In production, this would query the database
     *
     * Key: Text content (normalized)
     * Value: Pre-computed embedding vector
     */
    private final Map<String, float[]> embeddingCache = new HashMap<>();

    public PrecomputedEmbeddingProvider() {
        logger.info("Initialized PrecomputedEmbeddingProvider (Offline Mode)");
        loadPrecomputedEmbeddings();
    }

    @Override
    public float[] embed(String text) throws EmbeddingException {
        String normalizedText = normalizeText(text);

        float[] embedding = embeddingCache.get(normalizedText);

        if (embedding == null) {
            throw new EmbeddingException(
                "No pre-computed embedding found for text: \"" + text.substring(0, Math.min(50, text.length())) + "...\"\n" +
                "Options:\n" +
                "1. Use a live provider (azure-openai, vertex-ai, openai, ollama)\n" +
                "2. Add this text to the pre-computed database\n" +
                "3. Check if fallback is enabled: embedding.fallback-to-precomputed=true"
            );
        }

        logger.debug("Retrieved pre-computed embedding for text (length: {})", text.length());
        return embedding;
    }

    @Override
    public int getDimension() {
        return 1536; // OpenAI text-embedding-ada-002 format
    }

    @Override
    public String getProviderName() {
        return "Pre-computed (Offline)";
    }

    @Override
    public boolean isAvailable() {
        return true; // Always available
    }

    /**
     * Load pre-computed embeddings from database or resource files
     * For now, this is a stub. Real implementation would query DocumentChunkRepository
     */
    private void loadPrecomputedEmbeddings() {
        // TODO: In Exercise 3, participants will implement database lookup
        logger.info("Pre-computed embeddings loaded from database");
    }

    /**
     * Normalize text for consistent lookup
     */
    private String normalizeText(String text) {
        return text.trim().toLowerCase();
    }

    /**
     * Add a pre-computed embedding (for testing or workshop setup)
     */
    public void addEmbedding(String text, float[] embedding) {
        if (embedding.length != getDimension()) {
            throw new IllegalArgumentException(
                "Embedding dimension mismatch. Expected " + getDimension() + ", got " + embedding.length
            );
        }
        embeddingCache.put(normalizeText(text), embedding);
        logger.debug("Added pre-computed embedding for text (length: {})", text.length());
    }

    /**
     * Get cache size (for monitoring)
     */
    public int getCacheSize() {
        return embeddingCache.size();
    }
}
