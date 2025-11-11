package com.workshop.aeogeo.controller;

import com.workshop.aeogeo.model.FAQModel;
import com.workshop.aeogeo.model.ProductModel;
import com.workshop.aeogeo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * REST Controller for FAQ Schema.org JSON-LD generation.
 *
 * Exercise 2: Implement Schema.org FAQPage markup for voice search optimization.
 *
 * Your task: Implement the getProductFAQ() method to return valid Schema.org
 * FAQPage JSON-LD for product-specific frequently asked questions.
 */
@RestController
@RequestMapping("/api/products")
public class FAQController {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Generate Schema.org FAQPage JSON-LD for a product's FAQs.
     *
     * @param id Product ID
     * @return Schema.org FAQPage JSON-LD
     */
    @GetMapping("/{id}/faq")
    public ResponseEntity<Map<String, Object>> getProductFAQ(@PathVariable Long id) {
        // TODO: Step 1 - Fetch the product
        // ProductModel product = productRepository.findById(id)
        //     .orElseThrow(() -> new RuntimeException("Product not found"));

        // TODO: Step 2 - Get published FAQs, sorted by sort_order
        // List<FAQModel> faqs = product.getFaqs().stream()
        //     .filter(FAQModel::getIsPublished)
        //     .sorted(Comparator.comparing(FAQModel::getSortOrder))
        //     .collect(Collectors.toList());

        // TODO: Step 3 - Return 404 if no published FAQs
        // if (faqs.isEmpty()) {
        //     return ResponseEntity.notFound().build();
        // }

        // TODO: Step 4 - Create the base FAQPage schema
        // Map<String, Object> schema = new LinkedHashMap<>();
        // schema.put("@context", "https://schema.org");
        // schema.put("@type", "FAQPage");

        // TODO: Step 5 - Build the mainEntity array (list of questions)
        // List<Map<String, Object>> questions = new ArrayList<>();
        //
        // for (FAQModel faq : faqs) {
        //     // Create Question object
        //     Map<String, Object> question = new LinkedHashMap<>();
        //     question.put("@type", "Question");
        //     question.put("name", faq.getQuestion());
        //
        //     // Create nested Answer object
        //     Map<String, Object> answer = new LinkedHashMap<>();
        //     answer.put("@type", "Answer");
        //     answer.put("text", faq.getAnswer());
        //
        //     question.put("acceptedAnswer", answer);
        //     questions.add(question);
        // }

        // TODO: Step 6 - Add mainEntity to schema
        // schema.put("mainEntity", questions);

        // TODO: Step 7 - Return the schema
        // return ResponseEntity.ok(schema);

        // REMOVE THIS LINE when you implement the method:
        return ResponseEntity.ok(Map.of("error", "Not yet implemented"));
    }
}
