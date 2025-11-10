package com.sap.commerce.workshop.controller;

import com.sap.commerce.workshop.dto.SemanticSearchRequest;
import com.sap.commerce.workshop.dto.SemanticSearchResponse;
import com.sap.commerce.workshop.service.SemanticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Semantic Search Controller - SOLUTION for Exercise 3
 *
 * Complete implementation of semantic search REST API.
 */
@RestController
@RequestMapping("/api/search")
@Slf4j
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
        // Validate request
        if (request.getQuery() == null || request.getQuery().trim().isEmpty()) {
            log.warn("Empty query received");
            return ResponseEntity.badRequest().build();
        }

        // Set defaults if not provided
        int limit = request.getLimit() != null ? request.getLimit() : 5;
        double threshold = request.getThreshold() != null ? request.getThreshold() : 0.65;

        log.info("Received semantic search request: query='{}', limit={}, threshold={}",
                request.getQuery(), limit, threshold);

        // Perform search
        SemanticSearchResponse response = searchService.search(
                request.getQuery(),
                limit,
                threshold
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint for search service.
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Semantic search service is running");
    }
}
