package com.workshop.aeogeo.controller;

import com.workshop.aeogeo.model.ProductModel;
import com.workshop.aeogeo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Product Schema Controller - Exercise 1: Product Schema Markup
 *
 * TODO: Implement this controller to return Schema.org Product JSON-LD
 * for any product in the database.
 *
 * Endpoint: GET /api/products/{id}/schema
 *
 * Requirements:
 * 1. Fetch product by ID
 * 2. Build JSON-LD structure with @context and @type
 * 3. Include required fields: name, description, image, brand, offers
 * 4. Include recommended fields: aggregateRating, sku, category
 * 5. Handle null values gracefully
 *
 * Validation: Test with Google Rich Results Test
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
        // TODO: Implement this method

        // Step 1: Fetch the product from the repository
        // Hint: Use productRepository.findById(id) and handle Optional

        // Step 2: Create the base schema structure
        // Hint: Use LinkedHashMap to maintain order
        // Add @context: "https://schema.org"
        // Add @type: "Product"

        // Step 3: Add required product properties
        // - name
        // - description
        // - image (use product.getPrimaryImageUrl())
        // - sku (use product.getCode())

        // Step 4: Add brand (nested object)
        // Check if manufacturer exists
        // Create a Map with @type: "Brand" and name

        // Step 5: Add offers (nested object)
        // Create a Map with:
        // - @type: "Offer"
        // - price (as string, e.g., "2498.00")
        // - priceCurrency (e.g., "USD")
        // - availability (map stock level to Schema.org URL)

        // Step 6: Add aggregateRating (if available)
        // Check if product.getAverageRating() is not null
        // Create a Map with:
        // - @type: "AggregateRating"
        // - ratingValue (as string)
        // - reviewCount (as string)

        // Step 7: Add category (if available)
        // Check if product.getCategory() is not null

        // Step 8: Return the schema
        // Hint: return ResponseEntity.ok(schema);

        return ResponseEntity.notFound().build(); // Replace this with your implementation
    }

    /**
     * Helper method to map stock level to Schema.org availability.
     *
     * @param stockLevel Current stock level
     * @return Schema.org availability URL
     */
    private String getAvailability(Integer stockLevel) {
        // TODO: Implement this helper method
        // If stockLevel > 0: return "https://schema.org/InStock"
        // If stockLevel == 0: return "https://schema.org/OutOfStock"
        // If stockLevel is null: return "https://schema.org/PreOrder"

        return "https://schema.org/InStock"; // Replace with proper logic
    }
}
