# Exercise 3: RAG Pipeline with Vector Search

**Duration:** 90-120 minutes
**Difficulty:** Intermediate
**Topic:** Generative Engine Optimization (GEO) - Semantic Search with pgvector

## Learning Objectives

By completing this exercise, you will:

1. **Understand vector embeddings** and semantic similarity
2. **Implement semantic search** using pgvector and cosine similarity
3. **Build a RAG pipeline** for retrieval-augmented generation
4. **Create hybrid search** combining vector and keyword matching
5. **Optimize vector queries** with proper indexing strategies
6. **Prepare data for LLM consumption** with context formatting

## Background: Why RAG Matters for GEO

Retrieval-Augmented Generation (RAG) is the foundation of modern AI search experiences. LLMs like ChatGPT, Perplexity, and Claude use RAG to:

1. **Retrieve relevant context** from knowledge bases
2. **Augment prompts** with factual information
3. **Generate accurate answers** grounded in your data

**Traditional Keyword Search:**
```
Query: "best camera for video"
Matching: Exact words "camera" AND "video"
Results: Limited to lexical matches
```

**Vector Semantic Search:**
```
Query: "best camera for video"
Embedding: [0.23, -0.14, 0.89, ...] (1536 dimensions)
Matching: Cosine similarity with all product embeddings
Results: Semantically similar products (mirrorless cameras, cinema cameras, etc.)
```

### Benefits of Vector Search

- **Semantic understanding** - "laptop for coding" matches "developer workstation"
- **Multilingual** - Works across languages with multilingual embeddings
- **Typo tolerant** - "camra" still finds cameras
- **Conceptual matching** - "outdoor gear" finds jackets, boots, tents
- **No keyword stuffing** - Natural language queries work best

## Your Task

Implement a semantic search API endpoint that uses vector embeddings to find relevant products.

### Requirements

**Endpoint:** `POST /api/search/semantic`

**Request Body:**
```json
{
  "query": "camera for wildlife photography",
  "limit": 5
}
```

**Response:**
```json
{
  "query": "camera for wildlife photography",
  "results": [
    {
      "product": { /* full product object */ },
      "similarity": 0.87,
      "rank": 1
    }
  ],
  "executionTimeMs": 45
}
```

**Core Features:**
1. Generate embedding for user query
2. Find top-K similar products using cosine similarity
3. Return products ranked by similarity score
4. Include execution time for monitoring

**Bonus Features:**
- Hybrid search (combine vector + keyword)
- Result filtering (category, price range)
- Query rewriting for better results
- Caching for common queries

## Architecture Overview

```
User Query
    ↓
Generate Embedding (via EmbeddingProvider)
    ↓
pgvector Similarity Search (cosine distance)
    ↓
Rank & Filter Results
    ↓
Format for LLM Context
    ↓
Return JSON Response
```

## Getting Started

### Step 1: Update ProductModel for Vector Storage

The `ProductModel` already has an `embedding` field:

```java
@Column(columnDefinition = "vector(1536)")
private PGvector embedding;
```

This stores embeddings as native pgvector type for efficient similarity search.

### Step 2: Create Vector Search Repository

Create: `src/main/java/com/sap/commerce/workshop/repository/VectorSearchRepository.java`

Use native SQL queries for pgvector operations:

```java
@Query(value = "SELECT p.*, 1 - (p.embedding <=> cast(:queryEmbedding as vector)) as similarity " +
               "FROM products p " +
               "WHERE 1 - (p.embedding <=> cast(:queryEmbedding as vector)) > :threshold " +
               "ORDER BY similarity DESC " +
               "LIMIT :limit", nativeQuery = true)
List<Object[]> findSimilarProducts(@Param("queryEmbedding") String embeddingString,
                                    @Param("threshold") double threshold,
                                    @Param("limit") int limit);
```

**Note:** `<=>` is pgvector's cosine distance operator.

### Step 3: Create SemanticSearchService

Create: `src/main/java/com/sap/commerce/workshop/service/SemanticSearchService.java`

This service:
1. Accepts user query
2. Generates embedding via `EmbeddingProvider`
3. Searches database with pgvector
4. Returns ranked results

### Step 4: Create REST Controller

Create: `src/main/java/com/sap/commerce/workshop/controller/SemanticSearchController.java`

Handle POST requests with query text and return ranked products.

### Step 5: Populate Embeddings

Before searching, products need embeddings. Create a service to:

```java
@PostConstruct
public void generateProductEmbeddings() {
    List<ProductModel> products = productRepository.findAll();
    for (ProductModel product : products) {
        if (product.getEmbedding() == null) {
            String text = product.getName() + " " + product.getDescription();
            List<Float> embedding = embeddingProvider.generateEmbedding(text);
            product.setEmbedding(new PGvector(embedding.toArray(new Float[0])));
        }
    }
    productRepository.saveAll(products);
}
```

## pgvector Setup

### Creating Vector Indexes

For fast similarity search with 1000s+ products:

```sql
-- IVFFlat index (Inverted File with Flat compression)
CREATE INDEX ON products USING ivfflat (embedding vector_cosine_ops) WITH (lists = 100);

-- HNSW index (Hierarchical Navigable Small World) - Better for accuracy
CREATE INDEX ON products USING hnsw (embedding vector_cosine_ops);
```

**Index Comparison:**

| Index Type | Build Time | Query Speed | Accuracy | Use Case |
|------------|------------|-------------|----------|----------|
| No Index | Fast | Slow | 100% | <1K vectors |
| IVFFlat | Medium | Fast | 95%+ | 1K-1M vectors |
| HNSW | Slow | Very Fast | 99%+ | Production |

### Distance Operators

| Operator | Distance Type | Use Case |
|----------|---------------|----------|
| `<->` | L2 (Euclidean) | General purpose |
| `<=>` | Cosine | Text embeddings (recommended) |
| `<#>` | Inner product | Normalized vectors |

## Similarity Scoring

**Cosine Similarity** ranges from -1 to 1:
- `1.0` = Identical vectors
- `0.0` = Orthogonal (no similarity)
- `-1.0` = Opposite vectors

**Formula:**
```
similarity = 1 - cosine_distance
similarity = (A · B) / (||A|| × ||B||)
```

**Threshold Recommendations:**
- `> 0.8` - Highly similar (near-exact matches)
- `> 0.7` - Similar (good matches)
- `> 0.6` - Somewhat similar (broader results)
- `< 0.6` - Low similarity (consider excluding)

## Hybrid Search Strategy

Combine vector similarity with keyword matching:

```sql
WITH vector_results AS (
    SELECT id, name, 1 - (embedding <=> :query_embedding) as vector_score
    FROM products
    WHERE 1 - (embedding <=> :query_embedding) > 0.6
),
keyword_results AS (
    SELECT id, name,
           ts_rank(to_tsvector('english', name || ' ' || description),
                   plainto_tsquery('english', :query)) as keyword_score
    FROM products
    WHERE to_tsvector('english', name || ' ' || description) @@ plainto_tsquery('english', :query)
)
SELECT p.*,
       COALESCE(v.vector_score, 0) * 0.7 + COALESCE(k.keyword_score, 0) * 0.3 as final_score
FROM products p
LEFT JOIN vector_results v ON p.id = v.id
LEFT JOIN keyword_results k ON p.id = k.id
WHERE v.id IS NOT NULL OR k.id IS NOT NULL
ORDER BY final_score DESC
LIMIT :limit;
```

**Benefits:**
- Vector search finds semantic matches
- Keyword search finds exact terms
- Weighted combination balances both

## Testing Your Implementation

### Manual Testing

```bash
# Start application
mvn spring-boot:run

# Test semantic search
curl -X POST http://localhost:8080/api/search/semantic \
  -H "Content-Type: application/json" \
  -d '{"query": "camera for sports photography", "limit": 5}' \
  | jq

# Expected: Canon/Nikon/Sony cameras with fast autofocus
```

### Example Queries

Test with diverse queries:

```json
{"query": "laptop for developers", "limit": 5}
{"query": "running shoes for marathon", "limit": 3}
{"query": "warm jacket for winter hiking", "limit": 5}
{"query": "phone with good camera", "limit": 3}
{"query": "outdoor clothing", "limit": 10}
```

### Validation

Verify:
- [ ] Results are semantically relevant
- [ ] Similarity scores are reasonable (> 0.6)
- [ ] Execution time is < 100ms for 27 products
- [ ] Different queries return different results
- [ ] Typos still work ("camra" finds cameras)

## Success Criteria

- [ ] Semantic search endpoint implemented
- [ ] Embeddings generated for all products
- [ ] pgvector similarity query working
- [ ] Results ranked by similarity score
- [ ] Execution time tracked and returned
- [ ] Hybrid search implemented (bonus)
- [ ] Tests passing with realistic queries

## Progressive Hints

<details>
<summary>Hint 1: PGvector Conversion</summary>

Convert embedding List<Float> to PGvector:

```java
List<Float> embedding = embeddingProvider.generateEmbedding(text);
Float[] array = embedding.toArray(new Float[0]);
PGvector pgVector = new PGvector(array);
product.setEmbedding(pgVector);
```
</details>

<details>
<summary>Hint 2: Vector to String for Query</summary>

pgvector native queries need string representation:

```java
String embeddingString = "[" + embedding.stream()
    .map(String::valueOf)
    .collect(Collectors.joining(",")) + "]";
```
</details>

<details>
<summary>Hint 3: Native Query Result Parsing</summary>

Native queries return Object[] arrays. Parse them:

```java
Object[] row = (Object[]) result;
ProductModel product = (ProductModel) row[0];
Double similarity = (Double) row[1];
```
</details>

<details>
<summary>Hint 4: Similarity Threshold</summary>

Filter low-quality matches:

```java
WHERE 1 - (p.embedding <=> cast(:queryEmbedding as vector)) > 0.65
```

Adjust threshold based on your data quality.
</details>

<details>
<summary>Hint 5: Embedding Generation Service</summary>

Create a dedicated service to populate embeddings on startup:

```java
@Service
public class EmbeddingGenerationService {
    @PostConstruct
    public void generateAllEmbeddings() {
        // Generate for products without embeddings
    }
}
```
</details>

<details>
<summary>Hint 6: Execution Time Tracking</summary>

Use System.currentTimeMillis() before and after search:

```java
long startTime = System.currentTimeMillis();
List<SearchResult> results = performSearch(query);
long duration = System.currentTimeMillis() - startTime;
```
</details>

<details>
<summary>Hint 7: Result DTO</summary>

Create a DTO for search results:

```java
public class SearchResult {
    private ProductModel product;
    private double similarity;
    private int rank;
}
```
</details>

<details>
<summary>Hint 8: Hybrid Search Weights</summary>

Tune weights based on your use case:

```java
double finalScore = (vectorScore * 0.7) + (keywordScore * 0.3);
```

More weight to vector (0.7) for semantic understanding, less to keyword (0.3) for exact matches.
</details>

<details>
<summary>Hint 9: Index Creation</summary>

Create index in a migration file:

```sql
-- V2__add_vector_index.sql
CREATE INDEX idx_products_embedding ON products
USING ivfflat (embedding vector_cosine_ops)
WITH (lists = 100);
```
</details>

<details>
<summary>Hint 10: Complete Service Structure</summary>

```java
@Service
public class SemanticSearchService {
    @Autowired
    private EmbeddingProvider embeddingProvider;
    @Autowired
    private VectorSearchRepository vectorSearchRepository;

    public SearchResponse search(String query, int limit) {
        // 1. Generate query embedding
        // 2. Search with pgvector
        // 3. Parse results
        // 4. Return ranked response
    }
}
```
</details>

## Resources

- [pgvector GitHub](https://github.com/pgvector/pgvector)
- [pgvector-java](https://github.com/pgvector/pgvector-java)
- [Vector Similarity Search Guide](https://www.pinecone.io/learn/vector-similarity/)
- [RAG Pattern Overview](https://python.langchain.com/docs/use_cases/question_answering/)

## Next Steps

After completing:
1. Test with 10+ diverse queries
2. Experiment with similarity thresholds
3. Compare vector-only vs hybrid search
4. Measure query performance
5. **Proceed to Exercise 4: Bot Detection**

---

**Ready to build semantic search!** Start by generating embeddings for all products, then implement the search service. 🚀
