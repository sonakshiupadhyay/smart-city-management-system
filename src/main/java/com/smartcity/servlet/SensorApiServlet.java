package com.smartcity.servlet;

import com.google.gson.Gson;
import com.smartcity.dao.EnvironmentalDAO;
import com.smartcity.dao.IoTSensorDAO;
import com.smartcity.dao.TrafficDAO;
import com.smartcity.model.EnvironmentalData;
import com.smartcity.model.IoTSensor;
import com.smartcity.model.TrafficData;
import com.smartcity.service.IoTSimulationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SensorApiServlet - REST JSON endpoint for simulated sensor telemetry and live dashboard feeds.
 * 
 * @author Smart City Management System
 */
@WebServlet({"/api/sensors", "/api/live-data"})
public class SensorApiServlet extends HttpServlet {

    private IoTSensorDAO sensorDAO;
    private TrafficDAO trafficDAO;
    private EnvironmentalDAO environmentalDAO;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        super.init();
        this.sensorDAO = new IoTSensorDAO();
        this.trafficDAO = new TrafficDAO();
        this.environmentalDAO = new EnvironmentalDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<IoTSensor> sensors = sensorDAO.findAll();
            List<TrafficData> traffic = trafficDAO.findAll();
            List<EnvironmentalData> env = environmentalDAO.findAll();
            List<String> liveAlerts = IoTSimulationService.getInstance().getRecentAlerts();

            Map<String, Object> data = new HashMap<>();
            data.put("timestamp", System.currentTimeMillis());
            data.put("sensors", sensors);
            data.put("traffic", traffic);
            data.put("environment", env);
            data.put("liveAlerts", liveAlerts);
            data.put("status", "SUCCESS");

            response.getWriter().write(gson.toJson(data));
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "ERROR");
            errorMap.put("message", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(errorMap));
        }
    }
}
