package com.workshop.aeogeo.controller;

import com.workshop.aeogeo.repository.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * FAQ Controller - Exercise 2: FAQ Schema Markup
 *
 * TODO: Implement this controller to return Schema.org FAQPage JSON-LD
 * for any product's frequently asked questions.
 *
 * Endpoint: GET /api/products/{id}/faq
 *
 * Requirements:
 * 1. Fetch published FAQs for the product
 * 2. Build FAQPage structure with @context and @type
 * 3. Transform FAQs into Question objects with nested Answer
 * 4. Return 404 if product has no FAQs
 * 5. Validate with Google Rich Results Test
 */
@RestController
@RequestMapping("/api/products")
public class FAQController {

    @Autowired
    private FAQRepository faqRepository;

    /**
     * Get FAQ Schema (JSON-LD) for a specific product.
     *
     * @param productId Product ID
     * @return FAQPage schema in JSON-LD format
     */
    @GetMapping("/{productId}/faq")
    public ResponseEntity<Map<String, Object>> getProductFAQ(@PathVariable Long productId) {
        // TODO: Implement this method

        // Step 1: Fetch published FAQs for the product
        // Hint: Use faqRepository.findByProductIdAndIsPublishedTrueOrderBySortOrder(productId)

        // Step 2: Check if FAQs exist, return 404 if none

        // Step 3: Build FAQPage structure
        // Add @context: "https://schema.org"
        // Add @type: "FAQPage"

        // Step 4: Transform FAQs into mainEntity array
        // Each FAQ becomes a Question object with:
        // - @type: "Question"
        // - name: faq.getQuestion()
        // - acceptedAnswer: {
        //     @type: "Answer",
        //     text: faq.getAnswer()
        //   }

        // Step 5: Return the schema
        // Hint: return ResponseEntity.ok(faqPage);

        return ResponseEntity.notFound().build(); // Replace with your implementation
    }
}
