# Exercise 1: Product Schema Markup (Schema.org)

**Duration:** 45-60 minutes
**Difficulty:** Beginner
**Topic:** Answer Engine Optimization (AEO) - Structured Data

## Learning Objectives

By completing this exercise, you will:

1. **Understand Schema.org** vocabulary and JSON-LD format
2. **Implement Product schema** with all required and recommended properties
3. **Validate structured data** using Google Rich Results Test
4. **Learn AEO fundamentals** - how search engines parse product information
5. **Apply SAP Commerce patterns** to expose structured data via REST endpoints

## Background: Why Schema.org Matters for AEO

Modern search engines (Google, Bing) and answer engines (ChatGPT, Perplexity, Claude) rely on structured data to understand webpage content. Schema.org provides a standardized vocabulary for marking up data in a machine-readable format.

**Benefits of Product Schema:**
- **Rich snippets** in search results (ratings, price, availability)
- **Voice search optimization** - assistants can read product details
- **Answer engine visibility** - LLMs can extract accurate information
- **E-commerce integration** - Google Shopping, merchant centers
- **SEO rankings** - structured data is a ranking signal

### JSON-LD Format

JSON-LD (JavaScript Object Notation for Linked Data) is the recommended format by Google. It's embedded in a `<script>` tag with `type="application/ld+json"`.

**Example:**
```json
{
  "@context": "https://schema.org",
  "@type": "Product",
  "name": "Sony Alpha 7 IV",
  "description": "Full-frame mirrorless camera...",
  "brand": {
    "@type": "Brand",
    "name": "Sony"
  },
  "offers": {
    "@type": "Offer",
    "price": "2498.00",
    "priceCurrency": "USD"
  }
}
```

## Your Task

Implement a REST endpoint that returns Product schema (JSON-LD) for any product in our database.

### Requirements

**Endpoint:** `GET /api/products/{id}/schema`

**Required Properties:**
- `@context`: Must be `https://schema.org`
- `@type`: Must be `Product`
- `name`: Product name
- `description`: Product description
- `image`: Product image URL(s)
- `brand`: Manufacturer information (nested Brand schema)
- `offers`: Price, currency, availability (nested Offer schema)

**Recommended Properties (Implement These!):**
- `aggregateRating`: Average rating and review count
- `sku`: Product code
- `category`: Product category name

**Stretch Goals:**
- Support multiple images array
- Include technical specifications in `additionalProperty`
- Handle missing data gracefully (null checks)

### Expected Output Format

```json
{
  "@context": "https://schema.org",
  "@type": "Product",
  "name": "Sony Alpha 7 IV",
  "description": "Full-frame mirrorless camera with 33MP sensor...",
  "image": "https://images.unsplash.com/photo-1606982219448-b54e166523d5",
  "sku": "sony-a7iv",
  "brand": {
    "@type": "Brand",
    "name": "Sony"
  },
  "offers": {
    "@type": "Offer",
    "price": "2498.00",
    "priceCurrency": "USD",
    "availability": "https://schema.org/InStock"
  },
  "aggregateRating": {
    "@type": "AggregateRating",
    "ratingValue": "4.8",
    "reviewCount": "324"
  }
}
```

## Getting Started

### Step 1: Create the Controller

Create a new controller class:
```
src/main/java/com/sap/commerce/workshop/controller/ProductSchemaController.java
```

### Step 2: Inject Dependencies

You'll need:
- `ProductRepository` - to fetch product data
- `ObjectMapper` - to build JSON response

### Step 3: Implement the Endpoint

Use `@GetMapping` for the endpoint:
```java
@GetMapping("/api/products/{id}/schema")
public ResponseEntity<Map<String, Object>> getProductSchema(@PathVariable Long id)
```

### Step 4: Build the JSON-LD Structure

Use `Map<String, Object>` or Jackson `ObjectNode` to build the nested structure.

**Tip:** Start with required fields, then add recommended fields, then handle edge cases.

## Validation

### Manual Testing

1. Start the application:
   ```bash
   mvn spring-boot:run
   ```

2. Test the endpoint:
   ```bash
   curl http://localhost:8080/api/products/1/schema | jq
   ```

3. Verify all required fields are present

### Google Rich Results Test

1. Copy the JSON-LD output
2. Go to: https://search.google.com/test/rich-results
3. Paste your JSON-LD
4. Click "Test Code"
5. Verify: **"Page is eligible for rich results"**

**Common Issues:**
- Missing `@context` or `@type` → Not recognized as Schema.org
- Invalid price format → Must be string like "2498.00"
- Missing `offers` → Warning about incomplete data

## Success Criteria

- [ ] Endpoint returns valid JSON-LD for all products
- [ ] All required properties are present
- [ ] Aggregate rating included (when available)
- [ ] Google Rich Results Test passes with no errors
- [ ] Handles products without ratings gracefully
- [ ] Code is clean, readable, and follows Spring Boot conventions

## Progressive Hints

Stuck? We've provided 10 levels of progressive hints:

<details>
<summary>Hint 1: Controller Structure</summary>

Create a `@RestController` with the ProductRepository injected. Use `@GetMapping` with a path variable for product ID.
</details>

<details>
<summary>Hint 2: Fetching Product</summary>

Use `productRepository.findById(id)` and handle the Optional with `.orElseThrow()` or return 404 if not found.
</details>

<details>
<summary>Hint 3: Building JSON Structure</summary>

Use `Map<String, Object>` for nested structures. Start with `@context` and `@type` as the first entries.
</details>

<details>
<summary>Hint 4: Brand Schema</summary>

Brand is a nested object with its own `@type: "Brand"`. Create a separate Map for the brand and include manufacturer name.
</details>

<details>
<summary>Hint 5: Offers Schema</summary>

Offers requires `@type: "Offer"`, price as string, priceCurrency, and availability URL. Use `BigDecimal.toString()` for price.
</details>

<details>
<summary>Hint 6: Aggregate Rating</summary>

Only include aggregateRating if `product.getAverageRating()` is not null. Check before adding to the map.
</details>

<details>
<summary>Hint 7: Image Handling</summary>

Use `product.getPrimaryImageUrl()` helper method. For multiple images, create an array of image URLs.
</details>

<details>
<summary>Hint 8: Availability Status</summary>

Map stock level to Schema.org availability:
- `stockLevel > 0` → `"https://schema.org/InStock"`
- `stockLevel == 0` → `"https://schema.org/OutOfStock"`
- `stockLevel == null` → `"https://schema.org/PreOrder"`
</details>

<details>
<summary>Hint 9: Null Safety</summary>

Always check for null before accessing related entities:
```java
if (product.getManufacturer() != null) {
    // add brand
}
```
</details>

<details>
<summary>Hint 10: Complete Example Structure</summary>

```java
Map<String, Object> schema = new LinkedHashMap<>();
schema.put("@context", "https://schema.org");
schema.put("@type", "Product");
schema.put("name", product.getName());
// ... continue building the map
return ResponseEntity.ok(schema);
```
</details>

## Next Steps

After completing this exercise:

1. **Validate 3 different products** with Google Rich Results Test
2. **Experiment with additional properties** like `gtin`, `mpn`, `color`, `size`
3. **Review the solution branch** to compare your implementation
4. **Proceed to Exercise 2: FAQ Schema** to learn about FAQ rich snippets

## Resources

- [Schema.org Product](https://schema.org/Product)
- [Google Rich Results Test](https://search.google.com/test/rich-results)
- [Google Search Product Schema Guide](https://developers.google.com/search/docs/appearance/structured-data/product)
- [JSON-LD Specification](https://json-ld.org/)

## Troubleshooting

**Issue:** 404 Not Found
**Solution:** Check product ID exists in database. Try `http://localhost:8080/api/products/1/schema`

**Issue:** NullPointerException
**Solution:** Add null checks for manufacturer, category, and rating fields

**Issue:** Invalid JSON-LD in validator
**Solution:** Ensure @context is exactly `https://schema.org` (no trailing slash on org)

**Issue:** Price not showing in rich results
**Solution:** Price must be a string, not a number: `"2498.00"` not `2498.00`

---

**Time to code!** Start with the required fields, test iteratively, then enhance with recommended properties. Good luck! 🚀
