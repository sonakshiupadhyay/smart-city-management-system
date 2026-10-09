package com.smartcity.servlet;

import com.smartcity.dao.ServiceRequestDAO;
import com.smartcity.model.ServiceRequest;
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
 * ServiceRequestServlet - Controller for City Service Requests by Residents.
 * 
 * @author Smart City Management System
 */
@WebServlet({"/resident/services", "/admin/services"})
public class ServiceRequestServlet extends HttpServlet {

    private ServiceRequestDAO serviceRequestDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.serviceRequestDAO = new ServiceRequestDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        boolean isAdmin = user != null && "ADMIN".equalsIgnoreCase(user.getRole());

        try {
            List<ServiceRequest> requestList;
            if (isAdmin) {
                requestList = serviceRequestDAO.findAll();
            } else {
                int residentId = (user != null) ? user.getUserId() : 5;
                requestList = serviceRequestDAO.findByResidentId(residentId);
            }

            request.setAttribute("serviceRequests", requestList);
            request.setAttribute("isAdmin", isAdmin);

            if (isAdmin) {
                request.getRequestDispatcher("/WEB-INF/views/admin/services.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/views/resident/services.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error retrieving service requests: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/resident/services.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "create";

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        int currentUserId = (user != null) ? user.getUserId() : 5;

        try {
            if ("create".equalsIgnoreCase(action)) {
                String serviceType = request.getParameter("serviceType");
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String location = request.getParameter("location");
                String priority = request.getParameter("priority");

                ServiceRequest req = new ServiceRequest(currentUserId, serviceType, title, description, location, priority);
                serviceRequestDAO.insert(req);

                response.sendRedirect(request.getContextPath() + "/resident/services?msg=Service+request+submitted+successfully.+Tracking+ID+assigned.");
            } else if ("updateStatus".equalsIgnoreCase(action)) {
                int reqId = Integer.parseInt(request.getParameter("requestId"));
                String status = request.getParameter("status");
                String notes = request.getParameter("resolutionNotes");

                ServiceRequest req = serviceRequestDAO.findById(reqId);
                if (req != null) {
                    req.setStatus(status);
                    req.setAssignedTo(currentUserId);
                    req.setResolutionNotes(notes);
                    serviceRequestDAO.update(req);
                }

                String redirectUrl = (user != null && "ADMIN".equalsIgnoreCase(user.getRole())) ? 
                    "/admin/services?msg=Request+updated." : "/resident/services?msg=Request+updated.";
                response.sendRedirect(request.getContextPath() + redirectUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/resident/services?error=" + e.getMessage());
        }
    }
}
