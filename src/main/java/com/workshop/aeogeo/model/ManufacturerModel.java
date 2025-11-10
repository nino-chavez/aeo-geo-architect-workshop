package com.workshop.aeogeo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * ManufacturerModel - Mimics SAP Commerce Cloud's Manufacturer/Brand structure
 *
 * Examples:
 * - Electronics: Sony, Canon, Samsung, Apple, Dell, HP
 * - Apparel: Nike, Adidas, Levi's, North Face
 */
@Entity
@Table(name = "manufacturers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Manufacturer code
     * Examples: "SONY", "CANON", "NIKE", "ADIDAS"
     */
    @Column(unique = true, nullable = false)
    private String code;

    /**
     * Brand name
     */
    @Column(nullable = false)
    private String name;

    /**
     * Brand description
     */
    @Column(length = 2000)
    private String description;

    /**
     * Brand logo URL
     */
    private String logoUrl;

    /**
     * Brand website
     */
    private String website;

    /**
     * Products from this manufacturer
     */
    @OneToMany(mappedBy = "manufacturer")
    private List<ProductModel> products = new ArrayList<>();

    /**
     * Country of origin
     */
    private String countryOfOrigin;

    /**
     * Constructor for quick setup
     */
    public ManufacturerModel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Constructor with website
     */
    public ManufacturerModel(String code, String name, String website) {
        this.code = code;
        this.name = name;
        this.website = website;
    }
}
