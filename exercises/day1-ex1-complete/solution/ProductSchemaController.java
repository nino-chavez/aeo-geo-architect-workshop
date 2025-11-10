package com.sap.commerce.workshop.controller;

import com.sap.commerce.workshop.model.MediaModel;
import com.sap.commerce.workshop.model.ProductModel;
import com.sap.commerce.workshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Product Schema Controller - SOLUTION for Exercise 1
 *
 * Complete implementation of Schema.org Product JSON-LD endpoint.
 * Includes all required and recommended properties with proper null handling.
 */
@RestController
@RequestMapping("/api/products")
public class ProductSchemaController {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get Product Schema (JSON-LD) for a specific product.
     *
     * @param id Product ID
     * @return Product schema in JSON-LD format
     */
    @GetMapping("/{id}/schema")
    public ResponseEntity<Map<String, Object>> getProductSchema(@PathVariable Long id) {
        // Step 1: Fetch the product
        ProductModel product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Step 2: Build the schema structure
        Map<String, Object> schema = new LinkedHashMap<>();

        // Required: @context and @type
        schema.put("@context", "https://schema.org");
        schema.put("@type", "Product");

        // Required: Basic product information
        schema.put("name", product.getName());

        if (product.getDescription() != null) {
            schema.put("description", product.getDescription());
        }

        // Required: Image(s)
        if (!product.getImages().isEmpty()) {
            List<String> imageUrls = product.getImages().stream()
                    .map(MediaModel::getUrl)
                    .collect(Collectors.toList());

            // Use single string for one image, array for multiple
            if (imageUrls.size() == 1) {
                schema.put("image", imageUrls.get(0));
            } else {
                schema.put("image", imageUrls);
            }
        }

        // Recommended: SKU
        if (product.getCode() != null) {
            schema.put("sku", product.getCode());
        }

        // Required: Brand
        if (product.getManufacturer() != null) {
            Map<String, Object> brand = new LinkedHashMap<>();
            brand.put("@type", "Brand");
            brand.put("name", product.getManufacturer().getName());
            schema.put("brand", brand);
        }

        // Required: Offers
        if (product.getPrice() != null) {
            Map<String, Object> offers = new LinkedHashMap<>();
            offers.put("@type", "Offer");
            offers.put("price", product.getPrice().toString());
            offers.put("priceCurrency", product.getCurrencyCode() != null ? product.getCurrencyCode() : "USD");
            offers.put("availability", getAvailability(product.getStockLevel()));

            // Optional: Add URL to product page (if available)
            offers.put("url", "http://localhost:8080/products/" + product.getCode());

            schema.put("offers", offers);
        }

        // Recommended: Aggregate Rating
        if (product.getAverageRating() != null && product.getReviewCount() != null) {
            Map<String, Object> rating = new LinkedHashMap<>();
            rating.put("@type", "AggregateRating");
            rating.put("ratingValue", product.getAverageRating().toString());
            rating.put("reviewCount", product.getReviewCount().toString());
            schema.put("aggregateRating", rating);
        }

        // Recommended: Category
        if (product.getCategory() != null) {
            schema.put("category", product.getCategory().getName());
        }

        // Bonus: Additional properties (specifications)
        if (!product.getSpecifications().isEmpty()) {
            List<Map<String, Object>> additionalProperties = product.getSpecifications().stream()
                    .limit(5) // Limit to first 5 specs for brevity
                    .map(spec -> {
                        Map<String, Object> prop = new LinkedHashMap<>();
                        prop.put("@type", "PropertyValue");
                        prop.put("name", spec.getName());
                        prop.put("value", spec.getValue() + (spec.getUnit() != null ? " " + spec.getUnit() : ""));
                        return prop;
                    })
                    .collect(Collectors.toList());

            if (!additionalProperties.isEmpty()) {
                schema.put("additionalProperty", additionalProperties);
            }
        }

        return ResponseEntity.ok(schema);
    }

    /**
     * Helper method to map stock level to Schema.org availability.
     *
     * @param stockLevel Current stock level
     * @return Schema.org availability URL
     */
    private String getAvailability(Integer stockLevel) {
        if (stockLevel == null) {
            return "https://schema.org/PreOrder";
        } else if (stockLevel > 0) {
            return "https://schema.org/InStock";
        } else {
            return "https://schema.org/OutOfStock";
        }
    }
}
