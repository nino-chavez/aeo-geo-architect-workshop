# Exercise 3: RAG Pipeline with Vector Search - Progressive Hints

Use these hints progressively if you get stuck. This is a more advanced exercise involving semantic search and vector embeddings.

---

<details>
<summary><strong>Hint 1: Understanding Vector Embeddings</strong></summary>

Vector embeddings are numerical representations of text:
- "camera" → [0.078, -0.023, 0.045, -0.089, ...]
- "mirrorless camera" → [0.067, -0.034, 0.091, -0.023, ...]

Similar concepts have similar vectors. We use **cosine similarity** to measure how similar two vectors are.

</details>

---

<details>
<summary><strong>Hint 2: The RAG Pipeline Flow</strong></summary>

```
User Query ("best camera for video")
    ↓
Generate Embedding via EmbeddingProvider
    ↓
Compare with Product Embeddings (cosine similarity)
    ↓
Rank Results by Similarity Score
    ↓
Return Top N Products
```

You need to implement each step.

</details>

---

<details>
<summary><strong>Hint 3: Using the EmbeddingProvider</strong></summary>

Inject the EmbeddingProvider in your service:
```java
@Autowired
private EmbeddingProvider embeddingProvider;

public SemanticSearchResponse search(String query, int limit, double threshold) {
    List<Float> queryEmbedding = embeddingProvider.generateEmbedding(query);
    // Now use this to compare with products
}
```

The provider abstracts away whether you're using Azure, OpenAI, or precomputed embeddings.

</details>

---

<details>
<summary><strong>Hint 4: Fetching Products with Embeddings</strong></summary>

Get all products from the database:
```java
List<ProductModel> products = productRepository.findAll();
```

Each product should have an `embedding` field (stored as pgvector type in PostgreSQL).

</details>

---

<details>
<summary><strong>Hint 5: Calculating Cosine Similarity</strong></summary>

Cosine similarity formula:
```
similarity = (A · B) / (||A|| × ||B||)
```

Where:
- A · B = dot product (sum of element-wise multiplication)
- ||A|| = magnitude (square root of sum of squares)

Implement this in a helper method:
```java
private double cosineSimilarity(List<Float> a, List<Float> b) {
    // Calculate dot product
    // Calculate magnitudes
    // Return similarity
}
```

</details>

---

<details>
<summary><strong>Hint 6: Handling pgvector Type</strong></summary>

The `embedding` field is stored as PostgreSQL `vector` type but retrieved as a String:
```java
private List<Float> pgvectorToList(String pgvectorString) {
    // Remove brackets: "[0.1, 0.2, 0.3]" → "0.1, 0.2, 0.3"
    String cleaned = pgvectorString.replace("[", "").replace("]", "");

    // Split and convert to Float list
    return Arrays.stream(cleaned.split(","))
        .map(String::trim)
        .map(Float::parseFloat)
        .collect(Collectors.toList());
}
```

</details>

---

<details>
<summary><strong>Hint 7: Filtering by Similarity Threshold</strong></summary>

Not all products are relevant. Filter by threshold:
```java
List<SearchResult> results = new ArrayList<>();

for (ProductModel product : products) {
    if (product.getEmbedding() == null) continue;

    List<Float> productEmbedding = pgvectorToList(product.getEmbedding());
    double similarity = cosineSimilarity(queryEmbedding, productEmbedding);

    if (similarity >= threshold) {
        results.add(new SearchResult(product, similarity));
    }
}
```

Typical threshold: 0.7 (70% similarity).

</details>

---

<details>
<summary><strong>Hint 8: Sorting and Limiting Results</strong></summary>

Sort by similarity (highest first) and limit to top N:
```java
results.sort(Comparator.comparingDouble(SearchResult::getSimilarity).reversed());

List<SearchResult> topResults = results.stream()
    .limit(limit)
    .collect(Collectors.toList());
```

</details>

---

<details>
<summary><strong>Hint 9: Building the Response DTO</strong></summary>

Create a response object with metadata:
```java
SemanticSearchResponse response = new SemanticSearchResponse();
response.setQuery(query);
response.setResults(topResults);
response.setTotalResults(topResults.size());
response.setExecutionTimeMs(System.currentTimeMillis() - startTime);
response.setSimilarityThreshold(threshold);

return response;
```

This helps with debugging and performance monitoring.

</details>

---

<details>
<summary><strong>Hint 10: Controller Endpoint</strong></summary>

Create a POST endpoint that accepts search parameters:
```java
@PostMapping("/semantic")
public ResponseEntity<SemanticSearchResponse> semanticSearch(
    @RequestBody SemanticSearchRequest request) {

    SemanticSearchResponse response = semanticSearchService.search(
        request.getQuery(),
        request.getLimit(),
        request.getThreshold()
    );

    return ResponseEntity.ok(response);
}
```

POST is better than GET for complex queries.

</details>

---

## Still Stuck?

Check the solution files:
- `solution/SemanticSearchService.java` - Complete search logic
- `solution/SemanticSearchController.java` - REST API endpoint

Test with:
```bash
curl -X POST http://localhost:8080/api/search/semantic \
  -H "Content-Type: application/json" \
  -d '{"query": "camera for video recording", "limit": 5, "threshold": 0.7}'
```
