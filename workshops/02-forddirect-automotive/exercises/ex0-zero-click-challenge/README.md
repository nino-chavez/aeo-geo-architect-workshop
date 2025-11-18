# Exercise 0: The Zero-Click Challenge
## Project 3200 - FordDirect AI Search Workshop

**Duration**: 3 hours
**Format**: All participants (no squads yet)
**Goal**: Experience the problem firsthand before learning solutions

---

## 🎯 The Challenge

You are a construction business owner who needs to buy an F-150 this weekend. Instead of visiting Ford.com or Googling dealerships, you decide to use **AI search** because you've heard it's faster and more conversational.

**Your mission**:
> Find a Ford dealer near Aurora, CO with a white F-150 Lariat in stock under $55,000 using ONLY AI chat interfaces (ChatGPT, Perplexity, Gemini, Claude).

**Rules**:
- ❌ No traditional Google searches
- ❌ No visiting dealer websites directly
- ❌ No using Ford.com's dealer locator
- ✅ ONLY use conversational AI tools
- ✅ Document everything you find
- ✅ Note when AI gives wrong/incomplete information

---

## 📋 What You'll Discover

**Expected Outcome**: **You will mostly fail.**

But failure is the point. You will learn:
1. **Where AI gets dealer information** (and where it doesn't)
2. **What data is missing** (inventory, hours, services)
3. **When AI hallucinates** (makes up dealer names or details)
4. **Why traditional SEO doesn't help** (LLMs don't "browse" websites)
5. **The measurement problem** (same question = different answers)

This exercise creates urgency: **"We NEED to solve this for FordDirect's 3,200 dealers."**

---

## 🛠️ Setup

### **Tools You'll Need**
1. **ChatGPT** - https://chat.openai.com (free or Plus account)
2. **Perplexity** - https://www.perplexity.ai (free account)
3. **Google Gemini** - https://gemini.google.com (free, Google account)
4. **Claude** - https://claude.ai (free account)

### **Materials Provided**
- `queries.txt` - 20 pre-written test queries
- `baseline-audit-template.csv` - Spreadsheet for recording results
- `DEBRIEF_GUIDE.md` - Discussion questions for group debrief

### **Optional Tools**
- Screen recording software (to capture AI responses)
- Spreadsheet app (Excel, Google Sheets) for audit template

---

## 📝 Instructions

### **Part 1: The Personal Challenge** (30 minutes)

**Step 1**: Open ChatGPT and ask:
```
I need to buy a Ford F-150 for my construction business this weekend.
I'm located near Aurora, Colorado.
Can you help me find a dealer that has a white F-150 Lariat in stock under $55,000?
```

**Step 2**: Document the response:
- Did it mention specific dealers?
- Were dealer names accurate?
- Did it claim to have inventory information?
- Did it say "I can't access real-time data"?
- Did it give generic advice ("Visit Ford.com")?

**Step 3**: Repeat with Perplexity, Gemini, and Claude

**Step 4**: Compare responses:
- Did all 4 AI tools give the same answer?
- If different, which one was most helpful?
- Did any hallucinate (make up dealer names)?

---

### **Part 2: Systematic Testing** (90 minutes)

**Goal**: Test 20 different scenarios to understand patterns

**Process**:
1. Open `queries.txt` (provided)
2. For each query:
   - Run it in ChatGPT
   - Run it in Perplexity
   - Run it in Gemini
   - Record results in `baseline-audit-template.csv`
3. For each response, note:
   - **Dealer Mentioned?** (Yes/No/Generic)
   - **Dealer Name** (if mentioned)
   - **Accurate Info?** (check against reality if possible)
   - **Hallucination?** (made-up details)
   - **Helpful?** (1-5 scale)

**Example Row in Audit Template**:
| Query ID | AI Tool | Dealer Mentioned? | Dealer Name | Accurate? | Hallucination? | Helpfulness (1-5) | Notes |
|----------|---------|-------------------|-------------|-----------|----------------|-------------------|-------|
| Q001 | ChatGPT | Generic | - | N/A | No | 2 | Said "visit Ford.com" |
| Q001 | Perplexity | Yes | Aurora Ford | Unknown | Possibly | 4 | Mentioned dealer but no inventory |
| Q001 | Gemini | No | - | N/A | No | 1 | Refused to help with shopping |
| Q001 | Claude | Generic | - | N/A | No | 3 | Gave general advice about F-150s |

---

### **Part 3: Deep Dive - What's Missing?** (30 minutes)

**For 3 queries where AI failed, investigate**:

**Question 1**: Why couldn't the AI answer?
- No access to real-time inventory?
- Dealer data not in training set?
- Dealerships don't publish structured data?

**Question 2**: Where DOES the AI get its information?
- Try asking: "How do you know about Ford dealers?"
- Try asking: "Where did you get that dealer's phone number?"
- Document its responses

**Question 3**: What data WOULD help?
- If the dealer had a Schema.org markup with inventory, would AI find it?
- If Ford published a dealer API, would AI use it?
- What's the gap between what exists and what AI needs?

---

### **Part 4: Group Debrief** (30 minutes)

**Share findings with the group**:

**Discussion Questions**:
1. What surprised you?
2. How many of you got accurate dealer + inventory information?
3. Did anyone catch the AI hallucinating?
4. Which AI tool performed best? Why?
5. If you were a FordDirect dealer, how would you feel about this?
6. What would you fix first?

**Instructor Notes**:
- Expect frustration ("This doesn't work at all!")
- That's the point - motivate the need for solutions
- Transition: "Now let's learn HOW to fix this..."

---

## 📊 Expected Results

### **ChatGPT (GPT-4)**
- **Typical Response**: "I don't have access to real-time inventory data, but I can help you find Ford dealers near Aurora, CO. Here are some options..."
- **Strengths**: Usually accurate dealer names/addresses from training data
- **Weaknesses**: No inventory, often outdated info, can't verify stock

### **Perplexity**
- **Typical Response**: Searches the web in real-time, may find dealer websites
- **Strengths**: Most likely to find current info (uses live search)
- **Weaknesses**: Still no inventory access, may scrape inaccurate listings

### **Google Gemini**
- **Typical Response**: May link to Google Maps for dealers, but no inventory
- **Strengths**: Integration with Google Maps (accurate addresses)
- **Weaknesses**: Inconsistent responses, sometimes refuses shopping queries

### **Claude (Anthropic)**
- **Typical Response**: Similar to ChatGPT, acknowledges limitations
- **Strengths**: Honest about what it doesn't know
- **Weaknesses**: Conservative (less likely to speculate)

---

## 🎓 Learning Objectives

By the end of this exercise, you should be able to:

- ✅ **Explain the "Zero-Click" problem**: Users want answers without clicking links
- ✅ **Identify data gaps**: What dealer data is missing from LLM training?
- ✅ **Recognize hallucinations**: When AI makes up information
- ✅ **Understand non-determinism**: Same question ≠ same answer
- ✅ **Articulate the business impact**: "3,200 dealers are invisible to AI search"

---

## 📁 Deliverables

1. **Completed Audit Template** (`baseline-audit.csv`)
   - 20 queries × 3-4 AI tools = 60-80 data points
   - Shared with group for analysis

2. **Personal Observations** (written notes)
   - 3-5 key insights
   - Examples of hallucinations or failures
   - Ideas for solutions

3. **Group Debrief Summary** (collaborative doc)
   - Patterns identified
   - Most common failures
   - Hypotheses for why this happens

---

## 🚧 Common Discoveries

### **Discovery 1: "I can't access real-time data"**
**Why**: LLMs are trained on static datasets, not live web scraping (except Perplexity)
**Implication**: Dealers need to get their data into LLM training sets OR use RAG

### **Discovery 2: Dealer names are outdated**
**Why**: Training data is months/years old
**Example**: "Bob's Ford" is now "Bob's Ford-Lincoln" but AI uses old name
**Implication**: Need continuous data updates

### **Discovery 3: AI refuses shopping queries**
**Why**: Some LLMs avoid commercial recommendations to stay neutral
**Example**: Gemini may say "I can't recommend specific dealers"
**Implication**: Need to position as "information" not "ads"

### **Discovery 4: Inventory is NEVER available**
**Why**: Dealer inventory changes hourly, not suitable for static training data
**Solution**: This is where RAG (Retrieval-Augmented Generation) is needed

### **Discovery 5: Hallucinations are common**
**Example**: AI says "Aurora Ford on Main Street" but there's no Main Street location
**Why**: LLM fills gaps with plausible-sounding but false information
**Implication**: Measurement must detect hallucinations

---

## 💡 The "Aha" Moment

**The Insight**: Traditional SEO optimizes for Google's *search results page*. But LLMs provide *answers*, not links.

**Old World** (SEO):
1. User searches "Ford dealer Aurora CO"
2. Google shows 10 blue links
3. User clicks dealer website
4. Dealer gets traffic → leads → sales

**New World** (AEO/GEO):
1. User asks ChatGPT "Find me Ford dealer with F-150 in stock near Aurora"
2. ChatGPT gives answer directly (Zero-Click)
3. **Dealer either appears in answer OR is invisible**
4. No "ranking #2" - you're either cited or not

**The Stakes**: For FordDirect's 3,200 dealers, being invisible = lost sales.

---

## 🔗 Transition to Day 1 Afternoon

After this exercise, you should be asking:
- "How do we get dealer data into LLMs?"
- "How do we measure if we're succeeding?"
- "What's the difference between AEO and SEO?"

**Next**: We'll answer these questions by forming squads and building solutions.

**Squads**:
- **Squad A**: Design the data architecture that makes dealers discoverable
- **Squad B**: Build the measurement system to track visibility
- **Squad C**: Create Schema.org templates that AI can parse

---

## 📖 Resources

### **Background Reading** (optional)
- [Zero-Click Searches on Google](https://sparktoro.com/blog/zero-click-searches/)
- [How ChatGPT Gets Its Information](https://platform.openai.com/docs/guides/gpt)
- [RAG Explained](https://www.promptingguide.ai/techniques/rag)

### **Competitive Analysis**
- Try asking AI about BMW dealers (do they appear better?)
- Try asking about Tesla stores (different model - owned by Tesla)
- What are they doing right?

---

## 🎯 Success Criteria

You've completed this exercise successfully if:
- ✅ You tested at least 10 queries across 3 AI tools
- ✅ You documented at least one hallucination
- ✅ You can explain why LLMs can't access dealer inventory
- ✅ You feel frustrated/motivated ("This needs to be fixed!")

**Remember**: Failure is expected. Frustration is intentional. Motivation is the goal.

---

**Now you know the problem. Let's build the solution. 🚀**
