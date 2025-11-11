# Exercise 4: Bot Detection & GEO Analytics - Progressive Hints

Use these hints progressively if you get stuck. This exercise teaches you how to identify and track AI bot traffic.

---

<details>
<summary><strong>Hint 1: Understanding Bot User-Agents</strong></summary>

AI bots identify themselves in the User-Agent header:
- ChatGPT: `"Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; ChatGPT-User/1.0; +https://openai.com/bot)"`
- Perplexity: `"PerplexityBot/1.0"`
- Claude: `"Claude-Web/1.0"`

You need to detect these patterns in HTTP requests.

</details>

---

<details>
<summary><strong>Hint 2: Using a Servlet Filter</strong></summary>

A servlet filter intercepts all HTTP requests before they reach your controllers:
```java
@Component
@Order(1)
public class BotDetectionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // Detect bot here
        chain.doFilter(request, response); // Continue request
    }
}
```

`@Order(1)` ensures it runs early in the filter chain.

</details>

---

<details>
<summary><strong>Hint 3: Pattern Matching for Bot Detection</strong></summary>

Create a map of bot patterns:
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

Use regex patterns for flexible matching.

</details>

---

<details>
<summary><strong>Hint 4: Extracting User-Agent</strong></summary>

Get the User-Agent from the request:
```java
String userAgent = httpRequest.getHeader("User-Agent");

if (userAgent != null) {
    String botType = identifyBot(userAgent);
    if (botType != null) {
        // Bot detected!
    }
}
```

</details>

---

<details>
<summary><strong>Hint 5: Implementing Bot Identification</strong></summary>

Match the User-Agent against your patterns:
```java
private String identifyBot(String userAgent) {
    for (Map.Entry<String, Pattern> entry : BOT_PATTERNS.entrySet()) {
        if (entry.getValue().matcher(userAgent).find()) {
            return entry.getKey();
        }
    }
    return null; // Not a bot
}
```

</details>

---

<details>
<summary><strong>Hint 6: Extracting Product ID from URL</strong></summary>

If the bot is accessing a product page, extract the product ID:
```java
String requestUri = httpRequest.getRequestURI();

if (requestUri.contains("/api/products/")) {
    String[] parts = requestUri.split("/");
    for (int i = 0; i < parts.length; i++) {
        if (parts[i].equals("products") && i + 1 < parts.length) {
            try {
                return Long.parseLong(parts[i + 1]);
            } catch (NumberFormatException e) {
                // Not a valid product ID
            }
        }
    }
}
```

</details>

---

<details>
<summary><strong>Hint 7: Creating BotAccessLog Entity</strong></summary>

Store bot access information:
```java
@Entity
@Table(name = "bot_access_logs")
public class BotAccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String botType;
    private Long productId;
    private LocalDateTime accessTime;
    private String endpoint;
    private String ipAddress;
}
```

</details>

---

<details>
<summary><strong>Hint 8: Async Logging to Avoid Blocking</strong></summary>

Don't block the request while logging. Use `@Async`:
```java
@Service
public class BotLoggingService {
    @Async
    public void logBotAccess(String botType, Long productId, String endpoint, String ip) {
        BotAccessLog log = new BotAccessLog();
        log.setBotType(botType);
        log.setProductId(productId);
        log.setAccessTime(LocalDateTime.now());
        log.setEndpoint(endpoint);
        log.setIpAddress(ip);

        botAccessLogRepository.save(log);
    }
}
```

Enable async with `@EnableAsync` on your main application class.

</details>

---

<details>
<summary><strong>Hint 9: Building Analytics Dashboard</strong></summary>

Create aggregation queries in your repository:
```java
@Query("SELECT b.botType, COUNT(b) FROM BotAccessLog b GROUP BY b.botType")
List<Object[]> countByBotType();

@Query("SELECT b.productId, COUNT(b) FROM BotAccessLog b WHERE b.productId IS NOT NULL GROUP BY b.productId ORDER BY COUNT(b) DESC")
List<Object[]> findTopProductsByBotAccess();
```

</details>

---

<details>
<summary><strong>Hint 10: Analytics API Response</strong></summary>

Create a comprehensive analytics response:
```java
public class BotAnalytics {
    private long totalBotAccesses;
    private Map<String, Long> botsByType;
    private List<ProductAccessCount> topProductsByBots;
    private List<RecentBotAccess> recentAccesses;

    // Inner classes for structured data
    public static class ProductAccessCount {
        private Long productId;
        private String productName;
        private Long accessCount;
    }

    public static class RecentBotAccess {
        private String botType;
        private Long productId;
        private String productName;
        private String timestamp;
        private String endpoint;
    }
}
```

</details>

---

## Still Stuck?

Check the solution files:
- `solution/BotDetectionFilter.java` - Complete bot detection logic
- `solution/BotAnalyticsService.java` - Analytics aggregation
- `solution/BotAnalyticsController.java` - Dashboard API

Test with:
```bash
# Simulate a ChatGPT bot request
curl -H "User-Agent: ChatGPT-User/1.0" http://localhost:8080/api/products/1/schema

# View analytics
curl http://localhost:8080/api/analytics/bots
```
