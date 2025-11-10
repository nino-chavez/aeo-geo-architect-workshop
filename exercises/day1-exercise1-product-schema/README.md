# Day 1 - Exercise 1: Product Schema

**Goal**: Create Schema.org JSON-LD markup for a product so that Google and AI models can understand your catalog.

**Time**: 60 minutes

**What You'll Build**: A service that converts SAP Commerce `ProductModel` → Schema.org `Product` JSON-LD

---

## 🎯 The Problem

Your e-commerce site has 50,000 products. When someone searches Google for:
> "What's the megapixel count of the Sony Alpha camera?"

Google shows: *"We couldn't find relevant results"*

**Why?** Your product data looks like this to a machine:

```html
<div class="product">
  <h1>Sony Alpha A7 III</h1>
  <p>Amazing camera with great specs!</p>
  <span>$1,998.00</span>
</div>
```

The machine doesn't know:
- "Sony Alpha A7 III" is a **product name**
- "$1,998.00" is a **price** (not a serial number or year)
- "Sony" is a **brand**
- Where the **megapixel** spec is

---

## ✅ The Solution: Schema.org JSON-LD

Add this to your HTML `<head>`:

```json
{
  "@context": "https://schema.org",
  "@type": "Product",
  "name": "Sony Alpha A7 III",
  "brand": {
    "@type": "Brand",
    "name": "Sony"
  },
  "offers": {
    "@type": "Offer",
    "price": "1998.00",
    "priceCurrency": "USD"
  },
  "additionalProperty": [
    {
      "@type": "PropertyValue",
      "name": "Megapixels",
      "value": "24.2 MP"
    }
  ]
}
```

Now the machine **understands**:
- ✅ This is a Product
- ✅ The brand is Sony
- ✅ The price is $1,998 USD
- ✅ It has 24.2 megapixels

---

## 📋 Your Task

Build a Spring Boot service that:

1. **Fetches** a `ProductModel` from the database (we've mocked this for you)
2. **Maps** it to a Schema.org `ProductSchema` POJO
3. **Serializes** to JSON-LD
4. **Returns** it via a REST endpoint: `GET /products/{code}/schema`

---

## 🛠️ Step-by-Step Guide

### **Step 1: Explore the Existing Models (10 min)**

Look at the SAP Commerce-inspired models we've provided:

```bash
# Open these files and read the comments
src/main/java/com/workshop/aeogeo/model/ProductModel.java
src/main/java/com/workshop/aeogeo/model/ManufacturerModel.java
src/main/java/com/workshop/aeogeo/model/PriceRowModel.java
src/main/java/com/workshop/aeogeo/model/ClassificationAttributeModel.java
```

**Key observations**:
- `ProductModel` has: name, manufacturer, priceRow, features (classification attributes)
- This matches SAP Commerce Cloud's structure
- Classification attributes store technical specs (megapixels, screen size, etc.)

---

### **Step 2: Create Base Schema Class (10 min)**

All Schema.org objects need `@context` and `@type`. Create a base class:

**File**: `src/main/java/com/workshop/aeogeo/schema/BaseSchema.java`

```java
package com.workshop.aeogeo.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public abstract class BaseSchema {

    @JsonProperty("@context")
    private final String context = "https://schema.org";

    @JsonProperty("@type")
    private String type;

    public BaseSchema(String type) {
        this.type = type;
    }
}
```

**Why `@JsonProperty("@context")`?**
- JSON-LD uses `@context` (with `@` symbol)
- Java fields can't start with `@`
- Jackson's `@JsonProperty` annotation fixes this

---

### **Step 3: Create Product Schema POJO (20 min)**

Now create the `ProductSchema` class. This is where the magic happens.

**File**: `src/main/java/com/workshop/aeogeo/schema/ProductSchema.java`

**Your challenge**: Complete this class based on [Schema.org Product](https://schema.org/Product) specification.

**Starter code**:

```java
package com.workshop.aeogeo.schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductSchema extends BaseSchema {

    public ProductSchema() {
        super("Product");
    }

    // TODO: Add fields for:
    // - name (String)
    // - description (String)
    // - brand (BrandSchema - you'll create this)
    // - offers (OfferSchema - you'll create this)
    // - aggregateRating (AggregateRatingSchema - optional)
    // - additionalProperty (List<PropertyValueSchema> - for specs)
    // - image (String - URL)
    // - sku (String - product code)
}
```

**Stuck?** See `HINTS.md` for progressive hints.

---

### **Step 4: Create Nested Schema Classes (15 min)**

Schema.org uses **nested objects**. You need to create:

1. **BrandSchema**: Represents the manufacturer
2. **OfferSchema**: Represents price information
3. **PropertyValueSchema**: Represents technical specs
4. **(Optional) AggregateRatingSchema**: Represents reviews

**Hint**: All of these extend `BaseSchema` with different `@type` values.

**Example** (BrandSchema):

```java
package com.workshop.aeogeo.schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BrandSchema extends BaseSchema {

    public BrandSchema() {
        super("Brand");
    }

    private String name;
    // Optional: url, logo
}
```

**Your turn**: Create `OfferSchema` and `PropertyValueSchema`.

---

### **Step 5: Create the Mapping Service (15 min)**

Now write the service that maps `ProductModel` → `ProductSchema`.

**File**: `src/main/java/com/workshop/aeogeo/service/SchemaService.java`

```java
package com.workshop.aeogeo.service;

import com.workshop.aeogeo.model.*;
import com.workshop.aeogeo.schema.*;
import org.springframework.stereotype.Service;

@Service
public class SchemaService {

    public ProductSchema generateProductSchema(ProductModel product) {
        ProductSchema schema = new ProductSchema();

        // TODO: Map fields
        // schema.setName(product.getName());
        // schema.setBrand(mapManufacturer(product.getManufacturer()));
        // schema.setOffers(mapPriceRow(product.getPriceRow()));
        // schema.setAdditionalProperty(mapFeatures(product.getFeatures()));
        // ... etc

        return schema;
    }

    private BrandSchema mapManufacturer(ManufacturerModel manufacturer) {
        // TODO: Implement
        return null;
    }

    private OfferSchema mapPriceRow(PriceRowModel priceRow) {
        // TODO: Implement
        return null;
    }

    // Add more mapping methods as needed
}
```

---

### **Step 6: Create the REST Controller (10 min)**

Expose your schema via a REST endpoint.

**File**: `src/main/java/com/workshop/aeogeo/controller/ProductSchemaController.java`

```java
package com.workshop.aeogeo.controller;

import com.workshop.aeogeo.model.ProductModel;
import com.workshop.aeogeo.repository.ProductRepository;
import com.workshop.aeogeo.schema.ProductSchema;
import com.workshop.aeogeo.service.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductSchemaController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SchemaService schemaService;

    @GetMapping("/{code}/schema")
    public ResponseEntity<ProductSchema> getProductSchema(@PathVariable String code) {
        // TODO:
        // 1. Find product by code
        // 2. If not found, return 404
        // 3. Generate schema
        // 4. Return schema as JSON

        return null; // Replace with actual implementation
    }
}
```

---

## ✅ Validation

### **Step 1: Manual Test**

```bash
# Start your app
./mvnw spring-boot:run

# Test the endpoint
curl http://localhost:8080/api/products/CAMERA-SONY-A7III/schema | jq
```

**Expected output**:
```json
{
  "@context": "https://schema.org",
  "@type": "Product",
  "name": "Sony Alpha A7 III",
  "brand": {
    "@type": "Brand",
    "name": "Sony"
  },
  "offers": {
    "@type": "Offer",
    "price": "1998.00",
    "priceCurrency": "USD"
  }
}
```

### **Step 2: Google Rich Results Test**

1. Copy your JSON output
2. Go to: https://search.google.com/test/rich-results
3. Paste your JSON
4. Click "Test Code"

**Success criteria**:
- ✅ Shows "Valid Product"
- ✅ No errors
- ✅ Preview shows product name, price, brand

### **Step 3: Automated Validation**

```bash
# Run our validation script
./verify.sh
```

This script checks:
- ✅ Endpoint returns 200 OK
- ✅ JSON contains required fields (`@context`, `@type`, `name`, `brand`, `offers`)
- ✅ JSON validates against Schema.org spec

---

## 🎓 What You Learned

1. **Schema.org vocabulary**: Universal language for describing "things"
2. **JSON-LD syntax**: How to embed structured data in JSON
3. **Mapping pattern**: Domain model → Schema.org POJO → JSON
4. **Jackson annotations**: `@JsonProperty` for special characters
5. **Nested objects**: Schema.org uses composition (Brand inside Product)

---

## 🚀 Next Steps

- **Stuck?** See `HINTS.md` for progressive hints
- **Want to compare?** See `SOLUTION.md` for our complete solution
- **Want checkpoints?** See `checkpoints/` for step-by-step working code
- **Ready for more?** Move to Exercise 2: FAQ Schema

---

## 💡 Real-World Application

In production, you would:

1. **Server-Side Rendering**: Inject this JSON-LD into your HTML `<head>`
2. **Caching**: Cache generated schemas to avoid repeated computation
3. **Multilingual**: Generate schemas for each language
4. **Sitemap**: List all product URLs in `sitemap.xml` so Google can find them
5. **Monitoring**: Track which products get rich results in Google Search Console

**See**: `docs/MIGRATION_TO_SAP.md` for integrating with real SAP Commerce Cloud
