# Architecture Documentation

System design decisions and patterns used in the AEO/GEO Architect Workshop.

## Overview

The workshop uses a layered Spring Boot architecture with provider abstraction for embedding generation and pgvector for semantic search.

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                       Client (Browser)                      │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP/REST
┌──────────────────────────▼──────────────────────────────────┐
│                    Controllers Layer                        │
│  - ProductSchemaController                                  │
│  - FAQController                                            │
│  - SemanticSearchController                                 │
│  - BotAnalyticsController                                   │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                     Services Layer                          │
│  - SemanticSearchService                                    │
│  - BotAnalyticsService                                      │
│  - BotLoggingService (@Async)                               │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│              EmbeddingProvider Interface                    │
│         (Strategy Pattern for Multi-Provider)               │
│                                                             │
│  ┌───────────────┐  ┌───────────────┐  ┌──────────────┐   │
│  │ Precomputed   │  │ Azure OpenAI  │  │   OpenAI     │   │
│  └───────────────┘  └───────────────┘  └──────────────┘   │
│  ┌───────────────┐  ┌───────────────┐                     │
│  │    Ollama     │  │  Vertex AI    │                     │
│  └───────────────┘  └───────────────┘                     │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                  Repository Layer (Spring Data JPA)         │
│  - ProductRepository                                        │
│  - BotAccessLogRepository                                   │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│            PostgreSQL 16 with pgvector Extension            │
│  - Products (with vector embeddings)                        │
│  - Manufacturers, Categories, FAQs                          │
│  - BotAccessLogs                                            │
└─────────────────────────────────────────────────────────────┘
```

## Design Patterns

### 1. Strategy Pattern (Embedding Providers)

**Problem**: Need to support multiple embedding providers (Azure, OpenAI, Ollama, etc.) with different APIs and configurations.

**Solution**: Define an interface and create implementations for each provider.

```java
public interface EmbeddingProvider {
    List<Float> generateEmbedding(String text);
    String getProviderName();
}

@Service
@Profile("azure-openai")
public class AzureOpenAIEmbeddingProvider implements EmbeddingProvider {
    // Azure-specific implementation
}

@Service
@Profile("openai")
public class OpenAIEmbeddingProvider implements EmbeddingProvider {
    // OpenAI-specific implementation
}
```

**Benefits**:
- Easy to add new providers
- Switch providers via configuration
- Testable with mock implementations

### 2. Repository Pattern

**Problem**: Abstract data access logic from business logic.

**Solution**: Use Spring Data JPA repositories.

```java
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByManufacturerName(String name);
}
```

**Benefits**:
- Clean separation of concerns
- Built-in CRUD operations
- Query method generation

### 3. DTO Pattern

**Problem**: Separate internal models from API representations.

**Solution**: Use DTOs for request/response objects.

```java
@Data
public class SemanticSearchRequest {
    private String query;
    private Integer limit;
    private Double threshold;
}

@Data
public class SemanticSearchResponse {
    private String query;
    private List<SearchResult> results;
    private int totalResults;
    private long executionTimeMs;
}
```

**Benefits**:
- API versioning flexibility
- Hide internal implementation
- Validation at API boundary

### 4. Filter Pattern (Bot Detection)

**Problem**: Cross-cutting concern (bot detection) needs to intercept all requests.

**Solution**: Use Servlet Filter.

```java
@Component
@Order(1)
public class BotDetectionFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        // Detect bot, log access
        chain.doFilter(request, response);
    }
}
```

**Benefits**:
- Runs before controllers
- No code duplication
- Easy to enable/disable

## Key Technology Decisions

### Why Spring Boot 3.2?

- **Modern stack**: Latest Spring features
- **Native Java 17**: Records, sealed classes, pattern matching
- **Observability**: Built-in metrics and tracing
- **Docker-friendly**: Native image support

### Why PostgreSQL with pgvector?

- **Vector support**: Native vector similarity search
- **Performance**: Optimized indexes for cosine similarity
- **Maturity**: Battle-tested in production
- **Open source**: No licensing costs

**Alternatives considered**:
- **Pinecone**: SaaS, expensive at scale
- **Weaviate**: More complex setup
- **Redis with RediSearch**: Less mature vector support

### Why JSON-LD for Schema.org?

- **Google recommended**: Best support in search
- **Human-readable**: Easy to debug
- **Self-contained**: No external references needed
- **Flexible**: Easy to extend with custom properties

### Why Multi-Provider Architecture?

- **Flexibility**: Choose based on requirements
- **No vendor lock-in**: Easy to switch
- **Cost optimization**: Use different providers per environment
- **Learning**: Understand trade-offs of each approach

## Data Model

### Core Entities

```
ProductModel
├── id: Long (PK)
├── code: String (unique)
├── name: String
├── description: Text
├── price: BigDecimal
├── currencyCode: String
├── stockLevel: Integer
├── averageRating: BigDecimal
├── reviewCount: Integer
├── embedding: String (pgvector)
├── manufacturer: ManufacturerModel (ManyToOne)
├── category: CategoryModel (ManyToOne)
├── media: List<MediaModel> (OneToMany)
└── faqs: List<FAQModel> (OneToMany)

ManufacturerModel
├── id: Long (PK)
├── code: String (unique)
├── name: String
└── description: Text

CategoryModel
├── id: Long (PK)
├── code: String (unique)
├── name: String
└── parent: CategoryModel (ManyToOne)

FAQModel
├── id: Long (PK)
├── question: String
├── answer: Text
├── sortOrder: Integer
├── isPublished: Boolean
└── product: ProductModel (ManyToOne)

BotAccessLog
├── id: Long (PK)
├── botType: String
├── productId: Long
├── accessTime: LocalDateTime
├── endpoint: String
└── ipAddress: String
```

### Database Schema

```sql
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2),
    currency_code VARCHAR(3),
    stock_level INTEGER,
    average_rating DECIMAL(3,2),
    review_count INTEGER,
    embedding vector(20), -- Simplified for workshop
    manufacturer_id BIGINT REFERENCES manufacturers(id),
    category_id BIGINT REFERENCES categories(id)
);

CREATE INDEX idx_products_embedding ON products 
USING ivfflat (embedding vector_cosine_ops);
```

## Performance Considerations

### Embedding Generation

- **Cache**: Store embeddings in database after generation
- **Batch**: Generate multiple embeddings in one API call
- **Async**: Use @Async for non-blocking generation

### Vector Search

- **Indexing**: Use ivfflat or hnsw indexes
- **Filtering**: Apply filters before vector search when possible
- **Pagination**: Limit results to reasonable number (10-50)

### Bot Logging

- **Async**: Don't block requests while logging
- **Batching**: Consider batch inserts for high traffic
- **Retention**: Archive old logs periodically

## Security Considerations

### API Keys

- **Never commit**: Use environment variables
- **Rotation**: Regular key rotation policy
- **Encryption**: Encrypt at rest in production

### SQL Injection

- **Parameterized queries**: Spring Data JPA prevents this
- **Validation**: Validate all user inputs

### Rate Limiting

- **Bot detection**: Monitor unusual patterns
- **API throttling**: Implement rate limits per IP

## Scalability

### Horizontal Scaling

- **Stateless**: Application is stateless (can run multiple instances)
- **Database**: PostgreSQL can be replicated
- **Caching**: Add Redis for frequently accessed data

### Vertical Scaling

- **Database**: pgvector benefits from more RAM
- **Embeddings**: CPU-intensive, benefits from more cores

## Monitoring

### Key Metrics

- **Request latency**: P50, P95, P99
- **Embedding generation time**: Per provider
- **Vector search time**: Query performance
- **Bot detection rate**: % of traffic from bots
- **Error rate**: 4xx and 5xx responses

### Logging

```java
@Slf4j
public class SemanticSearchService {
    public SemanticSearchResponse search(String query, int limit, double threshold) {
        log.info("Semantic search: query='{}', limit={}, threshold={}",
query, limit, threshold);
        
        long startTime = System.currentTimeMillis();
        // ... search logic ...
        
        log.info("Search completed in {}ms, found {} results",
            System.currentTimeMillis() - startTime, results.size());
    }
}
```

## Future Enhancements

### Hybrid Search

Combine vector search with keyword search:
```sql
SELECT *, 
       (embedding <=> $1::vector) as similarity,
       ts_rank(to_tsvector(description), to_tsquery($2)) as keyword_score
FROM products
WHERE (embedding <=> $1::vector) < 0.3
   OR to_tsvector(description) @@ to_tsquery($2)
ORDER BY (similarity * 0.7 + keyword_score * 0.3) DESC;
```

### Reranking

Add a reranking model after initial retrieval:
1. Vector search returns top 100
2. Reranker scores top 100
3. Return top 10 after reranking

### Query Expansion

Use LLM to expand user queries:
- "camera" → "camera OR mirrorless OR DSLR OR photography"

---

**Last Updated**: January 2025
