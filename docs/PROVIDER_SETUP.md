# Provider Setup Guide

This guide provides detailed instructions for configuring each embedding provider supported by the workshop.

## Table of Contents

- [Precomputed (Default)](#precomputed-default)
- [Azure OpenAI](#azure-openai)
- [OpenAI (BYOK)](#openai-byok)
- [Ollama (Local)](#ollama-local)
- [Vertex AI (GCP)](#vertex-ai-gcp)

---

## Precomputed (Default)

**✅ Recommended for Workshop Start**

The precomputed provider uses pre-generated embeddings stored in `src/main/resources/embeddings/precomputed.json`. This allows immediate workshop start with zero configuration.

### Setup

No setup required! This is the default provider.

### Configuration

Already configured in `application.yml`:
```yaml
embedding:
  provider: precomputed
  precomputed:
    file: classpath:embeddings/precomputed.json
```

### How It Works

- Loads embeddings from JSON file at startup
- Falls back to deterministic embeddings for unknown queries
- Perfect for offline workshops or environments without internet

### When to Use

- First time through the workshop
- Offline/air-gapped environments
- No API keys available
- Cost-sensitive scenarios

---

## Azure OpenAI

**🏢 Recommended for Enterprise**

Azure OpenAI provides enterprise-grade embedding generation with organizational quota management and security.

### Prerequisites

- Azure subscription
- Azure OpenAI resource created
- `text-embedding-ada-002` deployment

### Setup Steps

1. **Create Azure OpenAI Resource**
   ```bash
   az cognitiveservices account create \
     --name your-openai-resource \
     --resource-group your-rg \
     --kind OpenAI \
     --sku S0 \
     --location eastus
   ```

2. **Deploy Embedding Model**
   - Go to Azure OpenAI Studio
   - Navigate to "Deployments"
   - Click "Create new deployment"
   - Select model: `text-embedding-ada-002`
   - Choose deployment name (e.g., `text-embedding-ada-002`)

3. **Get Credentials**
   - Endpoint: `https://your-resource.openai.azure.com/`
   - API Key: Found in "Keys and Endpoint" section

4. **Configure Environment**
   ```bash
   cp .env.azure .env
   ```

   Edit `.env`:
   ```
   EMBEDDING_PROVIDER=azure-openai
   AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
   AZURE_OPENAI_API_KEY=your-api-key-here
   AZURE_OPENAI_DEPLOYMENT=text-embedding-ada-002
   ```

5. **Run with Azure Profile**
   ```bash
   ./switch-provider.sh
   # Select: Azure OpenAI
   ```

   Or manually:
   ```bash
   export SPRING_PROFILES_ACTIVE=azure-openai
   mvn spring-boot:run
   ```

### Cost

- **Model**: `text-embedding-ada-002`
- **Cost**: ~$0.0001 per 1K tokens
- **Workshop Usage**: ~27 products × 50 tokens = ~$0.001
- **Negligible cost for workshop**

### Troubleshooting

**Error: "Deployment not found"**
- Verify deployment name matches `AZURE_OPENAI_DEPLOYMENT`
- Check deployment is in "Succeeded" state

**Error: "Access denied"**
- Verify API key is correct
- Check Azure RBAC roles (need "Cognitive Services User")

---

## OpenAI (BYOK)

**💳 Bring Your Own Key**

Direct OpenAI API access. Fastest setup for personal accounts.

### Prerequisites

- OpenAI account
- API key from https://platform.openai.com/api-keys

### Setup Steps

1. **Get API Key**
   - Visit: https://platform.openai.com/api-keys
   - Click "Create new secret key"
   - Copy key (starts with `sk-`)

2. **Configure Environment**
   ```bash
   cp .env.openai .env
   ```

   Edit `.env`:
   ```
   EMBEDDING_PROVIDER=openai
   OPENAI_API_KEY=sk-your-api-key-here
   OPENAI_MODEL=text-embedding-3-small
   ```

3. **Run with OpenAI Profile**
   ```bash
   ./switch-provider.sh
   # Select: OpenAI
   ```

### Cost

- **Model**: `text-embedding-3-small`
- **Cost**: ~$0.00002 per 1K tokens
- **Workshop Usage**: ~$0.0002 (less than a penny)

### Troubleshooting

**Error: "Invalid API key"**
- Verify key starts with `sk-`
- Check key hasn't expired
- Ensure billing is set up in OpenAI account

---

## Ollama (Local)

**🏠 100% Local/Offline**

Run embeddings entirely on your local machine with no API calls.

### Prerequisites

- Docker installed
- ~2GB disk space for model

### Setup Steps

1. **Start Ollama via Docker Compose**
   ```bash
   docker-compose --profile ollama up -d
   ```

2. **Pull Embedding Model**
   ```bash
   docker exec workshop-ollama ollama pull nomic-embed-text
   ```

   Wait for download (~750MB)

3. **Verify Model**
   ```bash
   docker exec workshop-ollama ollama list
   ```

   Should show `nomic-embed-text`

4. **Configure Environment**
   ```bash
   cp .env.ollama .env
   ```

   Edit `.env`:
   ```
   EMBEDDING_PROVIDER=ollama
   OLLAMA_ENDPOINT=http://localhost:11434
   OLLAMA_MODEL=nomic-embed-text
   ```

5. **Run with Ollama Profile**
   ```bash
   ./switch-provider.sh
   # Select: Ollama
   ```

### Performance

- **Speed**: ~100ms per embedding (local GPU)
- **Quality**: Comparable to OpenAI `text-embedding-3-small`
- **Privacy**: All data stays local

### Troubleshooting

**Error: "Connection refused"**
```bash
# Check Ollama is running
docker ps | grep ollama

# Check logs
docker logs workshop-ollama
```

**Error: "Model not found"**
```bash
# Pull model again
docker exec workshop-ollama ollama pull nomic-embed-text
```

---

## Vertex AI (GCP)

**☁️ Google Cloud Platform**

*Note: This is a stub implementation left as an advanced exercise.*

### Prerequisites

- GCP project with Vertex AI API enabled
- Service account with `aiplatform.user` role
- Google Cloud SDK installed

### Setup Steps

1. **Enable Vertex AI API**
   ```bash
   gcloud services enable aiplatform.googleapis.com
   ```

2. **Create Service Account**
   ```bash
   gcloud iam service-accounts create vertex-ai-user \
     --display-name="Vertex AI Workshop User"
   ```

3. **Grant Permissions**
   ```bash
   gcloud projects add-iam-policy-binding your-project-id \
     --member="serviceAccount:vertex-ai-user@your-project-id.iam.gserviceaccount.com" \
     --role="roles/aiplatform.user"
   ```

4. **Download Credentials**
   ```bash
   gcloud iam service-accounts keys create vertex-key.json \
     --iam-account=vertex-ai-user@your-project-id.iam.gserviceaccount.com
   ```

5. **Configure Environment**
   ```bash
   cp .env.gcp .env
   ```

   Edit `.env`:
   ```
   EMBEDDING_PROVIDER=vertex-ai
   GCP_PROJECT_ID=your-project-id
   GCP_LOCATION=us-central1
   GOOGLE_APPLICATION_CREDENTIALS=/path/to/vertex-key.json
   ```

6. **Run with Vertex AI Profile**
   ```bash
   ./switch-provider.sh
   # Select: Vertex AI
   ```

### Implementation Notes

The Vertex AI provider is currently a stub. To complete:

1. Add Vertex AI Java SDK dependency to `pom.xml`
2. Implement `VertexAIEmbeddingProvider.java`
3. Use `textembedding-gecko` model

See solution in `exercises/advanced/vertex-ai-implementation/` (if provided).

---

## Switching Providers

### Interactive Script

```bash
./switch-provider.sh
```

This script:
1. Shows available providers
2. Prompts for selection
3. Creates `.env` from template
4. Sets Spring profile

### Manual Switching

1. **Copy environment template**
   ```bash
   cp .env.{provider} .env
   ```

2. **Edit `.env` with credentials**

3. **Set Spring profile**
   ```bash
   export SPRING_PROFILES_ACTIVE={provider-name}
   ```

4. **Run application**
   ```bash
   mvn spring-boot:run
   ```

### Verification

Check which provider is active:
```bash
curl http://localhost:8080/health
```

Look for `embeddingProvider` field.

---

## Provider Comparison

| Provider | Setup Time | Cost | Speed | Offline | Enterprise |
|----------|------------|------|-------|---------|------------|
| **Precomputed** | 0 min | $0 | Instant | ✅ | ❌ |
| **Azure OpenAI** | 15-30 min | ~$0.001 | Fast | ❌ | ✅ |
| **OpenAI** | 1 min | ~$0.0002 | Fast | ❌ | ⚠️ |
| **Ollama** | 10 min | $0 | Medium | ✅ | ⚠️ |
| **Vertex AI** | 30+ min | ~$0.001 | Fast | ❌ | ✅ |

### Recommendations

- **First Run**: Precomputed
- **Personal Learning**: OpenAI
- **Enterprise Workshop**: Azure OpenAI
- **Air-Gapped**: Ollama
- **GCP Shops**: Vertex AI (after implementation)

---

## Need Help?

- Check `docs/TROUBLESHOOTING.md`
- Review provider-specific logs
- Verify API keys and endpoints
- Test connectivity with `curl`

---

**Last Updated**: January 2025
