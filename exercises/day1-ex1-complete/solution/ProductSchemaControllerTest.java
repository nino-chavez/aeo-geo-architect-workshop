package com.sap.commerce.workshop.controller;

import com.sap.commerce.workshop.model.*;
import com.sap.commerce.workshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Unit tests for ProductSchemaController - SOLUTION
 */
@WebMvcTest(ProductSchemaController.class)
class ProductSchemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void testGetProductSchema_Success() throws Exception {
        // Arrange
        ManufacturerModel manufacturer = new ManufacturerModel();
        manufacturer.setId(1L);
        manufacturer.setCode("sony");
        manufacturer.setName("Sony");

        CategoryModel category = new CategoryModel();
        category.setId(1L);
        category.setCode("cameras");
        category.setName("Cameras");

        MediaModel image = new MediaModel();
        image.setUrl("https://example.com/image.jpg");
        image.setPrimary(true);

        ProductModel product = new ProductModel();
        product.setId(1L);
        product.setCode("sony-a7iv");
        product.setName("Sony Alpha 7 IV");
        product.setDescription("Full-frame mirrorless camera");
        product.setManufacturer(manufacturer);
        product.setCategory(category);
        product.setPrice(BigDecimal.valueOf(2498.00));
        product.setCurrencyCode("USD");
        product.setStockLevel(45);
        product.setAverageRating(4.8);
        product.setReviewCount(324);
        product.setImages(List.of(image));
        product.setSpecifications(new ArrayList<>());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        mockMvc.perform(get("/api/products/1/schema"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['@context']").value("https://schema.org"))
                .andExpect(jsonPath("$['@type']").value("Product"))
                .andExpect(jsonPath("$.name").value("Sony Alpha 7 IV"))
                .andExpect(jsonPath("$.description").value("Full-frame mirrorless camera"))
                .andExpect(jsonPath("$.sku").value("sony-a7iv"))
                .andExpect(jsonPath("$.category").value("Cameras"))
                .andExpect(jsonPath("$.brand['@type']").value("Brand"))
                .andExpect(jsonPath("$.brand.name").value("Sony"))
                .andExpect(jsonPath("$.offers['@type']").value("Offer"))
                .andExpect(jsonPath("$.offers.price").value("2498.00"))
                .andExpect(jsonPath("$.offers.priceCurrency").value("USD"))
                .andExpect(jsonPath("$.offers.availability").value("https://schema.org/InStock"))
                .andExpect(jsonPath("$.aggregateRating['@type']").value("AggregateRating"))
                .andExpect(jsonPath("$.aggregateRating.ratingValue").value("4.8"))
                .andExpect(jsonPath("$.aggregateRating.reviewCount").value("324"))
                .andExpect(jsonPath("$.image").value("https://example.com/image.jpg"));
    }

    @Test
    void testGetProductSchema_ProductNotFound() throws Exception {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/products/999/schema"))
                .andExpect(status().is5xxServerError()); // Exception thrown
    }

    @Test
    void testGetProductSchema_NoRating() throws Exception {
        // Arrange
        ProductModel product = new ProductModel();
        product.setId(1L);
        product.setCode("test-product");
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setCurrencyCode("USD");
        product.setStockLevel(10);
        product.setAverageRating(null); // No rating
        product.setReviewCount(null);
        product.setImages(new ArrayList<>());
        product.setSpecifications(new ArrayList<>());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        mockMvc.perform(get("/api/products/1/schema"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aggregateRating").doesNotExist());
    }

    @Test
    void testGetProductSchema_OutOfStock() throws Exception {
        // Arrange
        ProductModel product = new ProductModel();
        product.setId(1L);
        product.setCode("test-product");
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setCurrencyCode("USD");
        product.setStockLevel(0); // Out of stock
        product.setImages(new ArrayList<>());
        product.setSpecifications(new ArrayList<>());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        mockMvc.perform(get("/api/products/1/schema"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.offers.availability").value("https://schema.org/OutOfStock"));
    }
}
