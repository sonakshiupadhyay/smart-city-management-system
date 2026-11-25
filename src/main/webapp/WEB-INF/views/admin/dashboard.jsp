<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="City Administrator Dashboard - SmartCity" />
<c:set var="activeMenu" value="dashboard" />
<jsp:include page="../common/header.jsp" />

<div class="app-container">
    <jsp:include page="../common/sidebar.jsp" />

    <main class="main-content">
        <header class="topbar">
            <div class="page-title-group">
                <h1>City Operations Center</h1>
                <p>Infrastructure management, rule-based resource analytics &amp; simulated telemetry overview</p>
            </div>
            <div class="topbar-actions">
                <div class="live-pulse-badge">
                    <span class="pulse-dot"></span> Sensor Grid (Simulated): <span id="liveSensorCount">${activeSensors}</span> Active Nodes
                </div>
            </div>
        </header>

        <!-- KPI Stat Cards -->
        <div class="grid-stats">
            <div class="stat-card">
                <div class="stat-header">
                    <span class="stat-title">Resource Efficiency</span>
                    <span class="stat-icon">⚡</span>
                </div>
                <div class="stat-value">${optimizationReport.overallEfficiency}%</div>
                <div class="stat-subtext">${criticalResources} critical, ${warningResources} warning grids</div>
            </div>

            <div class="stat-card">
                <div class="stat-header">
                    <span class="stat-title">Traffic Bottlenecks</span>
                    <span class="stat-icon">🚦</span>
                </div>
                <div class="stat-value">${congestedAreas.size()}</div>
                <div class="stat-subtext">Active high/severe congestion spots</div>
            </div>

            <div class="stat-card">
                <div class="stat-header">
                    <span class="stat-title">Public Safety Alerts</span>
                    <span class="stat-icon">🛡️</span>
                </div>
                <div class="stat-value">${criticalIncidents}</div>
                <div class="stat-subtext">Critical &amp; high priority open alerts</div>
            </div>

            <div class="stat-card">
                <div class="stat-header">
                    <span class="stat-title">Citizen Requests</span>
                    <span class="stat-icon">📋</span>
                </div>
                <div class="stat-value">${pendingRequests} / ${totalRequests}</div>
                <div class="stat-subtext">Pending / Total service requests</div>
            </div>
        </div>

        <!-- Charts and Analytics Insights -->
        <div class="grid-two-col">
            <div class="panel-card">
                <div class="panel-header">
                    <h2 class="panel-title"><span>📊</span> City Resource Allocation &amp; Capacity</h2>
                </div>
                <div style="height: 280px; position: relative;">
                    <canvas id="resourceChart"></canvas>
                </div>
            </div>

            <div class="panel-card">
                <div class="panel-header">
                    <h2 class="panel-title"><span>📈</span> Resource Optimization Analytics</h2>
                </div>
                <div style="font-size: 0.9rem; line-height: 1.6;">
                    <c:choose>
                        <c:when test="${not empty optimizationReport.recommendations}">
                            <ul style="list-style: none; display: flex; flex-direction: column; gap: 0.75rem;">
                                <c:forEach var="rec" items="${optimizationReport.recommendations}">
                                    <li style="padding: 0.75rem; background: rgba(245, 158, 11, 0.1); border-left: 3px solid var(--warning); border-radius: 4px;">
                                        ${rec}
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <p style="color: var(--success);">✓ All resource grids (Energy, Water, Waste) operating at optimal efficiency.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Simulated Telemetry Alerts & Traffic Hotspots -->
        <div class="grid-equal-col">
            <div class="panel-card">
                <div class="panel-header">
                    <h2 class="panel-title"><span>🚨</span> Simulated Sensor Alerts (Auto-Refreshed)</h2>
                </div>
                <div id="liveAlertsContainer">
                    <c:choose>
                        <c:when test="${not empty recentAlerts}">
                            <ul style="list-style: none; display: flex; flex-direction: column; gap: 0.5rem;">
                                <c:forEach var="al" items="${recentAlerts}">
                                    <li style="padding: 0.6rem 0.8rem; background: rgba(239, 68, 68, 0.1); border-left: 3px solid var(--danger); border-radius: 4px; font-size: 0.85rem;">
                                        ${al}
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <p style="color: var(--text-muted); font-size: 0.9rem;">No active alert violations on sensor nodes.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="panel-card">
                <div class="panel-header">
                    <h2 class="panel-title"><span>🚦</span> Traffic Congestion Hotspots</h2>
                    <a href="${pageContext.request.contextPath}/admin/traffic" class="btn btn-secondary" style="font-size: 0.8rem; padding: 0.35rem 0.75rem;">View All</a>
                </div>
                <div class="table-responsive">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Location</th>
                                <th>Congestion</th>
                                <th>Avg Speed</th>
                                <th>Suggested Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="t" items="${congestedAreas}">
                                <tr>
                                    <td><strong>${t.location}</strong></td>
                                    <td>
                                        <span class="badge badge-${t.congestionLevel.toLowerCase()}">${t.congestionLevel}</span>
                                    </td>
                                    <td>${t.averageSpeed} km/h</td>
                                    <td style="color: var(--text-secondary); font-size: 0.85rem;">${t.suggestion}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </main>
</div>

<script>
    // Initialize Resource Chart
    const ctx = document.getElementById('resourceChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: [
                <c:forEach var="r" items="${resources}" varStatus="status">
                    '${r.resourceName}'<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            ],
            datasets: [
                {
                    label: 'Current Usage',
                    data: [
                        <c:forEach var="r" items="${resources}" varStatus="status">
                            ${r.currentUsage}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    ],
                    backgroundColor: 'rgba(6, 182, 212, 0.7)',
                    borderColor: '#06b6d4',
                    borderWidth: 1
                },
                {
                    label: 'Total Capacity',
                    data: [
                        <c:forEach var="r" items="${resources}" varStatus="status">
                            ${r.totalCapacity}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    ],
                    backgroundColor: 'rgba(255, 255, 255, 0.1)',
                    borderColor: 'rgba(255, 255, 255, 0.3)',
                    borderWidth: 1
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    grid: { color: 'rgba(255, 255, 255, 0.05)' },
                    ticks: { color: '#94a3b8' }
                },
                x: {
                    grid: { display: false },
                    ticks: { color: '#94a3b8' }
                }
            },
            plugins: {
                legend: { labels: { color: '#f8fafc' } }
            }
        }
    });
</script>

<jsp:include page="../common/footer.jsp" />
