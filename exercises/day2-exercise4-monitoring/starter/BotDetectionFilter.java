package com.workshop.aeogeo.filter;

import com.workshop.aeogeo.service.BotLoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Servlet Filter for detecting AI bot traffic.
 *
 * Exercise 4: Implement bot detection filter to identify and log AI bot access.
 *
 * Your task: Implement the doFilter() method to detect AI bots (ChatGPT, Perplexity, etc.)
 * by analyzing User-Agent headers and log their access for GEO analytics.
 */
@Component
@Order(1)
@Slf4j
public class BotDetectionFilter implements Filter {

    @Autowired
    private BotLoggingService botLoggingService;

    /**
     * Map of bot names to regex patterns for detection.
     */
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

        // TODO: Step 1 - Get the User-Agent header
        // String userAgent = httpRequest.getHeader("User-Agent");

        // TODO: Step 2 - Check if it matches any bot pattern
        // if (userAgent != null) {
        //     String botType = identifyBot(userAgent);
        //
        //     if (botType != null) {
        //         // Bot detected!
        //         log.info("Bot detected: {} accessing {}", botType, httpRequest.getRequestURI());
        //
        //         // Extract product ID if accessing a product endpoint
        //         Long productId = extractProductId(httpRequest);
        //
        //         // Log the bot access (asynchronously)
        //         botLoggingService.logBotAccess(
        //             botType,
        //             productId,
        //             httpRequest.getRequestURI(),
        //             httpRequest.getRemoteAddr()
        //         );
        //     }
        // }

        // TODO: Step 3 - Continue the filter chain (let request proceed)
        // chain.doFilter(request, response);

        // REMOVE THIS LINE when you implement the method:
        chain.doFilter(request, response);
    }

    /**
     * Identify which bot (if any) is making the request.
     *
     * @param userAgent The User-Agent header value
     * @return Bot name if identified, null otherwise
     */
    private String identifyBot(String userAgent) {
        // TODO: Iterate through BOT_PATTERNS and match against userAgent
        //
        // for (Map.Entry<String, Pattern> entry : BOT_PATTERNS.entrySet()) {
        //     if (entry.getValue().matcher(userAgent).find()) {
        //         return entry.getKey(); // Found a match!
        //     }
        // }
        // return null; // No bot detected

        return null; // REMOVE THIS when implemented
    }

    /**
     * Extract product ID from URL if present.
     *
     * Example: /api/products/123/schema → 123
     *
     * @param request HTTP request
     * @return Product ID if found, null otherwise
     */
    private Long extractProductId(HttpServletRequest request) {
        // TODO: Parse the request URI to extract product ID
        //
        // String requestUri = request.getRequestURI();
        //
        // if (requestUri.contains("/api/products/")) {
        //     String[] parts = requestUri.split("/");
        //
        //     for (int i = 0; i < parts.length; i++) {
        //         if (parts[i].equals("products") && i + 1 < parts.length) {
        //             try {
        //                 return Long.parseLong(parts[i + 1]);
        //             } catch (NumberFormatException e) {
        //                 return null; // Not a valid number
        //             }
        //         }
        //     }
        // }
        // return null; // No product ID found

        return null; // REMOVE THIS when implemented
    }
}
