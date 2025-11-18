# Repository Restructure Plan
## Supporting Two Independent Workshops

**Goal**: Transform this repository into a **Workshop Library** containing two independent workshops that can evolve separately but share documentation and learnings.

---

## 🎯 Design Principles

1. **Complete Isolation**: Each workshop can run independently
2. **No Shared Dependencies**: Different tech stacks, different dependencies
3. **Shared Documentation**: Common concepts (AEO, GEO, RAG) documented once
4. **Clear Navigation**: Root README directs users to the right workshop
5. **Merge-Safe**: When `rfp-workshop` branch merges to `main`, both workshops coexist

---

## 📂 Proposed Structure

```
aeo-geo-architect-workshop/
│
├── README.md                          # 🆕 Root - Workshop selector
├── CONTRIBUTING.md                    # How to add new workshops
│
├── workshops/
│   │
│   ├── 01-sap-commerce/               # EXISTING workshop (moved here)
│   │   ├── README.md                  # Quick start for SAP Commerce
│   │   ├── GETTING_STARTED.md
│   │   ├── PROJECT_SUMMARY.md
│   │   ├── EXECUTIVE_SUMMARY.md
│   │   │
│   │   ├── exercises/
│   │   │   ├── day1-ex1-complete/
│   │   │   ├── day1-ex2-complete/
│   │   │   ├── day2-ex3-complete/
│   │   │   └── day2-ex4-complete/
│   │   │
│   │   ├── src/                       # Java/Spring Boot
│   │   │   └── main/java/com/workshop/aeogeo/
│   │   ├── pom.xml
│   │   ├── docker-compose.yml
│   │   ├── .env.example
│   │   └── switch-provider.sh
│   │
│   └── 02-forddirect-automotive/      # 🆕 NEW workshop (Project 3200)
│       ├── README.md                  # Quick start for FordDirect
│       ├── PROJECT_3200.md            # Trial by Fire guide
│       ├── RFP_CONTEXT.md             # FordDirect RFP summary
│       ├── RFP_ANALYSIS.md            # Strategic assessment
│       │
│       ├── exercises/
│       │   ├── ex0-zero-click-challenge/
│       │   │   ├── README.md
│       │   │   ├── queries.txt
│       │   │   ├── baseline-audit-template.csv
│       │   │   └── DEBRIEF_GUIDE.md
│       │   │
│       │   ├── ex1-entity-modeling/
│       │   │   ├── README.md
│       │   │   ├── HINTS.md
│       │   │   ├── starter/
│       │   │   │   └── dealer-entity.todo.mermaid
│       │   │   └── solution/
│       │   │       ├── dealer-entity.mermaid
│       │   │       ├── api-spec.yaml
│       │   │       └── database-schema.sql
│       │   │
│       │   ├── ex2-schema-templates/
│       │   │   ├── README.md
│       │   │   ├── HINTS.md
│       │   │   ├── starter/
│       │   │   │   └── template-structure.todo.json
│       │   │   └── solution/
│       │   │       ├── metro-dealer.json
│       │   │       ├── rural-dealer.json
│       │   │       ├── specialty-dealer.json
│       │   │       ├── dealership-group.json
│       │   │       ├── ev-focused-dealer.json
│       │   │       └── TEMPLATE_GUIDE.md
│       │   │
│       │   ├── ex3-measurement-framework/  # ⭐ CRITICAL
│       │   │   ├── README.md
│       │   │   ├── HINTS.md
│       │   │   ├── starter/
│       │   │   │   ├── audit_queries.todo.py
│       │   │   │   ├── llm_orchestrator.todo.py
│       │   │   │   ├── scorer.todo.py
│       │   │   │   └── dashboard.todo.py
│       │   │   └── solution/
│       │   │       ├── audit_queries.py
│       │   │       ├── llm_orchestrator.py
│       │   │       ├── scorer.py
│       │   │       ├── dashboard.py
│       │   │       ├── requirements.txt
│       │   │       └── METHODOLOGY.md
│       │   │
│       │   └── ex4-rag-prototype/
│       │       ├── README.md
│       │       └── solution/
│       │
│       ├── squads/
│       │   ├── SQUAD_A_DATA_ARCHITECTS.md
│       │   ├── SQUAD_B_MEASUREMENT_ENGINEERS.md
│       │   ├── SQUAD_C_SCHEMA_SPECIALISTS.md
│       │   └── COLLABORATION_GUIDE.md
│       │
│       ├── backend/                   # Python/FastAPI
│       │   ├── README.md              # Backend setup guide
│       │   ├── pyproject.toml         # Poetry dependencies
│       │   ├── poetry.lock
│       │   ├── docker-compose.yml
│       │   ├── .env.example
│       │   │
│       │   ├── app/
│       │   │   ├── main.py
│       │   │   ├── config.py
│       │   │   │
│       │   │   ├── models/
│       │   │   │   ├── __init__.py
│       │   │   │   ├── dealer.py
│       │   │   │   ├── vehicle.py
│       │   │   │   ├── inventory.py
│       │   │   │   └── audit.py
│       │   │   │
│       │   │   ├── services/
│       │   │   │   ├── __init__.py
│       │   │   │   ├── embedding_service.py
│       │   │   │   ├── schema_generator.py
│       │   │   │   ├── audit_service.py
│       │   │   │   └── readiness_scorer.py
│       │   │   │
│       │   │   ├── api/
│       │   │   │   ├── __init__.py
│       │   │   │   ├── dealers.py
│       │   │   │   ├── schema.py
│       │   │   │   ├── audit.py
│       │   │   │   └── health.py
│       │   │   │
│       │   │   └── providers/
│       │   │       ├── __init__.py
│       │   │       ├── base.py
│       │   │       ├── openai_provider.py
│       │   │       ├── anthropic_provider.py
│       │   │       └── vertex_provider.py
│       │   │
│       │   ├── tests/
│       │   │   ├── test_schema_generator.py
│       │   │   ├── test_audit_service.py
│       │   │   └── test_api.py
│       │   │
│       │   └── scripts/
│       │       ├── seed_dealers.py
│       │       ├── seed_inventory.py
│       │       ├── run_audit.py
│       │       └── generate_report.py
│       │
│       ├── frontend/                  # TypeScript/Next.js
│       │   ├── README.md              # Frontend setup guide
│       │   ├── package.json
│       │   ├── tsconfig.json
│       │   ├── next.config.js
│       │   ├── tailwind.config.ts
│       │   │
│       │   ├── app/
│       │   │   ├── layout.tsx
│       │   │   ├── page.tsx           # Dashboard home
│       │   │   │
│       │   │   ├── audit/
│       │   │   │   ├── page.tsx       # Audit results
│       │   │   │   └── [runId]/
│       │   │   │       └── page.tsx   # Audit run detail
│       │   │   │
│       │   │   ├── dealers/
│       │   │   │   ├── page.tsx       # Dealer list
│       │   │   │   └── [id]/
│       │   │   │       └── page.tsx   # Dealer detail
│       │   │   │
│       │   │   └── api/
│       │   │       └── [...]/route.ts # Proxy to FastAPI
│       │   │
│       │   ├── components/
│       │   │   ├── ui/                # shadcn/ui components
│       │   │   ├── AuditChart.tsx
│       │   │   ├── DealerCard.tsx
│       │   │   ├── VisibilityScore.tsx
│       │   │   └── CitationTable.tsx
│       │   │
│       │   └── lib/
│       │       ├── api.ts             # API client
│       │       └── utils.ts
│       │
│       └── data/
│           ├── dealers.json           # 50 sample dealers
│           ├── inventory.json         # Sample F-150 inventory
│           ├── test-queries.json      # 20 audit queries
│           └── README.md              # Data dictionary
│
├── docs/                              # 🔄 Shared documentation
│   ├── concepts/
│   │   ├── AEO_FUNDAMENTALS.md        # Shared: What is AEO?
│   │   ├── GEO_FUNDAMENTALS.md        # Shared: What is GEO?
│   │   ├── RAG_EXPLAINED.md           # Shared: RAG architecture
│   │   ├── VECTOR_EMBEDDINGS.md       # Shared: How embeddings work
│   │   └── SCHEMA_ORG_GUIDE.md        # Shared: Schema.org basics
│   │
│   ├── WORKSHOP_GUIDE.md              # How to choose which workshop
│   ├── FORDDIRECT_RFP_ANALYSIS.md     # Strategic RFP assessment
│   └── ARCHITECTURE_PATTERNS.md       # Common patterns across workshops
│
└── .github/
    └── workflows/
        ├── sap-commerce-ci.yml        # CI for workshop 01
        └── forddirect-ci.yml          # CI for workshop 02
```

---

## 🔄 Migration Steps

### **Step 1: Create Workshop Directories**
```bash
mkdir -p workshops/01-sap-commerce
mkdir -p workshops/02-forddirect-automotive
```

### **Step 2: Move Existing Workshop**
```bash
# Move SAP Commerce workshop content
mv src workshops/01-sap-commerce/
mv exercises workshops/01-sap-commerce/
mv pom.xml workshops/01-sap-commerce/
mv docker-compose.yml workshops/01-sap-commerce/
mv switch-provider.sh workshops/01-sap-commerce/
mv .env.* workshops/01-sap-commerce/
mv GETTING_STARTED.md workshops/01-sap-commerce/
mv PROJECT_SUMMARY.md workshops/01-sap-commerce/
mv EXECUTIVE_SUMMARY.md workshops/01-sap-commerce/

# Update README for workshop 01
cp README.md workshops/01-sap-commerce/README.md
```

### **Step 3: Create New Workshop Structure**
```bash
# Backend
mkdir -p workshops/02-forddirect-automotive/backend/{app/{models,services,api,providers},tests,scripts}

# Frontend
mkdir -p workshops/02-forddirect-automotive/frontend/{app,components,lib}

# Exercises
mkdir -p workshops/02-forddirect-automotive/exercises/{ex0-zero-click-challenge,ex1-entity-modeling,ex2-schema-templates,ex3-measurement-framework,ex4-rag-prototype}/{starter,solution}

# Squads
mkdir -p workshops/02-forddirect-automotive/squads

# Data
mkdir -p workshops/02-forddirect-automotive/data
```

### **Step 4: Create Shared Docs**
```bash
mkdir -p docs/concepts
```

### **Step 5: Update Root README**
Create new root README that serves as a workshop selector.

---

## 📄 Root README (New)

```markdown
# AEO/GEO Workshop Library

**Learn Answer Engine Optimization (AEO) & Generative Engine Optimization (GEO)**

This repository contains multiple workshops teaching how to make your data visible to AI search engines (ChatGPT, Perplexity, Google AI Overviews, etc.).

---

## 🎓 Choose Your Workshop

### **Workshop 1: SAP Commerce Cloud**
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

### **Workshop 2: FordDirect Automotive** 🆕
**Target Audience**: Technical architects, commerce strategists, measurement engineers
**Tech Stack**: Python/FastAPI, TypeScript/Next.js, PostgreSQL + pgvector
**Duration**: 5 days (40 hours total)
**Focus**: Automotive dealer AI visibility & measurement

**[Start Workshop 2 →](workshops/02-forddirect-automotive/README.md)**

**What You'll Learn**:
- Design dealer entity models for AI search
- Build AI visibility audit tools (measure "Share of Citation")
- Create Schema.org templates for 3,200 dealers
- Solve the "measurement trap" (LLMs are non-deterministic)
- Use real RFP as capstone project

**Special Features**:
- "Trial by Fire" squad-based learning
- Working Python audit tool (production-ready)
- RFP response deliverables included

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

**Not sure?** Start with Workshop 1 (it's shorter and covers fundamentals).

---

## 📚 Shared Concepts

Both workshops teach these core concepts (documented in `/docs/concepts/`):

- [What is AEO?](docs/concepts/AEO_FUNDAMENTALS.md)
- [What is GEO?](docs/concepts/GEO_FUNDAMENTALS.md)
- [RAG Architecture Explained](docs/concepts/RAG_EXPLAINED.md)
- [Vector Embeddings](docs/concepts/VECTOR_EMBEDDINGS.md)
- [Schema.org Guide](docs/concepts/SCHEMA_ORG_GUIDE.md)

---

## 🤝 Contributing

Want to add a new workshop? See [CONTRIBUTING.md](CONTRIBUTING.md)

---

## 📜 License

MIT License - Free to use, modify, and distribute.
```

---

## 🔧 Implementation Checklist

### **Phase 1: Restructure (This Branch)**
- [ ] Create `workshops/` directory structure
- [ ] Create `docs/concepts/` directory
- [ ] Write PROJECT_3200_PLAN.md (✅ Done)
- [ ] Write REPOSITORY_RESTRUCTURE_PLAN.md (✅ This doc)
- [ ] Create new root README.md
- [ ] Create CONTRIBUTING.md

### **Phase 2: Build Workshop 2 Foundation**
- [ ] Create Squad Briefs (3 files)
- [ ] Create Exercise 0 (Zero-Click Challenge)
- [ ] Create Exercise 3 starter code (Python)
- [ ] Scaffold FastAPI backend
- [ ] Scaffold Next.js frontend
- [ ] Create sample data (dealers, inventory, queries)

### **Phase 3: Develop Exercises**
- [ ] Exercise 1: Entity Modeling (README + solution)
- [ ] Exercise 2: Schema Templates (README + 5 templates)
- [ ] Exercise 3: Measurement Framework (README + full Python solution)
- [ ] Exercise 4: RAG Prototype (README + solution)

### **Phase 4: Test Integration**
- [ ] Verify Workshop 1 still works independently
- [ ] Verify Workshop 2 works independently
- [ ] Test cross-references in shared docs
- [ ] CI/CD for both workshops

### **Phase 5: Merge to Main**
- [ ] Final review
- [ ] Update root README
- [ ] Merge `rfp-workshop` → `main`
- [ ] Tag release: `v2.0.0-dual-workshops`

---

## 🎯 Success Criteria

After restructure and merge:

✅ **Workshop 1 (SAP Commerce)**:
- Runs independently in `workshops/01-sap-commerce/`
- Original functionality preserved
- All 4 exercises work
- Documentation updated with new path

✅ **Workshop 2 (FordDirect)**:
- Runs independently in `workshops/02-forddirect-automotive/`
- Backend (FastAPI) works standalone
- Frontend (Next.js) works standalone
- All 5 exercises complete
- Squad briefs clear and actionable

✅ **Repository**:
- Root README clearly directs users
- Shared docs referenced by both workshops
- CI/CD for each workshop
- Clean merge history

---

## 📊 Timeline Estimate

| Phase | Duration | Deliverable |
|-------|----------|-------------|
| **Phase 1**: Restructure | 2 hours | Directory structure, root README |
| **Phase 2**: Foundation | 4 hours | Squad briefs, scaffolds, sample data |
| **Phase 3**: Exercises | 8 hours | All 5 exercises complete with solutions |
| **Phase 4**: Testing | 2 hours | Both workshops verified |
| **Phase 5**: Merge | 1 hour | Clean merge to main |
| **Total** | ~17 hours | Dual-workshop repository |

With AI assistance (Claude Code), likely **10-12 hours** of actual work.

---

**Status**: 🟡 **PLANNED - Ready to Execute**

**Next Action**: Create root README → Build Squad Briefs → Scaffold backend
