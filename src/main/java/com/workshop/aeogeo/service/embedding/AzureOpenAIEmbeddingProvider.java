package com.workshop.aeogeo.service.embedding;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingsOptions;
import com.azure.core.credential.AzureKeyCredential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Azure OpenAI Embedding Provider.
 *
 * Integrates with Azure OpenAI Service for enterprise-grade embeddings.
 * Suitable for production environments with Azure infrastructure.
 *
 * Required environment variables:
 * - AZURE_OPENAI_ENDPOINT: Your Azure OpenAI endpoint URL
 * - AZURE_OPENAI_KEY: Your Azure OpenAI API key
 * - AZURE_OPENAI_DEPLOYMENT: Deployment name (default: text-embedding-ada-002)
 */
@Service
@Profile("azure")
@Slf4j
public class AzureOpenAIEmbeddingProvider implements EmbeddingProvider {

    @Value("${azure.openai.endpoint}")
    private String endpoint;

    @Value("${azure.openai.key}")
    private String apiKey;

    @Value("${azure.openai.deployment}")
    private String deploymentName;

    private OpenAIClient client;
    private static final int EMBEDDING_DIMENSION = 1536; // ada-002 dimension

    @PostConstruct
    public void initialize() {
        try {
            this.client = new OpenAIClientBuilder()
                    .endpoint(endpoint)
                    .credential(new AzureKeyCredential(apiKey))
                    .buildClient();

            log.info("Azure OpenAI client initialized successfully for deployment: {}", deploymentName);
        } catch (Exception e) {
            log.error("Failed to initialize Azure OpenAI client", e);
            throw new RuntimeException("Azure OpenAI initialization failed", e);
        }
    }

    @Override
    public List<Float> generateEmbedding(String text) {
        try {
            EmbeddingsOptions options = new EmbeddingsOptions(List.of(text));
            Embeddings embeddings = client.getEmbeddings(deploymentName, options);

            List<Double> embedding = embeddings.getData().get(0).getEmbedding();
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
            EmbeddingsOptions options = new EmbeddingsOptions(texts);
            Embeddings embeddings = client.getEmbeddings(deploymentName, options);

            return embeddings.getData().stream()
                    .map(EmbeddingItem::getEmbedding)
                    .map(embedding -> embedding.stream()
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
        return "azure";
    }
}
