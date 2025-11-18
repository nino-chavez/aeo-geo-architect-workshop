# Workshop 2: FordDirect Automotive AI Search
## Project 3200 - Trial by Fire

**Make 3,200 Ford/Lincoln dealers visible to AI search engines**

---

## 🎯 Mission

Build an automated AI visibility audit system for FordDirect's dealer network using a real-world RFP as your capstone project. This is not just a learning exercise - you're creating actual deliverables for an RFP response.

**The Challenge**: When customers ask ChatGPT or Perplexity for a "Ford dealer near me with F-150 in stock," most dealers are invisible. You will fix this.

---

## ⚡ Quick Start (5 Minutes)

```bash
# 1. Navigate to workshop
cd workshops/02-forddirect-automotive

# 2. Start backend (FastAPI)
cd backend
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate
pip install -r requirements.txt
uvicorn app.main:app --reload

# 3. Verify API is running
curl http://localhost:8000/health

# 4. Start with Exercise 0
cd ../exercises/ex0-zero-click-challenge
cat README.md
```

---

## 📋 Workshop Overview

### **Duration**: 5 days (40 hours)
### **Format**: Squad-based "Trial by Fire"
### **Tech Stack**: Python/FastAPI + TypeScript/Next.js
### **Output**: RFP deliverables + working audit tool

---

## 🏗️ Repository Structure

```
workshops/02-forddirect-automotive/
├── README.md                   # This file
├── PROJECT_3200.md             # Complete 5-day plan
├── RFP_CONTEXT.md              # FordDirect RFP details
│
├── exercises/
│   ├── ex0-zero-click-challenge/    # Day 1 AM: Experience the problem
│   ├── ex1-entity-modeling/         # Day 2 AM: Design data model
│   ├── ex2-schema-templates/        # Day 2 PM: Create JSON-LD
│   ├── ex3-measurement-framework/   # Day 3: Build audit tool ⭐ CRITICAL
│   └── ex4-rag-prototype/           # Day 3 PM: RAG (optional)
│
├── squads/
│   ├── SQUAD_A_DATA_ARCHITECTS.md
│   ├── SQUAD_B_MEASUREMENT_ENGINEERS.md  # ⭐ RFP Differentiator
│   └── SQUAD_C_SCHEMA_SPECIALISTS.md
│
├── backend/                    # Python/FastAPI
│   ├── app/
│   │   ├── main.py            # FastAPI application
│   │   ├── models/            # Pydantic models
│   │   ├── services/          # Business logic
│   │   └── api/               # API routes
│   └── pyproject.toml         # Dependencies (Poetry)
│
├── frontend/                   # TypeScript/Next.js (dashboard)
│   ├── app/
│   ├── components/
│   └── package.json
│
└── data/
    ├── dealers.json           # Sample dealer data
    └── test-queries.json      # Audit test queries
```

---

## 📚 The 5 Exercises

### **Exercise 0: Zero-Click Challenge** ⏱️ 3 hours
**Goal**: Experience the problem firsthand

Test 20 queries across ChatGPT, Perplexity, Gemini, and Claude. Document failures. Build motivation.

**Key Insight**: Most dealers are invisible to AI search.

**[Start Exercise 0 →](exercises/ex0-zero-click-challenge/README.md)**

---

### **Exercise 1: Entity Modeling** ⏱️ 3 hours
**Goal**: Design the "Dealer Entity Registry"

Create ER diagram, database schema, and OpenAPI spec for centralized dealer data.

**Deliverables**:
- Mermaid ER diagram
- PostgreSQL schema with PostGIS
- OpenAPI specification

**[Start Exercise 1 →](exercises/ex1-entity-modeling/README.md)**

---

### **Exercise 2: Schema Templates** ⏱️ 4 hours
**Goal**: Create 5 JSON-LD templates for dealer archetypes

Build Schema.org markup for Metro, Rural, Specialty, Dealership Group, and EV-Focused dealers.

**Deliverables**:
- 5 validated JSON-LD templates
- Template selection guide
- Implementation guide

**[Start Exercise 2 →](exercises/ex2-schema-templates/README.md)**

---

### **Exercise 3: Measurement Framework** ⏱️ 7 hours ⭐ **CRITICAL**
**Goal**: Build the AI visibility audit tool (RFP Deliverable #2)

Create Python scripts to query LLMs, score responses, and measure "Share of Citation."

**Deliverables**:
- `audit_queries.py` - Generate test queries
- `llm_orchestrator.py` - Query OpenAI, Anthropic, Google
- `scorer.py` - Calculate visibility scores (0-100)
- `dashboard.py` - Streamlit visualization

**This is the RFP differentiator. Most consultancies will fail this.**

**[Start Exercise 3 →](exercises/ex3-measurement-framework/README.md)**

---

### **Exercise 4: RAG Prototype** ⏱️ 90 minutes (Optional)
**Goal**: Semantic search over dealer reviews

Build a simple RAG system using pgvector and OpenAI embeddings.

**[Start Exercise 4 →](exercises/ex4-rag-prototype/README.md)**

---

## 👥 The 3 Squads

### **Squad A: Data Architects** (2-3 people)
**Focus**: Design dealer entity model, API, database schema

**Skills**: Data modeling, API design, architecture
**Exercises**: 1
**RFP Contribution**: "Proposed Architecture" section

**[Read Squad A Brief →](squads/SQUAD_A_DATA_ARCHITECTS.md)**

---

### **Squad B: Measurement Engineers** (3-4 people) ⭐ **CRITICAL**
**Focus**: Build the audit tool (Python)

**Skills**: Python, API integration, automation
**Exercises**: 3 (50% of workshop time)
**RFP Contribution**: "Measurement Methodology" (Deliverable #2)

**This squad builds the competitive advantage.**

**[Read Squad B Brief →](squads/SQUAD_B_MEASUREMENT_ENGINEERS.md)**

---

### **Squad C: Schema Specialists** (2-3 people)
**Focus**: Create JSON-LD templates

**Skills**: JSON, Schema.org, attention to detail
**Exercises**: 2
**RFP Contribution**: Technical Appendix with templates

**[Read Squad C Brief →](squads/SQUAD_C_SCHEMA_SPECIALISTS.md)**

---

## 🗓️ 5-Day Schedule

| Day | Morning (9am-12pm) | Afternoon (1pm-5pm) |
|-----|-------------------|---------------------|
| **Day 1** | Exercise 0: Zero-Click Challenge (all squads) | Squad formation, planning |
| **Day 2** | Exercise 1: Entity Modeling | Exercise 2: Schema Templates |
| **Day 3** | Exercise 3: Measurement Framework (50% of workshop!) | Exercise 3 continued + integration |
| **Day 4** | Integration across squads | RFP response drafting |
| **Day 5** | Pitch rehearsal | Final presentation |

**[See Detailed Plan →](PROJECT_3200.md)**

---

## 🎯 Success Criteria

**By end of workshop**:
- ✅ Working Python audit tool (can measure 50 dealers in <5 min)
- ✅ 5 validated Schema.org templates
- ✅ Complete database schema + API spec
- ✅ Draft RFP sections ready to submit
- ✅ Team understands "THE TRAP" (measurement in non-deterministic LLMs)

---

## 🛠️ Technical Setup

### **Prerequisites**
- Python 3.11+
- Docker Desktop
- Node.js 18+ (for frontend)
- Git
- Code editor (VS Code recommended)

### **Backend Setup**
```bash
cd backend
python -m venv venv
source venv/bin/activate
pip install poetry
poetry install
```

### **Environment Variables**
```bash
# .env
OPENAI_API_KEY=sk-...
ANTHROPIC_API_KEY=sk-ant-...
GOOGLE_API_KEY=...
```

### **Start Backend**
```bash
uvicorn app.main:app --reload
# API: http://localhost:8000
# Docs: http://localhost:8000/docs
```

---

## 📖 Key Concepts

### **The "Zero-Click" Problem**
Users want answers, not links. AI search engines (ChatGPT, Perplexity) provide direct answers without sending traffic to dealer websites.

### **The "Measurement Trap"**
LLMs are non-deterministic - the same question produces different answers. Traditional "rank tracking" (like SEMrush) doesn't work. You must measure "Share of Citation" instead.

### **AEO vs SEO**
- **SEO**: Optimize for Google's search results page (10 blue links)
- **AEO**: Optimize for AI's answer (zero-click)

### **The FordDirect Advantage**
3,200 dealers can't individually optimize for AI. FordDirect must centralize optimization (data feeds, Schema.org generation) to scale.

---

## 🎓 Learning Approach

This workshop uses **"Problem-First Learning"**:

1. **Day 1**: Experience failure (Zero-Click Challenge)
2. **Day 2-3**: Build solutions (with purpose, not abstract theory)
3. **Day 4**: Integration (see how pieces connect)
4. **Day 5**: Presentation (prove you solved it)

**Adults learn better from motivated problem-solving than lectures.**

---

## 📊 RFP Deliverables

**What you'll create that goes directly into the RFP response**:

1. **Measurement Framework** (Squad B) - Deliverable #2
   - Working Python audit tool
   - Methodology documentation
   - Sample audit results

2. **Data Architecture** (Squad A)
   - Entity-relationship diagram
   - Database schema
   - API specification

3. **Schema Templates** (Squad C)
   - 5 JSON-LD templates
   - Implementation guide
   - Validation reports

4. **Strategic Roadmap** (All squads)
   - 24-month implementation plan
   - Quick wins (0-3 months)
   - Long-term bets (9-24 months)

---

## 💡 Why This Workshop Wins the RFP

### **Most Consultancies Will Submit**:
- Generic "AI strategy" white papers
- Buzzword-heavy slides
- Promises of "AI rank tracking" (which is impossible)
- No working prototypes

### **You Will Submit**:
- Working Python audit tool (demonstrable)
- Honest methodology that accounts for non-determinism
- Technical depth (actual code, diagrams, schemas)
- Proof you understand "THE TRAP"

**Technical credibility > Beautiful slides**

---

## 🚀 Get Started

### **Option 1: Instructor-Led (Full Workshop)**
1. Read [PROJECT_3200.md](PROJECT_3200.md)
2. Form squads
3. Start Day 1 with Exercise 0

### **Option 2: Self-Study (Individual Learning)**
1. Start with Exercise 0
2. Progress through exercises 1-4 sequentially
3. Focus on understanding concepts over building everything

### **Option 3: RFP Fast-Track (2 weeks to deadline)**
1. Read Squad B brief
2. Jump to Exercise 3 (measurement framework)
3. Build audit tool first
4. Backfill other exercises as time permits

---

## 📞 Support

**Questions?**
- See individual exercise READMEs for detailed instructions
- Check Squad Briefs for team-specific guidance
- Review PROJECT_3200.md for the complete plan

---

**Ready to make 3,200 dealers visible to AI? Let's go. 🚀**
