package com.sap.commerce.workshop.service;

import com.pgvector.PGvector;
import com.sap.commerce.workshop.dto.SearchResult;
import com.sap.commerce.workshop.dto.SemanticSearchResponse;
import com.sap.commerce.workshop.model.ProductModel;
import com.sap.commerce.workshop.repository.ProductRepository;
import com.sap.commerce.workshop.service.embedding.EmbeddingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Semantic Search Service - SOLUTION for Exercise 3
 *
 * Complete implementation of semantic search using vector embeddings.
 */
@Service
@Slf4j
public class SemanticSearchService {

    @Autowired
    private EmbeddingProvider embeddingProvider;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Perform semantic search for products.
     *
     * @param query Query text
     * @param limit Maximum number of results
     * @param threshold Minimum similarity threshold
     * @return Search response with ranked results
     */
    public SemanticSearchResponse search(String query, int limit, double threshold) {
        long startTime = System.currentTimeMillis();

        log.info("Performing semantic search for query: '{}' (limit={}, threshold={})", query, limit, threshold);

        // Step 1: Generate embedding for the query
        List<Float> queryEmbedding = embeddingProvider.generateEmbedding(query);

        // Step 2: Fetch all products with embeddings
        List<ProductModel> products = productRepository.findAll();

        // Step 3: Calculate similarity for each product
        List<SearchResult> results = new ArrayList<>();
        int rank = 1;

        for (ProductModel product : products) {
            if (product.getEmbedding() == null) {
                continue; // Skip products without embeddings
            }

            // Convert PGvector to List<Float>
            List<Float> productEmbedding = pgvectorToList(product.getEmbedding());

            // Calculate cosine similarity
            double similarity = cosineSimilarity(queryEmbedding, productEmbedding);

            // Filter by threshold
            if (similarity >= threshold) {
                SearchResult result = new SearchResult();
                result.setProduct(product);
                result.setSimilarity(similarity);
                results.add(result);
            }
        }

        // Step 4: Sort by similarity (descending)
        results.sort(Comparator.comparingDouble(SearchResult::getSimilarity).reversed());

        // Step 5: Limit results and assign ranks
        List<SearchResult> limitedResults = results.stream()
                .limit(limit)
                .collect(Collectors.toList());

        for (int i = 0; i < limitedResults.size(); i++) {
            limitedResults.get(i).setRank(i + 1);
        }

        // Step 6: Build response
        long executionTime = System.currentTimeMillis() - startTime;

        log.info("Search completed in {}ms, found {} results", executionTime, limitedResults.size());

        SemanticSearchResponse response = new SemanticSearchResponse();
        response.setQuery(query);
        response.setResults(limitedResults);
        response.setExecutionTimeMs(executionTime);
        response.setTotalResults(results.size());

        return response;
    }

    /**
     * Calculate cosine similarity between two embeddings.
     *
     * Formula: similarity = (A · B) / (||A|| × ||B||)
     *
     * @param a First embedding
     * @param b Second embedding
     * @return Similarity score (0-1)
     */
    private double cosineSimilarity(List<Float> a, List<Float> b) {
        if (a.size() != b.size()) {
            throw new IllegalArgumentException("Embeddings must have same dimension");
        }

        // Calculate dot product (A · B)
        double dotProduct = 0.0;
        for (int i = 0; i < a.size(); i++) {
            dotProduct += a.get(i) * b.get(i);
        }

        // Calculate magnitude of A (||A||)
        double magnitudeA = 0.0;
        for (Float val : a) {
            magnitudeA += val * val;
        }
        magnitudeA = Math.sqrt(magnitudeA);

        // Calculate magnitude of B (||B||)
        double magnitudeB = 0.0;
        for (Float val : b) {
            magnitudeB += val * val;
        }
        magnitudeB = Math.sqrt(magnitudeB);

        // Return similarity
        if (magnitudeA == 0.0 || magnitudeB == 0.0) {
            return 0.0;
        }

        return dotProduct / (magnitudeA * magnitudeB);
    }

    /**
     * Convert PGvector to List<Float>.
     *
     * @param pgVector PGvector object
     * @return List of floats
     */
    private List<Float> pgvectorToList(PGvector pgVector) {
        if (pgVector == null) {
            return List.of();
        }

        float[] array = pgVector.toArray();
        List<Float> list = new ArrayList<>(array.length);
        for (float value : array) {
            list.add(value);
        }
        return list;
    }
}
