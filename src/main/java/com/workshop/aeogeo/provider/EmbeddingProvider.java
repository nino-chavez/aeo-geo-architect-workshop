package com.workshop.aeogeo.provider;

import java.util.List;

/**
 * EmbeddingProvider Interface
 *
 * Strategy pattern for supporting multiple embedding providers:
 * - Azure OpenAI (Enterprise)
 * - Google Vertex AI (GCP)
 * - OpenAI (BYOK)
 * - Ollama (Local)
 * - Pre-computed (Offline)
 *
 * This abstraction allows:
 * 1. Easy switching between providers (configuration-driven)
 * 2. Graceful fallback if primary provider fails
 * 3. Testing with mock providers
 * 4. Future provider additions without changing business logic
 */
public interface EmbeddingProvider {

    /**
     * Generate embedding vector for given text
     *
     * @param text Input text to embed
     * @return Float array representing the embedding vector
     * @throws EmbeddingException if generation fails
     */
    float[] embed(String text) throws EmbeddingException;

    /**
     * Batch embed multiple texts (more efficient for API-based providers)
     *
     * @param texts List of texts to embed
     * @return List of float arrays (one per input text)
     * @throws EmbeddingException if generation fails
     */
    default List<float[]> embedBatch(List<String> texts) throws EmbeddingException {
        // Default implementation: embed one at a time
        return texts.stream()
                .map(this::embedSafely)
                .toList();
    }

    /**
     * Get the dimension of vectors produced by this provider
     *
     * Important: All providers must return consistent dimensions
     * for compatibility with pgvector storage
     *
     * @return Vector dimension (e.g., 1536 for OpenAI, 768 for Vertex AI)
     */
    int getDimension();

    /**
     * Get provider name for logging/monitoring
     *
     * @return Human-readable provider name
     */
    String getProviderName();

    /**
     * Check if provider is available/configured
     *
     * @return true if provider can be used, false otherwise
     */
    boolean isAvailable();

    /**
     * Helper method for safe embedding (returns null on error instead of throwing)
     */
    default float[] embedSafely(String text) {
        try {
            return embed(text);
        } catch (EmbeddingException e) {
            return null;
        }
    }
}
