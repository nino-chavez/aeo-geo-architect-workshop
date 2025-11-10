package com.workshop.aeogeo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * MediaModel - Mimics SAP Commerce Cloud's Media structure
 *
 * SAP Commerce has sophisticated media management with:
 * - Multiple formats (thumbnail, zoom, product detail)
 * - Media folders and catalogs
 * - Media containers
 *
 * This simplified version focuses on product images.
 */
@Entity
@Table(name = "media")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The product this media belongs to
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductModel product;

    /**
     * Media URL
     */
    @Column(nullable = false)
    private String url;

    /**
     * Alt text (for accessibility and SEO)
     */
    private String altText;

    /**
     * Media type
     */
    @Enumerated(EnumType.STRING)
    private MediaType mediaType = MediaType.PRODUCT_IMAGE;

    /**
     * Format qualifier (thumbnail, zoom, product)
     */
    private String format;

    /**
     * Position for ordering
     */
    private Integer position;

    /**
     * Is this the primary image?
     */
    private Boolean isPrimary = false;

    /**
     * Constructor for quick image creation
     */
    public MediaModel(String url, String altText) {
        this.url = url;
        this.altText = altText;
    }

    /**
     * Constructor with format
     */
    public MediaModel(String url, String altText, String format) {
        this.url = url;
        this.altText = altText;
        this.format = format;
    }
}

enum MediaType {
    PRODUCT_IMAGE,
    PRODUCT_VIDEO,
    DOCUMENT,
    LOGO
}
