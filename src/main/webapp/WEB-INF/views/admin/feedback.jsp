<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Citizen Feedback Management - SmartCity" />
<c:set var="activeMenu" value="feedback" />
<jsp:include page="../common/header.jsp" />

<div class="app-container">
    <jsp:include page="../common/sidebar.jsp" />

    <main class="main-content">
        <header class="topbar">
            <div class="page-title-group">
                <h1>Citizen Feedback &amp; Public Reviews</h1>
                <p>Manage resident evaluations, ratings &amp; publish official city administrative replies</p>
            </div>
            <div class="topbar-actions">
                <div class="stat-icon" style="background:rgba(245,158,11,0.15); color:var(--warning); font-size:1rem; padding:0.5rem 1rem; border-radius:var(--radius-full); font-weight:700;">
                    ⭐ Avg City Rating: ${avgRating} / 5.0
                </div>
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
                            <th>Citizen</th>
                            <th>Category</th>
                            <th>Subject</th>
                            <th>Rating</th>
                            <th>Message</th>
                            <th>Status</th>
                            <th>Admin Response</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="fb" items="${feedbackList}">
                            <tr>
                                <td><strong>${fb.residentName != null ? fb.residentName : 'Citizen #'.concat(fb.residentId)}</strong></td>
                                <td><span class="badge badge-info">${fb.category}</span></td>
                                <td><strong>${fb.subject}</strong></td>
                                <td style="color: var(--warning); font-weight: 700;">
                                    <c:forEach begin="1" end="${fb.rating}">★</c:forEach>
                                </td>
                                <td style="max-width: 250px;">${fb.message}</td>
                                <td><span class="badge badge-${fb.status.toLowerCase()}">${fb.status}</span></td>
                                <td style="color: var(--text-secondary); font-size: 0.85rem; max-width: 200px;">
                                    ${fb.adminResponse != null ? fb.adminResponse : '<em>Awaiting reply</em>'}
                                </td>
                                <td>
                                    <button type="button" class="btn btn-secondary" style="padding: 0.35rem 0.65rem; font-size: 0.8rem;" 
                                            onclick="openReplyModal(${fb.feedbackId}, '${fb.subject}')">
                                        Reply
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<!-- Modal: Reply to Resident Feedback -->
<div id="replyFeedbackModal" class="modal-backdrop" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.7); backdrop-filter:blur(8px); z-index:999; align-items:center; justify-content:center;">
    <div style="background:var(--bg-surface); border:1px solid var(--border-color); border-radius:var(--radius-md); padding:2rem; width:100%; max-width:500px;">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem;">
            <h3>Official Administrative Reply</h3>
            <button type="button" onclick="closeModal('replyFeedbackModal')" style="background:none; border:none; color:#fff; font-size:1.5rem; cursor:pointer;">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/admin/feedback" method="POST">
            <input type="hidden" name="action" value="respond">
            <input type="hidden" id="replyFeedbackId" name="feedbackId" value="">
            <div class="form-group">
                <label>Feedback Subject</label>
                <input type="text" id="replySubjectText" class="form-control" readonly style="opacity:0.7;">
            </div>
            <div class="form-group">
                <label>Resolution Status</label>
                <select name="status" class="form-control">
                    <option value="ACKNOWLEDGED">ACKNOWLEDGED</option>
                    <option value="IN_REVIEW">IN_REVIEW</option>
                    <option value="ADDRESSED" selected>ADDRESSED</option>
                </select>
            </div>
            <div class="form-group">
                <label>City Administrator Response</label>
                <textarea name="adminResponse" class="form-control" placeholder="Write response to citizen..." required></textarea>
            </div>
            <div style="display:flex; gap:1rem; margin-top:1.5rem;">
                <button type="submit" class="btn btn-primary" style="flex:1;">Send Response</button>
                <button type="button" class="btn btn-secondary" onclick="closeModal('replyFeedbackModal')">Cancel</button>
            </div>
        </form>
    </div>
</div>

<script>
    function openReplyModal(id, subject) {
        document.getElementById('replyFeedbackId').value = id;
        document.getElementById('replySubjectText').value = subject;
        openModal('replyFeedbackModal');
    }
</script>

<jsp:include page="../common/footer.jsp" />
