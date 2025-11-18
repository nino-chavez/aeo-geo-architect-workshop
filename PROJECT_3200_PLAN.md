# Project 3200: FordDirect AI Search Workshop
## Greenfield Architecture Plan

**Mission**: Train Commerce technical resources (SAP Commerce, Shopify) on AEO/GEO using a real-world RFP as the capstone project.

**Context**: FordDirect RFP - Help 3,200 Ford/Lincoln dealers become visible in AI search (ChatGPT, Perplexity, Google AI Overviews).

---

## 🎯 Strategic Objectives

### Business Goals
1. **Win the FordDirect RFP** (Due: Dec 1, 2025)
2. **Upskill Commerce Practice** (Senior Managers + Managers)
3. **Build Reusable IP** (Workshop becomes repeatable asset)
4. **Demonstrate Technical Depth** (Differentiate from generic AI consultancies)

### Learning Objectives
- Understand the **SEO → AEO/GEO shift** (keywords → entities)
- Master **Schema.org** for automotive data
- Design **measurement frameworks** for non-deterministic systems (LLMs)
- Build **working prototypes** (not just slides)

---

## 🏗️ Repository Structure

```
aeo-geo-architect-workshop/
├── README.md                          # Root - points to both workshops
│
├── workshops/
│   ├── sap-commerce/                  # EXISTING: Original workshop (untouched)
│   │   ├── README.md
│   │   ├── exercises/
│   │   ├── src/                       # Java/Spring Boot
│   │   └── docker-compose.yml
│   │
│   └── forddirect-automotive/         # NEW: Project 3200 (this plan)
│       ├── README.md                  # Workshop overview
│       ├── PROJECT_3200.md            # Trial by Fire guide
│       ├── RFP_CONTEXT.md             # FordDirect RFP summary
│       │
│       ├── exercises/
│       │   ├── ex0-zero-click-challenge/
│       │   │   ├── README.md          # Problem-first learning
│       │   │   ├── queries.txt        # 20 test queries
│       │   │   └── baseline-audit.csv # Results template
│       │   │
│       │   ├── ex1-entity-modeling/
│       │   │   ├── README.md          # Design dealer data model
│       │   │   ├── HINTS.md
│       │   │   └── solution/
│       │   │       ├── dealer-entity.mermaid
│       │   │       └── api-spec.yaml  # OpenAPI spec
│       │   │
│       │   ├── ex2-schema-templates/
│       │   │   ├── README.md          # JSON-LD for dealers
│       │   │   ├── HINTS.md
│       │   │   └── solution/
│       │   │       ├── metro-dealer.json
│       │   │       ├── rural-dealer.json
│       │   │       └── specialty-dealer.json
│       │   │
│       │   ├── ex3-measurement-framework/  # ⭐ CRITICAL (50% of time)
│       │   │   ├── README.md          # RFP Deliverable #2
│       │   │   ├── HINTS.md
│       │   │   └── solution/
│       │   │       ├── audit_queries.py      # 20 test queries
│       │   │       ├── llm_orchestrator.py   # Call OpenAI/Anthropic
│       │   │       ├── scorer.py             # Visibility scoring
│       │   │       └── dashboard.py          # Streamlit UI
│       │   │
│       │   └── ex4-rag-prototype/     # Optional (advanced)
│       │       ├── README.md          # Semantic search over reviews
│       │       └── solution/
│       │
│       ├── squads/
│       │   ├── SQUAD_A_DATA_ARCHITECTS.md
│       │   ├── SQUAD_B_MEASUREMENT_ENGINEERS.md
│       │   └── SQUAD_C_SCHEMA_SPECIALISTS.md
│       │
│       ├── backend/                   # Python/FastAPI
│       │   ├── pyproject.toml
│       │   ├── poetry.lock
│       │   ├── docker-compose.yml
│       │   ├── .env.example
│       │   │
│       │   ├── app/
│       │   │   ├── main.py
│       │   │   ├── models/
│       │   │   │   ├── dealer.py
│       │   │   │   ├── vehicle.py
│       │   │   │   └── inventory.py
│       │   │   ├── services/
│       │   │   │   ├── embedding_service.py
│       │   │   │   ├── schema_generator.py
│       │   │   │   └── audit_service.py
│       │   │   ├── api/
│       │   │   │   ├── dealers.py
│       │   │   │   ├── schema.py
│       │   │   │   └── audit.py
│       │   │   └── providers/
│       │   │       ├── openai_provider.py
│       │   │       ├── anthropic_provider.py
│       │   │       └── vertex_provider.py
│       │   │
│       │   └── scripts/
│       │       ├── seed_data.py
│       │       ├── run_audit.py
│       │       └── generate_report.py
│       │
│       ├── frontend/                  # TypeScript/Next.js
│       │   ├── package.json
│       │   ├── tsconfig.json
│       │   ├── next.config.js
│       │   │
│       │   ├── app/
│       │   │   ├── page.tsx           # Dashboard home
│       │   │   ├── audit/
│       │   │   │   └── page.tsx       # Audit results
│       │   │   └── dealers/
│       │   │       └── [id]/page.tsx  # Dealer detail
│       │   │
│       │   └── components/
│       │       ├── AuditChart.tsx
│       │       ├── DealerCard.tsx
│       │       └── VisibilityScore.tsx
│       │
│       └── data/
│           ├── dealers.json           # 50 sample dealers
│           ├── inventory.json         # Sample F-150 inventory
│           └── test-queries.json      # 20 audit queries
│
└── docs/
    ├── WORKSHOP_GUIDE.md              # How to choose which workshop
    └── FORDDIRECT_RFP_ANALYSIS.md     # Strategic assessment
```

---

## 🔧 Technology Stack

### Backend (Python/FastAPI)
**Why Python:**
- Faster to write (3-5x less code than Java)
- Better LLM ecosystem (OpenAI SDK, Anthropic SDK, LangChain)
- Easier for diverse backgrounds (SAP Commerce, Shopify, non-Java)
- Async-native (critical for LLM API calls)

**Stack:**
```
Language: Python 3.11+
Framework: FastAPI 0.104+
ORM: SQLAlchemy 2.0
Vector DB: Qdrant (Docker) or Supabase (managed pgvector)
LLM SDKs: OpenAI, Anthropic (Claude), Google (Vertex AI)
Validation: Pydantic V2
Testing: Pytest
```

### Frontend (TypeScript/Next.js)
**Why TypeScript:**
- Shopify developers already know JavaScript/TypeScript
- Modern, type-safe
- Great for dashboards
- Can deploy to Vercel (show working prototype)

**Stack:**
```
Language: TypeScript 5.3+
Framework: Next.js 14 (App Router)
UI: Tailwind CSS + shadcn/ui
Charts: Recharts or Tremor
Data Fetching: React Query (TanStack Query)
Deployment: Vercel (optional)
```

### Database
```
Primary: PostgreSQL 16 + pgvector
Alternative: Qdrant (specialized vector DB)
Hosted Option: Supabase (for rapid prototyping)
```

### Infrastructure
```
Local Development: Docker Compose
  - PostgreSQL + pgvector
  - Qdrant (optional)
  - Backend (FastAPI)
  - Frontend (Next.js dev server)
```

---

## 📚 Workshop Flow (5-Day "Trial by Fire")

### **Pre-Work (1 Week Before)**
**Goal**: Reduce setup friction, establish baseline knowledge

**Tasks:**
1. Read assigned articles (2 hours):
   - "What is RAG?" (LangChain docs)
   - "AEO vs SEO" (Moz article)
   - "Schema.org for Local Business" (Google guide)
2. Environment setup (1 hour):
   - Install Docker Desktop
   - Clone workshop repo
   - Run `docker-compose up` (verify it works)
3. Complete "Developer Survey":
   - Current skill level (Python/Java/TypeScript)
   - Preferred squad assignment
   - RFP section interest

---

### **Day 1: Problem Discovery**

#### **Morning (9am - 12pm): The Zero-Click Challenge**
**Exercise 0** (3 hours)

**Format**: Everyone works together (no squads yet)

**The Challenge**:
> "It's Saturday morning. You need to buy an F-150 for your construction business. You open ChatGPT and ask: 'Find me a Ford dealer near Aurora, CO with a white F-150 Lariat in stock under $55k.'"

**Tasks**:
1. Query ChatGPT, Perplexity, Gemini, Claude (15 min each)
2. Document results in `baseline-audit.csv`:
   - Which dealers were mentioned?
   - Was the info accurate?
   - Did it hallucinate?
   - Did it say "I can't access real-time inventory"?
3. Repeat for 10 different cities + 10 different vehicle specs

**Expected Outcome**: **FAILURE**
- LLMs won't have real-time inventory
- They'll hallucinate dealer names
- They'll give generic advice ("Visit Ford.com")

**Debrief** (30 min):
- Why did we fail?
- What data is missing?
- Where do LLMs get their information?
- **KEY INSIGHT**: "FordDirect controls the data, but it's not reaching the LLMs"

**Deliverable**: Shared spreadsheet showing baseline (current state of AI visibility)

---

#### **Afternoon (1pm - 5pm): Deconstruct the Problem**

**Session 1: "How LLMs Work" (60 min)**
- Training data vs. retrieval (RAG)
- Context windows
- Why LLMs can't access your website directly
- The role of structured data

**Session 2: "The Measurement Trap" (60 min)**
- Guest lecture: You (as the strategist) present the RFP analysis
- Explain why "AI rank tracking" is impossible
- Introduce "Share of Citation" metric
- Show the competitive advantage this creates

**Session 3: Squad Formation (60 min)**
- Present the 3 squads
- Participants self-select (with guidance)
- Each squad gets their brief
- Review RFP deliverables (map squads to RFP sections)

**Session 4: Squad Planning (60 min)**
- Squads meet independently
- Define success criteria for their deliverables
- Assign internal roles
- Identify knowledge gaps

**End of Day 1 Deliverables**:
- ✅ Baseline audit complete
- ✅ Squads formed
- ✅ Everyone understands "the trap"

---

### **Day 2: Quick Wins (Schema.org)**

#### **Morning (9am - 12pm): Entity Modeling**
**Exercise 1** (3 hours)

**Squad A** (Data Architects):
- Design the "Dealer Entity" data model
- Define required vs. optional attributes
- Map relationships (Dealer → Inventory → Reviews)
- Create Mermaid ER diagram
- Draft OpenAPI spec for "Dealer API"

**Squad B** (Measurement Engineers):
- Define the 20 "test queries" for the audit
- Research existing tools (Profound, Yext, BrightData)
- Prototype first Python script (query OpenAI API)
- Document API rate limits and costs

**Squad C** (Schema Specialists):
- Research Schema.org types:
  - `LocalBusiness`
  - `AutomotiveBusiness`
  - `Vehicle`
  - `Product` with `Offer`
- Identify required properties
- Study Google Rich Results Test

---

#### **Afternoon (1pm - 5pm): Schema Templates**
**Exercise 2** (4 hours)

**Squad A**:
- Review Squad C's schemas
- Provide feedback on data model alignment
- Begin API spec (how to generate schemas programmatically)

**Squad B**:
- Continue Python script development
- Test against 3 LLM providers (OpenAI, Anthropic, Google)
- Document response variations (non-determinism)

**Squad C** (PRIMARY FOCUS):
- Create 3 reference JSON-LD templates:
  1. **Metro Dealer**: High volume, multi-location, large inventory
  2. **Rural Dealer**: Single location, service-focused, community ties
  3. **Specialty Dealer**: Commercial trucks, fleet sales
- Validate each with Google Rich Results Test
- Document template variables (dealer name, address, inventory, etc.)

**End of Day 2 Deliverables**:
- ✅ Squad A: Entity diagram + API spec draft
- ✅ Squad B: 20 test queries + working Python script
- ✅ Squad C: 3 validated JSON-LD templates

---

### **Day 3: The Measurement Framework (The RFP Win)**

**All Day (9am - 5pm): Exercise 3** (7 hours)

**CRITICAL**: This is 50% of the workshop value. This IS the RFP response.

#### **Squad B (PRIMARY FOCUS)** - Building Deliverable #2

**Morning (9am - 12pm): Automation Architecture**
1. Design the audit pipeline:
   ```
   Test Queries → LLM APIs → Response Parser → Scorer → Dashboard
   ```
2. Create `audit_queries.py`:
   - Read queries from `test-queries.json`
   - Construct prompts with city/vehicle variations
3. Create `llm_orchestrator.py`:
   - Call OpenAI, Anthropic, Google APIs
   - Handle rate limits, retries, errors
   - Log raw responses

**Afternoon (1pm - 5pm): Scoring & Dashboard**
4. Create `scorer.py`:
   - Parse LLM responses for dealer mentions
   - Score visibility (0-100):
     - 100 = Dealer mentioned with accurate details
     - 50 = Dealer mentioned but generic info
     - 0 = Dealer not mentioned
   - Calculate "Share of Citation" (% of queries where dealer appears)
5. Create `dashboard.py` (Streamlit):
   - Upload audit results (CSV)
   - Show visibility scores per dealer
   - Show aggregate metrics
   - Export report (PDF/CSV)

**End of Day 3 Deliverable**:
- ✅ **Working Python audit tool** (THIS GOES IN THE RFP)
- ✅ Sample audit report for 50 dealers
- ✅ Methodology documentation

#### **Squad A**: API Implementation
- Implement FastAPI endpoints:
  - `POST /api/dealers` (create dealer)
  - `GET /api/dealers/{id}/schema` (generate JSON-LD)
  - `GET /api/dealers/{id}/readiness` (AI readiness score)
- Connect to PostgreSQL
- Seed 50 sample dealers

#### **Squad C**: Advanced Templates
- Add 2 more templates:
  4. **Dealership Group** (multi-location corporate)
  5. **EV-Focused Dealer** (F-150 Lightning, charging stations)
- Create "Template Selection Guide"
- Begin implementation guide for FordDirect dev team

---

### **Day 4: Integration & Refinement**

#### **Morning (9am - 12pm): Convergence**

**All Squads**: Integration session
1. Squad C's JSON-LD templates → Squad A's API
2. Squad A's dealer data → Squad B's audit tool
3. Test end-to-end:
   - Create dealer via API
   - Generate Schema.org JSON-LD
   - Run audit to see if dealer appears in LLM responses

**Expected Issues**:
- Data format mismatches
- Missing attributes
- LLMs still not finding dealers (expected - shows why RAG is needed)

#### **Afternoon (1pm - 5pm): RFP Response Drafting**

**All Squads**: Collaborate on RFP sections

**Squad A** writes:
- "Proposed Architecture" section
- "Dealer Entity Registry" design
- "API Specification" appendix

**Squad B** writes:
- "Measurement Methodology" section (Deliverable #2)
- "KPIs and Success Metrics"
- "Quarterly Audit Process"

**Squad C** writes:
- "Implementation Guide" for FordDirect dev team
- "Schema.org Best Practices"
- Technical appendix with JSON-LD examples

**End of Day 4 Deliverables**:
- ✅ Working integration (API → Schema → Audit)
- ✅ Draft RFP sections (rough, needs polish)
- ✅ Identified gaps (what we can't solve yet)

---

### **Day 5: The Pitch**

#### **Morning (9am - 11am): Rehearsal**

Each squad presents their work (20 min each):
1. What we built
2. Technical challenges we solved
3. What we learned
4. How this maps to RFP deliverables

**Instructor** (you) gives feedback:
- Technical accuracy
- RFP alignment
- Presentation clarity

#### **Late Morning (11am - 12pm): The Pitch**

**Format**: Simulate presenting to FordDirect CIO

**Constraint**: No slides. Only:
- Live demo of audit tool
- Mermaid diagrams
- JSON-LD examples
- Code walkthrough

**Agenda**:
1. "The Problem" (5 min) - Zero-Click Challenge results
2. "The Solution" (10 min) - Architecture + demos
3. "The Measurement Framework" (10 min) - Live audit demo
4. "The Roadmap" (5 min) - 24-month plan

#### **Afternoon (1pm - 3pm): Debrief & Next Steps**

1. What worked?
2. What would we do differently?
3. Knowledge gaps identified
4. Plan for completing RFP response
5. Assign follow-up tasks

**End of Workshop Deliverables**:
- ✅ Working audit tool (Python)
- ✅ Dealer API (FastAPI)
- ✅ 5 JSON-LD templates
- ✅ Draft RFP sections
- ✅ Technical diagrams
- ✅ Upskilled team ready to execute

---

## 🎯 Squad Details

### **Squad A: Data Architects**
**Profile**: Senior Managers, 10+ years experience, data modeling background

**Size**: 2-3 people

**Skills Required**:
- Entity-relationship modeling
- API design
- Database schema design
- Architecture diagramming

**Skills NOT Required**:
- Coding (light Python/SQL is bonus, not required)
- LLM expertise
- Frontend development

**Deliverables**:
1. Dealer Entity ER diagram (Mermaid)
2. OpenAPI specification for Dealer API
3. Database schema (PostgreSQL)
4. FastAPI implementation (basic CRUD)
5. RFP Section: "Proposed Architecture"

**Tools**:
- Mermaid.js (diagrams)
- Swagger Editor (OpenAPI)
- FastAPI (Python framework)
- SQLAlchemy (ORM)

---

### **Squad B: Measurement Engineers** ⭐ CRITICAL
**Profile**: Managers, 5-8 years experience, scripting/automation background

**Size**: 3-4 people (largest squad, most critical)

**Skills Required**:
- Python or Node.js scripting
- API integration
- Data analysis
- Comfort with ambiguity (LLMs are non-deterministic)

**Skills NOT Required**:
- Deep LLM knowledge (we teach this)
- Machine learning expertise
- Vector database experience

**Deliverables**:
1. 20 test queries (JSON file)
2. Python audit tool:
   - `audit_queries.py`
   - `llm_orchestrator.py`
   - `scorer.py`
   - `dashboard.py` (Streamlit)
3. Sample audit report (50 dealers)
4. Methodology documentation
5. RFP Deliverable #2: "Measurement Framework"

**Tools**:
- Python 3.11+
- OpenAI SDK
- Anthropic SDK (Claude)
- Google Vertex AI SDK
- Streamlit (dashboard)
- Pandas (data analysis)

---

### **Squad C: Schema Specialists**
**Profile**: Managers/developers, frontend or Shopify background

**Size**: 2-3 people

**Skills Required**:
- JSON proficiency
- Attention to detail
- HTML/markup understanding (helpful)
- Documentation skills

**Skills NOT Required**:
- Backend development
- Python (nice to have, not required)
- Database knowledge

**Deliverables**:
1. 5 JSON-LD templates:
   - Metro dealer
   - Rural dealer
   - Specialty dealer
   - Dealership group
   - EV-focused dealer
2. Validation reports (Google Rich Results Test)
3. Template selection guide
4. Implementation guide for FordDirect
5. RFP Technical Appendix

**Tools**:
- JSON-LD Playground
- Google Rich Results Test
- Schema.org documentation
- VS Code (with JSON schema validation)

---

## 📊 Success Metrics

### **Immediate (End of Workshop)**
- ✅ 100% of participants understand "The Measurement Trap"
- ✅ Squad B has working audit tool (can run on 10+ dealers)
- ✅ Squad C has 3+ validated JSON-LD templates
- ✅ Squad A has complete API spec
- ✅ Draft RFP sections complete

### **Week 1 (RFP Completion)**
- ✅ Audit tool tested on 50 dealers
- ✅ RFP response submitted (Dec 1, 2025)
- ✅ Technical credibility demonstrated

### **Month 1 (If RFP Won)**
- ✅ Team can explain architecture to FordDirect
- ✅ Audit tool ready for production use
- ✅ Templates ready for FordDirect implementation

---

## 🚀 Post-Workshop: Path to Production

**Phase 1**: RFP Response (Week 1-2)
- Polish audit tool
- Complete RFP deliverables
- Prepare pitch deck

**Phase 2**: If RFP Won (Month 1-3)
- Productionize audit tool
- Integrate with FordDirect data feeds
- Deploy dashboard for FordDirect stakeholders

**Phase 3**: Ongoing (Month 3+)
- Quarterly audits
- Expand to all 3,200 dealers
- Measure impact on traffic/leads

---

## 💡 Key Innovations

### **1. Problem-First Learning**
Start with failure (Zero-Click Challenge), not theory

### **2. Focus on The Trap**
50% of time on measurement (the RFP differentiator)

### **3. Hybrid Tech Stack**
Python for speed, TypeScript for UI (not Java-centric)

### **4. Real Deliverables**
Working code that goes in RFP, not just learning exercises

### **5. Squad Specialization**
Not everyone learns everything (efficient upskilling)

---

## 📞 Next Steps

### **To Launch This Workshop:**

1. **Create Squad Briefs** (1-pagers for each team)
2. **Scaffold Backend** (FastAPI project structure)
3. **Scaffold Frontend** (Next.js dashboard)
4. **Create Exercise 0** (Zero-Click Challenge guide)
5. **Create Exercise 3** (Measurement framework starter code)
6. **Seed Sample Data** (50 dealers, 100 vehicles)

**Estimated Setup Time**: 8-12 hours (with AI assistance)

**Workshop Delivery Time**: 5 days (40 hours of participant time)

**RFP Impact**: High technical credibility, working prototype, differentiated response

---

**Status**: 🟡 **PLANNED - Ready to Build**

**Next Action**: Create Squad Briefs → Build Exercise 0 → Scaffold backend
