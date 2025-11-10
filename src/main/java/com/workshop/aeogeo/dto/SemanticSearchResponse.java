package com.workshop.aeogeo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for semantic search.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SemanticSearchResponse {

    private String query;

    private List<SearchResult> results;

    private long executionTimeMs;

    private int totalResults;
}
