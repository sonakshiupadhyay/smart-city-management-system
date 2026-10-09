<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Urban Planning Studio - SmartCity AI" />
<c:set var="activeMenu" value="dashboard" />
<jsp:include page="../common/header.jsp" />

<div class="app-container">
    <jsp:include page="../common/sidebar.jsp" />

    <main class="main-content">
        <header class="topbar">
            <div class="page-title-group">
                <h1>Urban Planning &amp; Sustainability Studio</h1>
                <p>Data-driven capital infrastructure development &amp; environmental impact intelligence</p>
            </div>
            <div class="topbar-actions">
                <a href="${pageContext.request.contextPath}/planner/infrastructure" class="btn btn-primary">
                    + New Project Proposal
                </a>
            </div>
        </header>

        <!-- KPI Stat Cards -->
        <div class="grid-stats">
            <div class="stat-card">
                <div class="stat-header">
                    <span class="stat-title">Sustainability Index</span>
                    <span class="stat-icon">🌿</span>
                </div>
                <div class="stat-value" style="color:var(--success);">${envMetrics.sustainabilityIndex} / 100</div>
                <div class="stat-subtext">Status: <strong>${envMetrics.overallStatus}</strong></div>
            </div>

            <div class="stat-card">
                <div class="stat-header">
                    <span class="stat-title">Average City AQI</span>
                    <span class="stat-icon">💨</span>
                </div>
                <div class="stat-value">${envMetrics.averageAqi}</div>
                <div class="stat-subtext">CO2: ${envMetrics.averageCo2} ppm | Noise: ${envMetrics.averageNoise} dB</div>
            </div>

            <div class="stat-card">
                <div class="stat-header">
                    <span class="stat-title">Capital Works Budget</span>
                    <span class="stat-icon">💰</span>
                </div>
                <div class="stat-value">$<fmt:formatNumber value="${totalBudget / 1000000}" maxFractionDigits="1" />M</div>
                <div class="stat-subtext">Spent: $<fmt:formatNumber value="${totalSpent / 1000000}" maxFractionDigits="1" />M (${String.format("%.1f", (totalSpent / (totalBudget > 0 ? totalBudget : 1)) * 100)}%)</div>
            </div>

            <div class="stat-card">
                <div class="stat-header">
                    <span class="stat-title">Active Projects</span>
                    <span class="stat-icon">🏗️</span>
                </div>
                <div class="stat-value">${inProgressCount} In-Progress</div>
                <div class="stat-subtext">${proposedCount} proposed, ${approvedCount} approved</div>
            </div>
        </div>

        <!-- Environmental Trends & Infrastructure Distribution Charts -->
        <div class="grid-two-col">
            <div class="panel-card">
                <div class="panel-header">
                    <h2 class="panel-title"><span>📊</span> Sector Air Quality Index (AQI)</h2>
                </div>
                <div style="height: 280px; position: relative;">
                    <canvas id="aqiChart"></canvas>
                </div>
            </div>

            <div class="panel-card">
                <div class="panel-header">
                    <h2 class="panel-title"><span>📐</span> Project Pipeline Status</h2>
                </div>
                <div style="height: 280px; position: relative;">
                    <canvas id="pipelineChart"></canvas>
                </div>
            </div>
        </div>

        <!-- High-Impact Infrastructure Projects Table -->
        <div class="panel-card">
            <div class="panel-header">
                <h2 class="panel-title"><span>🏗️</span> Key Infrastructure Initiatives</h2>
                <a href="${pageContext.request.contextPath}/planner/infrastructure" class="btn btn-secondary" style="font-size:0.8rem; padding:0.35rem 0.75rem;">View All Projects</a>
            </div>
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Project Name</th>
                            <th>Category</th>
                            <th>Location</th>
                            <th>Budget</th>
                            <th>Impact Score</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="proj" items="${projects}">
                            <tr>
                                <td><strong>${proj.projectName}</strong></td>
                                <td><span class="badge badge-info">${proj.category}</span></td>
                                <td>${proj.location}</td>
                                <td>$<fmt:formatNumber value="${proj.budget}" /></td>
                                <td>
                                    <span style="font-weight:700; color:var(--primary);">⭐ ${proj.impactScore} / 10</span>
                                </td>
                                <td>
                                    <span class="badge badge-${proj.status.toLowerCase()}">${proj.status}</span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </main>
</div>

<script>
    // AQI Chart
    const aqiCtx = document.getElementById('aqiChart').getContext('2d');
    new Chart(aqiCtx, {
        type: 'line',
        data: {
            labels: [
                <c:forEach var="e" items="${envList}" varStatus="status">
                    '${e.location}'<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            ],
            datasets: [{
                label: 'Air Quality Index (AQI)',
                data: [
                    <c:forEach var="e" items="${envList}" varStatus="status">
                        ${e.airQualityIndex}<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                ],
                borderColor: '#10b981',
                backgroundColor: 'rgba(16, 185, 129, 0.1)',
                tension: 0.3,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: { grid: { color: 'rgba(255, 255, 255, 0.05)' }, ticks: { color: '#94a3b8' } },
                x: { grid: { display: false }, ticks: { color: '#94a3b8' } }
            },
            plugins: { legend: { labels: { color: '#f8fafc' } } }
        }
    });

    // Pipeline Doughnut Chart
    const pipeCtx = document.getElementById('pipelineChart').getContext('2d');
    new Chart(pipeCtx, {
        type: 'doughnut',
        data: {
            labels: ['Proposed', 'Approved', 'In Progress', 'Completed'],
            datasets: [{
                data: [${proposedCount}, ${approvedCount}, ${inProgressCount}, ${completedCount}],
                backgroundColor: ['#3b82f6', '#06b6d4', '#f59e0b', '#10b981']
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { position: 'bottom', labels: { color: '#f8fafc' } } }
        }
    });
</script>

<jsp:include page="../common/footer.jsp" />
