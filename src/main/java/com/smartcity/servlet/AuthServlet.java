package com.smartcity.servlet;

import com.smartcity.dao.UserDAO;
import com.smartcity.model.CityAdministrator;
import com.smartcity.model.Resident;
import com.smartcity.model.UrbanPlanner;
import com.smartcity.model.User;
import com.smartcity.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AuthServlet - Handles User Authentication, Registration, and Session Lifecycle.
 * 
 * @author Smart City Management System
 */
@WebServlet({"/auth", "/login", "/logout", "/register"})
public class AuthServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if ("/logout".equals(servletPath) || "logout".equals(request.getParameter("action"))) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=You+have+been+logged+out+successfully.");
            return;
        }

        if ("/login".equals(servletPath)) {
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if ("/register".equals(servletPath)) {
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        String action = request.getParameter("action");

        if ("/register".equals(servletPath) || "register".equals(action)) {
            handleRegistration(request, response);
        } else {
            handleLogin(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            // First check plain password (for initial seed users)
            User user = userDAO.authenticate(username.trim(), password);
            
            // If not found, check with hashed password or username lookup
            if (user == null) {
                user = userDAO.authenticate(username.trim(), PasswordUtil.hashPassword(password));
            }
            if (user == null) {
                User candidate = userDAO.findByUsername(username.trim());
                if (candidate != null && (password.equals(candidate.getPassword()) || 
                    PasswordUtil.verifyPassword(password, candidate.getPassword()))) {
                    user = candidate;
                }
            }

            if (user != null) {
                if (!user.isActive()) {
                    request.setAttribute("error", "Account has been deactivated. Please contact city administrator.");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                    return;
                }

                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("fullName", user.getFullName());
                session.setAttribute("role", user.getRole());

                // Polymorphic redirect using getDashboardURL()
                response.sendRedirect(request.getContextPath() + user.getDashboardURL());
            } else {
                request.setAttribute("error", "Invalid username or password.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "System error during authentication: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");
        String department = request.getParameter("department");
        String address = request.getParameter("address");

        if (username == null || password == null || email == null || fullName == null ||
            username.trim().isEmpty() || password.trim().isEmpty() || email.trim().isEmpty() || fullName.trim().isEmpty()) {
            request.setAttribute("error", "All marked fields are mandatory.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (role == null || role.isEmpty()) {
            role = "RESIDENT";
        }

        try {
            if (userDAO.usernameExists(username.trim())) {
                request.setAttribute("error", "Username already exists. Please choose another.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
            if (userDAO.emailExists(email.trim())) {
                request.setAttribute("error", "Email already registered. Please login.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            User newUser;
            String hashedPassword = PasswordUtil.hashPassword(password);

            if ("ADMIN".equalsIgnoreCase(role)) {
                newUser = new CityAdministrator(username.trim(), hashedPassword, email.trim(), fullName.trim());
                newUser.setDepartment(department);
            } else if ("PLANNER".equalsIgnoreCase(role)) {
                newUser = new UrbanPlanner(username.trim(), hashedPassword, email.trim(), fullName.trim());
                newUser.setDepartment(department);
            } else {
                newUser = new Resident(username.trim(), hashedPassword, email.trim(), fullName.trim());
            }

            newUser.setPhone(phone);
            newUser.setAddress(address);

            int userId = userDAO.insert(newUser);
            if (userId > 0) {
                response.sendRedirect(request.getContextPath() + "/login.jsp?msg=Registration+successful.+Please+log+in+with+your+credentials.");
            } else {
                request.setAttribute("error", "Could not complete registration.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Registration error: " + e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
