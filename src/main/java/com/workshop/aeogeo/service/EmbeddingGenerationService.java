package com.workshop.aeogeo.service;

import com.pgvector.PGvector;
import com.workshop.aeogeo.model.ProductModel;
import com.workshop.aeogeo.repository.ProductRepository;
import com.workshop.aeogeo.service.embedding.EmbeddingProvider;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to generate and populate product embeddings.
 *
 * This runs on application startup and generates embeddings
 * for any products that don't have them yet.
 */
@Service
@Slf4j
public class EmbeddingGenerationService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmbeddingProvider embeddingProvider;

    /**
     * Generate embeddings for all products without them.
     * Runs automatically on application startup.
     */
    @PostConstruct
    public void generateProductEmbeddings() {
        log.info("Checking for products needing embeddings...");

        List<ProductModel> products = productRepository.findAll();
        int generated = 0;
        int skipped = 0;

        for (ProductModel product : products) {
            if (product.getEmbedding() == null) {
                try {
                    // Create text representation of product
                    String text = buildProductText(product);

                    // Generate embedding
                    List<Float> embedding = embeddingProvider.generateEmbedding(text);

                    // Convert to PGvector
                    Float[] array = embedding.toArray(new Float[0]);
                    PGvector pgVector = new PGvector(array);

                    // Save to product
                    product.setEmbedding(pgVector);
                    productRepository.save(product);

                    generated++;
                    log.debug("Generated embedding for product: {}", product.getName());
                } catch (Exception e) {
                    log.error("Failed to generate embedding for product {}: {}",
                            product.getName(), e.getMessage());
                }
            } else {
                skipped++;
            }
        }

        log.info("Embedding generation complete. Generated: {}, Skipped (already exists): {}, Total: {}",
                generated, skipped, products.size());
    }

    /**
     * Build a text representation of the product for embedding generation.
     *
     * Combines name, description, manufacturer, and category for rich context.
     *
     * @param product Product to generate text for
     * @return Text representation
     */
    private String buildProductText(ProductModel product) {
        StringBuilder text = new StringBuilder();

        // Add product name
        text.append(product.getName());

        // Add manufacturer
        if (product.getManufacturer() != null) {
            text.append(" by ").append(product.getManufacturer().getName());
        }

        // Add category
        if (product.getCategory() != null) {
            text.append(" in ").append(product.getCategory().getName());
        }

        // Add description
        if (product.getDescription() != null) {
            text.append(". ").append(product.getDescription());
        }

        return text.toString();
    }
}
