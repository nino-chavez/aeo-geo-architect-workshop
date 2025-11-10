package com.workshop.aeogeo.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * OllamaEmbeddingProvider - Fully Local Embeddings
 *
 * Benefits:
 * - 100% offline/local (no internet required)
 * - No API costs
 * - Data never leaves your machine
 * - Good for air-gapped environments
 *
 * Limitations:
 * - Slower than cloud APIs (unless you have GPU)
 * - Lower quality than commercial models
 * - Requires Docker or local Ollama installation
 *
 * Setup:
 * 1. Start Ollama: docker-compose --profile ollama up
 * 2. Pull model: docker exec aeo-geo-ollama ollama pull nomic-embed-text
 * 3. Set provider: EMBEDDING_PROVIDER=ollama
 *
 * Models:
 * - nomic-embed-text: 768 dims, optimized for retrieval (recommended)
 * - mxbai-embed-large: 1024 dims, higher quality
 * - all-minilm: 384 dims, fastest
 */
@Service
@ConditionalOnProperty(name = "embedding.provider", havingValue = "ollama")
public class OllamaEmbeddingProvider implements EmbeddingProvider {

    private static final Logger logger = LoggerFactory.getLogger(OllamaEmbeddingProvider.class);

    @Value("${ollama.endpoint:http://localhost:11434}")
    private String endpoint;

    @Value("${ollama.model:nomic-embed-text}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public float[] embed(String text) throws EmbeddingException {
        String url = endpoint + "/api/embeddings";

        Map<String, Object> body = Map.of(
            "model", model,
            "prompt", text
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body);

        try {
            logger.debug("Calling Ollama embedding API (model: {}, text length: {})", model, text.length());
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode embeddingNode = root.path("embedding");

            if (embeddingNode.isMissingNode()) {
                throw new EmbeddingException("Ollama response missing 'embedding' field. Is the model loaded?");
            }

            float[] embedding = new float[embeddingNode.size()];
            for (int i = 0; i < embeddingNode.size(); i++) {
                embedding[i] = (float) embeddingNode.get(i).asDouble();
            }

            logger.info("Successfully generated embedding via Ollama (model: {}, dimension: {})",
                       model, embedding.length);
            return embedding;

        } catch (Exception e) {
            logger.error("Ollama embedding failed: {}", e.getMessage());
            throw new EmbeddingException(
                "Ollama embedding failed. Is Ollama running? Try: docker-compose --profile ollama up",
                e
            );
        }
    }

    @Override
    public int getDimension() {
        // Different models have different dimensions
        return switch (model) {
            case "nomic-embed-text" -> 768;
            case "mxbai-embed-large" -> 1024;
            case "all-minilm" -> 384;
            default -> 768; // Default assumption
        };
    }

    @Override
    public String getProviderName() {
        return "Ollama (Local - " + model + ")";
    }

    @Override
    public boolean isAvailable() {
        try {
            // Try to ping Ollama
            restTemplate.getForEntity(endpoint + "/api/tags", String.class);
            return true;
        } catch (Exception e) {
            logger.warn("Ollama not available at {}: {}", endpoint, e.getMessage());
            return false;
        }
    }
}
