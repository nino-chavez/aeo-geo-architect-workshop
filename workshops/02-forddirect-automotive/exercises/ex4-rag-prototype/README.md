# Exercise 4: RAG Prototype (Optional)
## Semantic Search Over Dealer Reviews

**Duration**: 90 minutes (Optional - Day 3 afternoon)
**Squad**: Squad A + B (technical participants)
**Difficulty**: Advanced

---

## 🎯 Objective

Build a simple RAG (Retrieval-Augmented Generation) system that allows semantic search over dealer reviews. This demonstrates how to feed real-time dealer data to LLMs.

**What You'll Build**:
- Vector embeddings of dealer reviews
- Semantic search using pgvector
- LLM-powered answer generation

**Use Case**:
> "Which Ford dealer near me has the best service for commercial trucks?"

Instead of keyword search ("commercial" + "trucks"), RAG understands intent and returns dealers with relevant reviews about commercial vehicle service quality.

---

## 📋 Components

### **1. Review Vectorization**
Convert review text to embeddings:
```python
from openai import OpenAI

client = OpenAI()
review_text = "Great service for my F-150 fleet. Best commercial truck dealer!"
embedding = client.embeddings.create(
    input=review_text,
    model="text-embedding-3-small"
).data[0].embedding
```

### **2. Vector Search (pgvector)**
Store and search embeddings:
```sql
CREATE TABLE review_embeddings (
    review_id UUID,
    dealer_id UUID,
    embedding vector(1536),  -- OpenAI embedding dimension
    review_text TEXT
);

CREATE INDEX ON review_embeddings USING ivfflat (embedding vector_cosine_ops);

-- Search for similar reviews
SELECT dealer_id, review_text,
       1 - (embedding <=> query_embedding) AS similarity
FROM review_embeddings
ORDER BY embedding <=> query_embedding
LIMIT 5;
```

### **3. LLM Answer Generation**
Use retrieved reviews as context:
```python
# 1. Convert user question to embedding
# 2. Find top 5 similar reviews (vector search)
# 3. Pass reviews as context to LLM

prompt = f"""
Based on these customer reviews:
{reviews_context}

Answer this question: {user_question}
"""
```

---

## 🚀 Quick Start

See `solution/rag_service.py` for complete implementation.

**This exercise is OPTIONAL** - focus on Exercises 0-3 first.

---

## 📖 Resources

- [OpenAI Embeddings Guide](https://platform.openai.com/docs/guides/embeddings)
- [pgvector Documentation](https://github.com/pgvector/pgvector)
- [RAG Explained](https://www.promptingguide.ai/techniques/rag)

---

**RAG enables real-time data access for LLMs. 🔍**
