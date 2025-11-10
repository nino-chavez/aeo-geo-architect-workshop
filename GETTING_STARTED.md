# Getting Started - AEO/GEO Workshop

## 🚀 5-Minute Quick Start

```bash
# 1. Navigate to project
cd ~/Workspace/02-local-dev/wip/aeo-geo-architect-workshop

# 2. Start database
docker-compose up -d

# 3. Run the application
./mvnw spring-boot:run

# 4. Test (in another terminal)
curl http://localhost:8080/health
```

**✅ If you see "OK"**, you're ready to start the workshop!

---

## 📂 What's in This Project?

```
aeo-geo-architect-workshop/
├── README.md                  # Full workshop documentation
├── PROJECT_SUMMARY.md         # Complete project overview
├── GETTING_STARTED.md         # This file
├── docker-compose.yml         # Infrastructure (Postgres + pgvector)
├── switch-provider.sh         # Switch embedding providers
├── pom.xml                    # Maven dependencies
│
├── src/main/java/             # Application code
│   ├── model/                 # SAP Commerce-inspired models
│   ├── provider/              # 5 embedding providers
│   ├── schema/                # YOU BUILD THIS in exercises
│   └── service/               # YOU BUILD THIS in exercises
│
└── exercises/                 # Incremental learning guides
    ├── day1-exercise1-product-schema/
    │   ├── README.md          # Start here!
    │   └── HINTS.md           # Progressive hints
    └── (more exercises to come)
```

---

## 🎓 Your Learning Path

### **Day 1: AEO (Answer Engine Optimization)**
**Goal**: Make your product catalog machine-readable for Google and AI

**Start Here**:
```bash
cd exercises/day1-exercise1-product-schema
cat README.md
```

**Time**: 3 hours
- Phase 0 (15 min): GitHub Copilot intro
- Exercise 1 (60 min): Product Schema
- Exercise 2 (30 min): FAQ Schema [TO BE ADDED]
- Validation (15 min): Google Rich Results Test

### **Day 2: GEO (Generative Engine Optimization)** [TO BE ADDED]
**Goal**: Build a RAG pipeline for semantic search

**Time**: 3 hours
- Concepts (30 min): Vector embeddings
- Exercise 3 (90 min): RAG with pgvector
- Exercise 4 (30 min): Monitoring

---

## 🔧 System Requirements

### **Required**
- ✅ Java 17+ (`java -version`)
- ✅ Docker Desktop (`docker --version`)
- ✅ Git (`git --version`)
- ✅ 8GB RAM minimum
- ✅ 5GB free disk space

### **Recommended**
- 💡 IntelliJ IDEA or VS Code
- 💡 GitHub Copilot (enterprise license)
- 💡 Postgres client (optional): DBeaver, pgAdmin

### **No API Keys Required**
The workshop uses **pre-computed embeddings** by default. You can optionally add:
- Azure OpenAI (if your org has it)
- Vertex AI (if your org has GCP)
- OpenAI API key (get free one at platform.openai.com)

---

## 🧪 Testing Your Setup

### **Test 1: Java Version**
```bash
java -version
# Should show: openjdk version "17" or higher
```

### **Test 2: Docker Running**
```bash
docker ps
# Should NOT show "Cannot connect to Docker daemon"
```

### **Test 3: Database Started**
```bash
docker-compose ps
# Should show: aeo-geo-postgres (healthy)
```

### **Test 4: Application Health**
```bash
curl http://localhost:8080/health
# Should return: {"status":"UP"}
```

### **Test 5: Provider Status**
```bash
curl http://localhost:8080/api/health/providers
# Should show: "Pre-computed": {"healthy": true}
```

---

## 🐛 Common Issues

### **"Port 8080 already in use"**
```bash
# Find what's using it
lsof -i :8080

# Kill it, or change port in src/main/resources/application.yml:
server:
  port: 8081
```

### **"Docker daemon not running"**
- Open Docker Desktop
- Wait for it to fully start (whale icon stops animating)
- Try again: `docker-compose up -d`

### **"Permission denied: ./mvnw"**
```bash
chmod +x mvnw
./mvnw spring-boot:run
```

### **"Maven build fails"**
```bash
# Clean and rebuild
./mvnw clean install -DskipTests
```

---

## 📚 Learning Resources

### **Before You Start**
- Skim: [Schema.org Product](https://schema.org/Product) (don't read in detail)
- Bookmark: [JSON-LD Playground](https://json-ld.org/playground/)
- Bookmark: [Google Rich Results Test](https://search.google.com/test/rich-results)

### **SAP Commerce Context**
- This workshop uses data models inspired by SAP Commerce Cloud
- If you know SAP Commerce: You'll recognize ProductModel, PriceRow, etc.
- If you don't: No problem! The models are well-documented

### **AI Tooling**
- Have GitHub Copilot? Great! The exercises include Copilot prompts
- Don't have Copilot? No problem! The hints are comprehensive

---

## 🎯 Your First Exercise

### **Ready to start?** Follow these steps:

```bash
# 1. Open the exercise guide
cd exercises/day1-exercise1-product-schema
cat README.md

# 2. Open your IDE
idea .   # IntelliJ
code .   # VS Code

# 3. Read the problem statement
# 4. Try to solve it yourself
# 5. Stuck? Read HINTS.md (one hint at a time)
# 6. Validate: ./verify.sh
```

### **Expected Timeline**
- Read problem: 10 min
- Build solution: 40 min
- Test & validate: 10 min
- **Total**: 60 min

---

## 💡 Learning Tips

### **1. Try Before Hinting**
Don't rush to the hints! Struggling is part of learning.

### **2. Use AI Tools**
If you have GitHub Copilot, ask it to generate POJOs and explain errors.

### **3. Validate Often**
Run `curl http://localhost:8080/api/products/...` frequently to test your work.

### **4. Read Error Messages**
Spring Boot errors are verbose but helpful. Read them!

### **5. Pair Program**
If doing this with others, pair up. Explain your code to each other.

---

## 🎓 After the Workshop

### **Apply What You Learned**
1. **Week 1**: Add Schema.org to 3 products in your real catalog
2. **Week 2**: Stand up pgvector with your real data
3. **Month 1**: Measure impact in Google Search Console

### **Share Your Results**
- Post in #aeo-geo-champions Slack
- Show before/after screenshots
- Share lessons learned

### **Go Deeper**
- Implement Vertex AI provider (advanced exercise)
- Build a full RAG system with your docs
- Integrate with real SAP Commerce Cloud

---

## 📞 Get Help

### **During Workshop**
- 🙋 Ask the instructor
- 🤝 Pair with another participant
- 💻 Use GitHub Copilot to debug

### **Self-Study**
- 📖 Read HINTS.md (progressive hints)
- 🔍 Check docs/TROUBLESHOOTING.md
- 🧪 Compare with SOLUTION.md (if stuck)

### **After Workshop**
- 💬 Post in #aeo-geo-champions Slack
- 📧 Email the workshop organizer
- 🐛 Open an issue on GitHub

---

## ✅ Checklist Before Starting

- [ ] Java 17+ installed
- [ ] Docker Desktop running
- [ ] Project cloned
- [ ] `docker-compose up -d` succeeded
- [ ] `./mvnw spring-boot:run` succeeded
- [ ] `curl http://localhost:8080/health` returns OK
- [ ] GitHub Copilot active (if available)
- [ ] IDE opened to project root
- [ ] Read `exercises/day1-exercise1-product-schema/README.md`

**All checked?** You're ready! 🚀

---

## 🎉 Let's Begin!

```bash
cd exercises/day1-exercise1-product-schema
cat README.md
```

**Good luck, architect!** 💪
