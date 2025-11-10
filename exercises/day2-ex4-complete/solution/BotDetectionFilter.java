package com.sap.commerce.workshop.filter;

import com.sap.commerce.workshop.model.BotAccessLog;
import com.sap.commerce.workshop.repository.BotAccessLogRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bot Detection Filter - SOLUTION for Exercise 4
 *
 * Complete implementation of bot detection middleware.
 */
@Component
@Order(1)
@Slf4j
public class BotDetectionFilter implements Filter {

    @Autowired
    private BotAccessLogRepository botAccessLogRepository;

    // Comprehensive bot signature patterns
    private static final Map<String, Pattern> BOT_PATTERNS = Map.of(
            "ChatGPT", Pattern.compile("ChatGPT-User", Pattern.CASE_INSENSITIVE),
            "Perplexity", Pattern.compile("PerplexityBot", Pattern.CASE_INSENSITIVE),
            "Claude", Pattern.compile("Claude-Web|anthropic-ai", Pattern.CASE_INSENSITIVE),
            "Googlebot", Pattern.compile("Googlebot", Pattern.CASE_INSENSITIVE),
            "Bingbot", Pattern.compile("bingbot", Pattern.CASE_INSENSITIVE),
            "GPTBot", Pattern.compile("GPTBot", Pattern.CASE_INSENSITIVE)
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String userAgent = httpRequest.getHeader("User-Agent");

        // Identify bot type
        String botType = identifyBot(userAgent);

        // Log bot access if detected
        if (botType != null) {
            log.info("Bot detected: {} accessing {}", botType, httpRequest.getRequestURI());
            logBotAccess(botType, httpRequest);
        }

        // Continue filter chain
        chain.doFilter(request, response);
    }

    /**
     * Identify bot type from User-Agent string.
     *
     * @param userAgent User-Agent header value
     * @return Bot type name if detected, null otherwise
     */
    private String identifyBot(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }

        // Check each bot pattern
        for (Map.Entry<String, Pattern> entry : BOT_PATTERNS.entrySet()) {
            Matcher matcher = entry.getValue().matcher(userAgent);
            if (matcher.find()) {
                return entry.getKey();
            }
        }

        return null; // Not a known bot
    }

    /**
     * Log bot access to database asynchronously.
     *
     * @param botType Bot type identified
     * @param request HTTP request
     */
    @Async
    private void logBotAccess(String botType, HttpServletRequest request) {
        try {
            BotAccessLog log = new BotAccessLog();

            log.setBotType(botType);
            log.setProductId(extractProductId(request.getRequestURI()));
            log.setUserAgent(request.getHeader("User-Agent"));
            log.setIpAddress(request.getRemoteAddr());
            log.setEndpoint(request.getRequestURI());
            log.setHttpMethod(request.getMethod());
            log.setAccessTime(LocalDateTime.now());

            botAccessLogRepository.save(log);

            log.debug("Logged {} bot access to product {} at endpoint {}",
                    botType, log.getProductId(), log.getEndpoint());
        } catch (Exception e) {
            log.error("Failed to log bot access: {}", e.getMessage());
        }
    }

    /**
     * Extract product ID from request URI.
     *
     * @param uri Request URI
     * @return Product ID if found, null otherwise
     */
    private Long extractProductId(String uri) {
        // Pattern: /api/products/{id}/*
        Pattern pattern = Pattern.compile("/products/(\\d+)");
        Matcher matcher = pattern.matcher(uri);

        if (matcher.find()) {
            try {
                return Long.parseLong(matcher.group(1));
            } catch (NumberFormatException e) {
                log.warn("Failed to parse product ID from URI: {}", uri);
            }
        }

        return null;
    }
}
