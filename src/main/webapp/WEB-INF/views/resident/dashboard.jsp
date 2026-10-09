<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Resident Citizen Portal - SmartCity AI" />
<c:set var="activeMenu" value="dashboard" />
<jsp:include page="../common/header.jsp" />

<div class="app-container">
    <jsp:include page="../common/sidebar.jsp" />

    <main class="main-content">
        <header class="topbar">
            <div class="page-title-group">
                <h1>Resident Citizen Portal</h1>
                <p>Welcome back, ${sessionScope.fullName != null ? sessionScope.fullName : 'Resident'}. Access city municipal services &amp; real-time city bulletins.</p>
            </div>
            <div class="topbar-actions">
                <a href="${pageContext.request.contextPath}/resident/services" class="btn btn-primary">
                    + Request City Service
                </a>
            </div>
        </header>

        <!-- KPI Stat Cards -->
        <div class="grid-stats">
            <div class="stat-card">
                <div class="stat-header">
                    <span class="stat-title">My Active Requests</span>
                    <span class="stat-icon">🛠️</span>
                </div>
                <div class="stat-value">${pendingRequests}</div>
                <div class="stat-subtext">Requests pending review or in progress</div>
            </div>

            <div class="stat-card">
                <div class="stat-header">
                    <span class="stat-title">Total Requests</span>
                    <span class="stat-icon">📋</span>
                </div>
                <div class="stat-value">${myRequests.size()}</div>
                <div class="stat-subtext">All submitted municipal tickets</div>
            </div>

            <div class="stat-card">
                <div class="stat-header">
                    <span class="stat-title">Feedback Submitted</span>
                    <span class="stat-icon">⭐</span>
                </div>
                <div class="stat-value">${myFeedback.size()}</div>
                <div class="stat-subtext">Reviews submitted to city council</div>
            </div>

            <div class="stat-card">
                <div class="stat-header">
                    <span class="stat-title">Active City Bulletins</span>
                    <span class="stat-icon">📢</span>
                </div>
                <div class="stat-value">${activeAlerts.size()}</div>
                <div class="stat-subtext">Live weather, traffic &amp; emergency alerts</div>
            </div>
        </div>

        <!-- Real-Time Alerts & City Services Status -->
        <div class="grid-two-col">
            <div class="panel-card">
                <div class="panel-header">
                    <h2 class="panel-title"><span>📢</span> Real-Time City Alerts &amp; Event Bulletins</h2>
                    <a href="${pageContext.request.contextPath}/resident/events" class="btn btn-secondary" style="font-size:0.8rem; padding:0.35rem 0.75rem;">View All</a>
                </div>
                <div style="display:flex; flex-direction:column; gap:0.75rem;">
                    <c:forEach var="ev" items="${activeAlerts}">
                        <div style="padding:0.9rem; background:rgba(255,255,255,0.02); border-left:3px solid ${ev.severity == 'ALERT' || ev.severity == 'CRITICAL' ? 'var(--danger)' : (ev.severity == 'WARNING' ? 'var(--warning)' : 'var(--info)')}; border-radius:4px;">
                            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:0.25rem;">
                                <strong>${ev.title}</strong>
                                <span class="badge badge-${ev.severity.toLowerCase()}">${ev.severity}</span>
                            </div>
                            <p style="font-size:0.88rem; color:var(--text-secondary); margin-bottom:0.4rem;">${ev.description}</p>
                            <span style="font-size:0.78rem; color:var(--text-muted);">📍 ${ev.location} | Type: ${ev.eventType}</span>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- Available City Services Directory -->
            <div class="panel-card">
                <div class="panel-header">
                    <h2 class="panel-title"><span>🛠️</span> Quick Municipal Services</h2>
                </div>
                <div style="display:grid; grid-template-columns:1fr 1fr; gap:0.75rem;">
                    <a href="${pageContext.request.contextPath}/resident/services" style="padding:0.85rem; background:rgba(6,182,212,0.1); border:1px solid rgba(6,182,212,0.25); border-radius:var(--radius-sm); text-align:center;">
                        <span style="font-size:1.5rem; display:block;">💧</span>
                        <strong style="color:#fff; font-size:0.85rem;">Water Supply</strong>
                    </a>
                    <a href="${pageContext.request.contextPath}/resident/services" style="padding:0.85rem; background:rgba(139,92,246,0.1); border:1px solid rgba(139,92,246,0.25); border-radius:var(--radius-sm); text-align:center;">
                        <span style="font-size:1.5rem; display:block;">⚡</span>
                        <strong style="color:#fff; font-size:0.85rem;">Electricity</strong>
                    </a>
                    <a href="${pageContext.request.contextPath}/resident/services" style="padding:0.85rem; background:rgba(16,185,129,0.1); border:1px solid rgba(16,185,129,0.25); border-radius:var(--radius-sm); text-align:center;">
                        <span style="font-size:1.5rem; display:block;">♻️</span>
                        <strong style="color:#fff; font-size:0.85rem;">Waste Pickup</strong>
                    </a>
                    <a href="${pageContext.request.contextPath}/resident/services" style="padding:0.85rem; background:rgba(245,158,11,0.1); border:1px solid rgba(245,158,11,0.25); border-radius:var(--radius-sm); text-align:center;">
                        <span style="font-size:1.5rem; display:block;">🚧</span>
                        <strong style="color:#fff; font-size:0.85rem;">Road Repair</strong>
                    </a>
                </div>
            </div>
        </div>

        <!-- My Service Requests Table -->
        <div class="panel-card">
            <div class="panel-header">
                <h2 class="panel-title"><span>📋</span> My Service Requests &amp; Resolutions</h2>
                <a href="${pageContext.request.contextPath}/resident/services" class="btn btn-secondary" style="font-size:0.8rem; padding:0.35rem 0.75rem;">Manage Requests</a>
            </div>
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Ticket #</th>
                            <th>Service Category</th>
                            <th>Title</th>
                            <th>Location</th>
                            <th>Priority</th>
                            <th>Status</th>
                            <th>Resolution Notes</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="req" items="${myRequests}">
                            <tr>
                                <td>#REQ-${req.requestId}</td>
                                <td><span class="badge badge-info">${req.serviceType}</span></td>
                                <td><strong>${req.title}</strong></td>
                                <td>${req.location}</td>
                                <td><span class="badge badge-${req.priority.toLowerCase()}">${req.priority}</span></td>
                                <td><span class="badge badge-${req.status.toLowerCase()}">${req.status}</span></td>
                                <td style="font-size:0.85rem; color:var(--text-secondary);">
                                    ${req.resolutionNotes != null ? req.resolutionNotes : '<em>Under investigation</em>'}
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<jsp:include page="../common/footer.jsp" />
