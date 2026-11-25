package com.smartcity.service;

import com.smartcity.model.EnvironmentalData;
import com.smartcity.model.Resource;
import com.smartcity.model.TrafficData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AnalyticsServiceTest - Verifies rule-based analytics and statistical calculations.
 */
public class AnalyticsServiceTest {

    private AnalyticsService analyticsService;

    @BeforeEach
    public void setUp() {
        analyticsService = new AnalyticsService();
    }

    @Test
    public void testResourceOptimization() {
        List<Resource> resources = new ArrayList<>();
        
        Resource r1 = new Resource();
        r1.setResourceName("Central Power");
        r1.setTotalCapacity(1000);
        r1.setCurrentUsage(950); // 95% - critical load
        r1.setEfficiencyScore(75.0);
        resources.add(r1);

        Resource r2 = new Resource();
        r2.setResourceName("Backup Reservoir");
        r2.setTotalCapacity(2000);
        r2.setCurrentUsage(400); // 20% - underutilized
        r2.setEfficiencyScore(90.0);
        resources.add(r2);

        Map<String, Object> result = analyticsService.calculateResourceOptimization(resources);
        assertEquals(3000.0, result.get("totalCapacity"));
        assertEquals(1350.0, result.get("totalUsage"));

        @SuppressWarnings("unchecked")
        List<String> recommendations = (List<String>) result.get("recommendations");
        assertNotNull(recommendations);
        assertTrue(recommendations.size() >= 2);
    }

    @Test
    public void testTrafficBottleneckAnalysis() {
        List<TrafficData> traffic = new ArrayList<>();
        traffic.add(new TrafficData("Airport Road", "LOW", 60.0, 80));
        traffic.add(new TrafficData("Tech Park Junction", "HIGH", 12.0, 480));
        traffic.add(new TrafficData("City Center", "SEVERE", 6.0, 650));

        List<Map<String, Object>> bottlenecks = analyticsService.analyzeTrafficBottlenecks(traffic);
        assertEquals(2, bottlenecks.size());
        assertEquals("City Center", bottlenecks.get(0).get("location")); // highest vehicle count first
    }

    @Test
    public void testEnvironmentalMetrics() {
        List<EnvironmentalData> envList = new ArrayList<>();
        EnvironmentalData d1 = new EnvironmentalData("Downtown", 140, 75.0, 50.0);
        d1.setCo2Level(520.0);
        envList.add(d1);

        EnvironmentalData d2 = new EnvironmentalData("Green Park", 35, 30.0, 10.0);
        d2.setCo2Level(340.0);
        envList.add(d2);

        Map<String, Object> metrics = analyticsService.evaluateEnvironmentalMetrics(envList);
        assertNotNull(metrics);
        assertTrue((long) metrics.get("averageAqi") > 0);
        assertTrue((double) metrics.get("sustainabilityIndex") > 0);
    }
}
