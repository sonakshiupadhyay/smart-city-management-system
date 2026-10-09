<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="City Service Requests - SmartCity AI" />
<c:set var="activeMenu" value="services" />
<jsp:include page="../common/header.jsp" />

<div class="app-container">
    <jsp:include page="../common/sidebar.jsp" />

    <main class="main-content">
        <header class="topbar">
            <div class="page-title-group">
                <h1>Municipal City Services</h1>
                <p>Report infrastructure faults, request utility support &amp; monitor resolution progress</p>
            </div>
            <div class="topbar-actions">
                <button type="button" class="btn btn-primary" onclick="openModal('newServiceModal')">
                    + File Service Request
                </button>
            </div>
        </header>

        <c:if test="${not empty param.msg}">
            <div class="alert alert-success"><span>✓</span> ${param.msg}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger"><span>⚠</span> ${param.error}</div>
        </c:if>

        <div class="panel-card">
            <div class="panel-header">
                <h2 class="panel-title"><span>📋</span> My Filed Requests</h2>
            </div>
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Ticket #</th>
                            <th>Service Category</th>
                            <th>Subject / Details</th>
                            <th>Location</th>
                            <th>Priority</th>
                            <th>Status</th>
                            <th>Submitted On</th>
                            <th>Municipal Resolution</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="req" items="${serviceRequests}">
                            <tr>
                                <td>#REQ-${req.requestId}</td>
                                <td><span class="badge badge-info">${req.serviceType}</span></td>
                                <td>
                                    <strong>${req.title}</strong><br>
                                    <span style="font-size:0.85rem; color:var(--text-secondary);">${req.description}</span>
                                </td>
                                <td>${req.location}</td>
                                <td><span class="badge badge-${req.priority.toLowerCase()}">${req.priority}</span></td>
                                <td><span class="badge badge-${req.status.toLowerCase()}">${req.status}</span></td>
                                <td style="font-size:0.82rem; color:var(--text-muted);">${req.createdAt}</td>
                                <td style="font-size:0.85rem; color:var(--text-secondary);">
                                    ${req.resolutionNotes != null ? req.resolutionNotes : '<em>Department investigating</em>'}
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<!-- Modal: File Service Request -->
<div id="newServiceModal" class="modal-backdrop" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.7); backdrop-filter:blur(8px); z-index:999; align-items:center; justify-content:center;">
    <div style="background:var(--bg-surface); border:1px solid var(--border-color); border-radius:var(--radius-md); padding:2rem; width:100%; max-width:520px;">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem;">
            <h3>File Municipal Service Request</h3>
            <button type="button" onclick="closeModal('newServiceModal')" style="background:none; border:none; color:#fff; font-size:1.5rem; cursor:pointer;">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/resident/services" method="POST">
            <input type="hidden" name="action" value="create">
            <div class="form-group">
                <label>Service Category</label>
                <select name="serviceType" class="form-control" required>
                    <option value="WATER_SUPPLY">Water Supply</option>
                    <option value="ELECTRICITY">Electricity / Power Grid</option>
                    <option value="WASTE_COLLECTION">Waste &amp; Garbage Collection</option>
                    <option value="ROAD_REPAIR" selected>Road &amp; Pothole Repair</option>
                    <option value="STREET_LIGHTING">Street Lighting</option>
                    <option value="DRAINAGE">Drainage &amp; Storm Water</option>
                    <option value="PARK_MAINTENANCE">Park Maintenance</option>
                    <option value="OTHER">Other Municipal Issue</option>
                </select>
            </div>
            <div class="form-group">
                <label>Issue Title</label>
                <input type="text" name="title" class="form-control" placeholder="Brief summary of the issue..." required>
            </div>
            <div class="form-group">
                <label>Exact Location / Ward Address</label>
                <input type="text" name="location" class="form-control" placeholder="e.g. Sector 5, Lane 3, Near City Clinic" required>
            </div>
            <div class="form-group">
                <label>Urgency / Priority</label>
                <select name="priority" class="form-control">
                    <option value="LOW">LOW</option>
                    <option value="MEDIUM" selected>MEDIUM</option>
                    <option value="HIGH">HIGH</option>
                    <option value="URGENT">URGENT</option>
                </select>
            </div>
            <div class="form-group">
                <label>Detailed Description</label>
                <textarea name="description" class="form-control" placeholder="Provide details to assist field technicians..." required></textarea>
            </div>
            <div style="display:flex; gap:1rem; margin-top:1.5rem;">
                <button type="submit" class="btn btn-primary" style="flex:1;">Submit Ticket</button>
                <button type="button" class="btn btn-secondary" onclick="closeModal('newServiceModal')">Cancel</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
