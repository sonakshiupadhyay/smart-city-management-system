package com.smartcity.servlet;

import com.smartcity.dao.InfrastructureDAO;
import com.smartcity.model.InfrastructureProject;
import com.smartcity.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * InfrastructureServlet - Manages Infrastructure Projects and Planning Tools.
 * 
 * @author Smart City Management System
 */
@WebServlet("/planner/infrastructure")
public class InfrastructureServlet extends HttpServlet {

    private InfrastructureDAO infrastructureDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.infrastructureDAO = new InfrastructureDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String statusFilter = request.getParameter("status");
        try {
            List<InfrastructureProject> projects;
            if (statusFilter != null && !statusFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(statusFilter)) {
                projects = infrastructureDAO.findByStatus(statusFilter.toUpperCase());
            } else {
                projects = infrastructureDAO.findAll();
            }

            request.setAttribute("projects", projects);
            request.setAttribute("selectedStatus", statusFilter != null ? statusFilter : "ALL");

            request.getRequestDispatcher("/WEB-INF/views/planner/infrastructure.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading infrastructure projects: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/planner/infrastructure.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "add";

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        int plannerId = (user != null) ? user.getUserId() : 3;

        try {
            if ("add".equalsIgnoreCase(action)) {
                String name = request.getParameter("projectName");
                String description = request.getParameter("description");
                String category = request.getParameter("category");
                String location = request.getParameter("location");
                double budget = Double.parseDouble(request.getParameter("budget"));
                String startDateStr = request.getParameter("startDate");
                String endDateStr = request.getParameter("endDate");
                double impactScore = Double.parseDouble(request.getParameter("impactScore"));

                Date startDate = (startDateStr != null && !startDateStr.isEmpty()) ? Date.valueOf(startDateStr) : new Date(System.currentTimeMillis());
                Date endDate = (endDateStr != null && !endDateStr.isEmpty()) ? Date.valueOf(endDateStr) : null;

                InfrastructureProject project = new InfrastructureProject(name, description, category, location, budget);
                project.setPlannedBy(plannerId);
                project.setStartDate(startDate);
                project.setEndDate(endDate);
                project.setImpactScore(impactScore);
                project.setStatus("PROPOSED");

                infrastructureDAO.insert(project);
                response.sendRedirect(request.getContextPath() + "/planner/infrastructure?msg=Project+proposal+submitted+successfully.");
            } else if ("update".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("projectId"));
                InfrastructureProject project = infrastructureDAO.findById(id);
                if (project != null) {
                    project.setStatus(request.getParameter("status"));
                    project.setSpentAmount(Double.parseDouble(request.getParameter("spentAmount")));
                    project.setImpactScore(Double.parseDouble(request.getParameter("impactScore")));
                    infrastructureDAO.update(project);
                }
                response.sendRedirect(request.getContextPath() + "/planner/infrastructure?msg=Project+details+updated.");
            } else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("projectId"));
                infrastructureDAO.delete(id);
                response.sendRedirect(request.getContextPath() + "/planner/infrastructure?msg=Project+deleted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/planner/infrastructure?error=" + e.getMessage());
        }
    }
}
