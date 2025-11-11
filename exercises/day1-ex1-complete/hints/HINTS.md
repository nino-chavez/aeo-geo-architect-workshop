# Exercise 1: Product Schema - Progressive Hints

Use these hints progressively if you get stuck. Try to solve as much as you can before revealing each hint.

---

<details>
<summary><strong>Hint 1: Understanding Schema.org Structure</strong></summary>

Schema.org JSON-LD follows a specific structure:
```json
{
  "@context": "https://schema.org",
  "@type": "Product",
  "name": "Product Name Here"
}
```

The `@context` tells search engines you're using Schema.org vocabulary.
The `@type` specifies what kind of thing you're describing.

</details>

---

<details>
<summary><strong>Hint 2: Required Product Properties</strong></summary>

Google requires these minimum properties for Product rich results:
- `name` - The product name
- `image` - At least one product image
- `offers` - Pricing information

Start by mapping these three from your `ProductModel` entity.

</details>

---

<details>
<summary><strong>Hint 3: Creating the Offers Object</strong></summary>

The `offers` property should be an object of type `Offer`:
```json
"offers": {
  "@type": "Offer",
  "priceCurrency": "USD",
  "price": "1299.99",
  "availability": "https://schema.org/InStock"
}
```

Map this from your product's price and stock level.

</details>

---

<details>
<summary><strong>Hint 4: Handling the Brand</strong></summary>

The `brand` property can be either a `Brand` object or a simple text string:
```json
"brand": {
  "@type": "Brand",
  "name": "Sony"
}
```

Your `ProductModel` has a `ManufacturerModel` - use that!

</details>

---

<details>
<summary><strong>Hint 5: Adding Images</strong></summary>

Images can be a single URL string or an array of URLs:
```json
"image": [
  "https://example.com/image1.jpg",
  "https://example.com/image2.jpg"
]
```

Your `ProductModel` has a `List<MediaModel>` - extract the URLs.

</details>

---

<details>
<summary><strong>Hint 6: Aggregate Rating Structure</strong></summary>

If your product has reviews, add an `aggregateRating`:
```json
"aggregateRating": {
  "@type": "AggregateRating",
  "ratingValue": "4.8",
  "reviewCount": "324"
}
```

Check if `averageRating` and `reviewCount` are present before adding this.

</details>

---

<details>
<summary><strong>Hint 7: Using LinkedHashMap for Order</strong></summary>

JSON-LD property order matters for readability. Use `LinkedHashMap` to maintain insertion order:
```java
Map<String, Object> schema = new LinkedHashMap<>();
schema.put("@context", "https://schema.org");
schema.put("@type", "Product");
```

This keeps your JSON clean and predictable.

</details>

---

<details>
<summary><strong>Hint 8: Handling Null Values</strong></summary>

Not all products have all properties. Use defensive checks:
```java
if (product.getManufacturer() != null) {
    Map<String, Object> brand = new LinkedHashMap<>();
    brand.put("@type", "Brand");
    brand.put("name", product.getManufacturer().getName());
    schema.put("brand", brand);
}
```

This prevents NullPointerExceptions.

</details>

---

<details>
<summary><strong>Hint 9: Complete Controller Method Signature</strong></summary>

Your controller method should look like:
```java
@GetMapping("/{id}/schema")
public ResponseEntity<Map<String, Object>> getProductSchema(@PathVariable Long id) {
    // Your implementation here
}
```

Return `ResponseEntity.ok(schema)` at the end.

</details>

---

<details>
<summary><strong>Hint 10: Validation with Google Rich Results Test</strong></summary>

Once implemented:
1. Start your application
2. Call: `curl http://localhost:8080/api/products/1/schema`
3. Copy the JSON output
4. Go to: https://search.google.com/test/rich-results
5. Paste and validate

Google will tell you if anything is missing or invalid!

</details>

---

## Still Stuck?

Check the solution in `solution/ProductSchemaController.java` for the complete implementation.
