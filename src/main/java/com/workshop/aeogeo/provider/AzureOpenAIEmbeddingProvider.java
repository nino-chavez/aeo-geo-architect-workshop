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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AzureOpenAIEmbeddingProvider
 *
 * Connects to enterprise Azure OpenAI endpoint.
 *
 * Benefits:
 * - Enterprise compliance (data residency, SLAs)
 * - Org-wide quota management
 * - IT-approved, pre-configured
 * - Consistent with other Azure services
 *
 * Setup:
 * - Get endpoint from IT: https://your-org.openai.azure.com/
 * - Get API key from Azure Portal
 * - Get deployment name (usually "text-embedding-ada-002")
 *
 * Configuration:
 * AZURE_OPENAI_ENDPOINT=https://your-org.openai.azure.com/
 * AZURE_OPENAI_API_KEY=your-key
 * AZURE_OPENAI_DEPLOYMENT=text-embedding-ada-002
 */
@Service
@ConditionalOnProperty(name = "embedding.provider", havingValue = "azure-openai")
public class AzureOpenAIEmbeddingProvider implements EmbeddingProvider {

    private static final Logger logger = LoggerFactory.getLogger(AzureOpenAIEmbeddingProvider.class);

    @Value("${azure.openai.endpoint}")
    private String endpoint;

    @Value("${azure.openai.api-key}")
    private String apiKey;

    @Value("${azure.openai.deployment-name:text-embedding-ada-002}")
    private String deploymentName;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public float[] embed(String text) throws EmbeddingException {
        String url = String.format("%s/openai/deployments/%s/embeddings?api-version=2023-05-15",
                                   endpoint, deploymentName);

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of("input", text);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            logger.debug("Calling Azure OpenAI embedding API for text (length: {})", text.length());
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode embeddingNode = root.path("data").get(0).path("embedding");

            float[] embedding = new float[embeddingNode.size()];
            for (int i = 0; i < embeddingNode.size(); i++) {
                embedding[i] = (float) embeddingNode.get(i).asDouble();
            }

            logger.info("Successfully generated embedding via Azure OpenAI (dimension: {})", embedding.length);
            return embedding;

        } catch (Exception e) {
            logger.error("Azure OpenAI embedding failed: {}", e.getMessage());
            throw new EmbeddingException("Azure OpenAI embedding failed. Check endpoint/key configuration.", e);
        }
    }

    @Override
    public List<float[]> embedBatch(List<String> texts) throws EmbeddingException {
        // Azure OpenAI supports batch requests (more efficient)
        String url = String.format("%s/openai/deployments/%s/embeddings?api-version=2023-05-15",
                                   endpoint, deploymentName);

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of("input", texts);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            logger.debug("Calling Azure OpenAI batch embedding API for {} texts", texts.size());
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataArray = root.path("data");

            List<float[]> embeddings = new ArrayList<>();
            for (JsonNode item : dataArray) {
                JsonNode embeddingNode = item.path("embedding");
                float[] embedding = new float[embeddingNode.size()];
                for (int i = 0; i < embeddingNode.size(); i++) {
                    embedding[i] = (float) embeddingNode.get(i).asDouble();
                }
                embeddings.add(embedding);
            }

            logger.info("Successfully generated {} embeddings via Azure OpenAI", embeddings.size());
            return embeddings;

        } catch (Exception e) {
            logger.error("Azure OpenAI batch embedding failed: {}", e.getMessage());
            throw new EmbeddingException("Azure OpenAI batch embedding failed", e);
        }
    }

    @Override
    public int getDimension() {
        return 1536; // text-embedding-ada-002
    }

    @Override
    public String getProviderName() {
        return "Azure OpenAI";
    }

    @Override
    public boolean isAvailable() {
        return endpoint != null && !endpoint.isEmpty() &&
               apiKey != null && !apiKey.isEmpty();
    }
}
