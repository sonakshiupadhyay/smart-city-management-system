package com.smartcity.servlet;

import com.smartcity.dao.FeedbackDAO;
import com.smartcity.model.Feedback;
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
 * FeedbackServlet - Controller for Resident Citizen Feedback and Reviews.
 * 
 * @author Smart City Management System
 */
@WebServlet({"/resident/feedback", "/admin/feedback"})
public class FeedbackServlet extends HttpServlet {

    private FeedbackDAO feedbackDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.feedbackDAO = new FeedbackDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        boolean isAdmin = user != null && "ADMIN".equalsIgnoreCase(user.getRole());

        try {
            double avgRating = feedbackDAO.getAverageRating();
            request.setAttribute("avgRating", Math.round(avgRating * 10.0) / 10.0);

            if (isAdmin) {
                List<Feedback> allFeedback = feedbackDAO.findAll();
                request.setAttribute("feedbackList", allFeedback);
                request.getRequestDispatcher("/WEB-INF/views/admin/feedback.jsp").forward(request, response);
            } else {
                int residentId = (user != null) ? user.getUserId() : 5;
                List<Feedback> myFeedback = feedbackDAO.findByResidentId(residentId);
                request.setAttribute("feedbackList", myFeedback);
                request.getRequestDispatcher("/WEB-INF/views/resident/feedback.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading feedback records: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/resident/feedback.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "submit";

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        try {
            if ("submit".equalsIgnoreCase(action)) {
                int residentId = (user != null) ? user.getUserId() : 5;
                String category = request.getParameter("category");
                String subject = request.getParameter("subject");
                String message = request.getParameter("message");
                int rating = Integer.parseInt(request.getParameter("rating"));

                Feedback fb = new Feedback(residentId, category, subject, message, rating);
                feedbackDAO.insert(fb);

                response.sendRedirect(request.getContextPath() + "/resident/feedback?msg=Thank+you!+Your+feedback+has+been+recorded.");
            } else if ("respond".equalsIgnoreCase(action)) {
                int feedbackId = Integer.parseInt(request.getParameter("feedbackId"));
                String adminResponse = request.getParameter("adminResponse");
                String status = request.getParameter("status");
                if (status == null || status.isEmpty()) status = "ADDRESSED";

                feedbackDAO.respondToFeedback(feedbackId, status, adminResponse);
                response.sendRedirect(request.getContextPath() + "/admin/feedback?msg=Response+sent+to+resident.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/resident/feedback?error=" + e.getMessage());
        }
    }
}
