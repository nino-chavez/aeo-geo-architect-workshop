# Embedding Provider Cost Comparison

Detailed cost analysis for each embedding provider in the workshop.

## Cost Summary Table

| Provider | Model | Per 1K Tokens | Workshop (27 Products) | Month (1K Products) | Enterprise (1M Products) |
|----------|-------|---------------|------------------------|---------------------|--------------------------|
| **Precomputed** | N/A | $0 | $0 | $0 | $0 |
| **Azure OpenAI** | ada-002 | $0.0001 | $0.001 | $0.05 | $50 |
| **OpenAI** | embedding-3-small | $0.00002 | $0.0002 | $0.01 | $10 |
| **Ollama** | nomic-embed-text | $0 (local compute) | $0 | $0 | $0 |
| **Vertex AI** | textembedding-gecko | $0.00025 | $0.003 | $0.13 | $125 |

---

## Detailed Analysis

### Precomputed

**Cost**: $0

**Pros**:
- Zero API costs
- Instant response time
- Works offline
- No rate limits

**Cons**:
- Cannot generate new embeddings
- Fixed to pre-computed queries
- Not suitable for production

**Best For**:
- Workshop learning
- Offline environments
- Cost-free experimentation

---

### Azure OpenAI

**Model**: `text-embedding-ada-002`  
**Cost**: $0.0001 per 1K tokens

**Calculation Example**:
```
27 products × 50 tokens average = 1,350 tokens
1,350 / 1,000 × $0.0001 = $0.000135 (~$0.001)
```

**Pros**:
- Enterprise SLA and support
- Organizational quota management
- Azure security and compliance
- Batch processing support

**Cons**:
- Requires Azure setup
- More expensive than OpenAI direct
- Regional availability varies

**Best For**:
- Enterprise workshops
- Organizations already on Azure
- Compliance-sensitive environments

**Monthly Estimate** (1,000 products):
- 1,000 products × 50 tokens = 50,000 tokens
- 50K / 1K × $0.0001 = **$0.005/month**

---

### OpenAI (BYOK)

**Model**: `text-embedding-3-small`  
**Cost**: $0.00002 per 1K tokens

**Calculation Example**:
```
27 products × 50 tokens = 1,350 tokens
1,350 / 1,000 × $0.00002 = $0.000027 (~$0.0002)
```

**Pros**:
- Cheapest cloud option (5x cheaper than Azure)
- Fastest setup (1 minute)
- Latest models available first
- Simple API

**Cons**:
- Personal API key (not ideal for enterprise)
- Rate limits on free tier
- No SLA guarantees
- Usage tied to individual account

**Best For**:
- Personal learning
- Proof of concepts
- Individual workshops
- Budget-conscious scenarios

**Monthly Estimate** (1,000 products):
- 1,000 products × 50 tokens = 50,000 tokens
- 50K / 1K × $0.00002 = **$0.001/month** (less than a penny!)

---

### Ollama (Local)

**Model**: `nomic-embed-text`  
**API Cost**: $0

**Infrastructure Cost**:
- Model download: ~750MB (one-time)
- RAM usage: ~2GB during inference
- CPU/GPU: Local compute

**Pros**:
- Zero ongoing costs
- 100% data privacy (all local)
- No rate limits
- Works offline
- No vendor lock-in

**Cons**:
- Slower than cloud APIs (~100-500ms vs 50-100ms)
- Requires local resources
- Model updates manual
- Less convenient for distributed teams

**Best For**:
- Air-gapped environments
- Privacy-sensitive data
- High-volume processing (becomes cheaper than cloud at scale)
- Organizations avoiding cloud spend

**Cost at Scale**:
- 1M products: $0 (just local compute time)
- Break-even vs cloud: ~5,000+ products/month

---

### Vertex AI

**Model**: `textembedding-gecko`  
**Cost**: $0.00025 per 1K tokens

**Calculation Example**:
```
27 products × 50 tokens = 1,350 tokens
1,350 / 1,000 × $0.00025 = $0.0003375 (~$0.003)
```

**Pros**:
- GCP native integration
- Enterprise support
- Regional deployment options
- Batch API support

**Cons**:
- Most expensive option (2.5x Azure, 12.5x OpenAI)
- Complex setup
- GCP-only

**Best For**:
- Organizations standardized on GCP
- Multi-model AI pipelines on Vertex
- Advanced GCP feature needs

**Monthly Estimate** (1,000 products):
- 1,000 products × 50 tokens = 50,000 tokens
- 50K / 1K × $0.00025 = **$0.0125/month**

---

## Cost Optimization Strategies

### 1. Cache Embeddings

Store generated embeddings in database:
```java
if (product.getEmbedding() != null) {
    return product.getEmbedding(); // Use cached
}

// Only generate if missing
List<Float> embedding = embeddingProvider.generateEmbedding(product.getDescription());
product.setEmbedding(embedding);
productRepository.save(product);
```

**Savings**: 100% after initial generation

### 2. Batch Processing

Generate embeddings in batches:
```java
List<String> texts = products.stream()
    .map(ProductModel::getDescription)
    .collect(Collectors.toList());

List<List<Float>> embeddings = embeddingProvider.generateBatchEmbeddings(texts);
```

**Savings**: Reduces API calls by ~50%

### 3. Text Optimization

Reduce token usage:
```java
// Before: 200 tokens
String text = product.getName() + " " + product.getDescription() + " " + 
              product.getManufacturer().getName() + " " + product.getCategory().getName();

// After: 50 tokens (75% savings)
String text = product.getName() + " " + product.getManufacturer().getName();
```

### 4. Threshold Filtering

Only generate embeddings for active products:
```java
if (product.getStockLevel() > 0 && product.isActive()) {
    generateEmbedding(product);
}
```

---

## Break-Even Analysis

### When to Use Ollama vs Cloud?

**Cloud (OpenAI)**: $0.00002 per 1K tokens

**Ollama**: $0 per request, but:
- Server cost: ~$50/month (AWS t3.medium)
- Time cost: ~500ms vs 100ms per request

**Break-Even Point**:
```
50,000 products/month × 50 tokens = 2.5M tokens
2.5M / 1K × $0.00002 = $0.05/month

Cloud cheaper below 50K products/month
Ollama cheaper above 50K products/month
```

---

## Real-World Cost Examples

### Scenario 1: Small E-Commerce (10K SKUs)

- **Initial Load**: 10,000 × 50 tokens = 500K tokens
- **Monthly Updates**: 1,000 new/changed × 50 tokens = 50K tokens

**Costs**:
- Azure: $0.05 + $0.005 = $0.055/month
- OpenAI: $0.01 + $0.001 = $0.011/month ✅ Cheapest
- Ollama: $0 (local)

**Recommendation**: OpenAI (negligible cost, easy setup)

### Scenario 2: Enterprise (100K SKUs)

- **Initial Load**: 100,000 × 50 tokens = 5M tokens
- **Monthly Updates**: 10,000 × 50 tokens = 500K tokens

**Costs**:
- Azure: $0.50 + $0.05 = $0.55/month
- OpenAI: $0.10 + $0.01 = $0.11/month
- Ollama: $50/month (server) - $50/month

**Recommendation**: Ollama ✅ (cheapest at scale, plus privacy)

### Scenario 3: Marketplace (1M+ SKUs)

- **Initial Load**: 1M × 50 tokens = 50M tokens
- **Monthly Updates**: 100K × 50 tokens = 5M tokens

**Costs**:
- Azure: $5 + $0.50 = $5.50/month
- OpenAI: $1 + $0.10 = $1.10/month
- Ollama: $200/month (dedicated server) - $200/month ✅

**Recommendation**: Ollama on dedicated infrastructure

---

## Workshop Recommendation

**For Learning**: Start with **Precomputed** (free, instant)  
**For Practice**: Use **OpenAI** (pennies, easy setup)  
**For Enterprise**: Deploy **Azure OpenAI** (SLA, compliance)  
**For Production**: Consider **Ollama** (cost-effective at scale)

---

**Last Updated**: January 2025
