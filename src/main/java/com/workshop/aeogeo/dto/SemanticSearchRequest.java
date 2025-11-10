package com.workshop.aeogeo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for semantic search.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SemanticSearchRequest {

    private String query;

    private Integer limit = 5;

    private Double threshold = 0.65;
}
