# Exercise 4: Bot Detection & GEO Analytics

**Duration:** 60-90 minutes
**Difficulty:** Intermediate
**Topic:** Generative Engine Optimization (GEO) - Bot Identification & Monitoring

## Learning Objectives

By completing this exercise, you will:

1. **Identify AI agents** accessing your product data (ChatGPT, Perplexity, Claude)
2. **Implement bot detection** using User-Agent analysis
3. **Track GEO traffic** separately from human traffic
4. **Build analytics dashboard** for bot access patterns
5. **Understand LLM behavior** when accessing structured data

## Background: Why Bot Detection Matters for GEO

Modern AI search engines and chatbots access e-commerce sites differently than human users:

**Human User:**
```
Browser: Chrome/Safari
User-Agent: Mozilla/5.0...
Behavior: Browse pages, click, scroll
Session: 2-10 minutes
```

**AI Bot:**
```
Bot: ChatGPT
User-Agent: ChatGPT-User/1.0
Behavior: Direct API calls, fetch structured data
Session: <1 second
```

### Known AI Bot Signatures

| Bot | User-Agent Pattern | Purpose |
|-----|-------------------|----------|
| ChatGPT | `ChatGPT-User` | Web browsing feature |
| Perplexity | `PerplexityBot` | Search indexing |
| Claude | `Claude-Web` | Tool use / web search |
| Google | `Googlebot` | Search indexing |
| Bing | `Bingbot` | Search indexing |

### Why Track Bot Traffic?

1. **Understand GEO effectiveness** - Which bots access your data?
2. **Optimize for AI** - What data do bots request most?
3. **Rate limiting** - Prevent abuse from aggressive bots
4. **A/B testing** - Serve different data to bots vs humans
5. **Analytics** - Measure AI-driven traffic separately

## Your Task

Implement bot detection middleware and analytics dashboard.

### Requirements

**Part 1: Bot Detection Filter**
- Intercept all HTTP requests
- Analyze User-Agent header
- Identify known AI bots
- Tag requests with bot type
- Log bot access events

**Part 2: Analytics Storage**
- Create `BotAccessLog` entity
- Store: bot type, product accessed, timestamp, IP
- Repository for querying bot data

**Part 3: Analytics Dashboard**
- Endpoint: `GET /api/analytics/bots`
- Return bot access statistics
- Group by bot type
- Show most accessed products by bots

### Expected Endpoints

**Analytics Dashboard:**
```bash
GET /api/analytics/bots

Response:
{
  "totalBotAccesses": 1247,
  "botsByType": {
    "ChatGPT": 543,
    "Perplexity": 432,
    "Claude": 156,
    "Googlebot": 116
  },
  "topProductsByBots": [
    {"productId": 1, "productName": "Sony A7 IV", "accessCount": 89},
    {"productId": 15, "productName": "Nike Air Max 90", "accessCount": 67}
  ],
  "recentAccesses": [
    {"botType": "ChatGPT", "productId": 1, "timestamp": "2025-01-10T15:30:00Z"}
  ]
}
```

**Test Bot Detection:**
```bash
curl -H "User-Agent: ChatGPT-User/1.0" \
     http://localhost:8080/api/products/1/schema

# Should log this as ChatGPT access
```

## Getting Started

### Step 1: Create BotAccessLog Entity

```java
@Entity
@Table(name = "bot_access_logs")
public class BotAccessLog {
    @Id
    @GeneratedValue
    private Long id;

    private String botType;
    private Long productId;
    private String userAgent;
    private String ipAddress;
    private LocalDateTime accessTime;
    private String endpoint;
}
```

### Step 2: Create Bot Detection Filter

```java
@Component
public class BotDetectionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                        ServletResponse response,
                        FilterChain chain) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String userAgent = httpRequest.getHeader("User-Agent");

        String botType = identifyBot(userAgent);

        if (botType != null) {
            // Log bot access
            logBotAccess(botType, httpRequest);
        }

        chain.doFilter(request, response);
    }

    private String identifyBot(String userAgent) {
        // Check known bot patterns
    }
}
```

### Step 3: Create Analytics Service

```java
@Service
public class BotAnalyticsService {

    public BotAnalytics getAnalytics() {
        // Query bot access logs
        // Aggregate statistics
        // Return formatted analytics
    }
}
```

### Step 4: Create Analytics Controller

```java
@RestController
@RequestMapping("/api/analytics")
public class BotAnalyticsController {

    @GetMapping("/bots")
    public BotAnalytics getBotAnalytics() {
        // Return bot statistics
    }
}
```

## Bot Signature Database

Create a comprehensive list of bot signatures:

```java
private static final Map<String, Pattern> BOT_PATTERNS = Map.of(
    "ChatGPT", Pattern.compile("ChatGPT-User", Pattern.CASE_INSENSITIVE),
    "Perplexity", Pattern.compile("PerplexityBot", Pattern.CASE_INSENSITIVE),
    "Claude", Pattern.compile("Claude-Web|anthropic-ai", Pattern.CASE_INSENSITIVE),
    "Googlebot", Pattern.compile("Googlebot", Pattern.CASE_INSENSITIVE),
    "Bingbot", Pattern.compile("bingbot", Pattern.CASE_INSENSITIVE),
    "GPTBot", Pattern.compile("GPTBot", Pattern.CASE_INSENSITIVE)
);
```

## Testing Your Implementation

### Manual Testing

```bash
# Test with ChatGPT User-Agent
curl -H "User-Agent: ChatGPT-User/1.0" \
     http://localhost:8080/api/products/1/schema

# Test with Perplexity
curl -H "User-Agent: Mozilla/5.0 (compatible; PerplexityBot/1.0)" \
     http://localhost:8080/api/products/1/schema

# Test with Claude
curl -H "User-Agent: Claude-Web/1.0" \
     http://localhost:8080/api/products/1/schema

# Check analytics
curl http://localhost:8080/api/analytics/bots | jq
```

### Validation

Verify:
- [ ] Bot detection filter intercepts all requests
- [ ] Known bots are correctly identified
- [ ] Bot accesses are logged to database
- [ ] Analytics endpoint returns aggregated data
- [ ] Human traffic is not logged as bot traffic
- [ ] Dashboard shows real-time statistics

## Success Criteria

- [ ] Bot detection filter implemented
- [ ] 6+ bot types recognized
- [ ] BotAccessLog entity stores all bot requests
- [ ] Analytics dashboard endpoint working
- [ ] Statistics grouped by bot type
- [ ] Most accessed products tracked
- [ ] Tests passing for bot identification

## Progressive Hints

<details>
<summary>Hint 1: Filter Registration</summary>

Register filter with Spring:

```java
@Component
@Order(1)
public class BotDetectionFilter implements Filter {
    // Filter will run for all requests
}
```
</details>

<details>
<summary>Hint 2: User-Agent Extraction</summary>

```java
HttpServletRequest httpRequest = (HttpServletRequest) request;
String userAgent = httpRequest.getHeader("User-Agent");
```
</details>

<details>
<summary>Hint 3: Pattern Matching</summary>

```java
for (Map.Entry<String, Pattern> entry : BOT_PATTERNS.entrySet()) {
    if (entry.getValue().matcher(userAgent).find()) {
        return entry.getKey(); // Return bot type
    }
}
return null; // Not a bot
```
</details>

<details>
<summary>Hint 4: Extracting Product ID</summary>

Parse product ID from request URI:

```java
String uri = httpRequest.getRequestURI();
// /api/products/123/schema → extract 123
Pattern pattern = Pattern.compile("/products/(\\d+)");
Matcher matcher = pattern.matcher(uri);
if (matcher.find()) {
    return Long.parseLong(matcher.group(1));
}
```
</details>

<details>
<summary>Hint 5: Async Logging</summary>

Don't block requests with database writes:

```java
@Async
public void logBotAccess(BotAccessLog log) {
    repository.save(log);
}
```

Enable with `@EnableAsync` on main class.
</details>

## Resources

- [Common Bot User-Agents](https://www.useragents.me/bots)
- [OpenAI GPTBot](https://platform.openai.com/docs/gptbot)
- [Perplexity Bot](https://docs.perplexity.ai/docs/perplexitybot)
- [Spring Boot Filters](https://www.baeldung.com/spring-boot-add-filter)

## Next Steps

After completing:
1. Test with different bot user-agents
2. Analyze bot access patterns
3. Experiment with rate limiting specific bots
4. Consider serving optimized content to bots

---

**Congratulations!** You've completed the AEO/GEO Workshop! 🎉

You now understand:
- ✅ Schema.org structured data (Day 1)
- ✅ FAQ rich snippets (Day 1)
- ✅ Semantic search with vector embeddings (Day 2)
- ✅ Bot detection and GEO analytics (Day 2)

Your e-commerce platform is now optimized for both traditional search engines AND modern AI agents!
