package com.workshop.aeogeo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AEO/GEO Architect Workshop Application
 *
 * Answer Engine Optimization (AEO) and Generative Engine Optimization (GEO) Workshop
 * for experienced SAP Commerce Cloud architects.
 *
 * This application demonstrates:
 * - Schema.org structured data (JSON-LD) for AEO
 * - Vector embeddings and RAG pipelines for GEO
 * - Multi-provider embedding architecture (Azure OpenAI, Vertex AI, OpenAI, Ollama, Pre-computed)
 * - SAP Commerce-inspired data models (Electronics & Apparel catalogs)
 *
 * Quick Start:
 * 1. docker-compose up -d
 * 2. ./mvnw spring-boot:run
 * 3. curl http://localhost:8080/health
 *
 * @see <a href="README.md">README.md</a> for detailed instructions
 */
@SpringBootApplication
public class AeoGeoWorkshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(AeoGeoWorkshopApplication.class, args);
    }
}
