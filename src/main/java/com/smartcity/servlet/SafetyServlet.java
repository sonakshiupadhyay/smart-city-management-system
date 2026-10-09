package com.smartcity.servlet;

import com.smartcity.dao.IoTSensorDAO;
import com.smartcity.dao.SafetyDAO;
import com.smartcity.model.IoTSensor;
import com.smartcity.model.SafetyIncident;
import com.smartcity.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * SafetyServlet - Controller for Public Safety Management and CCTV Feeds.
 * 
 * @author Smart City Management System
 */
@WebServlet({"/admin/safety", "/safety"})
public class SafetyServlet extends HttpServlet {

    private SafetyDAO safetyDAO;
    private IoTSensorDAO sensorDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.safetyDAO = new SafetyDAO();
        this.sensorDAO = new IoTSensorDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String filter = request.getParameter("severity");
        try {
            List<SafetyIncident> incidents;
            if (filter != null && !filter.trim().isEmpty() && !"ALL".equalsIgnoreCase(filter)) {
                incidents = safetyDAO.findBySeverity(filter.toUpperCase());
            } else {
                incidents = safetyDAO.findAll();
            }

            List<IoTSensor> cctvSensors = sensorDAO.findByType("SAFETY");
            int openIncidents = safetyDAO.countByStatus("REPORTED") + safetyDAO.countByStatus("IN_PROGRESS");

            request.setAttribute("incidents", incidents);
            request.setAttribute("cctvSensors", cctvSensors);
            request.setAttribute("openIncidents", openIncidents);
            request.setAttribute("selectedSeverity", filter != null ? filter : "ALL");

            request.getRequestDispatcher("/WEB-INF/views/admin/safety.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading safety records: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/safety.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "updateStatus";

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        int currentUserId = (user != null) ? user.getUserId() : 1;

        try {
            if ("report".equalsIgnoreCase(action)) {
                String type = request.getParameter("incidentType");
                String location = request.getParameter("location");
                double lat = Double.parseDouble(request.getParameter("latitude"));
                double lng = Double.parseDouble(request.getParameter("longitude"));
                String description = request.getParameter("description");
                String severity = request.getParameter("severity");

                SafetyIncident incident = new SafetyIncident(type, location, description, severity);
                incident.setLatitude(lat);
                incident.setLongitude(lng);
                incident.setReportedBy(currentUserId);

                safetyDAO.insert(incident);
                response.sendRedirect(request.getContextPath() + "/admin/safety?msg=Emergency+incident+reported+successfully.");
            } else if ("updateStatus".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("incidentId"));
                String status = request.getParameter("status");

                safetyDAO.updateStatus(id, status, currentUserId);
                response.sendRedirect(request.getContextPath() + "/admin/safety?msg=Incident+status+updated+to+" + status + ".");
            } else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("incidentId"));
                safetyDAO.delete(id);
                response.sendRedirect(request.getContextPath() + "/admin/safety?msg=Incident+record+removed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/safety?error=" + e.getMessage());
        }
    }
}
