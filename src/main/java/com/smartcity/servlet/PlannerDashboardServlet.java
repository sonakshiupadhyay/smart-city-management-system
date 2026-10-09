package com.smartcity.servlet;

import com.smartcity.dao.EnvironmentalDAO;
import com.smartcity.dao.InfrastructureDAO;
import com.smartcity.model.EnvironmentalData;
import com.smartcity.model.InfrastructureProject;
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
 * PlannerDashboardServlet - Controller for Urban Planner Dashboard.
 * 
 * @author Smart City Management System
 */
@WebServlet("/planner/dashboard")
public class PlannerDashboardServlet extends HttpServlet {

    private InfrastructureDAO infrastructureDAO;
    private EnvironmentalDAO environmentalDAO;
    private AnalyticsService analyticsService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.infrastructureDAO = new InfrastructureDAO();
        this.environmentalDAO = new EnvironmentalDAO();
        this.analyticsService = new AnalyticsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<InfrastructureProject> projects = infrastructureDAO.findAll();
            List<EnvironmentalData> envList = environmentalDAO.findAll();
            Map<String, Object> envMetrics = analyticsService.evaluateEnvironmentalMetrics(envList);

            double totalBudget = projects.stream().mapToDouble(InfrastructureProject::getBudget).sum();
            double totalSpent = projects.stream().mapToDouble(InfrastructureProject::getSpentAmount).sum();

            long proposedCount = projects.stream().filter(p -> "PROPOSED".equalsIgnoreCase(p.getStatus())).count();
            long inProgressCount = projects.stream().filter(p -> "IN_PROGRESS".equalsIgnoreCase(p.getStatus())).count();
            long completedCount = projects.stream().filter(p -> "COMPLETED".equalsIgnoreCase(p.getStatus())).count();
            long approvedCount = projects.stream().filter(p -> "APPROVED".equalsIgnoreCase(p.getStatus())).count();

            request.setAttribute("projects", projects);
            request.setAttribute("envList", envList);
            request.setAttribute("envMetrics", envMetrics);

            request.setAttribute("totalBudget", totalBudget);
            request.setAttribute("totalSpent", totalSpent);
            request.setAttribute("proposedCount", proposedCount);
            request.setAttribute("inProgressCount", inProgressCount);
            request.setAttribute("completedCount", completedCount);
            request.setAttribute("approvedCount", approvedCount);

            request.getRequestDispatcher("/WEB-INF/views/planner/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading planner dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/planner/dashboard.jsp").forward(request, response);
        }
    }
}
