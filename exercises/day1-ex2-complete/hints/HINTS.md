# Exercise 2: FAQ Schema - Progressive Hints

Use these hints progressively if you get stuck. Try to solve as much as you can before revealing each hint.

---

<details>
<summary><strong>Hint 1: Understanding FAQPage Structure</strong></summary>

FAQPage schema has a specific structure:
```json
{
  "@context": "https://schema.org",
  "@type": "FAQPage",
  "mainEntity": [
    // Array of Question objects
  ]
}
```

The `mainEntity` contains all your Q&A pairs.

</details>

---

<details>
<summary><strong>Hint 2: Question Object Structure</strong></summary>

Each question in `mainEntity` follows this pattern:
```json
{
  "@type": "Question",
  "name": "What is the battery life?",
  "acceptedAnswer": {
    "@type": "Answer",
    "text": "The battery lasts approximately 520 shots."
  }
}
```

Notice the nested `Answer` object.

</details>

---

<details>
<summary><strong>Hint 3: Fetching Product FAQs</strong></summary>

Your `ProductModel` has a relationship to `FAQModel`:
```java
ProductModel product = productRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Product not found"));

List<FAQModel> faqs = product.getFaqs();
```

Use this to get all FAQs for a product.

</details>

---

<details>
<summary><strong>Hint 4: Filtering Published FAQs</strong></summary>

Not all FAQs should be published. Filter them:
```java
List<FAQModel> publishedFaqs = faqs.stream()
    .filter(FAQModel::getIsPublished)
    .sorted(Comparator.comparing(FAQModel::getSortOrder))
    .collect(Collectors.toList());
```

This ensures only approved, ordered FAQs appear.

</details>

---

<details>
<summary><strong>Hint 5: Mapping FAQ to Question Object</strong></summary>

For each FAQ, create a Question object:
```java
Map<String, Object> question = new LinkedHashMap<>();
question.put("@type", "Question");
question.put("name", faq.getQuestion());

Map<String, Object> answer = new LinkedHashMap<>();
answer.put("@type", "Answer");
answer.put("text", faq.getAnswer());

question.put("acceptedAnswer", answer);
```

Then add it to the mainEntity array.

</details>

---

<details>
<summary><strong>Hint 6: Building the Complete Schema</strong></summary>

Put it all together:
```java
Map<String, Object> schema = new LinkedHashMap<>();
schema.put("@context", "https://schema.org");
schema.put("@type", "FAQPage");

List<Map<String, Object>> questions = new ArrayList<>();
// Add all question objects to this list
schema.put("mainEntity", questions);
```

</details>

---

<details>
<summary><strong>Hint 7: Handling Products with No FAQs</strong></summary>

What if a product has no published FAQs?

Option 1: Return 404
```java
if (publishedFaqs.isEmpty()) {
    return ResponseEntity.notFound().build();
}
```

Option 2: Return empty FAQPage (less ideal)

Choose option 1 for better UX.

</details>

---

<details>
<summary><strong>Hint 8: Controller Endpoint Pattern</strong></summary>

Your endpoint should match the pattern from Exercise 1:
```java
@GetMapping("/{id}/faq")
public ResponseEntity<Map<String, Object>> getProductFAQ(@PathVariable Long id) {
    // Your implementation
}
```

Keep the URL pattern consistent: `/api/products/{id}/faq`

</details>

---

<details>
<summary><strong>Hint 9: Using Streams for Cleaner Code</strong></summary>

You can use Java Streams to map FAQs directly:
```java
List<Map<String, Object>> questions = publishedFaqs.stream()
    .map(faq -> {
        Map<String, Object> question = new LinkedHashMap<>();
        // Build question object
        return question;
    })
    .collect(Collectors.toList());
```

This is more concise than a for-loop.

</details>

---

<details>
<summary><strong>Hint 10: Validation</strong></summary>

Test your implementation:
1. Start application
2. Call: `curl http://localhost:8080/api/products/1/faq`
3. Copy JSON output
4. Validate at: https://search.google.com/test/rich-results

Google should recognize it as a valid FAQPage!

</details>

---

## Still Stuck?

Check the solution in `solution/FAQController.java` for the complete implementation.
