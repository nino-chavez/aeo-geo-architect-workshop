# Exercise 3: Measurement Framework
## Build the AI Visibility Audit Tool (RFP Deliverable #2)

**Duration**: Full Day 3 (7 hours)
**Squad**: Squad B (Measurement Engineers)
**Criticality**: ⭐⭐⭐ **THIS IS THE RFP DIFFERENTIATOR**

---

## 🎯 Objective

Build an automated system that measures how often Ford/Lincoln dealers appear in AI search engine responses. This solves "THE TRAP" - measuring visibility in non-deterministic LLMs.

**What You're Building**:
1. **Test query generator** - 20 automotive search queries
2. **LLM orchestrator** - Query OpenAI, Anthropic, Google, Perplexity
3. **Response scorer** - Parse LLM responses, calculate visibility (0-100)
4. **Dashboard** - Visualize results (Streamlit)

**Success Criteria**:
- ✅ Working Python tool that audits 50 dealers in < 5 minutes
- ✅ Visibility scores (0-100) for each dealer
- ✅ "Share of Citation" metric (% of queries where dealer appears)
- ✅ Hallucination detection
- ✅ Repeatable quarterly (save results, track trends)

---

## 📋 The 4 Components

### **Component 1: `audit_queries.py`**
**Purpose**: Generate test queries from templates

**Input**: `test-queries.json` (20 query templates)
**Output**: 100+ expanded queries (with variables substituted)

**Example**:
```python
Template: "Find me a {brand} dealer near {city} with {model} in stock"
Variables: brand=[Ford, Lincoln], city=[Aurora, Denver], model=[F-150, Navigator]
Output: 8 queries (2 brands × 2 cities × 2 models)
```

---

### **Component 2: `llm_orchestrator.py`**
**Purpose**: Query multiple LLM providers in parallel

**Supports**:
- OpenAI (GPT-4)
- Anthropic (Claude)
- Google (Gemini)
- Perplexity (optional)

**Features**:
- Async execution (query all providers simultaneously)
- Rate limiting (respect API quotas)
- Error handling (retry logic, fallbacks)
- Response logging (save raw responses for analysis)

---

### **Component 3: `scorer.py`**
**Purpose**: Parse LLM responses and calculate visibility scores

**Scoring Algorithm**:
```
Visibility Score (0-100) = weighted average of:
- Dealer mentioned by name (40 points)
- Accurate details provided (30 points)
- Inventory mentioned (20 points)
- Call-to-action included (10 points)

Adjustments:
- Hallucination detected: -50 points
- Generic response ("Visit Ford.com"): 0 points
```

**Additional Metrics**:
- **Share of Citation**: % of queries where dealer appears (across all LLMs)
- **Accuracy Score**: How correct is the information?
- **Hallucination Rate**: % of responses with fabricated information

---

### **Component 4: `dashboard.py`**
**Purpose**: Streamlit dashboard for visualizing results

**Pages**:
1. **Overview** - Aggregate metrics (avg visibility, total dealers audited)
2. **Dealer Detail** - Individual dealer scores, recommendations
3. **Query Analysis** - Which queries perform best/worst?
4. **LLM Comparison** - Which AI tool shows dealers most often?
5. **Export** - Download CSV/PDF report

---

## 🛠️ Technical Stack

### **Required Libraries**
```bash
pip install openai anthropic google-cloud-aiplatform streamlit pandas plotly aiohttp python-dotenv pydantic
```

### **Environment Variables** (`.env`)
```bash
OPENAI_API_KEY=sk-...
ANTHROPIC_API_KEY=sk-ant-...
GOOGLE_APPLICATION_CREDENTIALS=path/to/vertex-creds.json
PERPLEXITY_API_KEY=pplx-...  # optional
```

---

## 📝 Starter Code (TODOs)

### **File 1: `starter/audit_queries.py`**

```python
"""
Generate test queries for dealer visibility audit
"""

import json
from typing import List, Dict
from itertools import product

def load_query_templates(file_path: str = "test-queries.json") -> List[Dict]:
    """
    Load query templates from JSON file
    TODO: Implement JSON loading
    """
    pass

def expand_query(template: str, variables: Dict[str, List[str]]) -> List[str]:
    """
    Expand a template query with all variable combinations

    Example:
        template = "Find a {brand} dealer near {city}"
        variables = {"brand": ["Ford", "Lincoln"], "city": ["Aurora", "Denver"]}
        returns = [
            "Find a Ford dealer near Aurora",
            "Find a Ford dealer near Denver",
            "Find a Lincoln dealer near Aurora",
            "Find a Lincoln dealer near Denver"
        ]

    TODO: Implement template expansion using itertools.product
    """
    pass

def generate_all_queries() -> List[Dict]:
    """
    Generate all test queries from templates
    TODO: Load templates, expand each, return list of queries with metadata
    """
    pass

if __name__ == "__main__":
    queries = generate_all_queries()
    print(f"Generated {len(queries)} test queries")
    for q in queries[:5]:
        print(f"  - {q['text']}")
```

### **File 2: `starter/llm_orchestrator.py`**

```python
"""
Orchestrate queries to multiple LLM providers
"""

import asyncio
import aiohttp
from typing import List, Dict
from openai import AsyncOpenAI
from anthropic import AsyncAnthropic

class LLMOrchestrator:
    def __init__(self):
        """
        TODO: Initialize API clients for OpenAI, Anthropic, Google
        """
        pass

    async def query_openai(self, prompt: str) -> str:
        """
        TODO: Query OpenAI GPT-4
        """
        pass

    async def query_anthropic(self, prompt: str) -> str:
        """
        TODO: Query Anthropic Claude
        """
        pass

    async def query_google(self, prompt: str) -> str:
        """
        TODO: Query Google Gemini via Vertex AI
        """
        pass

    async def query_all(self, prompt: str) -> Dict[str, str]:
        """
        Query all LLM providers in parallel
        TODO: Use asyncio.gather to query all providers simultaneously
        Returns: {"openai": "response...", "anthropic": "response...", "google": "response..."}
        """
        pass

    async def audit_dealers(self, queries: List[str]) -> List[Dict]:
        """
        Run full audit: query all LLMs for all queries
        TODO: Iterate through queries, call query_all for each, save results
        """
        pass

if __name__ == "__main__":
    orchestrator = LLMOrchestrator()
    test_query = "Find me a Ford dealer near Aurora, CO with F-150 in stock"
    results = asyncio.run(orchestrator.query_all(test_query))
    print(results)
```

### **File 3: `starter/scorer.py`**

```python
"""
Score LLM responses for dealer visibility
"""

import re
from typing import List, Dict

class VisibilityScorer:
    def __init__(self, dealers: List[Dict]):
        """
        Initialize with ground truth dealer data
        TODO: Load dealer names, addresses, codes
        """
        self.dealers = dealers

    def detect_dealer_mention(self, response: str) -> List[str]:
        """
        Detect which dealers are mentioned in the response
        TODO: Use fuzzy string matching to find dealer names
        Hint: Use fuzzywuzzy or regex
        """
        pass

    def check_accuracy(self, dealer_id: str, response: str) -> bool:
        """
        Check if dealer info in response is accurate
        TODO: Verify address, phone, hours mentioned are correct
        """
        pass

    def detect_hallucination(self, response: str) -> bool:
        """
        Detect if LLM fabricated information
        TODO: Check for dealer names that don't exist in our database
        """
        pass

    def calculate_score(self, response: str, expected_dealer_id: str) -> Dict:
        """
        Calculate visibility score (0-100) for a response

        Scoring:
        - Dealer mentioned by name: 40 points
        - Accurate details: 30 points
        - Inventory mentioned: 20 points
        - Call-to-action: 10 points
        - Hallucination: -50 points

        TODO: Implement scoring logic
        Returns: {
            "score": 85,
            "dealer_mentioned": True,
            "accurate": True,
            "has_inventory": True,
            "has_cta": True,
            "hallucination": False
        }
        """
        pass

    def calculate_share_of_citation(self, all_results: List[Dict], dealer_id: str) -> float:
        """
        Calculate % of queries where dealer appears
        TODO: Count mentions across all queries, divide by total
        Returns: 0.0 to 1.0 (e.g., 0.35 = dealer appeared in 35% of queries)
        """
        pass

if __name__ == "__main__":
    scorer = VisibilityScorer(dealers=[{"dealer_id": "CO-AUR-001", "name": "Aurora Ford"}])
    test_response = "I found Aurora Ford at 14000 E Iliff Ave with F-150s in stock."
    score = scorer.calculate_score(test_response, "CO-AUR-001")
    print(f"Visibility Score: {score['score']}/100")
```

### **File 4: `starter/dashboard.py`**

```python
"""
Streamlit dashboard for audit results visualization
"""

import streamlit as st
import pandas as pd
import plotly.express as px

def load_audit_results(file_path: str) -> pd.DataFrame:
    """
    TODO: Load audit results from CSV
    """
    pass

def show_overview(df: pd.DataFrame):
    """
    TODO: Show aggregate metrics (avg score, total dealers, etc.)
    """
    pass

def show_dealer_detail(df: pd.DataFrame, dealer_id: str):
    """
    TODO: Show individual dealer performance
    """
    pass

def show_llm_comparison(df: pd.DataFrame):
    """
    TODO: Compare which LLM shows dealers most often
    """
    pass

def main():
    st.title("FordDirect Dealer AI Visibility Audit")

    # TODO: Load data
    # TODO: Sidebar navigation
    # TODO: Render selected page

    pass

if __name__ == "__main__":
    main()
```

---

## ✅ Validation Checklist

- [ ] Can query at least 2 LLM providers (OpenAI + Anthropic minimum)
- [ ] Generates 20+ test queries from templates
- [ ] Scores responses with consistent methodology
- [ ] Detects hallucinations (dealer names that don't exist)
- [ ] Calculates "Share of Citation" metric
- [ ] Dashboard loads and displays results
- [ ] Can export results to CSV
- [ ] Full audit runs in < 5 minutes for 10 dealers

---

## 📖 Resources

**LLM SDKs**:
- [OpenAI Python SDK](https://github.com/openai/openai-python)
- [Anthropic Python SDK](https://github.com/anthropics/anthropic-sdk-python)
- [Google Vertex AI](https://cloud.google.com/vertex-ai/docs/python-sdk/use-vertex-ai-python-sdk)

**Streamlit**:
- [Streamlit Docs](https://docs.streamlit.io/)
- [Gallery Examples](https://streamlit.io/gallery)

---

**This is the RFP differentiator. Build it well. 🎯**
