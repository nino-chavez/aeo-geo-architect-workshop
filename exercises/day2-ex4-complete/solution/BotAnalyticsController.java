package com.sap.commerce.workshop.controller;

import com.sap.commerce.workshop.dto.BotAnalytics;
import com.sap.commerce.workshop.service.BotAnalyticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Bot Analytics Controller - SOLUTION for Exercise 4
 *
 * Complete implementation of bot analytics dashboard.
 */
@RestController
@RequestMapping("/api/analytics")
@Slf4j
public class BotAnalyticsController {

    @Autowired
    private BotAnalyticsService analyticsService;

    /**
     * Get bot access analytics.
     *
     * @return Analytics dashboard data
     */
    @GetMapping("/bots")
    public ResponseEntity<BotAnalytics> getBotAnalytics() {
        log.info("Fetching bot analytics");

        BotAnalytics analytics = analyticsService.getAnalytics();

        return ResponseEntity.ok(analytics);
    }
}
