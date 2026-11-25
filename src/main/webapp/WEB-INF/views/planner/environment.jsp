<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Environmental Monitoring - SmartCity" />
<c:set var="activeMenu" value="environment" />
<jsp:include page="../common/header.jsp" />

<div class="app-container">
    <jsp:include page="../common/sidebar.jsp" />

    <main class="main-content">
        <header class="topbar">
            <div class="page-title-group">
                <h1>Environmental Impact &amp; Sustainability</h1>
                <p>Tracked &amp; simulated air quality (AQI), greenhouse emissions, noise decibels &amp; rule-based advisory</p>
            </div>
            <div class="topbar-actions">
                <button type="button" class="btn btn-primary" onclick="openModal('addEnvModal')">
                    + Deploy Monitoring Station
                </button>
            </div>
        </header>

        <c:if test="${not empty param.msg}">
            <div class="alert alert-success"><span>✓</span> ${param.msg}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger"><span>⚠</span> ${param.error}</div>
        </c:if>

        <!-- Sustainability Composite Score Banner -->
        <div class="panel-card" style="background: linear-gradient(135deg, rgba(16,185,129,0.1), rgba(6,182,212,0.1)); border-left: 4px solid var(--success);">
            <div style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:1rem;">
                <div>
                    <h3 style="color:var(--success); margin-bottom:0.25rem;">City Eco-Index: ${sustainabilityReport.sustainabilityIndex} / 100</h3>
                    <p style="font-size:0.92rem; color:var(--text-secondary);">
                        Aggregate Status: <strong>${sustainabilityReport.overallStatus}</strong> | Average AQI: <strong>${sustainabilityReport.averageAqi}</strong> | Mean Noise: <strong>${sustainabilityReport.averageNoise} dB</strong>
                    </p>
                </div>
                <div class="stat-value" style="font-size:2.8rem; color:var(--success);">
                    🌱
                </div>
            </div>
        </div>

        <!-- Environmental Stations Table -->
        <div class="panel-card">
            <div class="panel-header">
                <h2 class="panel-title"><span>🌿</span> Urban Environmental Monitoring Stations</h2>
            </div>
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Location Station</th>
                            <th>Air Quality (AQI)</th>
                            <th>Noise</th>
                            <th>CO2 Level</th>
                            <th>Temp / Humidity</th>
                            <th>Status</th>
                            <th>Sustainability Advisory</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="env" items="${envList}">
                            <tr>
                                <td><strong>${env.location}</strong></td>
                                <td>
                                    <strong style="font-size:1.1rem;">${env.airQualityIndex}</strong>
                                    <c:choose>
                                        <c:when test="${env.airQualityIndex <= 50}"><span style="color:var(--success); font-size:0.75rem;">(Good)</span></c:when>
                                        <c:when test="${env.airQualityIndex <= 100}"><span style="color:var(--warning); font-size:0.75rem;">(Moderate)</span></c:when>
                                        <c:otherwise><span style="color:var(--danger); font-size:0.75rem;">(Unhealthy)</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${env.noiseLevel} dB</td>
                                <td>${env.co2Level} ppm</td>
                                <td>${env.temperature}°C / ${env.humidity}%</td>
                                <td>
                                    <span class="badge badge-${env.envStatus.toLowerCase()}">${env.envStatus}</span>
                                </td>
                                <td style="font-size:0.85rem; color:var(--text-secondary); max-width:240px;">
                                    ${env.recommendation}
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/planner/environment" method="POST" style="display:inline;" onsubmit="return confirm('Remove environmental station?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="envId" value="${env.envId}">
                                        <button type="submit" class="btn btn-danger" style="padding:0.3rem 0.6rem; font-size:0.78rem;">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<!-- Modal: Deploy Environmental Station -->
<div id="addEnvModal" class="modal-backdrop" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.7); backdrop-filter:blur(8px); z-index:999; align-items:center; justify-content:center;">
    <div style="background:var(--bg-surface); border:1px solid var(--border-color); border-radius:var(--radius-md); padding:2rem; width:100%; max-width:520px;">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem;">
            <h3>Deploy Environmental Station</h3>
            <button type="button" onclick="closeModal('addEnvModal')" style="background:none; border:none; color:#fff; font-size:1.5rem; cursor:pointer;">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/planner/environment" method="POST">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
                <label>Station Location / District</label>
                <input type="text" name="location" class="form-control" placeholder="e.g. Waterfront Promenade" required>
            </div>
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:1rem;">
                <div class="form-group">
                    <label>Latitude</label>
                    <input type="number" step="any" name="latitude" class="form-control" value="12.9716" required>
                </div>
                <div class="form-group">
                    <label>Longitude</label>
                    <input type="number" step="any" name="longitude" class="form-control" value="77.5946" required>
                </div>
            </div>
            <div style="display:grid; grid-template-columns:1fr 1fr 1fr; gap:1rem;">
                <div class="form-group">
                    <label>AQI Index</label>
                    <input type="number" name="airQualityIndex" class="form-control" placeholder="65" required>
                </div>
                <div class="form-group">
                    <label>Noise (dB)</label>
                    <input type="number" step="any" name="noiseLevel" class="form-control" placeholder="55.0" required>
                </div>
                <div class="form-group">
                    <label>Pollution (PM2.5)</label>
                    <input type="number" step="any" name="pollutionLevel" class="form-control" placeholder="28.0" required>
                </div>
            </div>
            <div style="display:grid; grid-template-columns:1fr 1fr 1fr; gap:1rem;">
                <div class="form-group">
                    <label>Temp (°C)</label>
                    <input type="number" step="any" name="temperature" class="form-control" placeholder="27.5" required>
                </div>
                <div class="form-group">
                    <label>Humidity (%)</label>
                    <input type="number" step="any" name="humidity" class="form-control" placeholder="68.0" required>
                </div>
                <div class="form-group">
                    <label>CO2 (ppm)</label>
                    <input type="number" step="any" name="co2Level" class="form-control" placeholder="390.0" required>
                </div>
            </div>
            <div class="form-group">
                <label>Sustainability Advisory</label>
                <textarea name="recommendation" class="form-control" placeholder="Recommended mitigation measures..."></textarea>
            </div>
            <div style="display:flex; gap:1rem; margin-top:1.5rem;">
                <button type="submit" class="btn btn-primary" style="flex:1;">Deploy Station</button>
                <button type="button" class="btn btn-secondary" onclick="closeModal('addEnvModal')">Cancel</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
