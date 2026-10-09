<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Citizen Registration - Smart City Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>

    <div class="auth-wrapper" style="padding-top: 3rem; padding-bottom: 3rem;">
        <div class="auth-card" style="max-width: 540px;">
            <div class="auth-header">
                <div class="logo-badge" style="margin: 0 auto 1rem;">⚡</div>
                <h2>Register New Account</h2>
                <p style="color: var(--text-secondary); font-size: 0.9rem;">Join the smart city citizen &amp; staff network</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <span>⚠</span> ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="POST">
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                    <div class="form-group">
                        <label for="username">Username *</label>
                        <input type="text" id="username" name="username" class="form-control" placeholder="john_doe" required>
                    </div>

                    <div class="form-group">
                        <label for="fullName">Full Name *</label>
                        <input type="text" id="fullName" name="fullName" class="form-control" placeholder="John Doe" required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="email">Email Address *</label>
                    <input type="email" id="email" name="email" class="form-control" placeholder="john@example.com" required>
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                    <div class="form-group">
                        <label for="password">Password *</label>
                        <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
                    </div>

                    <div class="form-group">
                        <label for="phone">Phone Number</label>
                        <input type="tel" id="phone" name="phone" class="form-control" placeholder="9876543210">
                    </div>
                </div>

                <div class="form-group">
                    <label for="role">User Role</label>
                    <select id="role" name="role" class="form-control" onchange="toggleDepartment()">
                        <option value="RESIDENT" selected>Resident Citizen</option>
                        <option value="PLANNER">Urban Planner</option>
                        <option value="ADMIN">City Administrator</option>
                    </select>
                </div>

                <div class="form-group" id="deptGroup" style="display: none;">
                    <label for="department">Department</label>
                    <input type="text" id="department" name="department" class="form-control" placeholder="e.g. Infrastructure Development, Traffic Management">
                </div>

                <div class="form-group">
                    <label for="address">Residential Address / Ward</label>
                    <input type="text" id="address" name="address" class="form-control" placeholder="Sector 4, Main Boulevard">
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; padding: 0.85rem; margin-top: 0.5rem;">
                    Complete Registration &rarr;
                </button>
            </form>

            <div style="margin-top: 1.5rem; text-align: center; font-size: 0.88rem; color: var(--text-secondary);">
                Already have an account? <a href="${pageContext.request.contextPath}/login.jsp">Sign In</a>
            </div>
        </div>
    </div>

    <script>
        function toggleDepartment() {
            const role = document.getElementById('role').value;
            const deptGroup = document.getElementById('deptGroup');
            deptGroup.style.display = (role === 'ADMIN' || role === 'PLANNER') ? 'block' : 'none';
        }
    </script>

</body>
</html>
