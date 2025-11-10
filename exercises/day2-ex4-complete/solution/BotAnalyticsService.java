package com.sap.commerce.workshop.service;

import com.sap.commerce.workshop.dto.BotAnalytics;
import com.sap.commerce.workshop.model.BotAccessLog;
import com.sap.commerce.workshop.model.ProductModel;
import com.sap.commerce.workshop.repository.BotAccessLogRepository;
import com.sap.commerce.workshop.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Bot Analytics Service - SOLUTION for Exercise 4
 *
 * Complete implementation of bot analytics aggregation.
 */
@Service
@Slf4j
public class BotAnalyticsService {

    @Autowired
    private BotAccessLogRepository botAccessLogRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get comprehensive bot analytics.
     *
     * @return Bot analytics dashboard data
     */
    public BotAnalytics getAnalytics() {
        BotAnalytics analytics = new BotAnalytics();

        // Total bot accesses
        long totalAccesses = botAccessLogRepository.count();
        analytics.setTotalBotAccesses(totalAccesses);

        // Accesses by bot type
        List<Object[]> botTypeCounts = botAccessLogRepository.countByBotType();
        Map<String, Long> botsByType = botTypeCounts.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));
        analytics.setBotsByType(botsByType);

        // Top products by bot access
        List<Object[]> topProducts = botAccessLogRepository.findTopProductsByBotAccess();
        List<BotAnalytics.ProductAccessCount> productCounts = topProducts.stream()
                .limit(10)
                .map(row -> {
                    Long productId = (Long) row[0];
                    Long count = (Long) row[1];

                    ProductModel product = productRepository.findById(productId).orElse(null);
                    String productName = product != null ? product.getName() : "Unknown";

                    return new BotAnalytics.ProductAccessCount(productId, productName, count);
                })
                .collect(Collectors.toList());
        analytics.setTopProductsByBots(productCounts);

        // Recent bot accesses
        List<BotAccessLog> recentLogs = botAccessLogRepository.findRecentAccesses();
        List<BotAnalytics.RecentBotAccess> recentAccesses = recentLogs.stream()
                .limit(10)
                .map(log -> {
                    String productName = null;
                    if (log.getProductId() != null) {
                        ProductModel product = productRepository.findById(log.getProductId()).orElse(null);
                        productName = product != null ? product.getName() : null;
                    }

                    return new BotAnalytics.RecentBotAccess(
                            log.getBotType(),
                            log.getProductId(),
                            productName,
                            log.getAccessTime().format(DateTimeFormatter.ISO_DATE_TIME),
                            log.getEndpoint()
                    );
                })
                .collect(Collectors.toList());
        analytics.setRecentAccesses(recentAccesses);

        log.info("Generated bot analytics: {} total accesses, {} bot types",
                totalAccesses, botsByType.size());

        return analytics;
    }
}
