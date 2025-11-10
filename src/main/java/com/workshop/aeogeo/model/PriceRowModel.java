package com.workshop.aeogeo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * PriceRowModel - Mimics SAP Commerce Cloud's PriceRow structure
 *
 * SAP Commerce uses PriceRows to handle complex pricing scenarios:
 * - Different prices for different user groups
 * - Time-based pricing
 * - Currency-specific pricing
 * - Volume-based discounts
 *
 * For this workshop, we'll use a simplified version.
 */
@Entity
@Table(name = "price_rows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceRowModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The product this price belongs to
     */
    @OneToOne
    @JoinColumn(name = "product_id")
    private ProductModel product;

    /**
     * Price value
     */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    /**
     * Currency code (ISO 4217)
     * Examples: "USD", "EUR", "GBP"
     */
    @Column(nullable = false, length = 3)
    private String currency;

    /**
     * Original price (for discount display)
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal originalPrice;

    /**
     * Price validity start date
     */
    private LocalDateTime validFrom;

    /**
     * Price validity end date
     */
    private LocalDateTime validTo;

    /**
     * Net price (excluding tax) flag
     */
    private Boolean netPrice = false;

    /**
     * Constructor for simple pricing
     */
    public PriceRowModel(BigDecimal price, String currency) {
        this.price = price;
        this.currency = currency;
    }

    /**
     * Constructor with discount
     */
    public PriceRowModel(BigDecimal price, BigDecimal originalPrice, String currency) {
        this.price = price;
        this.originalPrice = originalPrice;
        this.currency = currency;
    }

    /**
     * Check if price is currently valid
     */
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        boolean afterStart = validFrom == null || now.isAfter(validFrom);
        boolean beforeEnd = validTo == null || now.isBefore(validTo);
        return afterStart && beforeEnd;
    }

    /**
     * Calculate discount percentage
     */
    public Double getDiscountPercentage() {
        if (originalPrice == null || originalPrice.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        BigDecimal discount = originalPrice.subtract(price);
        BigDecimal percentage = discount.divide(originalPrice, 4, BigDecimal.ROUND_HALF_UP)
                                       .multiply(BigDecimal.valueOf(100));
        return percentage.doubleValue();
    }
}
