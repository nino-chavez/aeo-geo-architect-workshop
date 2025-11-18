# Squad B: Measurement Engineers
## Project 3200 - FordDirect AI Search Workshop

**Mission**: Build the audit framework that measures dealer visibility across AI search engines (ChatGPT, Perplexity, Gemini, Claude). This is **RFP Deliverable #2** and the primary differentiator for our response.

---

## 🎯 Your Objective

**Build a repeatable, automated system that answers**:
> "When someone asks ChatGPT for a Ford dealer near Aurora, CO with an F-150 in stock, which dealers appear? How often? How accurately?"

**Why This Matters**:
- FordDirect has 3,200 dealers - manual testing is impossible
- LLMs are non-deterministic (same question = different answers)
- Most consultancies will promise "AI rank tracking" and fail
- You will solve THE TRAP that others miss

---

## 👥 Team Composition

**Squad Size**: 3-4 people

**Required Skills**:
- Python or Node.js (scripting/automation)
- API integration experience
- Comfort with APIs and JSON
- Data analysis mindset

**Nice to Have** (not required):
- LLM experience
- Statistics background
- Dashboard/visualization skills

**NOT Required**:
- Machine learning expertise
- Vector database knowledge
- Frontend development

---

## 📦 What You'll Build

### **Deliverable #1: Audit Query Set**
**File**: `test-queries.json`

**Content**: 20 carefully designed test queries that represent real customer searches:

```json
{
  "queries": [
    {
      "id": "q001",
      "template": "Find me a {vehicle} dealer near {city} with {trim} in stock under ${price}",
      "variables": {
        "vehicle": "Ford F-150",
        "city": "Aurora, CO",
        "trim": "Lariat",
        "price": "55000"
      },
      "expected_dealers": ["dealer_12345", "dealer_67890"],
      "query_type": "inventory_search"
    },
    {
      "id": "q002",
      "template": "Which Ford dealer near {city} has the best service for {vehicle_type}?",
      "variables": {
        "city": "Denver, CO",
        "vehicle_type": "commercial trucks"
      },
      "expected_dealers": [],
      "query_type": "service_search"
    }
  ]
}
```

**Query Types to Cover**:
1. **Inventory Search** (10 queries): "dealer with X vehicle in stock"
2. **Service Search** (5 queries): "dealer with best service"
3. **Comparison** (3 queries): "compare dealers near me"
4. **Specific Feature** (2 queries): "dealer with EV charging stations"

---

### **Deliverable #2: LLM Orchestrator**
**File**: `llm_orchestrator.py`

**Purpose**: Call multiple LLM providers, handle rate limits, log responses

**Features**:
- Support 4 providers:
  - OpenAI (GPT-4)
  - Anthropic (Claude)
  - Google (Gemini)
  - Perplexity (optional)
- Async execution (query all providers in parallel)
- Rate limiting (respect API quotas)
- Error handling (retry logic, fallbacks)
- Response logging (save raw responses for analysis)

**Example Usage**:
```python
from llm_orchestrator import LLMOrchestrator

orchestrator = LLMOrchestrator()

query = "Find me a Ford dealer near Aurora, CO with F-150 Lariat in stock"
responses = await orchestrator.query_all(query)

# responses = {
#   "openai": "I found 3 dealers near Aurora...",
#   "anthropic": "Here are the closest Ford dealers...",
#   "google": "Based on my search...",
# }
```

---

### **Deliverable #3: Response Scorer**
**File**: `scorer.py`

**Purpose**: Parse LLM responses and calculate visibility scores

**Scoring Algorithm**:

```python
def score_response(response: str, expected_dealers: List[str]) -> dict:
    """
    Returns:
    {
        "visibility_score": 0-100,
        "dealers_mentioned": ["dealer_12345"],
        "accuracy_score": 0-100,
        "citation_type": "direct|indirect|none"
    }
    """
```

**Scoring Rubric**:

| Score | Criteria |
|-------|----------|
| **100** | Dealer mentioned by name with accurate details (address, phone, inventory) |
| **75** | Dealer mentioned by name with generic info |
| **50** | Dealer location mentioned but not by name ("dealers in Aurora") |
| **25** | City mentioned but no specific dealers |
| **0** | No relevant information |

**Additional Metrics**:
- **Share of Citation**: % of queries where dealer appears (across all LLMs)
- **Accuracy Score**: How correct is the information? (check against ground truth)
- **Hallucination Rate**: % of queries with fabricated information

---

### **Deliverable #4: Audit Dashboard**
**File**: `dashboard.py` (Streamlit)

**Purpose**: Visualize audit results for FordDirect stakeholders

**Pages**:

1. **Overview**
   - Total dealers audited
   - Average visibility score
   - Share of citation (network-wide)
   - Trend over time (if multiple audits)

2. **Dealer Detail**
   - Individual dealer visibility score
   - Queries where dealer appeared
   - Comparison to regional average
   - Recommendations (what's missing?)

3. **Query Analysis**
   - Which queries have highest/lowest dealer coverage?
   - LLM comparison (which provider shows dealers most often?)
   - Hallucination examples

4. **Export**
   - Download CSV report
   - Generate PDF summary for executives

**Tech Stack**:
- Streamlit (rapid prototyping)
- Pandas (data analysis)
- Plotly (interactive charts)
- Optional: Export to Next.js frontend later

---

## 🗓️ 5-Day Timeline

### **Day 1: Setup & Research**
**Morning** (9am-12pm):
- Participate in "Zero-Click Challenge" (Exercise 0)
- Document baseline findings

**Afternoon** (1pm-5pm):
- Define the 20 test queries
- Research LLM provider APIs
- Set up Python environment
- Get API keys (OpenAI, Anthropic, Google)

**Deliverable**: `test-queries.json` (draft)

---

### **Day 2: Prototype**
**Morning** (9am-12pm):
- Build first version of `llm_orchestrator.py`
- Test with 3 queries, 1 provider (OpenAI)
- Handle errors and edge cases

**Afternoon** (1pm-5pm):
- Add remaining providers (Anthropic, Google)
- Implement async execution
- Add rate limiting
- Log raw responses to JSON files

**Deliverable**: Working orchestrator (can query 3 LLMs)

---

### **Day 3: Scoring & Analysis** ⭐ CRITICAL
**All Day** (9am-5pm):
- Build `scorer.py`
- Parse LLM responses (NLP/regex to extract dealer names)
- Calculate visibility scores
- Test scoring on 20 queries × 3 LLMs = 60 responses
- Document methodology

**Challenge**: LLM responses are unstructured text. You need to:
- Detect dealer mentions (regex, fuzzy matching)
- Verify accuracy (compare to ground truth data)
- Handle variations ("Bob's Ford" vs "Bob Ford Lincoln")

**Deliverable**: Scoring algorithm + sample results

---

### **Day 4: Dashboard**
**Morning** (9am-12pm):
- Build Streamlit dashboard (basic version)
- Load sample audit results
- Create 3 key charts:
  - Visibility score distribution
  - Share of citation over time
  - LLM comparison (which AI shows dealers most?)

**Afternoon** (1pm-5pm):
- Add dealer detail page
- Add export functionality (CSV, PDF)
- Polish UI

**Deliverable**: Working dashboard (can upload CSV, see visualizations)

---

### **Day 5: RFP Integration**
**Morning** (9am-11am):
- Run full audit on 50 sample dealers
- Generate report
- Prepare demo

**Late Morning** (11am-12pm):
- Present to "FordDirect CIO" (instructor acting as client)
- Live demo of audit tool
- Explain methodology

**Afternoon** (1pm-3pm):
- Write RFP section: "Measurement Methodology"
- Document limitations and future enhancements
- Handoff to Squad A/C for integration

---

## 🛠️ Technical Setup

### **Python Environment**
```bash
cd workshops/02-forddirect-automotive/backend
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate
pip install -r requirements.txt
```

### **Required Libraries**
```
# requirements.txt
openai==1.12.0
anthropic==0.18.0
google-cloud-aiplatform==1.42.0
streamlit==1.31.0
pandas==2.2.0
plotly==5.18.0
aiohttp==3.9.0
python-dotenv==1.0.0
pydantic==2.6.0
```

### **Environment Variables**
```bash
# .env
OPENAI_API_KEY=sk-...
ANTHROPIC_API_KEY=sk-ant-...
GOOGLE_APPLICATION_CREDENTIALS=path/to/vertex-ai-creds.json

# Optional
PERPLEXITY_API_KEY=pplx-...
```

### **Project Structure**
```
exercises/ex3-measurement-framework/
├── solution/
│   ├── audit_queries.py      # Query generator
│   ├── llm_orchestrator.py   # LLM API wrapper
│   ├── scorer.py             # Response parser & scorer
│   ├── dashboard.py          # Streamlit UI
│   ├── requirements.txt
│   └── .env.example
├── starter/
│   └── (TODOs - you fill in the blanks)
└── README.md                 # Exercise instructions
```

---

## 📊 Success Criteria

### **Minimum Viable** (Must Have)
- ✅ 20 test queries defined
- ✅ Can query OpenAI and Anthropic APIs
- ✅ Basic scoring algorithm (even if simple regex)
- ✅ Can generate CSV report with visibility scores

### **Target** (Should Have)
- ✅ All 4 LLM providers working
- ✅ Async execution (fast)
- ✅ Sophisticated scoring (detects hallucinations)
- ✅ Streamlit dashboard (basic UI)

### **Stretch** (Nice to Have)
- ✅ Fuzzy matching for dealer names
- ✅ Trend analysis (compare multiple audits over time)
- ✅ Export to Next.js frontend
- ✅ Automated weekly audit runs (cron job)

---

## 🚧 Common Challenges & Solutions

### **Challenge 1: "LLMs give different answers every time"**
**Solution**: Run each query 3 times, take median score. This is EXPECTED behavior - your methodology accounts for non-determinism.

### **Challenge 2: "How do I detect if a dealer was mentioned?"**
**Solution**:
- Load ground truth dealer names from database
- Use fuzzy matching (Levenshtein distance)
- Consider aliases ("Bob's Ford" = "Bob Ford Lincoln")

### **Challenge 3: "Rate limits are slowing me down"**
**Solution**:
- Use async/await (query providers in parallel)
- Implement exponential backoff
- Cache results (don't re-run same query)

### **Challenge 4: "Scoring feels subjective"**
**Solution**: That's okay! Document your methodology. Consistency > perfection. The RFP cares that you HAVE a methodology, not that it's flawless.

---

## 🤝 Collaboration Points

### **With Squad A (Data Architects)**
- You need: Ground truth dealer data (names, addresses, inventory)
- They need: Your scoring algorithm (for "AI Readiness Score" API)

**Integration Point**: Day 4 - Your scorer becomes a microservice

### **With Squad C (Schema Specialists)**
- You need: Their JSON-LD templates (to understand what data is exposed)
- They need: Your audit results (which dealers are invisible?)

**Integration Point**: Day 4 - Run audit before/after adding Schema.org

---

## 📖 Resources

### **LLM Provider Docs**
- [OpenAI API Reference](https://platform.openai.com/docs/api-reference)
- [Anthropic Claude API](https://docs.anthropic.com/claude/reference/getting-started)
- [Google Vertex AI](https://cloud.google.com/vertex-ai/docs/generative-ai/start/quickstarts/api-quickstart)

### **Tools & Libraries**
- [Streamlit Gallery](https://streamlit.io/gallery) - Dashboard inspiration
- [fuzzywuzzy](https://github.com/seatgeek/fuzzywuzzy) - Fuzzy string matching
- [Pydantic](https://docs.pydantic.dev/) - Data validation

### **Inspiration**
- [Profound](https://www.profound.company/) - Commercial GEO monitoring tool
- [Yext](https://www.yext.com/products/ai-search) - Entity management platform

---

## 🎯 RFP Contribution

**Your Section**: "Measurement Framework" (Deliverable #2)

**What You'll Write** (in RFP):

1. **Methodology**
   - How we measure dealer visibility
   - Why traditional "rank tracking" doesn't work for AI
   - Our "Share of Citation" metric

2. **KPIs**
   - Visibility Score (0-100)
   - Share of Citation (%)
   - Accuracy Score (hallucination detection)

3. **Process**
   - Quarterly audit cadence
   - 20 test queries (examples provided)
   - 4 LLM providers monitored

4. **Tooling**
   - Python audit scripts (open source, FordDirect can run independently)
   - Streamlit dashboard (screenshot included)
   - CSV export for Excel analysis

5. **Limitations & Future Work**
   - LLM non-determinism (run multiple times for confidence)
   - Manual validation needed (spot-check results)
   - Future: Real-time monitoring via LLM APIs

---

## 💡 Key Insight for Your Squad

**Most consultancies will fail this deliverable** because they'll try to build "SEMrush for ChatGPT."

**You will succeed** because you understand:
- LLMs are probabilistic (not deterministic rankings)
- Measurement is about sampling and trends (not precision)
- The methodology matters more than perfect accuracy

**Your competitive advantage**: Working code + honest methodology > beautiful slides with false promises

---

## 🚀 Getting Started (Day 1 AM)

1. Read this brief fully
2. Participate in Exercise 0 (Zero-Click Challenge)
3. Meet with your squad (1 hour)
4. Assign roles:
   - **Lead**: Overall coordination, RFP writing
   - **API Engineer**: LLM orchestrator
   - **Data Analyst**: Scorer + methodology
   - **UI Developer**: Dashboard (if 4 people)
5. Set up Python environment
6. Get API keys from instructor

**First Task**: Define 5 test queries and run them manually in ChatGPT. Document what you observe.

---

**Good luck, Measurement Engineers! You're building the RFP's secret weapon. 🎯**
