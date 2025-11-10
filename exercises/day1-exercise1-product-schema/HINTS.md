# Exercise 1: Product Schema - Progressive Hints

**Instructions**: Reveal hints one at a time. Try to solve the problem before reading the next hint.

---

## 🔍 Hint 1: ProductSchema Fields (Basic)

<details>
<summary>Click to reveal</summary>

Your `ProductSchema` class needs these basic fields:

```java
public class ProductSchema extends BaseSchema {
    public ProductSchema() {
        super("Product");
    }

    private String name;
    private String description;
    private String sku;
    private String image;
    private BrandSchema brand;
    private OfferSchema offers;

    // Add getters/setters (or use @Data from Lombok)
}
```

**Why these fields?**
- `name`: Product title (required by Google)
- `description`: Marketing copy
- `sku`: Product code (your unique identifier)
- `image`: URL to product image (required for rich results)
- `brand`: Who makes it (required)
- `offers`: Price information (required)

</details>

---

## 🔍 Hint 2: OfferSchema Structure

<details>
<summary>Click to reveal</summary>

Schema.org requires price information in a specific format:

```java
@Data
@EqualsAndHashCode(callSuper = true)
public class OfferSchema extends BaseSchema {
    public OfferSchema() {
        super("Offer");
    }

    private String price;           // "1998.00" (String, not number!)
    private String priceCurrency;   // "USD"
    private String availability;    // "https://schema.org/InStock"
    private String url;             // Link to product page (optional)
}
```

**Important**: Price must be a String like `"1998.00"`, not a number!

**Availability enum values**:
- `https://schema.org/InStock`
- `https://schema.org/OutOfStock`
- `https://schema.org/PreOrder`

</details>

---

## 🔍 Hint 3: Mapping Manufacturer to Brand

<details>
<summary>Click to reveal</summary>

Your `mapManufacturer` method should look like this:

```java
private BrandSchema mapManufacturer(ManufacturerModel manufacturer) {
    if (manufacturer == null) {
        return null;
    }

    BrandSchema brand = new BrandSchema();
    brand.setName(manufacturer.getName());

    // Optional: Add more fields
    if (manufacturer.getWebsite() != null) {
        // brand.setUrl(manufacturer.getWebsite());
    }

    return brand;
}
```

**Null safety**: Always check if the manufacturer is null!

</details>

---

## 🔍 Hint 4: Mapping PriceRow to Offer

<details>
<summary>Click to reveal</summary>

Convert SAP Commerce's `PriceRowModel` to Schema.org's `OfferSchema`:

```java
private OfferSchema mapPriceRow(PriceRowModel priceRow) {
    if (priceRow == null) {
        return null;
    }

    OfferSchema offer = new OfferSchema();

    // Convert BigDecimal to String
    offer.setPrice(priceRow.getPrice().toString());
    offer.setPriceCurrency(priceRow.getCurrency());

    // Set availability based on stock
    // If product is in stock: "https://schema.org/InStock"
    // If out of stock: "https://schema.org/OutOfStock"

    return offer;
}
```

**Pro tip**: Check `product.isInStock()` to set availability correctly.

</details>

---

## 🔍 Hint 5: Mapping Classification Attributes (Technical Specs)

<details>
<summary>Click to reveal</summary>

SAP Commerce stores technical specs as `ClassificationAttributeModel`. Map them to `PropertyValueSchema`:

**First, create PropertyValueSchema**:

```java
@Data
@EqualsAndHashCode(callSuper = true)
public class PropertyValueSchema extends BaseSchema {
    public PropertyValueSchema() {
        super("PropertyValue");
    }

    private String name;        // "Megapixels"
    private String value;       // "24.2 MP"
}
```

**Then, map the list**:

```java
private List<PropertyValueSchema> mapFeatures(List<ClassificationAttributeModel> features) {
    if (features == null || features.isEmpty()) {
        return null;
    }

    return features.stream()
        .map(this::mapSingleFeature)
        .collect(Collectors.toList());
}

private PropertyValueSchema mapSingleFeature(ClassificationAttributeModel feature) {
    PropertyValueSchema prop = new PropertyValueSchema();
    prop.setName(feature.getAttributeName());
    prop.setValue(feature.getFormattedValue()); // Includes unit (e.g., "24.2 MP")
    return prop;
}
```

</details>

---

## 🔍 Hint 6: Handling Product Images

<details>
<summary>Click to reveal</summary>

Schema.org `Product` can have a single `image` (String) or multiple `image` (array).

For simplicity, use the **primary image**:

```java
private String getProductImage(ProductModel product) {
    if (product.getImages() == null || product.getImages().isEmpty()) {
        return null;
    }

    // Find the primary image
    return product.getImages().stream()
        .filter(MediaModel::getIsPrimary)
        .map(MediaModel::getUrl)
        .findFirst()
        .orElse(product.getImages().get(0).getUrl()); // Fallback to first image
}
```

Then in your schema generation:
```java
schema.setImage(getProductImage(product));
```

</details>

---

## 🔍 Hint 7: REST Controller Implementation

<details>
<summary>Click to reveal</summary>

Complete controller implementation:

```java
@GetMapping("/{code}/schema")
public ResponseEntity<ProductSchema> getProductSchema(@PathVariable String code) {
    // Find product by code
    Optional<ProductModel> productOpt = productRepository.findByCode(code);

    if (productOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    ProductModel product = productOpt.get();

    // Generate schema
    ProductSchema schema = schemaService.generateProductSchema(product);

    // Return as JSON
    return ResponseEntity.ok(schema);
}
```

**Spring Boot magic**: The `ProductSchema` is automatically serialized to JSON!

</details>

---

## 🔍 Hint 8: Adding Aggregate Rating (Optional)

<details>
<summary>Click to reveal</summary>

If your product has reviews, add `AggregateRating`:

**Create AggregateRatingSchema**:

```java
@Data
@EqualsAndHashCode(callSuper = true)
public class AggregateRatingSchema extends BaseSchema {
    public AggregateRatingSchema() {
        super("AggregateRating");
    }

    private Double ratingValue;     // 4.5
    private Integer reviewCount;    // 127
    private Double bestRating;      // 5.0 (optional)
    private Double worstRating;     // 1.0 (optional)
}
```

**Map it**:

```java
private AggregateRatingSchema mapRating(ProductModel product) {
    if (product.getAverageRating() == null || product.getReviewCount() == null) {
        return null;
    }

    AggregateRatingSchema rating = new AggregateRatingSchema();
    rating.setRatingValue(product.getAverageRating());
    rating.setReviewCount(product.getReviewCount());
    rating.setBestRating(5.0);
    rating.setWorstRating(1.0);

    return rating;
}
```

**Add to ProductSchema**:

```java
schema.setAggregateRating(mapRating(product));
```

</details>

---

## 🔍 Hint 9: Using GitHub Copilot

<details>
<summary>Click to reveal</summary>

If you have GitHub Copilot, try these prompts:

**Prompt 1** (Generate POJO):
```
Create a Java class ProductSchema that extends BaseSchema with fields
for name, description, brand (BrandSchema), offers (OfferSchema),
and additionalProperty (List<PropertyValueSchema>). Use Lombok annotations.
```

**Prompt 2** (Generate mapping):
```
Write a method that maps a ProductModel to ProductSchema.
The ProductModel has: name, description, manufacturer (ManufacturerModel),
priceRow (PriceRowModel), and features (List<ClassificationAttributeModel>).
```

**Prompt 3** (Fix errors):
```
This JSON needs to validate against Schema.org Product specification.
What fields am I missing?
```

</details>

---

## 🔍 Hint 10: Common Mistakes

<details>
<summary>Click to reveal</summary>

**Mistake 1**: Price as a number
```java
// ❌ Wrong
offer.setPrice(1998.00);

// ✅ Correct
offer.setPrice("1998.00");
```

**Mistake 2**: Forgetting `@JsonProperty`
```java
// ❌ Wrong - Java can't have fields starting with @
private String @context;

// ✅ Correct
@JsonProperty("@context")
private String context;
```

**Mistake 3**: Not calling super()
```java
// ❌ Wrong
public ProductSchema() {
    // Missing super call!
}

// ✅ Correct
public ProductSchema() {
    super("Product");
}
```

**Mistake 4**: Circular references
```java
// ❌ Wrong - causes infinite loop
@Data  // Generates toString() that includes all fields
public class ProductSchema extends BaseSchema {
    private BrandSchema brand;
}

@Data
public class BrandSchema extends BaseSchema {
    private ProductSchema product;  // CIRCULAR!
}

// ✅ Correct - no circular references in Schema.org
// Brand should NOT reference Product
```

</details>

---

## 🚀 Still Stuck?

1. **Check the checkpoints**: See `checkpoints/` for working code at each step
2. **Read the solution**: See `SOLUTION.md` for our complete implementation
3. **Ask for help**: Pair with another participant or ask the instructor
4. **Use AI**: Ask GitHub Copilot or Claude to explain the error

Remember: **Struggling is part of learning!** Don't rush to the solution.
