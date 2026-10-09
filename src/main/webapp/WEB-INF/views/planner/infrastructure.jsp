<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Infrastructure Project Planning - SmartCity AI" />
<c:set var="activeMenu" value="infrastructure" />
<jsp:include page="../common/header.jsp" />

<div class="app-container">
    <jsp:include page="../common/sidebar.jsp" />

    <main class="main-content">
        <header class="topbar">
            <div class="page-title-group">
                <h1>Infrastructure Project Planning</h1>
                <p>Plan, propose, track budgets &amp; evaluate environmental &amp; economic impacts</p>
            </div>
            <div class="topbar-actions">
                <button type="button" class="btn btn-primary" onclick="openModal('addProjectModal')">
                    + Submit Project Proposal
                </button>
            </div>
        </header>

        <c:if test="${not empty param.msg}">
            <div class="alert alert-success"><span>✓</span> ${param.msg}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger"><span>⚠</span> ${param.error}</div>
        </c:if>

        <!-- Project Pipeline Filters -->
        <div style="display:flex; gap:0.5rem; margin-bottom:1.5rem;">
            <a href="${pageContext.request.contextPath}/planner/infrastructure?status=ALL" class="btn ${selectedStatus == 'ALL' ? 'btn-primary' : 'btn-secondary'}" style="padding:0.4rem 0.85rem; font-size:0.85rem;">All Projects</a>
            <a href="${pageContext.request.contextPath}/planner/infrastructure?status=PROPOSED" class="btn ${selectedStatus == 'PROPOSED' ? 'btn-primary' : 'btn-secondary'}" style="padding:0.4rem 0.85rem; font-size:0.85rem;">Proposed</a>
            <a href="${pageContext.request.contextPath}/planner/infrastructure?status=APPROVED" class="btn ${selectedStatus == 'APPROVED' ? 'btn-primary' : 'btn-secondary'}" style="padding:0.4rem 0.85rem; font-size:0.85rem;">Approved</a>
            <a href="${pageContext.request.contextPath}/planner/infrastructure?status=IN_PROGRESS" class="btn ${selectedStatus == 'IN_PROGRESS' ? 'btn-primary' : 'btn-secondary'}" style="padding:0.4rem 0.85rem; font-size:0.85rem;">In Progress</a>
            <a href="${pageContext.request.contextPath}/planner/infrastructure?status=COMPLETED" class="btn ${selectedStatus == 'COMPLETED' ? 'btn-primary' : 'btn-secondary'}" style="padding:0.4rem 0.85rem; font-size:0.85rem;">Completed</a>
        </div>

        <div class="panel-card">
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Project</th>
                            <th>Category &amp; Location</th>
                            <th>Budget vs Spent</th>
                            <th>Timeline</th>
                            <th>Impact Score</th>
                            <th>Status</th>
                            <th>Update Lifecycle</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${projects}">
                            <tr>
                                <td>
                                    <strong>${p.projectName}</strong><br>
                                    <span style="font-size:0.82rem; color:var(--text-secondary);">${p.description}</span>
                                </td>
                                <td>
                                    <span class="badge badge-info">${p.category}</span><br>
                                    <span style="font-size:0.8rem; color:var(--text-muted);">📍 ${p.location}</span>
                                </td>
                                <td>
                                    $<fmt:formatNumber value="${p.spentAmount}" /> / $<fmt:formatNumber value="${p.budget}" />
                                    <div style="width: 100%; height: 5px; background: rgba(255,255,255,0.1); border-radius: 3px; margin-top: 4px; overflow: hidden;">
                                        <div style="height: 100%; width: ${(p.spentAmount / (p.budget > 0 ? p.budget : 1)) * 100}%; background: var(--primary);"></div>
                                    </div>
                                </td>
                                <td style="font-size:0.82rem; color:var(--text-muted);">
                                    ${p.startDate} &rarr; ${p.endDate != null ? p.endDate : 'Ongoing'}
                                </td>
                                <td><strong style="color:var(--primary);">⭐ ${p.impactScore} / 10</strong></td>
                                <td><span class="badge badge-${p.status.toLowerCase()}">${p.status}</span></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/planner/infrastructure" method="POST" style="display:flex; flex-direction:column; gap:0.3rem;">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="projectId" value="${p.projectId}">
                                        <input type="hidden" name="impactScore" value="${p.impactScore}">
                                        <div style="display:flex; gap:0.3rem;">
                                            <select name="status" class="form-control" style="padding:0.25rem; font-size:0.78rem;">
                                                <option value="PROPOSED" ${p.status == 'PROPOSED' ? 'selected' : ''}>PROPOSED</option>
                                                <option value="APPROVED" ${p.status == 'APPROVED' ? 'selected' : ''}>APPROVED</option>
                                                <option value="IN_PROGRESS" ${p.status == 'IN_PROGRESS' ? 'selected' : ''}>IN_PROGRESS</option>
                                                <option value="COMPLETED" ${p.status == 'COMPLETED' ? 'selected' : ''}>COMPLETED</option>
                                                <option value="ON_HOLD" ${p.status == 'ON_HOLD' ? 'selected' : ''}>ON_HOLD</option>
                                            </select>
                                            <button type="submit" class="btn btn-secondary" style="padding:0.25rem 0.5rem; font-size:0.75rem;">Save</button>
                                        </div>
                                        <input type="number" step="any" name="spentAmount" class="form-control" value="${p.spentAmount}" placeholder="Spent $" style="padding:0.25rem; font-size:0.78rem;">
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

<!-- Modal: New Infrastructure Project Proposal -->
<div id="addProjectModal" class="modal-backdrop" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.7); backdrop-filter:blur(8px); z-index:999; align-items:center; justify-content:center;">
    <div style="background:var(--bg-surface); border:1px solid var(--border-color); border-radius:var(--radius-md); padding:2rem; width:100%; max-width:540px;">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem;">
            <h3>Submit Infrastructure Proposal</h3>
            <button type="button" onclick="closeModal('addProjectModal')" style="background:none; border:none; color:#fff; font-size:1.5rem; cursor:pointer;">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/planner/infrastructure" method="POST">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
                <label>Project Title</label>
                <input type="text" name="projectName" class="form-control" placeholder="e.g. Automated Underground Waste Chute System" required>
            </div>
            <div class="form-group">
                <label>Description &amp; Objective</label>
                <textarea name="description" class="form-control" placeholder="Project goals, benefits, technical requirements..." required></textarea>
            </div>
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:1rem;">
                <div class="form-group">
                    <label>Category</label>
                    <select name="category" class="form-control" required>
                        <option value="TRANSPORTATION">TRANSPORTATION</option>
                        <option value="HOUSING">HOUSING</option>
                        <option value="UTILITIES">UTILITIES</option>
                        <option value="PUBLIC_SPACES">PUBLIC_SPACES</option>
                        <option value="TECHNOLOGY">TECHNOLOGY</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Location / Zone</label>
                    <input type="text" name="location" class="form-control" placeholder="Central District" required>
                </div>
            </div>
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:1rem;">
                <div class="form-group">
                    <label>Allocated Budget ($ USD)</label>
                    <input type="number" step="any" name="budget" class="form-control" placeholder="5000000" required>
                </div>
                <div class="form-group">
                    <label>Impact Score (1-10)</label>
                    <input type="number" step="0.1" name="impactScore" class="form-control" placeholder="8.5" min="1" max="10" required>
                </div>
            </div>
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:1rem;">
                <div class="form-group">
                    <label>Start Date</label>
                    <input type="date" name="startDate" class="form-control">
                </div>
                <div class="form-group">
                    <label>End Date</label>
                    <input type="date" name="endDate" class="form-control">
                </div>
            </div>
            <div style="display:flex; gap:1rem; margin-top:1.5rem;">
                <button type="submit" class="btn btn-primary" style="flex:1;">Submit Proposal</button>
                <button type="button" class="btn btn-secondary" onclick="closeModal('addProjectModal')">Cancel</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
