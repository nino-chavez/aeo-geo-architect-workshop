package com.workshop.aeogeo.provider;

/**
 * EmbeddingException
 *
 * Thrown when embedding generation fails.
 *
 * Common causes:
 * - API key invalid or expired
 * - Rate limit exceeded
 * - Network connectivity issues
 * - Provider service outage
 * - Invalid input text (too long, empty, etc.)
 */
public class EmbeddingException extends Exception {

    public EmbeddingException(String message) {
        super(message);
    }

    public EmbeddingException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmbeddingException(Throwable cause) {
        super(cause);
    }
}
