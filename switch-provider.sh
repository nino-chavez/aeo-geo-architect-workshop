#!/bin/bash

# Provider Switcher Script
# Easily switch between embedding providers

echo "========================================="
echo " AEO/GEO Workshop - Provider Switcher"
echo "========================================="
echo ""
echo "Select embedding provider:"
echo "1) Pre-computed (no API, offline) [DEFAULT]"
echo "2) Azure OpenAI (enterprise)"
echo "3) Google Vertex AI (GCP)"
echo "4) OpenAI (BYOK - Bring Your Own Key)"
echo "5) Ollama (fully local)"
echo ""

read -p "Choice [1-5]: " choice

case $choice in
  1)
    cp .env.precomputed .env
    echo ""
    echo "✓ Switched to pre-computed embeddings (offline mode)"
    echo ""
    echo "No configuration needed. Just run:"
    echo "  ./mvnw spring-boot:run"
    ;;
  2)
    cp .env.azure .env
    echo ""
    echo "✓ Switched to Azure OpenAI"
    echo ""
    echo "⚠️  IMPORTANT: Edit .env and add:"
    echo "  AZURE_OPENAI_ENDPOINT=https://your-org.openai.azure.com/"
    echo "  AZURE_OPENAI_API_KEY=your-key"
    echo ""
    echo "Then run:"
    echo "  ./mvnw spring-boot:run -Dspring-boot.run.profiles=azure-openai"
    ;;
  3)
    cp .env.gcp .env
    echo ""
    echo "✓ Switched to Google Vertex AI"
    echo ""
    echo "⚠️  IMPORTANT: First authenticate:"
    echo "  gcloud auth application-default login"
    echo ""
    echo "Then edit .env and add:"
    echo "  GCP_PROJECT_ID=your-project-id"
    echo ""
    echo "Then run:"
    echo "  ./mvnw spring-boot:run -Dspring-boot.run.profiles=vertex-ai"
    ;;
  4)
    cp .env.openai .env
    echo ""
    echo "✓ Switched to OpenAI (BYOK)"
    echo ""
    echo "⚠️  IMPORTANT: Edit .env and add:"
    echo "  OPENAI_API_KEY=sk-proj-xxxxx"
    echo ""
    echo "Get your key at: https://platform.openai.com/api-keys"
    echo "Cost: ~$0.02 for the entire workshop"
    echo ""
    echo "Then run:"
    echo "  ./mvnw spring-boot:run -Dspring-boot.run.profiles=openai"
    ;;
  5)
    cp .env.ollama .env
    echo ""
    echo "✓ Switched to Ollama (local)"
    echo ""
    echo "Starting Ollama and pulling model..."
    docker-compose --profile ollama up -d
    sleep 3
    docker exec aeo-geo-ollama ollama pull nomic-embed-text
    echo ""
    echo "✓ Ollama ready"
    echo ""
    echo "Run:"
    echo "  ./mvnw spring-boot:run -Dspring-boot.run.profiles=ollama"
    ;;
  *)
    echo ""
    echo "❌ Invalid choice"
    exit 1
    ;;
esac

echo ""
echo "========================================="
echo " Next Steps"
echo "========================================="
echo "1. If you made configuration changes, edit .env"
echo "2. Restart your app to apply changes"
echo "3. Check health: curl http://localhost:8080/api/health/providers"
echo ""
