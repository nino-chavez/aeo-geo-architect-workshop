package com.workshop.aeogeo.controller;

import com.workshop.aeogeo.dto.SemanticSearchRequest;
import com.workshop.aeogeo.dto.SemanticSearchResponse;
import com.workshop.aeogeo.service.SemanticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Semantic Search Controller - Exercise 3: RAG Pipeline
 *
 * TODO: Implement semantic search endpoint using vector embeddings
 *
 * Endpoint: POST /api/search/semantic
 *
 * Requirements:
 * 1. Accept user query text and optional parameters
 * 2. Call SemanticSearchService to perform vector search
 * 3. Return ranked results with similarity scores
 * 4. Track and return execution time
 */
@RestController
@RequestMapping("/api/search")
public class SemanticSearchController {

    @Autowired
    private SemanticSearchService searchService;

    /**
     * Perform semantic search using vector embeddings.
     *
     * @param request Search request with query and parameters
     * @return Ranked search results with similarity scores
     */
    @PostMapping("/semantic")
    public ResponseEntity<SemanticSearchResponse> semanticSearch(@RequestBody SemanticSearchRequest request) {
        // TODO: Implement this endpoint

        // Step 1: Validate request
        // Check that query is not null or empty

        // Step 2: Call search service
        // Pass query text, limit, and threshold to service

        // Step 3: Return response
        // Wrap results in ResponseEntity.ok()

        return ResponseEntity.badRequest().build(); // Replace with your implementation
    }

    /**
     * Health check endpoint for search service.
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Semantic search service is running");
    }
}
