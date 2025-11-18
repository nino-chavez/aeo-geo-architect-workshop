# AEO/GEO Workshop Library
**Answer Engine Optimization (AEO) & Generative Engine Optimization (GEO)**

Learn how to make your data visible to AI search engines (ChatGPT, Perplexity, Google AI Overviews, Claude).

---

## 🎓 Choose Your Workshop

This repository contains **two independent workshops** teaching AEO/GEO for different contexts:

### **Workshop 1: SAP Commerce Cloud** (Original)
**Target Audience**: SAP Commerce Cloud architects and developers
**Tech Stack**: Java 17, Spring Boot, PostgreSQL + pgvector
**Duration**: 2 days (6 hours total)
**Focus**: Product catalog optimization for e-commerce

**[Start Workshop 1 →](workshops/01-sap-commerce/README.md)**

**What You'll Learn**:
- Map SAP Commerce products to Schema.org
- Build RAG pipelines for semantic product search
- Detect AI bot traffic (ChatGPT, Perplexity, etc.)
- Multi-provider embeddings (Azure OpenAI, Vertex AI, Ollama)

---

### **Workshop 2: FordDirect Automotive** 🆕 (Project 3200)
**Target Audience**: Technical architects, commerce strategists, measurement engineers
**Tech Stack**: Python/FastAPI, TypeScript/Next.js, PostgreSQL + pgvector
**Duration**: 5 days (40 hours total)
**Focus**: Automotive dealer AI visibility & measurement for 3,200 dealerships

**[Start Workshop 2 →](workshops/02-forddirect-automotive/README.md)**

**What You'll Learn**:
- Design dealer entity models for AI search
- Build AI visibility audit tools (measure "Share of Citation")
- Create Schema.org templates for 3,200 dealers
- Solve the "measurement trap" (LLMs are non-deterministic)
- Use real RFP as capstone project

**Special Features**:
- "Trial by Fire" squad-based learning (3 squads)
- Working Python audit tool (production-ready)
- RFP response deliverables included
- Hybrid tech stack (Python backend + TypeScript dashboard)

---

## 🎯 Which Workshop Should I Choose?

| Criteria | Workshop 1 (SAP Commerce) | Workshop 2 (FordDirect) |
|----------|---------------------------|-------------------------|
| **Industry** | E-commerce (any vertical) | Automotive dealers |
| **Primary Language** | Java | Python + TypeScript |
| **Complexity** | Beginner-Intermediate | Intermediate-Advanced |
| **Duration** | 2 days | 5 days |
| **Focus** | Product catalog optimization | Dealer visibility measurement |
| **Output** | Learning exercises | RFP deliverables + learning |
| **Team Size** | Individual or small group | Squad-based (6-10 people) |
| **Real-World Application** | Generic e-commerce | Specific RFP response |

**Not sure?** Start with Workshop 1 (it's shorter and covers fundamentals).

---

## 📚 Shared Concepts

Both workshops teach these core concepts:

- **What is AEO?** - Answer Engine Optimization (make data answerable by AI)
- **What is GEO?** - Generative Engine Optimization (optimize for LLM retrieval)
- **RAG Architecture** - Retrieval-Augmented Generation explained
- **Vector Embeddings** - How similarity search works
- **Schema.org** - Structured data for AI

**Documentation**: See `/docs/concepts/` for shared fundamentals

---

## 🚀 Quick Start

### **If You're New to AEO/GEO**
1. Read: [What is AEO?](docs/concepts/AEO_FUNDAMENTALS.md) (5 min)
2. Read: [AEO vs SEO](docs/concepts/GEO_FUNDAMENTALS.md) (5 min)
3. Choose your workshop based on industry/tech stack
4. Follow the workshop's README for setup

### **If You're Here for FordDirect RFP**
1. Go directly to [Workshop 2](workshops/02-forddirect-automotive/README.md)
2. Read [PROJECT_3200.md](workshops/02-forddirect-automotive/PROJECT_3200.md) for the trial-by-fire plan
3. Review [Squad Briefs](workshops/02-forddirect-automotive/squads/) to understand team structure

---

## 🏗️ Repository Structure

```
aeo-geo-architect-workshop/
├── README.md                     # This file - workshop selector
│
├── workshops/
│   ├── 01-sap-commerce/          # Workshop 1: E-commerce (Java)
│   │   ├── README.md
│   │   ├── src/                  # Spring Boot application
│   │   ├── exercises/            # 4 hands-on exercises
│   │   └── docker-compose.yml
│   │
│   └── 02-forddirect-automotive/ # Workshop 2: Automotive (Python/TypeScript)
│       ├── README.md
│       ├── PROJECT_3200.md       # Trial by Fire plan
│       ├── exercises/            # 5 exercises (0-4)
│       ├── squads/               # 3 squad briefs
│       ├── backend/              # FastAPI (Python)
│       ├── frontend/             # Next.js (TypeScript)
│       └── data/                 # Sample dealers, queries
│
└── docs/                         # Shared documentation
    └── concepts/                 # AEO, GEO, RAG fundamentals
```

---

## 💡 Key Innovations

### **Workshop 1 (SAP Commerce)**
- Multi-provider embeddings (works offline or with Azure/GCP/OpenAI)
- SAP Commerce-inspired data models
- Progressive learning with hints and checkpoints
- GitHub Copilot integration

### **Workshop 2 (FordDirect)**
- **Problem-first learning** (Zero-Click Challenge starts with failure)
- **Solves "THE TRAP"** (measurement in non-deterministic LLMs)
- **Squad-based specialization** (not everyone learns everything)
- **Real RFP deliverables** (actual work product, not just exercises)
- **Hybrid tech stack** (Python for speed, TypeScript for UI)

---

## 🎓 Prerequisites

### **Workshop 1 (SAP Commerce)**
- Java 17+
- Docker Desktop
- Maven 3.8+ (or use included `./mvnw`)
- Basic Spring Boot knowledge (helpful but not required)

### **Workshop 2 (FordDirect)**
- Python 3.11+
- Docker Desktop
- Node.js 18+ (for frontend)
- Basic FastAPI or Express knowledge (helpful but not required)

**Both workshops**:
- Git
- Code editor (VS Code or IntelliJ IDEA recommended)
- Willingness to learn AI/LLM concepts

---

## 🤝 Contributing

Want to add a new workshop? See [CONTRIBUTING.md](CONTRIBUTING.md)

**Ideas for future workshops**:
- Workshop 3: Healthcare providers (HIPAA-compliant AEO)
- Workshop 4: Real estate agents (local search optimization)
- Workshop 5: Financial services (compliance + AI visibility)

---

## 📖 External Resources

**AEO/GEO Fundamentals**:
- [What is RAG?](https://www.promptingguide.ai/techniques/rag)
- [Schema.org Vocabulary](https://schema.org/)
- [Google Structured Data Guidelines](https://developers.google.com/search/docs/appearance/structured-data/intro-structured-data)

**LLM Providers**:
- [OpenAI API Docs](https://platform.openai.com/docs)
- [Anthropic Claude Docs](https://docs.anthropic.com/)
- [Google Vertex AI](https://cloud.google.com/vertex-ai/docs)

**Tools**:
- [Google Rich Results Test](https://search.google.com/test/rich-results)
- [JSON-LD Playground](https://json-ld.org/playground/)
- [Schema Markup Validator](https://validator.schema.org/)

---

## 📜 License

MIT License - Free to use, modify, and distribute.

---

## 🎉 Get Started

**For SAP Commerce practitioners**:
```bash
cd workshops/01-sap-commerce
cat README.md
```

**For FordDirect RFP team (Project 3200)**:
```bash
cd workshops/02-forddirect-automotive
cat PROJECT_3200.md
```

**Good luck! 🚀**
