package com.workshop.aeogeo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * ClassificationAttributeModel - Mimics SAP Commerce Cloud's Classification System
 *
 * SAP Commerce uses a sophisticated classification system to manage product attributes.
 * This simplified version captures the essential pattern.
 *
 * Examples:
 * Electronics:
 * - "Megapixels" = "24.2 MP"
 * - "Sensor Size" = "APS-C"
 * - "ISO Range" = "100-25600"
 * - "Screen Size" = "15.6 inches"
 * - "Processor" = "Intel Core i7"
 * - "RAM" = "16 GB"
 *
 * Apparel:
 * - "Material" = "100% Cotton"
 * - "Size" = "Medium"
 * - "Color" = "Navy Blue"
 * - "Fit" = "Slim"
 * - "Care Instructions" = "Machine wash cold"
 */
@Entity
@Table(name = "classification_attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationAttributeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The product this attribute belongs to
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductModel product;

    /**
     * Attribute name/key
     * Examples: "Megapixels", "Screen Size", "Material", "Color"
     */
    @Column(nullable = false)
    private String attributeName;

    /**
     * Attribute value
     * Examples: "24.2 MP", "15.6 inches", "100% Cotton", "Navy Blue"
     */
    @Column(nullable = false)
    private String attributeValue;

    /**
     * Unit of measurement (optional)
     * Examples: "MP", "inches", "GB", "kg"
     */
    private String unit;

    /**
     * Classification system category
     * Examples: "Technical", "Physical", "Material"
     */
    private String classificationType;

    /**
     * Position for ordering (SAP Commerce uses this for display order)
     */
    private Integer position;

    /**
     * Constructor for quick attribute creation
     */
    public ClassificationAttributeModel(String attributeName, String attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    /**
     * Constructor with unit
     */
    public ClassificationAttributeModel(String attributeName, String attributeValue, String unit) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.unit = unit;
    }

    /**
     * Get formatted value with unit
     */
    public String getFormattedValue() {
        if (unit != null && !unit.isEmpty()) {
            return attributeValue + " " + unit;
        }
        return attributeValue;
    }
}
