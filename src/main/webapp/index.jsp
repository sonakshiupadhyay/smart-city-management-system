<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart City Management System - AI &amp; IoT Urban Infrastructure</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>

    <header style="padding: 1.5rem 3rem; display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid var(--border-color); background: rgba(17,24,39,0.7); backdrop-filter: blur(16px);">
        <div style="display: flex; align-items: center; gap: 0.75rem;">
            <div class="logo-badge">⚡</div>
            <span style="font-size: 1.35rem; font-weight: 800; font-family: 'Outfit', sans-serif;">SmartCity <span style="color: var(--primary);">Platform</span></span>
        </div>
        <div style="display: flex; align-items: center; gap: 1rem;">
            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-secondary">Sign In</a>
            <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-primary">Citizen Registration</a>
        </div>
    </header>

    <main>
        <section class="hero-section">
            <div class="hero-pill">
                <span class="pulse-dot"></span> Next-Gen Cyber-Physical Smart City Network
            </div>
            <h1 class="hero-title">Empowering Cities with IoT Sensors, AI Analytics &amp; Big Data</h1>
            <p class="hero-desc">
                An integrated urban ecosystem monitoring and optimizing energy grids, municipal water reservoirs, 
                smart traffic flow, air quality indices, and citizen public safety in real-time.
            </p>
            <div style="display: flex; gap: 1rem; justify-content: center;">
                <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary" style="padding: 0.9rem 2rem; font-size: 1.05rem;">
                    Launch City Portal
                </a>
                <a href="#roles" class="btn btn-secondary" style="padding: 0.9rem 2rem; font-size: 1.05rem;">
                    Explore User Roles
                </a>
            </div>
        </section>

        <!-- Role-based Architecture showcase -->
        <section id="roles" style="max-width: 1200px; margin: 0 auto; padding: 2rem 1.5rem;">
            <div style="text-align: center; margin-bottom: 2.5rem;">
                <h2>Tailored Dashboards for Every Stakeholder</h2>
                <p style="color: var(--text-secondary); margin-top: 0.5rem;">Role-based access designed specifically for city administration, planning, and resident citizens.</p>
            </div>

            <div class="role-cards-grid">
                <!-- Administrator Card -->
                <div class="role-card">
                    <div class="role-icon-box">🏛️</div>
                    <h3 style="font-size: 1.4rem; margin-bottom: 0.75rem;">City Administrator</h3>
                    <p style="color: var(--text-secondary); font-size: 0.92rem; margin-bottom: 1.5rem;">
                        Oversee city-wide operations with real-time AI resource optimization reports, 
                        traffic congestion algorithms, and CCTV incident response management.
                    </p>
                    <ul style="color: var(--text-muted); font-size: 0.85rem; margin-bottom: 1.5rem; list-style-position: inside; line-height: 1.8;">
                        <li>Energy, Water &amp; Waste Management</li>
                        <li>Real-Time Traffic Congestion Alerts</li>
                        <li>Emergency &amp; Public Safety Dispatch</li>
                    </ul>
                    <a href="${pageContext.request.contextPath}/login.jsp?demoRole=ADMIN" class="btn btn-secondary" style="width: 100%;">
                        Access Admin Console &rarr;
                    </a>
                </div>

                <!-- Urban Planner Card -->
                <div class="role-card">
                    <div class="role-icon-box">📐</div>
                    <h3 style="font-size: 1.4rem; margin-bottom: 0.75rem;">Urban Planner</h3>
                    <p style="color: var(--text-secondary); font-size: 0.92rem; margin-bottom: 1.5rem;">
                        Design data-driven capital infrastructure proposals, analyze sustainability impact scores, 
                        and track urban air quality, noise, and greenhouse emissions.
                    </p>
                    <ul style="color: var(--text-muted); font-size: 0.85rem; margin-bottom: 1.5rem; list-style-position: inside; line-height: 1.8;">
                        <li>Infrastructure Capital Project Design</li>
                        <li>Air Quality &amp; Pollution Monitoring</li>
                        <li>Urban Sustainability Score Engine</li>
                    </ul>
                    <a href="${pageContext.request.contextPath}/login.jsp?demoRole=PLANNER" class="btn btn-secondary" style="width: 100%;">
                        Access Planner Studio &rarr;
                    </a>
                </div>

                <!-- Resident Citizen Card -->
                <div class="role-card">
                    <div class="role-icon-box">🏡</div>
                    <h3 style="font-size: 1.4rem; margin-bottom: 0.75rem;">Resident Citizen</h3>
                    <p style="color: var(--text-secondary); font-size: 0.92rem; margin-bottom: 1.5rem;">
                        Request municipal services (pothole repair, water supply, waste pickup), 
                        submit feedback, and receive live city bulletins and traffic alerts.
                    </p>
                    <ul style="color: var(--text-muted); font-size: 0.85rem; margin-bottom: 1.5rem; list-style-position: inside; line-height: 1.8;">
                        <li>1-Click Municipal Service Requests</li>
                        <li>Citizen Feedback &amp; Review System</li>
                        <li>Live Traffic &amp; Weather Bulletins</li>
                    </ul>
                    <a href="${pageContext.request.contextPath}/login.jsp?demoRole=RESIDENT" class="btn btn-secondary" style="width: 100%;">
                        Access Citizen Portal &rarr;
                    </a>
                </div>
            </div>
        </section>
    </main>

    <footer class="footer">
        <p>&copy; 2026 Smart City Management System. Developed with Java Servlets, JDBC, Big Data Analytics &amp; IoT telemetry.</p>
    </footer>

</body>
</html>
