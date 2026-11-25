package com.smartcity.servlet;

import com.smartcity.dao.*;
import com.smartcity.model.*;
import com.smartcity.service.AnalyticsService;
import com.smartcity.service.IoTSimulationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * AdminDashboardServlet - Controller for City Administrator Dashboard.
 * 
 * @author Smart City Management System
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private ResourceDAO resourceDAO;
    private TrafficDAO trafficDAO;
    private SafetyDAO safetyDAO;
    private ServiceRequestDAO serviceRequestDAO;
    private FeedbackDAO feedbackDAO;
    private IoTSensorDAO sensorDAO;
    private AnalyticsService analyticsService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.resourceDAO = new ResourceDAO();
        this.trafficDAO = new TrafficDAO();
        this.safetyDAO = new SafetyDAO();
        this.serviceRequestDAO = new ServiceRequestDAO();
        this.feedbackDAO = new FeedbackDAO();
        this.sensorDAO = new IoTSensorDAO();
        this.analyticsService = new AnalyticsService();

        // Start background simulated sensor telemetry
        IoTSimulationService.getInstance().startSimulation();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Resource statistics
            List<Resource> resources = resourceDAO.findAll();
            int criticalResources = resourceDAO.countByStatus("CRITICAL");
            int warningResources = resourceDAO.countByStatus("WARNING");
            Map<String, Object> optimizationReport = analyticsService.calculateResourceOptimization(resources);

            // Traffic statistics
            List<TrafficData> trafficList = trafficDAO.findAll();
            List<TrafficData> congestedAreas = trafficDAO.findCongestedAreas();

            // Safety incidents
            List<SafetyIncident> recentIncidents = safetyDAO.findAll();
            int criticalIncidents = safetyDAO.countBySeverity("CRITICAL") + safetyDAO.countBySeverity("HIGH");

            // Service requests and feedback metrics
            int totalRequests = serviceRequestDAO.count();
            int pendingRequests = serviceRequestDAO.countByStatus("SUBMITTED") + serviceRequestDAO.countByStatus("IN_PROGRESS");
            int totalFeedback = feedbackDAO.count();
            double avgRating = feedbackDAO.getAverageRating();

            // Active simulated sensor node count
            int activeSensors = sensorDAO.findActive().size();
            List<String> recentAlerts = IoTSimulationService.getInstance().getRecentAlerts();

            // Attach to request attributes
            request.setAttribute("resources", resources);
            request.setAttribute("criticalResources", criticalResources);
            request.setAttribute("warningResources", warningResources);
            request.setAttribute("optimizationReport", optimizationReport);

            request.setAttribute("trafficList", trafficList);
            request.setAttribute("congestedAreas", congestedAreas);

            request.setAttribute("recentIncidents", recentIncidents);
            request.setAttribute("criticalIncidents", criticalIncidents);

            request.setAttribute("totalRequests", totalRequests);
            request.setAttribute("pendingRequests", pendingRequests);
            request.setAttribute("totalFeedback", totalFeedback);
            request.setAttribute("avgRating", Math.round(avgRating * 10.0) / 10.0);

            request.setAttribute("activeSensors", activeSensors);
            request.setAttribute("recentAlerts", recentAlerts);

            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load administrator dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
        }
    }
}
