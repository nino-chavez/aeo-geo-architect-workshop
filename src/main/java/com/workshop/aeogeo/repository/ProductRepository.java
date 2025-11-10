package com.workshop.aeogeo.repository;

import com.workshop.aeogeo.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    /**
     * Find product by code (unique identifier)
     */
    Optional<ProductModel> findByCode(String code);

    /**
     * Find products by manufacturer
     */
    @Query("SELECT p FROM ProductModel p WHERE p.manufacturer.code = :manufacturerCode")
    List<ProductModel> findByManufacturerCode(String manufacturerCode);

    /**
     * Find products by category
     */
    @Query("SELECT p FROM ProductModel p WHERE p.category.code = :categoryCode")
    List<ProductModel> findByCategoryCode(String categoryCode);

    /**
     * Find products with rating above threshold
     */
    @Query("SELECT p FROM ProductModel p WHERE p.averageRating >= :minRating")
    List<ProductModel> findByMinimumRating(Double minRating);

    /**
     * Find in-stock products
     */
    @Query("SELECT p FROM ProductModel p WHERE p.stockLevel > 0")
    List<ProductModel> findInStock();

    /**
     * Search products by name or description
     */
    @Query("SELECT p FROM ProductModel p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<ProductModel> searchProducts(String searchTerm);
}
