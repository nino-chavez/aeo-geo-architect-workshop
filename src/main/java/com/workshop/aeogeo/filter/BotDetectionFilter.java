package com.workshop.aeogeo.filter;

import com.workshop.aeogeo.model.BotAccessLog;
import com.workshop.aeogeo.repository.BotAccessLogRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bot Detection Filter - Exercise 4: Bot Detection
 *
 * TODO: Implement bot detection middleware
 *
 * This filter:
 * 1. Intercepts all HTTP requests
 * 2. Analyzes User-Agent header
 * 3. Identifies known AI bots
 * 4. Logs bot access to database
 */
@Component
@Order(1)
@Slf4j
public class BotDetectionFilter implements Filter {

    @Autowired
    private BotAccessLogRepository botAccessLogRepository;

    // TODO: Define bot signature patterns
    // Map bot names to regex patterns for User-Agent matching
    private static final Map<String, Pattern> BOT_PATTERNS = Map.of(
            "ChatGPT", Pattern.compile("ChatGPT-User", Pattern.CASE_INSENSITIVE)
            // TODO: Add more bot patterns (Perplexity, Claude, Googlebot, etc.)
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // TODO: Implement bot detection logic

        // Step 1: Cast to HttpServletRequest
        // HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Step 2: Extract User-Agent header
        // String userAgent = httpRequest.getHeader("User-Agent");

        // Step 3: Identify if this is a bot
        // String botType = identifyBot(userAgent);

        // Step 4: If bot detected, log the access
        // if (botType != null) {
        //     logBotAccess(botType, httpRequest);
        // }

        // Step 5: Continue filter chain
        chain.doFilter(request, response);
    }

    /**
     * Identify bot type from User-Agent string.
     *
     * @param userAgent User-Agent header value
     * @return Bot type name if detected, null otherwise
     */
    private String identifyBot(String userAgent) {
        // TODO: Implement bot identification

        // Check if userAgent is null or empty
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }

        // Loop through BOT_PATTERNS and check if userAgent matches
        // Return the bot type name if match found

        return null; // Replace with actual implementation
    }

    /**
     * Log bot access to database.
     *
     * @param botType Bot type identified
     * @param request HTTP request
     */
    private void logBotAccess(String botType, HttpServletRequest request) {
        // TODO: Implement bot access logging

        // Step 1: Extract product ID from URI (if present)
        // Example: /api/products/123/schema → productId = 123

        // Step 2: Create BotAccessLog object

        // Step 3: Set all fields (botType, productId, userAgent, IP, endpoint, time)

        // Step 4: Save to repository

        // Note: Consider using @Async to avoid blocking requests
    }

    /**
     * Extract product ID from request URI.
     *
     * @param uri Request URI
     * @return Product ID if found, null otherwise
     */
    private Long extractProductId(String uri) {
        // TODO: Implement product ID extraction
        // Pattern: /products/(\\d+)
        // Extract the number and return as Long

        return null; // Replace with actual implementation
    }
}
