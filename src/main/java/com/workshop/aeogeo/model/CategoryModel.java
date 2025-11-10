package com.workshop.aeogeo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * CategoryModel - Mimics SAP Commerce Cloud's Category structure
 *
 * SAP Commerce uses hierarchical categories:
 * - Electronics
 *   - Cameras
 *     - Digital Cameras
 *     - Film Cameras
 *   - Computers
 *     - Laptops
 *     - Desktops
 * - Apparel
 *   - Men
 *     - Shirts
 *     - Pants
 *   - Women
 *     - Dresses
 *     - Shoes
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Category code
     * Examples: "electronics", "cameras", "laptops", "apparel", "mens-shirts"
     */
    @Column(unique = true, nullable = false)
    private String code;

    /**
     * Category name
     */
    @Column(nullable = false)
    private String name;

    /**
     * Category description
     */
    @Column(length = 2000)
    private String description;

    /**
     * Parent category (for hierarchy)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CategoryModel parent;

    /**
     * Child categories
     */
    @OneToMany(mappedBy = "parent")
    private List<CategoryModel> children = new ArrayList<>();

    /**
     * Products in this category
     */
    @OneToMany(mappedBy = "category")
    private List<ProductModel> products = new ArrayList<>();

    /**
     * Constructor for quick setup
     */
    public CategoryModel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Constructor with parent
     */
    public CategoryModel(String code, String name, CategoryModel parent) {
        this.code = code;
        this.name = name;
        this.parent = parent;
    }

    /**
     * Helper to add child category
     */
    public void addChild(CategoryModel child) {
        children.add(child);
        child.setParent(this);
    }

    /**
     * Get full category path (e.g., "Electronics > Cameras > Digital Cameras")
     */
    public String getCategoryPath() {
        if (parent == null) {
            return name;
        }
        return parent.getCategoryPath() + " > " + name;
    }
}
