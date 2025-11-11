# Migration to SAP Commerce Cloud

Guide for adapting workshop patterns to production SAP Commerce implementations.

## Overview

This workshop uses SAP Commerce-inspired models to make concepts familiar to SAP architects. This guide shows how to migrate workshop patterns to actual SAP Commerce installations.

## Data Model Mapping

### Workshop → SAP Commerce

| Workshop Model | SAP Commerce Type | Notes |
|----------------|-------------------|-------|
| `ProductModel` | `de.hybris.platform.core.model.product.ProductModel` | Direct mapping |
| `ManufacturerModel` | `ManufacturerModel` | Already exists in SAP |
| `PriceRowModel` | `PriceRowModel` | SAP Commerce pattern |
| `CategoryModel` | `CategoryModel` | Hierarchical structure |
| `MediaModel` | `MediaModel` | Product images |
| `FAQModel` | Custom type (create) | New type needed |

### Creating Custom Types in SAP Commerce

Add to `items.xml`:
```xml
<itemtype code="FAQ" autocreate="true" generate="true">
    <deployment table="faqs" typecode="10001"/>
    <attributes>
        <attribute qualifier="question" type="java.lang.String">
            <persistence type="property"/>
        </attribute>
        <attribute qualifier="answer" type="java.lang.String">
            <persistence type="property"/>
        </attribute>
        <attribute qualifier="product" type="Product">
            <persistence type="property"/>
        </attribute>
        <attribute qualifier="sortOrder" type="java.lang.Integer">
            <persistence type="property"/>
        </attribute>
        <attribute qualifier="isPublished" type="java.lang.Boolean">
            <persistence type="property"/>
        </attribute>
    </attributes>
</itemtype>
```

## Integration Points

### 1. Schema.org JSON-LD Generation

**Workshop Approach**: REST controller generates JSON-LD  
**SAP Approach**: Create custom CMS component or controller

```java
@Controller
@RequestMapping("/products")
public class ProductSchemaController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping("/{code}/schema")
    @ResponseBody
    public Map<String, Object> getSchema(@PathVariable String code) {
        ProductModel product = productService.getProductForCode(code);
        return buildProductSchema(product);
    }
}
```

### 2. Vector Embeddings Storage

**Option A**: Add as dynamic attribute
```xml
<attribute qualifier="embedding" type="java.lang.String">
    <persistence type="property">
        <columntype>TEXT</columntype>
    </persistence>
</attribute>
```

**Option B**: Separate table with foreign key
```xml
<itemtype code="ProductEmbedding" autocreate="true" generate="true">
    <deployment table="product_embeddings" typecode="10002"/>
    <attributes>
        <attribute qualifier="product" type="Product">
            <persistence type="property"/>
        </attribute>
        <attribute qualifier="embedding" type="java.lang.String">
            <persistence type="property">
                <columntype>TEXT</columntype>
            </persistence>
        </attribute>
    </attributes>
</itemtype>
```

### 3. Semantic Search Service

Create as Spring service in SAP Commerce:
```java
@Service
public class SemanticSearchService {
    
    @Resource
    private FlexibleSearchService flexibleSearchService;
    
    @Resource
    private EmbeddingProvider embeddingProvider;
    
    public List<ProductModel> semanticSearch(String query) {
        List<Float> queryEmbedding = embeddingProvider.generateEmbedding(query);
        
        // Query all products with embeddings
        SearchResult<ProductModel> results = flexibleSearchService.search(
            "SELECT {pk} FROM {Product} WHERE {embedding} IS NOT NULL"
        );
        
        // Calculate similarities and rank
        return rankByCosineSimilarity(results.getResult(), queryEmbedding);
    }
}
```

## Deployment Considerations

### 1. Performance

- **Cache embeddings**: Use SAP's caching infrastructure
- **Batch generation**: Use CronJobs for bulk embedding generation
- **Async processing**: Use SAP's task service for bot logging

### 2. Security

- Secure API keys in SAP's configuration
- Use SAP's encryption for sensitive data
- Implement proper authorization checks

### 3. Scalability

- Use SAP Cloud (CCv2) auto-scaling
- Consider separate microservice for embeddings
- Cache Schema.org JSON-LD responses

## Recommended Architecture

```
SAP Commerce Cloud
├── Custom Extension: aeogeosupport
│   ├── models/
│   │   ├── FAQModel (custom)
│   │   └── ProductEmbedding (custom)
│   ├── services/
│   │   ├── SchemaOrgService
│   │   ├── SemanticSearchService
│   │   └── EmbeddingProvider (interface)
│   ├── controllers/
│   │   ├── ProductSchemaController
│   │   └── SemanticSearchController
│   └── cronjobs/
│       └── EmbeddingGenerationJob
└── Integration with SAP systems
    ├── Product data from PIM
    ├── Pricing from ERP
    └── Inventory from WMS
```

## Migration Checklist

- [ ] Create custom extension in SAP Commerce
- [ ] Define custom item types (FAQ, ProductEmbedding)
- [ ] Implement Schema.org service
- [ ] Implement semantic search service
- [ ] Set up embedding provider configuration
- [ ] Create CronJob for bulk embedding generation
- [ ] Add bot detection filter to web.xml
- [ ] Configure caching strategy
- [ ] Set up monitoring and logging
- [ ] Test with production data
- [ ] Deploy to development environment
- [ ] Performance test
- [ ] Security audit
- [ ] Deploy to production

---

**Last Updated**: January 2025
