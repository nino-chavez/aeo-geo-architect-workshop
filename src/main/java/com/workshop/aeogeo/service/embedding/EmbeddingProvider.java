package com.workshop.aeogeo.service.embedding;

import java.util.List;

/**
 * Strategy interface for generating text embeddings from different providers.
 *
 * Implementations provide embeddings from various sources:
 * - PrecomputedEmbeddingProvider: Pre-generated embeddings (offline)
 * - AzureOpenAIEmbeddingProvider: Azure OpenAI Service
 * - OpenAIEmbeddingProvider: Direct OpenAI API
 * - OllamaEmbeddingProvider: Local Ollama instance
 * - VertexAIEmbeddingProvider: Google Cloud Vertex AI
 *
 * This abstraction allows workshop participants to switch providers
 * without changing application code.
 */
public interface EmbeddingProvider {

    /**
     * Generate an embedding vector for a single text input.
     *
     * @param text The input text to embed
     * @return A floating-point vector representation of the text
     */
    List<Float> generateEmbedding(String text);

    /**
     * Generate embedding vectors for multiple text inputs (batch operation).
     *
     * @param texts List of input texts to embed
     * @return List of embedding vectors, one per input text
     */
    List<List<Float>> generateEmbeddings(List<String> texts);

    /**
     * Get the dimension size of embeddings produced by this provider.
     *
     * @return Number of dimensions in the embedding vector
     */
    int getEmbeddingDimension();

    /**
     * Get the name/identifier of this provider.
     *
     * @return Provider name (e.g., "precomputed", "azure", "openai")
     */
    String getProviderName();
}
