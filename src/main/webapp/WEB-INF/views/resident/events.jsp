<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="City Events & Real-Time Alerts - SmartCity AI" />
<c:set var="activeMenu" value="events" />
<jsp:include page="../common/header.jsp" />

<div class="app-container">
    <jsp:include page="../common/sidebar.jsp" />

    <main class="main-content">
        <header class="topbar">
            <div class="page-title-group">
                <h1>City Bulletins &amp; Real-Time Information</h1>
                <p>Live alerts, scheduled maintenance, traffic diversions &amp; public events across the city</p>
            </div>
            <c:if test="${sessionScope.role == 'ADMIN'}">
                <div class="topbar-actions">
                    <button type="button" class="btn btn-primary" onclick="openModal('newEventModal')">
                        + Broadcast City Bulletin
                    </button>
                </div>
            </c:if>
        </header>

        <c:if test="${not empty param.msg}">
            <div class="alert alert-success"><span>✓</span> ${param.msg}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger"><span>⚠</span> ${param.error}</div>
        </c:if>

        <!-- Active Alerts Feed -->
        <div class="panel-card">
            <div class="panel-header">
                <h2 class="panel-title"><span>📢</span> Active City Broadcasts</h2>
            </div>
            <div style="display:grid; grid-template-columns:repeat(auto-fit, minmax(320px, 1fr)); gap:1.25rem;">
                <c:forEach var="ev" items="${activeEvents}">
                    <div style="background:rgba(255,255,255,0.02); border:1px solid var(--border-color); border-left:4px solid ${ev.severity == 'ALERT' || ev.severity == 'CRITICAL' ? 'var(--danger)' : (ev.severity == 'WARNING' ? 'var(--warning)' : 'var(--info)')}; border-radius:var(--radius-sm); padding:1.25rem; display:flex; flex-direction:column; justify-content:space-between;">
                        <div>
                            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:0.5rem;">
                                <span class="badge badge-info">${ev.eventType}</span>
                                <span class="badge badge-${ev.severity.toLowerCase()}">${ev.severity}</span>
                            </div>
                            <h3 style="font-size:1.15rem; margin-bottom:0.5rem;">${ev.title}</h3>
                            <p style="font-size:0.9rem; color:var(--text-secondary); margin-bottom:1rem; line-height:1.5;">${ev.description}</p>
                        </div>
                        <div style="border-top:1px solid rgba(255,255,255,0.05); padding-top:0.75rem; font-size:0.8rem; color:var(--text-muted); display:flex; justify-content:space-between; align-items:center;">
                            <span>📍 ${ev.location}</span>
                            <span>⏱️ ${ev.startTime}</span>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- All City Events Table -->
        <div class="panel-card">
            <div class="panel-header">
                <h2 class="panel-title"><span>🗓️</span> All Bulletins Archive</h2>
            </div>
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Bulletin Title</th>
                            <th>Type</th>
                            <th>Location</th>
                            <th>Severity</th>
                            <th>Start Time</th>
                            <th>End Time</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${events}">
                            <tr>
                                <td><strong>${item.title}</strong></td>
                                <td><span class="badge badge-info">${item.eventType}</span></td>
                                <td>${item.location}</td>
                                <td><span class="badge badge-${item.severity.toLowerCase()}">${item.severity}</span></td>
                                <td style="font-size:0.82rem; color:var(--text-muted);">${item.startTime}</td>
                                <td style="font-size:0.82rem; color:var(--text-muted);">${item.endTime != null ? item.endTime : 'N/A'}</td>
                                <td>
                                    <span class="badge ${item.active ? 'badge-optimal' : 'badge-low'}">
                                        ${item.active ? 'Active' : 'Archived'}
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<!-- Modal: Broadcast City Bulletin (Admin only) -->
<div id="newEventModal" class="modal-backdrop" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.7); backdrop-filter:blur(8px); z-index:999; align-items:center; justify-content:center;">
    <div style="background:var(--bg-surface); border:1px solid var(--border-color); border-radius:var(--radius-md); padding:2rem; width:100%; max-width:500px;">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem;">
            <h3>Broadcast City Bulletin</h3>
            <button type="button" onclick="closeModal('newEventModal')" style="background:none; border:none; color:#fff; font-size:1.5rem; cursor:pointer;">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/events" method="POST">
            <input type="hidden" name="action" value="create">
            <div class="form-group">
                <label>Title</label>
                <input type="text" name="title" class="form-control" placeholder="e.g. Metro Construction Detour" required>
            </div>
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:1rem;">
                <div class="form-group">
                    <label>Event Type</label>
                    <select name="eventType" class="form-control" required>
                        <option value="TRAFFIC">TRAFFIC</option>
                        <option value="WEATHER">WEATHER</option>
                        <option value="MAINTENANCE">MAINTENANCE</option>
                        <option value="FESTIVAL">FESTIVAL</option>
                        <option value="EMERGENCY">EMERGENCY</option>
                        <option value="GENERAL">GENERAL</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Severity</label>
                    <select name="severity" class="form-control">
                        <option value="INFO">INFO</option>
                        <option value="WARNING">WARNING</option>
                        <option value="ALERT">ALERT</option>
                        <option value="CRITICAL">CRITICAL</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label>Location</label>
                <input type="text" name="location" class="form-control" placeholder="City-wide or Specific District" required>
            </div>
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:1rem;">
                <div class="form-group">
                    <label>Latitude</label>
                    <input type="number" step="any" name="latitude" class="form-control" value="12.9716">
                </div>
                <div class="form-group">
                    <label>Longitude</label>
                    <input type="number" step="any" name="longitude" class="form-control" value="77.5946">
                </div>
            </div>
            <div class="form-group">
                <label>Bulletin Description</label>
                <textarea name="description" class="form-control" placeholder="Public information notice..." required></textarea>
            </div>
            <div style="display:flex; gap:1rem; margin-top:1.5rem;">
                <button type="submit" class="btn btn-primary" style="flex:1;">Broadcast Now</button>
                <button type="button" class="btn btn-secondary" onclick="closeModal('newEventModal')">Cancel</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
