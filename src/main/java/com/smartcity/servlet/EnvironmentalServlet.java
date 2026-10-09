package com.smartcity.servlet;

import com.smartcity.dao.EnvironmentalDAO;
import com.smartcity.model.EnvironmentalData;
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
 * EnvironmentalServlet - Controller for Environmental Monitoring and Sustainability Reports.
 * 
 * @author Smart City Management System
 */
@WebServlet("/planner/environment")
public class EnvironmentalServlet extends HttpServlet {

    private EnvironmentalDAO environmentalDAO;
    private AnalyticsService analyticsService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.environmentalDAO = new EnvironmentalDAO();
        this.analyticsService = new AnalyticsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<EnvironmentalData> envList = environmentalDAO.findAll();
            List<EnvironmentalData> alertList = environmentalDAO.findHighPollutionAlerts();
            Map<String, Object> sustainabilityReport = analyticsService.evaluateEnvironmentalMetrics(envList);

            request.setAttribute("envList", envList);
            request.setAttribute("alertList", alertList);
            request.setAttribute("sustainabilityReport", sustainabilityReport);

            request.getRequestDispatcher("/WEB-INF/views/planner/environment.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading environmental data: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/planner/environment.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "add";

        try {
            if ("add".equalsIgnoreCase(action)) {
                String location = request.getParameter("location");
                double lat = Double.parseDouble(request.getParameter("latitude"));
                double lng = Double.parseDouble(request.getParameter("longitude"));
                int aqi = Integer.parseInt(request.getParameter("airQualityIndex"));
                double noise = Double.parseDouble(request.getParameter("noiseLevel"));
                double pollution = Double.parseDouble(request.getParameter("pollutionLevel"));
                double temp = Double.parseDouble(request.getParameter("temperature"));
                double hum = Double.parseDouble(request.getParameter("humidity"));
                double co2 = Double.parseDouble(request.getParameter("co2Level"));
                String recommendation = request.getParameter("recommendation");

                String status = "GOOD";
                if (aqi > 200 || co2 > 600) status = "HAZARDOUS";
                else if (aqi > 100 || co2 > 450) status = "UNHEALTHY";
                else if (aqi > 50) status = "MODERATE";

                EnvironmentalData data = new EnvironmentalData(location, aqi, noise, pollution);
                data.setLatitude(lat);
                data.setLongitude(lng);
                data.setTemperature(temp);
                data.setHumidity(hum);
                data.setCo2Level(co2);
                data.setEnvStatus(status);
                data.setRecommendation(recommendation != null && !recommendation.trim().isEmpty() ? 
                    recommendation : "Maintain regular air quality observation.");

                environmentalDAO.insert(data);
                response.sendRedirect(request.getContextPath() + "/planner/environment?msg=Environmental+station+record+logged.");
            } else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("envId"));
                environmentalDAO.delete(id);
                response.sendRedirect(request.getContextPath() + "/planner/environment?msg=Record+deleted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/planner/environment?error=" + e.getMessage());
        }
    }
}
