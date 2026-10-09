<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In - Smart City Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>

    <div class="auth-wrapper">
        <div class="auth-card">
            <div class="auth-header">
                <div class="logo-badge" style="margin: 0 auto 1rem;">⚡</div>
                <h2>Smart City Access</h2>
                <p style="color: var(--text-secondary); font-size: 0.9rem;">Sign in to access your role-based dashboard</p>
            </div>

            <c:if test="${not empty param.msg}">
                <div class="alert alert-success">
                    <span>✓</span> ${param.msg}
                </div>
            </c:if>

            <c:if test="${not empty param.error}">
                <div class="alert alert-danger">
                    <span>⚠</span> ${param.error}
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <span>⚠</span> ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="POST">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" class="form-control" placeholder="e.g. admin1, planner1, resident1" required autofocus>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; padding: 0.85rem; margin-top: 0.5rem;">
                    Sign In to Dashboard &rarr;
                </button>
            </form>

            <!-- Quick Demo Credentials Selector -->
            <div style="margin-top: 2rem; padding-top: 1.5rem; border-top: 1px solid var(--border-color);">
                <div style="font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-muted); text-align: center; margin-bottom: 0.85rem;">
                    Quick Demo Access (1-Click Fill)
                </div>
                <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 0.5rem;">
                    <button type="button" class="btn btn-secondary" style="font-size: 0.78rem; padding: 0.5rem;" onclick="fillCredentials('admin1', 'password123')">
                        Admin
                    </button>
                    <button type="button" class="btn btn-secondary" style="font-size: 0.78rem; padding: 0.5rem;" onclick="fillCredentials('planner1', 'password123')">
                        Planner
                    </button>
                    <button type="button" class="btn btn-secondary" style="font-size: 0.78rem; padding: 0.5rem;" onclick="fillCredentials('resident1', 'password123')">
                        Resident
                    </button>
                </div>
            </div>

            <div style="margin-top: 1.5rem; text-align: center; font-size: 0.88rem; color: var(--text-secondary);">
                New resident? <a href="${pageContext.request.contextPath}/register.jsp">Create an account</a>
                <br>
                <a href="${pageContext.request.contextPath}/index.jsp" style="font-size: 0.82rem; color: var(--text-muted); display: inline-block; margin-top: 0.5rem;">&larr; Back to Home</a>
            </div>
        </div>
    </div>

    <script>
        function fillCredentials(u, p) {
            document.getElementById('username').value = u;
            document.getElementById('password').value = p;
        }

        // Auto-fill if demoRole query param passed
        const urlParams = new URLSearchParams(window.location.search);
        const demoRole = urlParams.get('demoRole');
        if (demoRole === 'ADMIN') fillCredentials('admin1', 'password123');
        else if (demoRole === 'PLANNER') fillCredentials('planner1', 'password123');
        else if (demoRole === 'RESIDENT') fillCredentials('resident1', 'password123');
    </script>

</body>
</html>
