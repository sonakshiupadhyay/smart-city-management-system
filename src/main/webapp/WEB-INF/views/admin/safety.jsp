<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Public Safety - SmartCity" />
<c:set var="activeMenu" value="safety" />
<jsp:include page="../common/header.jsp" />

<div class="app-container">
    <jsp:include page="../common/sidebar.jsp" />

    <main class="main-content">
        <header class="topbar">
            <div class="page-title-group">
                <h1>Public Safety &amp; Emergency Response</h1>
                <p>Simulated security node status, emergency dispatch &amp; incident lifecycle tracking</p>
            </div>
            <div class="topbar-actions">
                <button type="button" class="btn btn-primary" onclick="openModal('reportIncidentModal')">
                    🚨 Report Emergency Incident
                </button>
            </div>
        </header>

        <c:if test="${not empty param.msg}">
            <div class="alert alert-success"><span>✓</span> ${param.msg}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger"><span>⚠</span> ${param.error}</div>
        </c:if>

        <!-- Simulated Security Node Status Grid -->
        <div class="panel-card">
            <div class="panel-header">
                <h2 class="panel-title"><span>📹</span> Security Monitoring Nodes (Simulated Status)</h2>
            </div>
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 1.25rem;">
                <c:forEach var="cam" items="${cctvSensors}">
                    <div style="background: rgba(0,0,0,0.4); border: 1px solid var(--border-color); border-radius: var(--radius-sm); overflow: hidden;">
                        <div style="height: 140px; background: #070b12; position: relative; display: flex; align-items: center; justify-content: center;">
                            <span style="font-size: 2.2rem; opacity: 0.3;">🎥</span>
                            <div style="position: absolute; top: 8px; left: 8px; font-size: 0.75rem; background: rgba(0,0,0,0.6); padding: 2px 6px; border-radius: 4px; font-family: monospace;">
                                CAM #${cam.sensorName}
                            </div>
                            <div style="position: absolute; top: 8px; right: 8px;">
                                <span class="badge ${cam.status == 'ACTIVE' ? 'badge-optimal' : 'badge-warning'}">
                                    <span class="pulse-dot" style="display:inline-block; margin-right:4px;"></span>${cam.status}
                                </span>
                            </div>
                            <div style="position: absolute; bottom: 8px; left: 8px; font-size: 0.75rem; color: #94a3b8;">
                                📍 ${cam.location}
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- Incidents Table -->
        <div class="panel-card">
            <div class="panel-header">
                <h2 class="panel-title"><span>🛡️</span> Active Public Safety Incidents</h2>
            </div>
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Incident Type</th>
                            <th>Location</th>
                            <th>Description</th>
                            <th>Severity</th>
                            <th>Status</th>
                            <th>Reported At</th>
                            <th>Update Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="inc" items="${incidents}">
                            <tr>
                                <td><strong>${inc.incidentType}</strong></td>
                                <td>${inc.location}</td>
                                <td style="max-width: 250px;">${inc.description}</td>
                                <td>
                                    <span class="badge badge-${inc.severity.toLowerCase()}">${inc.severity}</span>
                                </td>
                                <td>
                                    <span class="badge badge-${inc.status.toLowerCase()}">${inc.status}</span>
                                </td>
                                <td style="font-size: 0.82rem; color: var(--text-muted);">${inc.reportedAt}</td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/admin/safety" method="POST" style="display:flex; gap:0.4rem;">
                                        <input type="hidden" name="action" value="updateStatus">
                                        <input type="hidden" name="incidentId" value="${inc.incidentId}">
                                        <select name="status" class="form-control" style="padding:0.3rem 0.5rem; font-size:0.8rem; width:120px;">
                                            <option value="REPORTED" ${inc.status == 'REPORTED' ? 'selected' : ''}>REPORTED</option>
                                            <option value="IN_PROGRESS" ${inc.status == 'IN_PROGRESS' ? 'selected' : ''}>IN_PROGRESS</option>
                                            <option value="RESOLVED" ${inc.status == 'RESOLVED' ? 'selected' : ''}>RESOLVED</option>
                                            <option value="CLOSED" ${inc.status == 'CLOSED' ? 'selected' : ''}>CLOSED</option>
                                        </select>
                                        <button type="submit" class="btn btn-secondary" style="padding:0.3rem 0.6rem; font-size:0.78rem;">Save</button>
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

<!-- Modal: Report Emergency Incident -->
<div id="reportIncidentModal" class="modal-backdrop" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.7); backdrop-filter:blur(8px); z-index:999; align-items:center; justify-content:center;">
    <div style="background:var(--bg-surface); border:1px solid var(--border-color); border-radius:var(--radius-md); padding:2rem; width:100%; max-width:500px;">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem;">
            <h3>Report Public Safety Incident</h3>
            <button type="button" onclick="closeModal('reportIncidentModal')" style="background:none; border:none; color:#fff; font-size:1.5rem; cursor:pointer;">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/admin/safety" method="POST">
            <input type="hidden" name="action" value="report">
            <div class="form-group">
                <label>Incident Type</label>
                <select name="incidentType" class="form-control" required>
                    <option value="SECURITY">SECURITY (Breach, Theft)</option>
                    <option value="EMERGENCY">EMERGENCY (Medical, Rescue)</option>
                    <option value="FIRE">FIRE (Hazard, Outbreak)</option>
                    <option value="ACCIDENT">ACCIDENT (Road Collision)</option>
                    <option value="OTHER">OTHER</option>
                </select>
            </div>
            <div class="form-group">
                <label>Incident Location</label>
                <input type="text" name="location" class="form-control" placeholder="e.g. Central Market Entrance" required>
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
            <div class="form-group">
                <label>Severity Level</label>
                <select name="severity" class="form-control">
                    <option value="LOW">LOW</option>
                    <option value="MEDIUM" selected>MEDIUM</option>
                    <option value="HIGH">HIGH</option>
                    <option value="CRITICAL">CRITICAL</option>
                </select>
            </div>
            <div class="form-group">
                <label>Incident Details &amp; Observation</label>
                <textarea name="description" class="form-control" placeholder="Describe the incident..." required></textarea>
            </div>
            <div style="display:flex; gap:1rem; margin-top:1.5rem;">
                <button type="submit" class="btn btn-primary" style="flex:1;">Dispatch Alert</button>
                <button type="button" class="btn btn-secondary" onclick="closeModal('reportIncidentModal')">Cancel</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
