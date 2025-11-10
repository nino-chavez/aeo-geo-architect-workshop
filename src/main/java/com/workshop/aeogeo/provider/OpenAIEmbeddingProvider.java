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
 * OpenAIEmbeddingProvider - Bring Your Own Key (BYOK)
 *
 * Benefits:
 * - Easy to set up (get key from platform.openai.com)
 * - Low cost (~$0.02 per workshop)
 * - Latest models available quickly
 * - Good for individual learning/prototyping
 *
 * Setup:
 * 1. Get API key: https://platform.openai.com/api-keys
 * 2. Set environment: OPENAI_API_KEY=sk-proj-...
 * 3. Optionally set model: OPENAI_MODEL=text-embedding-3-small (default)
 *
 * Models:
 * - text-embedding-3-small: 1536 dims, $0.02/1M tokens (recommended)
 * - text-embedding-3-large: 3072 dims, $0.13/1M tokens (best quality)
 * - text-embedding-ada-002: 1536 dims, $0.10/1M tokens (legacy)
 */
@Service
@ConditionalOnProperty(name = "embedding.provider", havingValue = "openai")
public class OpenAIEmbeddingProvider implements EmbeddingProvider {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIEmbeddingProvider.class);
    private static final String OPENAI_ENDPOINT = "https://api.openai.com/v1/embeddings";

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model:text-embedding-3-small}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public float[] embed(String text) throws EmbeddingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
            "input", text,
            "model", model
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            logger.debug("Calling OpenAI embedding API (model: {}, text length: {})", model, text.length());
            ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_ENDPOINT, request, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode embeddingNode = root.path("data").get(0).path("embedding");

            float[] embedding = new float[embeddingNode.size()];
            for (int i = 0; i < embeddingNode.size(); i++) {
                embedding[i] = (float) embeddingNode.get(i).asDouble();
            }

            logger.info("Successfully generated embedding via OpenAI (model: {}, dimension: {})",
                       model, embedding.length);
            return embedding;

        } catch (Exception e) {
            logger.error("OpenAI embedding failed: {}", e.getMessage());
            throw new EmbeddingException("OpenAI embedding failed. Check API key and model.", e);
        }
    }

    @Override
    public int getDimension() {
        // text-embedding-3-large has 3072 dimensions, others have 1536
        return model.contains("3-large") ? 3072 : 1536;
    }

    @Override
    public String getProviderName() {
        return "OpenAI (" + model + ")";
    }

    @Override
    public boolean isAvailable() {
        return apiKey != null && !apiKey.isEmpty() && apiKey.startsWith("sk-");
    }
}
