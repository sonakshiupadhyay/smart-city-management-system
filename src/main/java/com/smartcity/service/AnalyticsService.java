package com.smartcity.service;

import com.smartcity.model.EnvironmentalData;
import com.smartcity.model.Resource;
import com.smartcity.model.TrafficData;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AnalyticsService - Rule-based analytics and statistical calculations for city operations.
 * Demonstrates Collections, Generics, Stream API, and complex business logic.
 * 
 * <p>Note: The calculations in this service are rule-based heuristics and threshold
 * comparisons, not machine-learning or AI models. Labels such as "optimization" and
 * "recommendation" refer to deterministic logic applied to the data.</p>
 * 
 * @author Smart City Management System
 */
public class AnalyticsService {

    /**
     * Resource Analytics: Calculates resource efficiency statistics across all city resources
     * using threshold-based heuristics.
     */
    public Map<String, Object> calculateResourceOptimization(List<Resource> resources) {
        Map<String, Object> report = new HashMap<>();
        if (resources == null || resources.isEmpty()) {
            report.put("totalCapacity", 0.0);
            report.put("totalUsage", 0.0);
            report.put("overallEfficiency", 100.0);
            report.put("recommendations", Collections.emptyList());
            return report;
        }

        double totalCapacity = resources.stream().mapToDouble(Resource::getTotalCapacity).sum();
        double totalUsage = resources.stream().mapToDouble(Resource::getCurrentUsage).sum();
        double averageEfficiency = resources.stream().mapToDouble(Resource::getEfficiencyScore).average().orElse(100.0);

        List<String> recommendations = new ArrayList<>();
        for (Resource r : resources) {
            double usageRatio = (r.getCurrentUsage() / r.getTotalCapacity()) * 100.0;
            if (usageRatio > 85.0) {
                recommendations.add("CRITICAL LOAD: " + r.getResourceName() + " is operating at " +
                    String.format("%.1f%%", usageRatio) + " capacity. Load-shedding or grid rebalancing suggested.");
            } else if (usageRatio < 30.0) {
                recommendations.add("LOW UTILIZATION: " + r.getResourceName() + " has " +
                    String.format("%.1f%%", (100.0 - usageRatio)) + " idle reserves. Consider redirecting to peak sectors.");
            }
        }

        report.put("totalCapacity", totalCapacity);
        report.put("totalUsage", totalUsage);
        report.put("overallEfficiency", Math.round(averageEfficiency * 10.0) / 10.0);
        report.put("recommendations", recommendations);
        return report;
    }

    /**
     * Traffic Analytics: Analyzes congestion hotspots and suggests alternate routes
     * based on configurable threshold rules.
     */
    public List<Map<String, Object>> analyzeTrafficBottlenecks(List<TrafficData> trafficList) {
        if (trafficList == null) return Collections.emptyList();

        return trafficList.stream()
            .filter(t -> "HIGH".equalsIgnoreCase(t.getCongestionLevel()) || "SEVERE".equalsIgnoreCase(t.getCongestionLevel()))
            .sorted(Comparator.comparingInt(TrafficData::getVehicleCount).reversed())
            .map(t -> {
                Map<String, Object> map = new HashMap<>();
                map.put("location", t.getLocation());
                map.put("vehicleCount", t.getVehicleCount());
                map.put("averageSpeed", t.getAverageSpeed());
                map.put("congestionLevel", t.getCongestionLevel());
                map.put("recommendation", t.getSuggestion() != null ? t.getSuggestion() : "Adjust adaptive traffic signal intervals by +15s.");
                return map;
            })
            .collect(Collectors.toList());
    }

    /**
     * Environmental Sustainability Score: Evaluates urban air, noise, and greenhouse metrics.
     */
    public Map<String, Object> evaluateEnvironmentalMetrics(List<EnvironmentalData> envList) {
        Map<String, Object> metrics = new HashMap<>();
        if (envList == null || envList.isEmpty()) {
            metrics.put("averageAqi", 0);
            metrics.put("sustainabilityIndex", 100.0);
            metrics.put("status", "OPTIMAL");
            return metrics;
        }

        double avgAqi = envList.stream().mapToInt(EnvironmentalData::getAirQualityIndex).average().orElse(0.0);
        double avgNoise = envList.stream().mapToDouble(EnvironmentalData::getNoiseLevel).average().orElse(0.0);
        double avgCo2 = envList.stream().mapToDouble(EnvironmentalData::getCo2Level).average().orElse(0.0);

        // Sustainability score calculated out of 100 based on standard limits
        double score = 100.0 - ((avgAqi / 300.0) * 40.0) - ((avgNoise / 100.0) * 30.0) - ((avgCo2 / 800.0) * 30.0);
        score = Math.max(0.0, Math.min(100.0, score));

        String overallStatus;
        if (avgAqi < 50) overallStatus = "EXCELLENT";
        else if (avgAqi < 100) overallStatus = "MODERATE";
        else if (avgAqi < 150) overallStatus = "UNHEALTHY FOR SENSITIVE GROUPS";
        else overallStatus = "HAZARDOUS";

        metrics.put("averageAqi", Math.round(avgAqi));
        metrics.put("averageNoise", Math.round(avgNoise * 10.0) / 10.0);
        metrics.put("averageCo2", Math.round(avgCo2 * 10.0) / 10.0);
        metrics.put("sustainabilityIndex", Math.round(score * 10.0) / 10.0);
        metrics.put("overallStatus", overallStatus);
        return metrics;
    }
}
