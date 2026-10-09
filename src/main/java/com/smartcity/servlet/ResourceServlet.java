package com.smartcity.servlet;

import com.smartcity.dao.ResourceDAO;
import com.smartcity.model.Resource;
import com.smartcity.model.User;
import com.smartcity.service.AnalyticsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * ResourceServlet - Handles CRUD operations and optimization for City Resources.
 * 
 * @author Smart City Management System
 */
@WebServlet("/admin/resources")
public class ResourceServlet extends HttpServlet {

    private ResourceDAO resourceDAO;
    private AnalyticsService analyticsService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.resourceDAO = new ResourceDAO();
        this.analyticsService = new AnalyticsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String typeFilter = request.getParameter("type");
        try {
            List<Resource> resources;
            if (typeFilter != null && !typeFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(typeFilter)) {
                resources = resourceDAO.findByType(typeFilter.toUpperCase());
            } else {
                resources = resourceDAO.findAll();
            }

            Map<String, Object> optimizationReport = analyticsService.calculateResourceOptimization(resources);

            request.setAttribute("resources", resources);
            request.setAttribute("selectedType", typeFilter != null ? typeFilter : "ALL");
            request.setAttribute("optimizationReport", optimizationReport);

            request.getRequestDispatcher("/WEB-INF/views/admin/resources.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading resources: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/resources.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "add";

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        int userId = (user != null) ? user.getUserId() : 1;

        try {
            switch (action) {
                case "add": {
                    String name = request.getParameter("resourceName");
                    String type = request.getParameter("resourceType");
                    String location = request.getParameter("location");
                    double capacity = Double.parseDouble(request.getParameter("totalCapacity"));
                    double usage = Double.parseDouble(request.getParameter("currentUsage"));
                    String unit = request.getParameter("unit");

                    // Status and efficiency determination
                    double ratio = (usage / capacity) * 100.0;
                    String status = "OPTIMAL";
                    if (ratio > 85.0) status = "CRITICAL";
                    else if (ratio > 70.0) status = "WARNING";

                    double efficiency = Math.max(10.0, Math.min(100.0, 100.0 - (ratio > 80 ? (ratio - 80) * 2 : 0)));

                    Resource resource = new Resource();
                    resource.setResourceName(name);
                    resource.setResourceType(type);
                    resource.setLocation(location);
                    resource.setTotalCapacity(capacity);
                    resource.setCurrentUsage(usage);
                    resource.setUnit(unit);
                    resource.setStatus(status);
                    resource.setEfficiencyScore(Math.round(efficiency * 10.0) / 10.0);
                    resource.setManagedBy(userId);

                    resourceDAO.insert(resource);
                    response.sendRedirect(request.getContextPath() + "/admin/resources?msg=Resource+added+successfully.");
                    break;
                }
                case "update": {
                    int id = Integer.parseInt(request.getParameter("resourceId"));
                    Resource resource = resourceDAO.findById(id);
                    if (resource != null) {
                        double capacity = Double.parseDouble(request.getParameter("totalCapacity"));
                        double usage = Double.parseDouble(request.getParameter("currentUsage"));
                        String location = request.getParameter("location");

                        double ratio = (usage / capacity) * 100.0;
                        String status = "OPTIMAL";
                        if (ratio > 85.0) status = "CRITICAL";
                        else if (ratio > 70.0) status = "WARNING";

                        resource.setTotalCapacity(capacity);
                        resource.setCurrentUsage(usage);
                        resource.setLocation(location);
                        resource.setStatus(status);
                        resourceDAO.update(resource);
                    }
                    response.sendRedirect(request.getContextPath() + "/admin/resources?msg=Resource+updated+successfully.");
                    break;
                }
                case "delete": {
                    int id = Integer.parseInt(request.getParameter("resourceId"));
                    resourceDAO.delete(id);
                    response.sendRedirect(request.getContextPath() + "/admin/resources?msg=Resource+removed+successfully.");
                    break;
                }
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/resources");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/resources?error=" + e.getMessage());
        }
    }
}
