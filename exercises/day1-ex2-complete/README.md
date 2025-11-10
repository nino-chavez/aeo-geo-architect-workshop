# Exercise 2: FAQ Schema Markup (Schema.org)

**Duration:** 30-45 minutes
**Difficulty:** Beginner
**Topic:** Answer Engine Optimization (AEO) - FAQ Rich Snippets

## Learning Objectives

By completing this exercise, you will:

1. **Implement FAQPage schema** with Question/Answer pairs
2. **Understand how FAQ rich snippets** appear in search results
3. **Learn schema nesting** (FAQPage → Question → Answer)
4. **Practice data transformation** from database models to structured markup

## Background: FAQ Rich Snippets

FAQ (Frequently Asked Questions) structured data helps search engines display your Q&A content directly in search results as expandable rich snippets. This increases visibility and click-through rates.

**Benefits:**
- **Featured snippets** in Google Search
- **Voice assistant answers** - Alexa, Siri, Google Assistant
- **Answer engines** - ChatGPT, Perplexity can cite your FAQs
- **Increased CTR** - FAQ snippets stand out in search results
- **Zero-click answers** - Build brand authority

### FAQPage Schema Structure

```json
{
  "@context": "https://schema.org",
  "@type": "FAQPage",
  "mainEntity": [
    {
      "@type": "Question",
      "name": "What is the battery life?",
      "acceptedAnswer": {
        "@type": "Answer",
        "text": "Approximately 520 shots per charge."
      }
    }
  ]
}
```

## Your Task

Implement a REST endpoint that returns FAQPage schema for any product with FAQs.

### Requirements

**Endpoint:** `GET /api/products/{id}/faq`

**Required Structure:**
- `@context`: `https://schema.org`
- `@type`: `FAQPage`
- `mainEntity`: Array of Question objects

**Each Question must have:**
- `@type`: `Question`
- `name`: The question text
- `acceptedAnswer`: Nested Answer object

**Each Answer must have:**
- `@type`: `Answer`
- `text`: The answer text

### Expected Output

```json
{
  "@context": "https://schema.org",
  "@type": "FAQPage",
  "mainEntity": [
    {
      "@type": "Question",
      "name": "What is the battery life of the Sony A7 IV?",
      "acceptedAnswer": {
        "@type": "Answer",
        "text": "The Sony A7 IV can capture approximately 520 shots per charge..."
      }
    },
    {
      "@type": "Question",
      "name": "Does the Sony A7 IV support dual card slots?",
      "acceptedAnswer": {
        "@type": "Answer",
        "text": "Yes, the Sony A7 IV features dual card slots..."
      }
    }
  ]
}
```

## Getting Started

### Step 1: Create the Controller

Create: `src/main/java/com/sap/commerce/workshop/controller/FAQController.java`

### Step 2: Inject FAQRepository

You'll need to fetch FAQs for the product.

### Step 3: Build the Schema

1. Create base FAQPage structure
2. Fetch published FAQs for the product
3. Transform each FAQ into a Question object
4. Each Question contains an acceptedAnswer Answer object

## Validation

### Manual Testing

```bash
mvn spring-boot:run
curl http://localhost:8080/api/products/1/faq | jq
```

### Google Rich Results Test

1. Copy JSON-LD output
2. Paste into https://search.google.com/test/rich-results
3. Verify "Page is eligible for FAQ rich results"

## Success Criteria

- [ ] Endpoint returns valid FAQPage JSON-LD
- [ ] All published FAQs included
- [ ] Questions properly nested with acceptedAnswer
- [ ] Google Rich Results Test passes
- [ ] Returns 404 if product has no FAQs
- [ ] Code is clean and maintainable

## Progressive Hints

<details>
<summary>Hint 1: Controller Setup</summary>

Create a `@RestController` and inject `FAQRepository`. Use `@GetMapping("/{id}/faq")`.
</details>

<details>
<summary>Hint 2: Fetching FAQs</summary>

Use `faqRepository.findByProductIdAndIsPublishedTrueOrderBySortOrder(productId)` to get published FAQs in order.
</details>

<details>
<summary>Hint 3: FAQPage Structure</summary>

```java
Map<String, Object> faqPage = new LinkedHashMap<>();
faqPage.put("@context", "https://schema.org");
faqPage.put("@type", "FAQPage");
faqPage.put("mainEntity", /* array of questions */);
```
</details>

<details>
<summary>Hint 4: Question Objects</summary>

Each question is a Map with `@type`, `name`, and `acceptedAnswer`. Use stream().map() to transform FAQModel list.
</details>

<details>
<summary>Hint 5: Complete Example</summary>

```java
List<Map<String, Object>> questions = faqs.stream()
    .map(faq -> {
        Map<String, Object> answer = new LinkedHashMap<>();
        answer.put("@type", "Answer");
        answer.put("text", faq.getAnswer());

        Map<String, Object> question = new LinkedHashMap<>();
        question.put("@type", "Question");
        question.put("name", faq.getQuestion());
        question.put("acceptedAnswer", answer);

        return question;
    })
    .collect(Collectors.toList());
```
</details>

## Resources

- [Schema.org FAQPage](https://schema.org/FAQPage)
- [Google FAQ Rich Results Guide](https://developers.google.com/search/docs/appearance/structured-data/faqpage)
- [Schema.org Question](https://schema.org/Question)
- [Schema.org Answer](https://schema.org/Answer)

## Next Steps

After completing:
1. Test with multiple products
2. Compare solution with your implementation
3. **Proceed to Day 2 Exercise 3: RAG Pipeline**

---

**Happy coding!** This exercise builds on Exercise 1 concepts. 🚀
