<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<aside class="sidebar">
    <div class="sidebar-logo">
        <div class="logo-badge">⚡</div>
        <div>
            <div class="sidebar-brand-name">SmartCity</div>
            <div class="sidebar-role-badge">${sessionScope.role != null ? sessionScope.role : 'GUEST'}</div>
        </div>
    </div>

    <ul class="nav-menu">
        <c:choose>
            <%-- CITY ADMINISTRATOR NAVIGATION --%>
            <c:when test="${sessionScope.role == 'ADMIN'}">
                <li class="nav-item ${activeMenu == 'dashboard' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/admin/dashboard">
                        <span>📊</span> <span>Overview Dashboard</span>
                    </a>
                </li>
                <li class="nav-item ${activeMenu == 'resources' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/admin/resources">
                        <span>⚡</span> <span>Resource Management</span>
                    </a>
                </li>
                <li class="nav-item ${activeMenu == 'traffic' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/admin/traffic">
                        <span>🚦</span> <span>Traffic Monitoring</span>
                    </a>
                </li>
                <li class="nav-item ${activeMenu == 'safety' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/admin/safety">
                        <span>🛡️</span> <span>Public Safety</span>
                    </a>
                </li>
                <li class="nav-item ${activeMenu == 'services' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/admin/services">
                        <span>📋</span> <span>Service Requests</span>
                    </a>
                </li>
                <li class="nav-item ${activeMenu == 'feedback' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/admin/feedback">
                        <span>💬</span> <span>Citizen Feedback</span>
                    </a>
                </li>
            </c:when>

            <%-- URBAN PLANNER NAVIGATION --%>
            <c:when test="${sessionScope.role == 'PLANNER'}">
                <li class="nav-item ${activeMenu == 'dashboard' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/planner/dashboard">
                        <span>🏗️</span> <span>Planning Hub</span>
                    </a>
                </li>
                <li class="nav-item ${activeMenu == 'infrastructure' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/planner/infrastructure">
                        <span>📐</span> <span>Infrastructure Projects</span>
                    </a>
                </li>
                <li class="nav-item ${activeMenu == 'environment' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/planner/environment">
                        <span>🌿</span> <span>Environmental Monitor</span>
                    </a>
                </li>
            </c:when>

            <%-- RESIDENT CITIZEN NAVIGATION --%>
            <c:when test="${sessionScope.role == 'RESIDENT'}">
                <li class="nav-item ${activeMenu == 'dashboard' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/resident/dashboard">
                        <span>🏠</span> <span>Citizen Hub</span>
                    </a>
                </li>
                <li class="nav-item ${activeMenu == 'services' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/resident/services">
                        <span>🛠️</span> <span>City Services</span>
                    </a>
                </li>
                <li class="nav-item ${activeMenu == 'feedback' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/resident/feedback">
                        <span>⭐</span> <span>Submit Feedback</span>
                    </a>
                </li>
                <li class="nav-item ${activeMenu == 'events' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/resident/events">
                        <span>📢</span> <span>Alerts &amp; Events</span>
                    </a>
                </li>
            </c:when>
        </c:choose>
    </ul>

    <div class="sidebar-footer">
        <div class="user-profile-preview">
            <div class="user-avatar">${sessionScope.fullName != null ? sessionScope.fullName.substring(0,1) : 'U'}</div>
            <div>
                <div class="user-meta-name">${sessionScope.fullName != null ? sessionScope.fullName : 'Guest'}</div>
                <div class="user-meta-role">${sessionScope.role != null ? sessionScope.role : 'Visitor'}</div>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/logout" title="Sign Out" style="color:var(--danger); font-size:1.1rem; padding:0.4rem;">
            🚪
        </a>
    </div>
</aside>
