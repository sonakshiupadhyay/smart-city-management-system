<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Resource Management - SmartCity AI" />
<c:set var="activeMenu" value="resources" />
<jsp:include page="../common/header.jsp" />

<div class="app-container">
    <jsp:include page="../common/sidebar.jsp" />

    <main class="main-content">
        <header class="topbar">
            <div class="page-title-group">
                <h1>Resource Management</h1>
                <p>Monitor, optimize and rebalance municipal energy, water, and waste facilities</p>
            </div>
            <div class="topbar-actions">
                <button type="button" class="btn btn-primary" onclick="openModal('addResourceModal')">
                    + Allocate New Resource
                </button>
            </div>
        </header>

        <c:if test="${not empty param.msg}">
            <div class="alert alert-success"><span>✓</span> ${param.msg}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger"><span>⚠</span> ${param.error}</div>
        </c:if>

        <!-- Filter tabs -->
        <div style="display: flex; gap: 0.5rem; margin-bottom: 1.5rem;">
            <a href="${pageContext.request.contextPath}/admin/resources?type=ALL" 
               class="btn ${selectedType == 'ALL' ? 'btn-primary' : 'btn-secondary'}" style="padding: 0.5rem 1rem; font-size: 0.85rem;">All Types</a>
            <a href="${pageContext.request.contextPath}/admin/resources?type=ENERGY" 
               class="btn ${selectedType == 'ENERGY' ? 'btn-primary' : 'btn-secondary'}" style="padding: 0.5rem 1rem; font-size: 0.85rem;">⚡ Energy</a>
            <a href="${pageContext.request.contextPath}/admin/resources?type=WATER" 
               class="btn ${selectedType == 'WATER' ? 'btn-primary' : 'btn-secondary'}" style="padding: 0.5rem 1rem; font-size: 0.85rem;">💧 Water</a>
            <a href="${pageContext.request.contextPath}/admin/resources?type=WASTE" 
               class="btn ${selectedType == 'WASTE' ? 'btn-primary' : 'btn-secondary'}" style="padding: 0.5rem 1rem; font-size: 0.85rem;">♻️ Waste</a>
        </div>

        <!-- Resources Data Table -->
        <div class="panel-card">
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Type</th>
                            <th>Resource Name</th>
                            <th>Location</th>
                            <th>Current Usage / Capacity</th>
                            <th>Efficiency</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="res" items="${resources}">
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${res.resourceType == 'ENERGY'}"><span style="color:#06b6d4;">⚡ Energy</span></c:when>
                                        <c:when test="${res.resourceType == 'WATER'}"><span style="color:#3b82f6;">💧 Water</span></c:when>
                                        <c:otherwise><span style="color:#10b981;">♻️ Waste</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td><strong>${res.resourceName}</strong></td>
                                <td>${res.location}</td>
                                <td>
                                    ${res.currentUsage} / ${res.totalCapacity} ${res.unit}
                                    <div style="width: 100%; height: 6px; background: rgba(255,255,255,0.1); border-radius: 3px; margin-top: 5px; overflow: hidden;">
                                        <div style="height: 100%; width: ${(res.currentUsage / res.totalCapacity) * 100}%; background: ${res.status == 'CRITICAL' ? 'var(--danger)' : (res.status == 'WARNING' ? 'var(--warning)' : 'var(--success)')};"></div>
                                    </div>
                                </td>
                                <td><strong>${res.efficiencyScore}%</strong></td>
                                <td>
                                    <span class="badge badge-${res.status.toLowerCase()}">${res.status}</span>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/admin/resources" method="POST" style="display:inline;" onsubmit="return confirm('Remove this resource?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="resourceId" value="${res.resourceId}">
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

<!-- Modal: Allocate New Resource -->
<div id="addResourceModal" class="modal-backdrop" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.7); backdrop-filter:blur(8px); z-index:999; align-items:center; justify-content:center;">
    <div style="background:var(--bg-surface); border:1px solid var(--border-color); border-radius:var(--radius-md); padding:2rem; width:100%; max-width:500px;">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem;">
            <h3>Allocate City Resource</h3>
            <button type="button" onclick="closeModal('addResourceModal')" style="background:none; border:none; color:#fff; font-size:1.5rem; cursor:pointer;">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/admin/resources" method="POST">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
                <label>Resource Type</label>
                <select name="resourceType" class="form-control" required>
                    <option value="ENERGY">ENERGY (Solar, Grid, Thermal)</option>
                    <option value="WATER">WATER (Treatment, Reservoir)</option>
                    <option value="WASTE">WASTE (Recycling, Landfill)</option>
                </select>
            </div>
            <div class="form-group">
                <label>Resource Facility Name</label>
                <input type="text" name="resourceName" class="form-control" placeholder="e.g. Solar Farm Phase 2" required>
            </div>
            <div class="form-group">
                <label>Location / Sector</label>
                <input type="text" name="location" class="form-control" placeholder="Sector 9, High Tech Corridor" required>
            </div>
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:1rem;">
                <div class="form-group">
                    <label>Total Capacity</label>
                    <input type="number" step="any" name="totalCapacity" class="form-control" placeholder="10000" required>
                </div>
                <div class="form-group">
                    <label>Current Usage</label>
                    <input type="number" step="any" name="currentUsage" class="form-control" placeholder="5000" required>
                </div>
            </div>
            <div class="form-group">
                <label>Measurement Unit</label>
                <input type="text" name="unit" class="form-control" placeholder="kWh, Liters, Tons" required>
            </div>
            <div style="display:flex; gap:1rem; margin-top:1.5rem;">
                <button type="submit" class="btn btn-primary" style="flex:1;">Submit Allocation</button>
                <button type="button" class="btn btn-secondary" onclick="closeModal('addResourceModal')">Cancel</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
