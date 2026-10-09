package com.smartcity.filter;

import com.smartcity.model.User;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AuthenticationFilter - Intercepts requests to enforce role-based access control (RBAC).
 * 
 * @author Smart City Management System
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String relativePath = uri.substring(contextPath.length());

        // Allow public resources and assets
        if (isPublicResource(relativePath)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // If not logged in and accessing protected pages
        if (user == null) {
            httpResponse.sendRedirect(contextPath + "/login.jsp?error=Please+login+to+access+the+system");
            return;
        }

        String role = user.getRole();

        // Role-based access enforcement
        if (relativePath.startsWith("/admin/") && !"ADMIN".equalsIgnoreCase(role)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Administrator role required.");
            return;
        }

        if (relativePath.startsWith("/planner/") && !"PLANNER".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Urban Planner role required.");
            return;
        }

        if (relativePath.startsWith("/resident/") && !"RESIDENT".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Resident role required.");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicResource(String path) {
        return path.equals("/") ||
               path.equals("/index.jsp") ||
               path.equals("/login") ||
               path.equals("/login.jsp") ||
               path.equals("/logout") ||
               path.equals("/register") ||
               path.equals("/register.jsp") ||
               path.startsWith("/auth") ||
               path.startsWith("/api/") ||
               path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/") ||
               path.startsWith("/favicon.ico");
    }

    @Override
    public void destroy() {}
}
