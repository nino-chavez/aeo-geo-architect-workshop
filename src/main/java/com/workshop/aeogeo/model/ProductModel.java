package com.workshop.aeogeo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductModel - Mimics SAP Commerce Cloud's Product entity structure
 *
 * This represents products from the electronics and apparel demo catalogs
 * that ship with SAP Commerce (formerly Hybris).
 *
 * Example products:
 * - Electronics: Cameras, Laptops, Smartphones
 * - Apparel: T-shirts, Jackets, Shoes
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Product code (unique identifier)
     * Examples: "CAMERA-X100", "LAPTOP-ZENBOOK", "TSHIRT-SUMMER"
     */
    @Column(unique = true, nullable = false)
    private String code;

    /**
     * Product name
     */
    @Column(nullable = false)
    private String name;

    /**
     * Long description (marketing copy)
     */
    @Column(length = 5000)
    private String description;

    /**
     * Short summary (for listings)
     */
    @Column(length = 500)
    private String summary;

    /**
     * Manufacturer/Brand relationship
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    private ManufacturerModel manufacturer;

    /**
     * Price information (SAP Commerce uses PriceRow)
     */
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PriceRowModel priceRow;

    /**
     * Product category (Electronics, Apparel, etc.)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryModel category;

    /**
     * Classification attributes (technical specifications)
     * In SAP Commerce, these are stored in the classification system
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassificationAttributeModel> features = new ArrayList<>();

    /**
     * Average rating (for reviews)
     */
    private Double averageRating;

    /**
     * Number of reviews
     */
    private Integer reviewCount;

    /**
     * EAN/GTIN barcode
     */
    private String ean;

    /**
     * Stock availability
     */
    private Integer stockLevel;

    /**
     * Product images
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaModel> images = new ArrayList<>();

    /**
     * Approval status (SAP Commerce workflow)
     */
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.APPROVED;

    /**
     * Creation timestamp
     */
    private LocalDateTime creationTime;

    /**
     * Last modified timestamp
     */
    private LocalDateTime modifiedTime;

    /**
     * Helper method to add classification attribute
     */
    public void addFeature(ClassificationAttributeModel feature) {
        features.add(feature);
        feature.setProduct(this);
    }

    /**
     * Helper method to add image
     */
    public void addImage(MediaModel image) {
        images.add(image);
        image.setProduct(this);
    }

    /**
     * Check if product is in stock
     */
    public boolean isInStock() {
        return stockLevel != null && stockLevel > 0;
    }

    @PrePersist
    protected void onCreate() {
        creationTime = LocalDateTime.now();
        modifiedTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedTime = LocalDateTime.now();
    }
}

enum ApprovalStatus {
    APPROVED,
    PENDING,
    REJECTED
}
