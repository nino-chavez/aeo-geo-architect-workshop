package com.workshop.aeogeo.controller;

import com.workshop.aeogeo.dto.SemanticSearchRequest;
import com.workshop.aeogeo.dto.SemanticSearchResponse;
import com.workshop.aeogeo.service.SemanticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Semantic Search API.
 *
 * Exercise 3: Create REST endpoint for semantic product search.
 *
 * Your task: Implement the semanticSearch() method to accept search queries
 * and return relevant products using semantic similarity.
 */
@RestController
@RequestMapping("/api/search")
@Slf4j
public class SemanticSearchController {

    @Autowired
    private SemanticSearchService semanticSearchService;

    /**
     * Perform semantic search on products.
     *
     * @param request Search request containing query, limit, and threshold
     * @return Search response with relevant products and similarity scores
     */
    @PostMapping("/semantic")
    public ResponseEntity<SemanticSearchResponse> semanticSearch(@RequestBody SemanticSearchRequest request) {
        // TODO: Step 1 - Validate the request
        // if (request.getQuery() == null || request.getQuery().trim().isEmpty()) {
        //     return ResponseEntity.badRequest().build();
        // }

        // TODO: Step 2 - Set defaults for optional parameters
        // int limit = request.getLimit() != null ? request.getLimit() : 10;
        // double threshold = request.getThreshold() != null ? request.getThreshold() : 0.7;

        // TODO: Step 3 - Call the semantic search service
        // SemanticSearchResponse response = semanticSearchService.search(
        //     request.getQuery(),
        //     limit,
        //     threshold
        // );

        // TODO: Step 4 - Return the response
        // return ResponseEntity.ok(response);

        // REMOVE THIS LINE when you implement the method:
        return ResponseEntity.ok(new SemanticSearchResponse());
    }

    /**
     * Health check endpoint for semantic search.
     *
     * @return Status information
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "healthy",
            "service", "semantic-search",
            "message", "Semantic search is operational"
        ));
    }
}
