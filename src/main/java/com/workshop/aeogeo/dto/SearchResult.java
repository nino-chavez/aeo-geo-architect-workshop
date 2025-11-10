package com.workshop.aeogeo.dto;

import com.workshop.aeogeo.model.ProductModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Individual search result with similarity score.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {

    private ProductModel product;

    private double similarity;

    private int rank;
}
