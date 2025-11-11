package com.workshop.aeogeo.service;

import com.workshop.aeogeo.dto.SearchResult;
import com.workshop.aeogeo.dto.SemanticSearchResponse;
import com.workshop.aeogeo.model.ProductModel;
import com.workshop.aeogeo.repository.ProductRepository;
import com.workshop.aeogeo.service.embedding.EmbeddingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Semantic Search Service using Vector Embeddings.
 *
 * Exercise 3: Implement semantic product search using cosine similarity.
 *
 * Your task: Implement the search() method to find products semantically
 * similar to a user's query using vector embeddings and cosine similarity.
 */
@Service
@Slf4j
public class SemanticSearchService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmbeddingProvider embeddingProvider;

    /**
     * Perform semantic search on products.
     *
     * @param query User search query
     * @param limit Maximum number of results to return
     * @param threshold Minimum similarity score (0.0 to 1.0)
     * @return Search results with similarity scores
     */
    public SemanticSearchResponse search(String query, int limit, double threshold) {
        long startTime = System.currentTimeMillis();

        // TODO: Step 1 - Generate embedding for the user's query
        // List<Float> queryEmbedding = embeddingProvider.generateEmbedding(query);

        // TODO: Step 2 - Fetch all products from database
        // List<ProductModel> products = productRepository.findAll();

        // TODO: Step 3 - Calculate similarity for each product
        // List<SearchResult> results = new ArrayList<>();
        //
        // for (ProductModel product : products) {
        //     // Skip products without embeddings
        //     if (product.getEmbedding() == null) {
        //         continue;
        //     }
        //
        //     // Convert pgvector string to List<Float>
        //     List<Float> productEmbedding = pgvectorToList(product.getEmbedding());
        //
        //     // Calculate cosine similarity
        //     double similarity = cosineSimilarity(queryEmbedding, productEmbedding);
        //
        //     // Only include results above threshold
        //     if (similarity >= threshold) {
        //         results.add(new SearchResult(product, similarity));
        //     }
        // }

        // TODO: Step 4 - Sort results by similarity (highest first)
        // results.sort(Comparator.comparingDouble(SearchResult::getSimilarity).reversed());

        // TODO: Step 5 - Limit to top N results
        // List<SearchResult> topResults = results.stream()
        //     .limit(limit)
        //     .collect(Collectors.toList());

        // TODO: Step 6 - Build response with metadata
        // long executionTime = System.currentTimeMillis() - startTime;
        //
        // SemanticSearchResponse response = new SemanticSearchResponse();
        // response.setQuery(query);
        // response.setResults(topResults);
        // response.setTotalResults(topResults.size());
        // response.setExecutionTimeMs(executionTime);
        // response.setSimilarityThreshold(threshold);
        //
        // log.info("Semantic search for '{}' returned {} results in {}ms",
        //     query, topResults.size(), executionTime);
        //
        // return response;

        // REMOVE THIS LINE when you implement the method:
        return new SemanticSearchResponse();
    }

    /**
     * Calculate cosine similarity between two vectors.
     *
     * Cosine similarity = (A · B) / (||A|| × ||B||)
     *
     * @param a First vector
     * @param b Second vector
     * @return Similarity score between 0.0 and 1.0
     */
    private double cosineSimilarity(List<Float> a, List<Float> b) {
        // TODO: Implement cosine similarity calculation
        //
        // Step 1: Calculate dot product (A · B)
        // double dotProduct = 0.0;
        // for (int i = 0; i < a.size(); i++) {
        //     dotProduct += a.get(i) * b.get(i);
        // }
        //
        // Step 2: Calculate magnitude of A (||A||)
        // double magnitudeA = Math.sqrt(
        //     a.stream().mapToDouble(v -> v * v).sum()
        // );
        //
        // Step 3: Calculate magnitude of B (||B||)
        // double magnitudeB = Math.sqrt(
        //     b.stream().mapToDouble(v -> v * v).sum()
        // );
        //
        // Step 4: Return similarity
        // return dotProduct / (magnitudeA * magnitudeB);

        return 0.0; // REMOVE THIS when implemented
    }

    /**
     * Convert PostgreSQL pgvector string to List of Floats.
     *
     * Example: "[0.1, 0.2, 0.3]" → [0.1, 0.2, 0.3]
     *
     * @param pgvectorString The vector as stored in PostgreSQL
     * @return List of float values
     */
    private List<Float> pgvectorToList(String pgvectorString) {
        // TODO: Implement pgvector string parsing
        //
        // Remove brackets: "[0.1, 0.2, 0.3]" → "0.1, 0.2, 0.3"
        // String cleaned = pgvectorString.replace("[", "").replace("]", "");
        //
        // Split by comma and convert to Float
        // return Arrays.stream(cleaned.split(","))
        //     .map(String::trim)
        //     .map(Float::parseFloat)
        //     .collect(Collectors.toList());

        return new ArrayList<>(); // REMOVE THIS when implemented
    }
}
