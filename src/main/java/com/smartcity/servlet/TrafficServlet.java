package com.smartcity.servlet;

import com.smartcity.dao.TrafficDAO;
import com.smartcity.model.TrafficData;
import com.smartcity.service.AnalyticsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * TrafficServlet - Controller for Traffic Monitoring and Congestion Analytics.
 * 
 * @author Smart City Management System
 */
@WebServlet({"/admin/traffic", "/traffic"})
public class TrafficServlet extends HttpServlet {

    private TrafficDAO trafficDAO;
    private AnalyticsService analyticsService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.trafficDAO = new TrafficDAO();
        this.analyticsService = new AnalyticsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<TrafficData> trafficList = trafficDAO.findAll();
            List<Map<String, Object>> bottleneckAnalysis = analyticsService.analyzeTrafficBottlenecks(trafficList);

            request.setAttribute("trafficList", trafficList);
            request.setAttribute("bottleneckAnalysis", bottleneckAnalysis);

            request.getRequestDispatcher("/WEB-INF/views/admin/traffic.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading traffic information: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/traffic.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "update";

        try {
            if ("add".equalsIgnoreCase(action)) {
                String location = request.getParameter("location");
                double lat = Double.parseDouble(request.getParameter("latitude"));
                double lng = Double.parseDouble(request.getParameter("longitude"));
                String level = request.getParameter("congestionLevel");
                double speed = Double.parseDouble(request.getParameter("averageSpeed"));
                int vehicles = Integer.parseInt(request.getParameter("vehicleCount"));
                String alert = request.getParameter("alertType");
                String suggestion = request.getParameter("suggestion");

                if (suggestion == null || suggestion.trim().isEmpty()) {
                    if ("SEVERE".equals(level)) suggestion = "Reroute traffic to arterial expressways.";
                    else if ("HIGH".equals(level)) suggestion = "Extend green signal duration by 20s.";
                    else suggestion = "Traffic moving smoothly.";
                }

                TrafficData data = new TrafficData(location, level, speed, vehicles);
                data.setLatitude(lat);
                data.setLongitude(lng);
                data.setAlertType(alert);
                data.setSuggestion(suggestion);

                trafficDAO.insert(data);
                response.sendRedirect(request.getContextPath() + "/admin/traffic?msg=Traffic+sensor+location+added.");
            } else if ("update".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("trafficId"));
                String level = request.getParameter("congestionLevel");
                double speed = Double.parseDouble(request.getParameter("averageSpeed"));
                int vehicles = Integer.parseInt(request.getParameter("vehicleCount"));

                trafficDAO.updateCongestion(id, level, speed, vehicles);
                response.sendRedirect(request.getContextPath() + "/admin/traffic?msg=Traffic+status+updated.");
            } else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("trafficId"));
                trafficDAO.delete(id);
                response.sendRedirect(request.getContextPath() + "/admin/traffic?msg=Traffic+entry+removed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/traffic?error=" + e.getMessage());
        }
    }
}
