package com.smartcity.servlet;

import com.smartcity.dao.CityEventDAO;
import com.smartcity.model.CityEvent;
import com.smartcity.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * CityEventServlet - Controller for City Events, Weather Alerts, and Real-Time Bulletins.
 * 
 * @author Smart City Management System
 */
@WebServlet({"/events", "/resident/events", "/admin/events"})
public class CityEventServlet extends HttpServlet {

    private CityEventDAO cityEventDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.cityEventDAO = new CityEventDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<CityEvent> events = cityEventDAO.findAll();
            List<CityEvent> activeEvents = cityEventDAO.findActiveEvents();

            request.setAttribute("events", events);
            request.setAttribute("activeEvents", activeEvents);

            request.getRequestDispatcher("/WEB-INF/views/resident/events.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading city events: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/resident/events.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "create";

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        int creatorId = (user != null) ? user.getUserId() : 1;

        try {
            if ("create".equalsIgnoreCase(action)) {
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String eventType = request.getParameter("eventType");
                String location = request.getParameter("location");
                double lat = Double.parseDouble(request.getParameter("latitude"));
                double lng = Double.parseDouble(request.getParameter("longitude"));
                String severity = request.getParameter("severity");

                CityEvent event = new CityEvent(title, description, eventType, location, severity);
                event.setLatitude(lat);
                event.setLongitude(lng);
                event.setCreatedBy(creatorId);
                event.setStartTime(new Timestamp(System.currentTimeMillis()));

                cityEventDAO.insert(event);
                response.sendRedirect(request.getContextPath() + "/resident/events?msg=City+bulletin+published.");
            } else if ("toggle".equalsIgnoreCase(action)) {
                int eventId = Integer.parseInt(request.getParameter("eventId"));
                CityEvent event = cityEventDAO.findById(eventId);
                if (event != null) {
                    event.setActive(!event.isActive());
                    cityEventDAO.update(event);
                }
                response.sendRedirect(request.getContextPath() + "/resident/events?msg=Bulletin+status+updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/resident/events?error=" + e.getMessage());
        }
    }
}
