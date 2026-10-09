<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Submit Feedback - SmartCity AI" />
<c:set var="activeMenu" value="feedback" />
<jsp:include page="../common/header.jsp" />

<div class="app-container">
    <jsp:include page="../common/sidebar.jsp" />

    <main class="main-content">
        <header class="topbar">
            <div class="page-title-group">
                <h1>Citizen Voice &amp; City Feedback</h1>
                <p>Provide feedback on municipal services &amp; track official council responses</p>
            </div>
            <div class="topbar-actions">
                <button type="button" class="btn btn-primary" onclick="openModal('feedbackModal')">
                    ⭐ Submit New Feedback
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
                <h2 class="panel-title"><span>💬</span> My Feedback &amp; City Responses</h2>
            </div>
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Category</th>
                            <th>Subject</th>
                            <th>Rating</th>
                            <th>My Message</th>
                            <th>Status</th>
                            <th>Submitted On</th>
                            <th>Official City Response</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="fb" items="${feedbackList}">
                            <tr>
                                <td><span class="badge badge-info">${fb.category}</span></td>
                                <td><strong>${fb.subject}</strong></td>
                                <td style="color:var(--warning); font-size:1.1rem; white-space:nowrap;">
                                    <c:forEach begin="1" end="${fb.rating}">★</c:forEach>
                                </td>
                                <td style="max-width:240px;">${fb.message}</td>
                                <td><span class="badge badge-${fb.status.toLowerCase()}">${fb.status}</span></td>
                                <td style="font-size:0.82rem; color:var(--text-muted);">${fb.createdAt}</td>
                                <td style="font-size:0.85rem; color:var(--text-secondary); max-width:220px;">
                                    ${fb.adminResponse != null ? fb.adminResponse : '<em>Awaiting city review</em>'}
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<!-- Modal: Submit Feedback -->
<div id="feedbackModal" class="modal-backdrop" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.7); backdrop-filter:blur(8px); z-index:999; align-items:center; justify-content:center;">
    <div style="background:var(--bg-surface); border:1px solid var(--border-color); border-radius:var(--radius-md); padding:2rem; width:100%; max-width:500px;">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem;">
            <h3>Submit Citizen Feedback</h3>
            <button type="button" onclick="closeModal('feedbackModal')" style="background:none; border:none; color:#fff; font-size:1.5rem; cursor:pointer;">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/resident/feedback" method="POST">
            <input type="hidden" name="action" value="submit">
            <div class="form-group">
                <label>Category</label>
                <select name="category" class="form-control" required>
                    <option value="SERVICES">Municipal Services</option>
                    <option value="INFRASTRUCTURE">Infrastructure &amp; Roads</option>
                    <option value="TRANSPORT">Public Transportation</option>
                    <option value="SAFETY">Public Safety &amp; Lighting</option>
                    <option value="ENVIRONMENT">Parks &amp; Environment</option>
                    <option value="GENERAL">General Suggestion</option>
                </select>
            </div>
            <div class="form-group">
                <label>Subject</label>
                <input type="text" name="subject" class="form-control" placeholder="Brief summary of feedback..." required>
            </div>
            <div class="form-group">
                <label>Satisfaction Rating (1 to 5 Stars)</label>
                <select name="rating" class="form-control" required>
                    <option value="5">★★★★★ (5 Stars - Excellent)</option>
                    <option value="4">★★★★☆ (4 Stars - Good)</option>
                    <option value="3" selected>★★★☆☆ (3 Stars - Average)</option>
                    <option value="2">★★☆☆☆ (2 Stars - Needs Improvement)</option>
                    <option value="1">★☆☆☆☆ (1 Star - Poor)</option>
                </select>
            </div>
            <div class="form-group">
                <label>Feedback Message</label>
                <textarea name="message" class="form-control" placeholder="Your experience, observations or recommendations..." required></textarea>
            </div>
            <div style="display:flex; gap:1rem; margin-top:1.5rem;">
                <button type="submit" class="btn btn-primary" style="flex:1;">Submit Feedback</button>
                <button type="button" class="btn btn-secondary" onclick="closeModal('feedbackModal')">Cancel</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
