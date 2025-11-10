package com.sap.commerce.workshop.controller;

import com.sap.commerce.workshop.model.FAQModel;
import com.sap.commerce.workshop.repository.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * FAQ Controller - SOLUTION for Exercise 2
 *
 * Complete implementation of Schema.org FAQPage JSON-LD endpoint.
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
        // Step 1: Fetch published FAQs
        List<FAQModel> faqs = faqRepository.findByProductIdAndIsPublishedTrueOrderBySortOrder(productId);

        // Step 2: Return 404 if no FAQs found
        if (faqs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Step 3: Build FAQPage structure
        Map<String, Object> faqPage = new LinkedHashMap<>();
        faqPage.put("@context", "https://schema.org");
        faqPage.put("@type", "FAQPage");

        // Step 4: Transform FAQs into Question objects
        List<Map<String, Object>> questions = faqs.stream()
                .map(faq -> {
                    // Create Answer object
                    Map<String, Object> answer = new LinkedHashMap<>();
                    answer.put("@type", "Answer");
                    answer.put("text", faq.getAnswer());

                    // Create Question object
                    Map<String, Object> question = new LinkedHashMap<>();
                    question.put("@type", "Question");
                    question.put("name", faq.getQuestion());
                    question.put("acceptedAnswer", answer);

                    return question;
                })
                .collect(Collectors.toList());

        faqPage.put("mainEntity", questions);

        // Step 5: Return the schema
        return ResponseEntity.ok(faqPage);
    }
}
