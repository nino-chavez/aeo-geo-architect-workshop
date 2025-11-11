# Troubleshooting Guide

Common issues and solutions for the AEO/GEO Architect Workshop.

## Table of Contents

- [Application Won't Start](#application-wont-start)
- [Database Issues](#database-issues)
- [Embedding Provider Errors](#embedding-provider-errors)
- [Exercise-Specific Issues](#exercise-specific-issues)
- [Docker Problems](#docker-problems)

---

## Application Won't Start

### Port 8080 Already in Use

**Symptom**:
```
Web server failed to start. Port 8080 was already in use.
```

**Solution**:
```bash
# Find process using port 8080
lsof -ti:8080

# Kill the process
lsof -ti:8080 | xargs kill -9

# Or use a different port
export SERVER_PORT=8081
mvn spring-boot:run
```

### Maven Build Fails

**Symptom**:
```
Failed to execute goal on project aeo-geo-workshop
```

**Solution**:
```bash
# Clean and rebuild
mvn clean install -U

# If dependencies are corrupted, clear local repo
rm -rf ~/.m2/repository/com/workshop/aeogeo
mvn clean install
```

### Java Version Mismatch

**Symptom**:
```
class file has wrong version 61.0, should be 52.0
```

**Solution**:
```bash
# Check Java version
java -version

# Should be Java 17 or higher
# Set JAVA_HOME if needed
export JAVA_HOME=/path/to/java17
```

---

## Database Issues

### PostgreSQL Connection Refused

**Symptom**:
```
Connection to localhost:5432 refused
```

**Solution**:
```bash
# Check Docker container is running
docker ps | grep postgres

# If not running, start it
docker-compose up -d

# Check PostgreSQL logs
docker logs workshop-postgres

# Verify connection
docker exec -it workshop-postgres psql -U postgres -d aeo_geo_workshop
```

### Database Schema Not Created

**Symptom**:
```
Table "products" doesn't exist
```

**Solution**:
```bash
# Hibernate should create tables automatically
# Check application.yml has:
spring:
  jpa:
    hibernate:
      ddl-auto: update

# If still failing, manually create schema
docker exec -it workshop-postgres psql -U postgres -d aeo_geo_workshop
# Then run schema.sql if provided
```

### Data Not Seeded

**Symptom**:
```
curl http://localhost:8080/api/products returns empty
```

**Solution**:
```bash
# Check if data.sql exists
ls -la src/main/resources/data.sql

# Verify SQL init mode
grep "sql.init.mode" src/main/resources/application.yml
# Should be: mode: always

# Check logs for SQL execution
mvn spring-boot:run | grep "data.sql"
```

### pgvector Extension Not Installed

**Symptom**:
```
ERROR: type "vector" does not exist
```

**Solution**:
```bash
# Verify pgvector is installed in Docker image
docker exec -it workshop-postgres psql -U postgres -d aeo_geo_workshop -c "CREATE EXTENSION IF NOT EXISTS vector;"

# If using custom PostgreSQL, install pgvector:
# https://github.com/pgvector/pgvector#installation
```

---

## Embedding Provider Errors

### Precomputed: File Not Found

**Symptom**:
```
Failed to load precomputed embeddings
```

**Solution**:
```bash
# Verify file exists
ls -la src/main/resources/embeddings/precomputed.json

# Check application.yml configuration
grep "precomputed.file" src/main/resources/application.yml

# Should be: file: classpath:embeddings/precomputed.json
```

### Azure OpenAI: Authentication Failed

**Symptom**:
```
401 Unauthorized - Access denied due to invalid credentials
```

**Solution**:
```bash
# Verify API key
echo $AZURE_OPENAI_API_KEY

# Check endpoint format
# Correct: https://your-resource.openai.azure.com/
# Wrong: https://your-resource.openai.azure.com (missing trailing slash)

# Test with curl
curl -X POST "https://your-resource.openai.azure.com/openai/deployments/text-embedding-ada-002/embeddings?api-version=2023-05-15" \
  -H "api-key: $AZURE_OPENAI_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{"input": "test"}'
```

### Azure OpenAI: Deployment Not Found

**Symptom**:
```
404 - The API deployment for this resource does not exist
```

**Solution**:
- Verify deployment name matches `AZURE_OPENAI_DEPLOYMENT`
- Check deployment exists in Azure OpenAI Studio
- Ensure deployment is in "Succeeded" state
- Try: `text-embedding-ada-002` (exact name)

### OpenAI: Rate Limit Exceeded

**Symptom**:
```
429 - Rate limit exceeded
```

**Solution**:
```bash
# Wait 60 seconds and retry

# Or reduce request frequency
# Add delay between requests in code:
Thread.sleep(1000); // 1 second delay
```

### Ollama: Connection Refused

**Symptom**:
```
Connection refused: http://localhost:11434
```

**Solution**:
```bash
# Check Ollama container is running
docker ps | grep ollama

# Start Ollama if needed
docker-compose --profile ollama up -d

# Check Ollama logs
docker logs workshop-ollama

# Test Ollama API
curl http://localhost:11434/api/tags
```

### Ollama: Model Not Found

**Symptom**:
```
model 'nomic-embed-text' not found
```

**Solution**:
```bash
# Pull the model
docker exec workshop-ollama ollama pull nomic-embed-text

# Verify model exists
docker exec workshop-ollama ollama list

# Should show: nomic-embed-text
```

---

## Exercise-Specific Issues

### Exercise 1: Schema Validation Fails

**Symptom**:
Google Rich Results Test shows errors

**Solution**:
- Check `@context` is `"https://schema.org"`
- Verify `@type` is `"Product"`
- Ensure `offers` has `@type: "Offer"`
- Check all required fields: `name`, `image`, `offers`

### Exercise 2: FAQ Not Found

**Symptom**:
```
404 - No FAQs found for product
```

**Solution**:
```bash
# Check if product has FAQs
curl http://localhost:8080/api/products/1 | jq '.faqs'

# Verify FAQs are published
# In database:
docker exec -it workshop-postgres psql -U postgres -d aeo_geo_workshop
SELECT * FROM faqs WHERE product_id = 1 AND is_published = true;
```

### Exercise 3: Cosine Similarity Returns 0

**Symptom**:
All products have similarity score of 0.0

**Solution**:
- Check embeddings are loaded (not null)
- Verify pgvector string parsing is correct
- Test cosine similarity with known vectors:
  ```java
  List<Float> a = Arrays.asList(1.0f, 0.0f, 0.0f);
  List<Float> b = Arrays.asList(1.0f, 0.0f, 0.0f);
  double similarity = cosineSimilarity(a, b);
  // Should be 1.0
  ```

### Exercise 3: NullPointerException in Search

**Symptom**:
```
NullPointerException at SemanticSearchService.search
```

**Solution**:
```java
// Add null check for embeddings
if (product.getEmbedding() == null) {
    log.warn("Product {} has no embedding, skipping", product.getId());
    continue;
}
```

### Exercise 4: Bot Not Detected

**Symptom**:
Bot access not logged even with bot User-Agent

**Solution**:
```bash
# Test bot detection
curl -H "User-Agent: ChatGPT-User/1.0" http://localhost:8080/api/products/1/schema

# Check logs
# Should see: "Bot detected: ChatGPT accessing /api/products/1/schema"

# Verify @EnableAsync is on main application class
grep "@EnableAsync" src/main/java/com/workshop/aeogeo/WorkshopApplication.java
```

### Exercise 4: Analytics Dashboard Empty

**Symptom**:
```json
{
  "totalBotAccesses": 0,
  "botsByType": {}
}
```

**Solution**:
```bash
# Check if bot_access_logs table exists
docker exec -it workshop-postgres psql -U postgres -d aeo_geo_workshop
\dt bot_access_logs

# If not, Hibernate should create it
# Check ddl-auto is set to 'update'

# Manually create if needed
CREATE TABLE bot_access_logs (
  id SERIAL PRIMARY KEY,
  bot_type VARCHAR(50),
  product_id BIGINT,
  access_time TIMESTAMP,
  endpoint VARCHAR(255),
  ip_address VARCHAR(50)
);
```

---

## Docker Problems

### Docker Daemon Not Running

**Symptom**:
```
Cannot connect to the Docker daemon
```

**Solution**:
```bash
# Start Docker Desktop (macOS/Windows)
# Or start Docker service (Linux)
sudo systemctl start docker
```

### Container Port Already Allocated

**Symptom**:
```
Bind for 0.0.0.0:5432 failed: port is already allocated
```

**Solution**:
```bash
# Find process using port
lsof -ti:5432

# Kill it
lsof -ti:5432 | xargs kill -9

# Or change port in docker-compose.yml
ports:
  - "5433:5432" # Use 5433 on host
```

### Out of Disk Space

**Symptom**:
```
no space left on device
```

**Solution**:
```bash
# Clean Docker resources
docker system prune -a

# Remove unused volumes
docker volume prune

# Check disk space
df -h
```

---

## Still Need Help?

### Check Logs

```bash
# Application logs
mvn spring-boot:run

# Docker logs
docker logs workshop-postgres
docker logs workshop-ollama

# Verbose logging
export LOGGING_LEVEL_COM_WORKSHOP_AEOGEO=DEBUG
mvn spring-boot:run
```

### Verify Configuration

```bash
# Show active profile
echo $SPRING_PROFILES_ACTIVE

# Show environment variables
env | grep -E "(EMBEDDING|AZURE|OPENAI|OLLAMA)"

# Test database connection
docker exec -it workshop-postgres psql -U postgres -d aeo_geo_workshop -c "SELECT COUNT(*) FROM products;"
```

### Reset Everything

```bash
# Nuclear option: start fresh
docker-compose down -v
mvn clean
rm -rf target/
docker-compose up -d
mvn clean install
mvn spring-boot:run
```

### Get Support

- Review exercise README for specific guidance
- Check hint files in `exercises/*/hints/`
- Compare with solution code in `exercises/*/solution/`
- Ask instructor or workshop facilitator

---

**Last Updated**: January 2025
