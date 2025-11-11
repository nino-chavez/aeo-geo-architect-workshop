package com.workshop.aeogeo.controller;

import com.workshop.aeogeo.model.ProductModel;
import com.workshop.aeogeo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST Controller for Product Schema.org JSON-LD generation.
 *
 * Exercise 1: Implement Schema.org Product markup for SEO/AEO optimization.
 *
 * Your task: Implement the getProductSchema() method to return valid Schema.org
 * Product JSON-LD that passes Google Rich Results Test validation.
 */
@RestController
@RequestMapping("/api/products")
public class ProductSchemaController {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Generate Schema.org Product JSON-LD for a given product.
     *
     * @param id Product ID
     * @return Schema.org Product JSON-LD
     */
    @GetMapping("/{id}/schema")
    public ResponseEntity<Map<String, Object>> getProductSchema(@PathVariable Long id) {
        // TODO: Step 1 - Fetch the product from the repository
        // ProductModel product = productRepository.findById(id)
        //     .orElseThrow(() -> new RuntimeException("Product not found"));

        // TODO: Step 2 - Create the base schema structure
        // Map<String, Object> schema = new LinkedHashMap<>();
        // schema.put("@context", "https://schema.org");
        // schema.put("@type", "Product");

        // TODO: Step 3 - Add required product properties
        // schema.put("name", product.getName());
        // schema.put("description", product.getDescription());

        // TODO: Step 4 - Add the brand/manufacturer
        // if (product.getManufacturer() != null) {
        //     Map<String, Object> brand = new LinkedHashMap<>();
        //     brand.put("@type", "Brand");
        //     brand.put("name", product.getManufacturer().getName());
        //     schema.put("brand", brand);
        // }

        // TODO: Step 5 - Add product images
        // Extract image URLs from product.getMedia() list
        // if (product.getMedia() != null && !product.getMedia().isEmpty()) {
        //     List<String> imageUrls = product.getMedia().stream()
        //         .map(media -> media.getUrl())
        //         .collect(Collectors.toList());
        //     schema.put("image", imageUrls);
        // }

        // TODO: Step 6 - Add offers (pricing information)
        // Map<String, Object> offers = new LinkedHashMap<>();
        // offers.put("@type", "Offer");
        // offers.put("price", product.getPrice().toString());
        // offers.put("priceCurrency", product.getCurrencyCode());
        //
        // Determine availability based on stock level
        // String availability = product.getStockLevel() > 0 ?
        //     "https://schema.org/InStock" : "https://schema.org/OutOfStock";
        // offers.put("availability", availability);
        // schema.put("offers", offers);

        // TODO: Step 7 - Add aggregate rating (if available)
        // if (product.getAverageRating() != null && product.getReviewCount() != null) {
        //     Map<String, Object> rating = new LinkedHashMap<>();
        //     rating.put("@type", "AggregateRating");
        //     rating.put("ratingValue", product.getAverageRating().toString());
        //     rating.put("reviewCount", product.getReviewCount().toString());
        //     schema.put("aggregateRating", rating);
        // }

        // TODO: Step 8 - Return the schema
        // return ResponseEntity.ok(schema);

        // REMOVE THIS LINE when you implement the method:
        return ResponseEntity.ok(Map.of("error", "Not yet implemented"));
    }
}
