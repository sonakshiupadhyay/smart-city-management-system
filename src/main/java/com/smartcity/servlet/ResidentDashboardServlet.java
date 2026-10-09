package com.smartcity.servlet;

import com.smartcity.dao.CityEventDAO;
import com.smartcity.dao.FeedbackDAO;
import com.smartcity.dao.ServiceRequestDAO;
import com.smartcity.dao.TrafficDAO;
import com.smartcity.model.CityEvent;
import com.smartcity.model.Feedback;
import com.smartcity.model.ServiceRequest;
import com.smartcity.model.TrafficData;
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
 * ResidentDashboardServlet - Controller for Resident Citizen Dashboard.
 * 
 * @author Smart City Management System
 */
@WebServlet("/resident/dashboard")
public class ResidentDashboardServlet extends HttpServlet {

    private ServiceRequestDAO serviceRequestDAO;
    private FeedbackDAO feedbackDAO;
    private CityEventDAO cityEventDAO;
    private TrafficDAO trafficDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.serviceRequestDAO = new ServiceRequestDAO();
        this.feedbackDAO = new FeedbackDAO();
        this.cityEventDAO = new CityEventDAO();
        this.trafficDAO = new TrafficDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        int residentId = (user != null) ? user.getUserId() : 5;

        try {
            List<ServiceRequest> myRequests = serviceRequestDAO.findByResidentId(residentId);
            List<Feedback> myFeedback = feedbackDAO.findByResidentId(residentId);
            List<CityEvent> activeAlerts = cityEventDAO.findActiveEvents();
            List<TrafficData> liveTraffic = trafficDAO.findAll();

            long pendingRequests = myRequests.stream()
                .filter(r -> "SUBMITTED".equalsIgnoreCase(r.getStatus()) || "IN_PROGRESS".equalsIgnoreCase(r.getStatus()))
                .count();

            request.setAttribute("myRequests", myRequests);
            request.setAttribute("myFeedback", myFeedback);
            request.setAttribute("activeAlerts", activeAlerts);
            request.setAttribute("liveTraffic", liveTraffic);
            request.setAttribute("pendingRequests", pendingRequests);

            request.getRequestDispatcher("/WEB-INF/views/resident/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading resident dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/resident/dashboard.jsp").forward(request, response);
        }
    }
}
