<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Traffic Monitoring - SmartCity AI" />
<c:set var="activeMenu" value="traffic" />
<jsp:include page="../common/header.jsp" />

<div class="app-container">
    <jsp:include page="../common/sidebar.jsp" />

    <main class="main-content">
        <header class="topbar">
            <div class="page-title-group">
                <h1>Smart Traffic Operations</h1>
                <p>Real-time vehicle telemetry, adaptive signal suggestions &amp; congestion mitigation</p>
            </div>
            <div class="topbar-actions">
                <button type="button" class="btn btn-primary" onclick="openModal('addTrafficModal')">
                    + Deploy Traffic Sensor Node
                </button>
            </div>
        </header>

        <c:if test="${not empty param.msg}">
            <div class="alert alert-success"><span>✓</span> ${param.msg}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger"><span>⚠</span> ${param.error}</div>
        </c:if>

        <!-- AI Congestion Optimization Recommendations -->
        <div class="panel-card" style="border-left: 4px solid var(--primary);">
            <div class="panel-header">
                <h2 class="panel-title"><span>🤖</span> AI Traffic Routing Recommendations</h2>
            </div>
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 1rem;">
                <c:forEach var="item" items="${bottleneckAnalysis}">
                    <div style="background: rgba(255,255,255,0.03); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 1rem;">
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem;">
                            <strong>${item.location}</strong>
                            <span class="badge badge-${item.congestionLevel.toString().toLowerCase()}">${item.congestionLevel}</span>
                        </div>
                        <p style="font-size: 0.88rem; color: var(--text-secondary); margin-bottom: 0.5rem;">
                            🚗 Vehicles: ${item.vehicleCount} | ⚡ Speed: ${item.averageSpeed} km/h
                        </p>
                        <p style="font-size: 0.85rem; color: var(--primary);">
                            💡 ${item.aiRecommendation}
                        </p>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- All Traffic Sensor Nodes Table -->
        <div class="panel-card">
            <div class="panel-header">
                <h2 class="panel-title"><span>🚦</span> City Arterial Monitoring Points</h2>
            </div>
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Corridor / Junction</th>
                            <th>Coordinates</th>
                            <th>Congestion</th>
                            <th>Vehicle Count</th>
                            <th>Average Speed</th>
                            <th>Active Alert</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="tf" items="${trafficList}">
                            <tr>
                                <td><strong>${tf.location}</strong></td>
                                <td style="font-size: 0.82rem; color: var(--text-muted);">${tf.latitude}, ${tf.longitude}</td>
                                <td>
                                    <span class="badge badge-${tf.congestionLevel.toLowerCase()}">${tf.congestionLevel}</span>
                                </td>
                                <td>${tf.vehicleCount} / hr</td>
                                <td>${tf.averageSpeed} km/h</td>
                                <td>${tf.alertType != null ? tf.alertType : 'Normal'}</td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/admin/traffic" method="POST" style="display:inline;" onsubmit="return confirm('Remove sensor point?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="trafficId" value="${tf.trafficId}">
                                        <button type="submit" class="btn btn-danger" style="padding: 0.35rem 0.65rem; font-size: 0.78rem;">Delete</button>
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

<!-- Modal: Deploy Traffic Sensor Node -->
<div id="addTrafficModal" class="modal-backdrop" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.7); backdrop-filter:blur(8px); z-index:999; align-items:center; justify-content:center;">
    <div style="background:var(--bg-surface); border:1px solid var(--border-color); border-radius:var(--radius-md); padding:2rem; width:100%; max-width:500px;">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem;">
            <h3>Deploy Traffic Sensor Node</h3>
            <button type="button" onclick="closeModal('addTrafficModal')" style="background:none; border:none; color:#fff; font-size:1.5rem; cursor:pointer;">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/admin/traffic" method="POST">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
                <label>Junction / Highway Corridor</label>
                <input type="text" name="location" class="form-control" placeholder="e.g. Ring Road Cloverleaf" required>
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
                <label>Congestion Level</label>
                <select name="congestionLevel" class="form-control">
                    <option value="LOW">LOW (Free flow)</option>
                    <option value="MODERATE">MODERATE (Dense)</option>
                    <option value="HIGH">HIGH (Congested)</option>
                    <option value="SEVERE">SEVERE (Standstill)</option>
                </select>
            </div>
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:1rem;">
                <div class="form-group">
                    <label>Average Speed (km/h)</label>
                    <input type="number" step="any" name="averageSpeed" class="form-control" placeholder="35.0" required>
                </div>
                <div class="form-group">
                    <label>Vehicles Per Hour</label>
                    <input type="number" name="vehicleCount" class="form-control" placeholder="320" required>
                </div>
            </div>
            <div class="form-group">
                <label>Alert Notice (Optional)</label>
                <input type="text" name="alertType" class="form-control" placeholder="e.g. Road Construction, Accident Alert">
            </div>
            <div style="display:flex; gap:1rem; margin-top:1.5rem;">
                <button type="submit" class="btn btn-primary" style="flex:1;">Deploy Sensor</button>
                <button type="button" class="btn btn-secondary" onclick="closeModal('addTrafficModal')">Cancel</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
