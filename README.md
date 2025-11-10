# AEO/GEO Architect Workshop

**Answer Engine Optimization (AEO) & Generative Engine Optimization (GEO)**
A hands-on workshop for experienced SAP Commerce Cloud architects

---

## 🎯 What You'll Learn

- **AEO (Answer Engine Optimization)**: Structure your product data so Google Featured Snippets, Knowledge Panels, and AI assistants can directly answer user questions
- **GEO (Generative Engine Optimization)**: Build RAG (Retrieval-Augmented Generation) pipelines so LLMs like ChatGPT and Perplexity use your catalog as their source of truth
- **Multi-Provider Architecture**: Design systems that work with Azure OpenAI, Vertex AI, OpenAI, or fully local embeddings
- **SAP Commerce Patterns**: Apply these techniques to real e-commerce catalogs (Electronics & Apparel)

---

## ⚡ Quick Start (5 Minutes)

```bash
# 1. Clone the repository
git clone <repo-url>
cd aeo-geo-architect-workshop

# 2. Start the database
docker-compose up -d

# 3. Run the application
./mvnw spring-boot:run

# 4. Test it works
curl http://localhost:8080/health
```

**That's it!** The workshop uses pre-computed embeddings by default—no API keys needed.

---

## 📚 Workshop Structure

### **Day 1: AEO Fundamentals (3 Hours)**
Learn how to make your product catalog machine-readable using Schema.org

- **Phase 0** (15 min): GitHub Copilot quick start
- **Exercise 1** (60 min): Product Schema — Map SAP Commerce products to JSON-LD
- **Exercise 2** (30 min): FAQ Schema — Create machine-readable support content
- **Validation** (15 min): Google Rich Results Test

**See**: `exercises/day1-exercise1-product-schema/README.md`

### **Day 2: GEO + RAG Pipeline (3 Hours)**
Build a vector search system that LLMs can query

- **Concepts** (30 min): Vector embeddings explained
- **Exercise 3** (90 min): RAG Pipeline — Semantic search with pgvector
- **Exercise 4** (30 min): Monitoring — Bot detection and health checks
- **Capstone** (30 min): Apply to your real SAP Commerce catalog

**See**: `exercises/day2-exercise3-rag-pipeline/README.md`

---

## 🏗️ Architecture

### **Data Models (SAP Commerce-Inspired)**

This workshop uses data models mimicking SAP Commerce Cloud's demo catalogs:

**Electronics Catalog**:
- Products: Cameras, Laptops, Smartphones
- Attributes: Megapixels, Screen Size, Processor, RAM
- Manufacturers: Sony, Canon, Samsung, Dell, HP

**Apparel Catalog**:
- Products: T-Shirts, Jackets, Shoes
- Attributes: Material, Size, Color, Fit
- Manufacturers: Nike, Adidas, Levi's

### **Technology Stack**

| Component | Technology | Purpose |
|-----------|------------|---------|
| **Backend** | Spring Boot 3.2, Java 17 | Application framework |
| **Database** | PostgreSQL 16 + pgvector | Vector similarity search |
| **Embeddings** | Multi-provider (see below) | Convert text → vectors |
| **Schema.org** | JSON-LD | Structured data for AEO |
| **AI Tooling** | GitHub Copilot | Accelerate development |

---

## 🔌 Embedding Providers

The workshop supports 5 embedding providers. **You choose which to use.**

| Provider | Setup Time | Cost | Use Case |
|----------|------------|------|----------|
| **Pre-computed** ⭐ | 0 min | $0 | Day 1, offline workshops |
| **Azure OpenAI** | 2 min | Org pays | Enterprise with Azure |
| **Vertex AI** | 5 min | Org pays | Enterprise with GCP |
| **OpenAI (BYOK)** | 1 min | ~$0.02 | Individual learning |
| **Ollama** | 10 min | $0 | Air-gapped/fully local |

### **Switching Providers**

```bash
./switch-provider.sh
# Follow the prompts to select your provider
```

**See**: `docs/PROVIDER_SETUP.md` for detailed instructions

---

## 📂 Project Structure

```
aeo-geo-architect-workshop/
├── src/main/java/com/workshop/aeogeo/
│   ├── model/              # SAP Commerce-inspired data models
│   │   ├── ProductModel.java
│   │   ├── ManufacturerModel.java
│   │   ├── PriceRowModel.java
│   │   └── ...
│   ├── schema/             # Schema.org POJOs (YOU BUILD THESE)
│   │   └── (empty - exercises go here)
│   ├── provider/           # Embedding providers
│   │   ├── EmbeddingProvider.java
│   │   ├── PrecomputedEmbeddingProvider.java
│   │   ├── AzureOpenAIEmbeddingProvider.java
│   │   └── ...
│   └── service/            # Business logic (YOU BUILD THESE)
├── exercises/              # Incremental learning guides
│   ├── day1-exercise1-product-schema/
│   │   ├── README.md       # Exercise instructions
│   │   ├── HINTS.md        # Progressive hints
│   │   ├── SOLUTION.md     # Complete solution
│   │   └── verify.sh       # Automated validation
│   └── ...
├── docs/                   # Additional documentation
│   ├── PROVIDER_SETUP.md
│   ├── COST_COMPARISON.md
│   └── MIGRATION_TO_SAP.md
└── docker-compose.yml      # PostgreSQL + pgvector + Ollama
```

---

## 🎓 Learning Approach

This workshop follows a **"Learn by Doing"** methodology:

### **For Self-Study**
1. Read `exercises/day1-exercise1-product-schema/README.md`
2. Try to build it yourself (stubs are provided)
3. Stuck? Read `HINTS.md` (progressive hints, reveal one at a time)
4. Still stuck? Look at `checkpoints/` for intermediate steps
5. Done? Run `./verify.sh` to validate your work
6. Compare your solution to `SOLUTION.md`

### **For Group Workshop**
- Instructor demonstrates concepts (15 min)
- Participants work on exercises (45-90 min)
- Pairs review each other's code (peer learning)
- Instructor reviews common issues (15 min)

---

## 🚀 Prerequisites

### **Required**
- Java 17+ (`java -version`)
- Docker Desktop (`docker --version`)
- Maven 3.8+ (`mvn --version`) or use included `./mvnw`
- Git (`git --version`)

### **Recommended**
- IntelliJ IDEA or VS Code
- GitHub Copilot (enterprise license assumed)
- Basic understanding of:
  - Spring Boot
  - REST APIs
  - JSON
  - SQL

### **SAP Commerce Knowledge**
- Helpful but not required
- We use SAP Commerce terminology (ProductModel, PriceRow, etc.)
- Data models mimic SAP's Electronics & Apparel catalogs
- Migration guide provided: `docs/MIGRATION_TO_SAP.md`

---

## 🛠️ Troubleshooting

### **"Docker won't start"**
```bash
# Check Docker is running
docker ps

# Restart Docker Desktop
# Then try: docker-compose up -d
```

### **"Maven build fails"**
```bash
# Check Java version (must be 17+)
java -version

# Clean and rebuild
./mvnw clean install
```

### **"Embedding failed" error**
```bash
# Check which provider is active
curl http://localhost:8080/api/health/providers

# If your provider is unhealthy, switch to pre-computed:
export EMBEDDING_PROVIDER=precomputed
./mvnw spring-boot:run
```

### **"Port 8080 already in use"**
```bash
# Find what's using the port
lsof -i :8080

# Kill it or change the port in application.yml
```

**See**: `docs/TROUBLESHOOTING.md` for more help

---

## 📖 Additional Resources

### **Documentation**
- `docs/PROVIDER_SETUP.md` — Detailed provider configuration
- `docs/COST_COMPARISON.md` — Provider cost analysis
- `docs/MIGRATION_TO_SAP.md` — Integrate with real SAP Commerce
- `docs/ARCHITECTURE.md` — System design decisions

### **External References**
- [Schema.org Vocabulary](https://schema.org/)
- [Google Rich Results Test](https://search.google.com/test/rich-results)
- [JSON-LD Playground](https://json-ld.org/playground/)
- [pgvector Documentation](https://github.com/pgvector/pgvector)

---

## 🤝 Contributing

Found a bug? Have a suggestion? Open an issue or PR!

---

## 📜 License

MIT License - Free to use, modify, and distribute.

---

## 🎉 Get Started

```bash
# Start your learning journey
cd exercises/day1-exercise1-product-schema
cat README.md
```

**Good luck, architects! 🚀**
