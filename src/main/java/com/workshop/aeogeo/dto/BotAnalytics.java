package com.workshop.aeogeo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO for bot analytics dashboard.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotAnalytics {

    private long totalBotAccesses;

    private Map<String, Long> botsByType;

    private List<ProductAccessCount> topProductsByBots;

    private List<RecentBotAccess> recentAccesses;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductAccessCount {
        private Long productId;
        private String productName;
        private Long accessCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentBotAccess {
        private String botType;
        private Long productId;
        private String productName;
        private String timestamp;
        private String endpoint;
    }
}
