<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Municipal Service Requests - SmartCity AI" />
<c:set var="activeMenu" value="services" />
<jsp:include page="../common/header.jsp" />

<div class="app-container">
    <jsp:include page="../common/sidebar.jsp" />

    <main class="main-content">
        <header class="topbar">
            <div class="page-title-group">
                <h1>Municipal Service Requests</h1>
                <p>Track citizen utility tickets, assign departments &amp; log work resolutions</p>
            </div>
        </header>

        <c:if test="${not empty param.msg}">
            <div class="alert alert-success"><span>✓</span> ${param.msg}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger"><span>⚠</span> ${param.error}</div>
        </c:if>

        <div class="panel-card">
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Ticket #</th>
                            <th>Resident</th>
                            <th>Service Category</th>
                            <th>Title &amp; Location</th>
                            <th>Priority</th>
                            <th>Status</th>
                            <th>Created</th>
                            <th>Resolution Notes / Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="req" items="${serviceRequests}">
                            <tr>
                                <td>#REQ-${req.requestId}</td>
                                <td><strong>${req.residentName != null ? req.residentName : 'Citizen #'.concat(req.residentId)}</strong></td>
                                <td><span class="badge badge-info">${req.serviceType}</span></td>
                                <td>
                                    <strong>${req.title}</strong><br>
                                    <span style="font-size:0.8rem; color:var(--text-muted);">📍 ${req.location}</span>
                                </td>
                                <td><span class="badge badge-${req.priority.toLowerCase()}">${req.priority}</span></td>
                                <td><span class="badge badge-${req.status.toLowerCase()}">${req.status}</span></td>
                                <td style="font-size:0.82rem; color:var(--text-muted);">${req.createdAt}</td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/admin/services" method="POST" style="display:flex; flex-direction:column; gap:0.4rem;">
                                        <input type="hidden" name="action" value="updateStatus">
                                        <input type="hidden" name="requestId" value="${req.requestId}">
                                        <div style="display:flex; gap:0.3rem;">
                                            <select name="status" class="form-control" style="padding:0.25rem; font-size:0.8rem;">
                                                <option value="SUBMITTED" ${req.status == 'SUBMITTED' ? 'selected' : ''}>SUBMITTED</option>
                                                <option value="IN_REVIEW" ${req.status == 'IN_REVIEW' ? 'selected' : ''}>IN_REVIEW</option>
                                                <option value="IN_PROGRESS" ${req.status == 'IN_PROGRESS' ? 'selected' : ''}>IN_PROGRESS</option>
                                                <option value="RESOLVED" ${req.status == 'RESOLVED' ? 'selected' : ''}>RESOLVED</option>
                                                <option value="REJECTED" ${req.status == 'REJECTED' ? 'selected' : ''}>REJECTED</option>
                                            </select>
                                            <button type="submit" class="btn btn-secondary" style="padding:0.25rem 0.6rem; font-size:0.75rem;">Update</button>
                                        </div>
                                        <input type="text" name="resolutionNotes" class="form-control" placeholder="Resolution notes..." value="${req.resolutionNotes}" style="padding:0.25rem; font-size:0.8rem;">
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

<jsp:include page="../common/footer.jsp" />
