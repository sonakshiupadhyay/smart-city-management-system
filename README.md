<div align="center">

<h1>🏙️ Smart City Management System</h1>

<p><strong>A Java Web Application for urban infrastructure monitoring, municipal operations, rule-based analytics, and simulated sensor telemetry.</strong></p>

<p>
  <img src="https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 11+" />
  <img src="https://img.shields.io/badge/Servlets%204.0%20%26%20JSP-2.3-007396?style=for-the-badge&logo=openjdk&logoColor=white" alt="Servlets & JSP" />
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL" />
  <img src="https://img.shields.io/badge/HikariCP-4.0.3-2C5BB4?style=for-the-badge" alt="HikariCP" />
  <img src="https://img.shields.io/badge/Apache%20Tomcat-9.0%2B-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black" alt="Apache Tomcat" />
  <img src="https://img.shields.io/badge/Apache%20Maven-3.8%2B-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Apache Maven" />
  <img src="https://img.shields.io/badge/JUnit-5.9.3-25A162?style=for-the-badge&logo=junit5&logoColor=white" alt="JUnit 5" />
</p>

</div>

> **Note on "IoT" and "Analytics" features:**  
> The sensor telemetry in this project is **simulated** — `IoTSimulationService` generates randomized readings in a background thread; no real IoT hardware or external sensor APIs are connected. Similarly, the analytics and optimization features (resource efficiency, traffic bottleneck detection, environmental scoring) use **rule-based heuristics and threshold comparisons**, not machine-learning or AI models. If real integrations are added in the future, these labels can be updated accordingly.

---

## 🛠️ Tech Stack

| Layer | Technology | Purpose & Version |
|-------|------------|-------------------|
| **Backend Core** | ![Java](https://img.shields.io/badge/Java-11%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white) ![Servlets](https://img.shields.io/badge/Java%20Servlets-4.0.1-007396?style=flat-square&logo=openjdk&logoColor=white) ![JSP](https://img.shields.io/badge/JSP%20%26%20JSTL-2.3.3%20%2F%201.2-007396?style=flat-square) | MVC controllers, role-based auth filters, server-rendered views, and multithreaded `ScheduledExecutorService` telemetry simulation |
| **Database & Pooling** | ![MySQL](https://img.shields.io/badge/MySQL-8.0.33-4479A1?style=flat-square&logo=mysql&logoColor=white) ![HikariCP](https://img.shields.io/badge/HikariCP-4.0.3-2C5BB4?style=flat-square) ![H2](https://img.shields.io/badge/H2%20Database-2.2.224-09476B?style=flat-square) | JDBC persistence with MySQL primary storage, in-memory H2 (MySQL mode) fallback, and thread-safe HikariCP connection pooling (`SLF4J 1.7.36`) |
| **Data & Serialization** | ![Gson](https://img.shields.io/badge/Google%20Gson-2.10.1-4285F4?style=flat-square&logo=json&logoColor=white) | REST JSON payloads for simulated sensor feeds (`/api/sensors`, `/api/live-data`) |
| **Frontend & Charts** | ![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=html5&logoColor=white) ![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=css&logoColor=white) ![JavaScript](https://img.shields.io/badge/JavaScript-ES6-F7DF1E?style=flat-square&logo=javascript&logoColor=black) ![Chart.js](https://img.shields.io/badge/Chart.js-CDN-FF6384?style=flat-square&logo=chartdotjs&logoColor=white) | Responsive dark glassmorphism UI, resource/AQI/pipeline charts, and periodic Fetch API telemetry polling |
| **Container & Build** | ![Tomcat](https://img.shields.io/badge/Apache%20Tomcat-9.0%2B-F8DC75?style=flat-square&logo=apachetomcat&logoColor=black) ![Maven](https://img.shields.io/badge/Apache%20Maven-WAR%203.3.2-C71A36?style=flat-square&logo=apachemaven&logoColor=white) | Servlet container deployment (`tomcat7-maven-plugin 2.2` configured) and Maven WAR packaging |
| **Testing** | ![JUnit5](https://img.shields.io/badge/JUnit%205-5.9.3-25A162?style=flat-square&logo=junit5&logoColor=white) | Automated unit tests for OOP hierarchy, rule-based analytics, and SHA-256 password hashing |

---

## 👥 User Roles & Capabilities

### 1. 🏛️ City Administrator
- **Resource Management**: Allocate and manage municipal energy grids, water plants, and recycling facilities with rule-based efficiency scoring.
- **Traffic Monitoring**: Traffic congestion telemetry (simulated), vehicle density tracking, and threshold-based routing suggestions.
- **Public Safety**: Monitor emergency security alerts and incident response dispatch.
- **Municipal Services & Feedback**: Review resident utility requests and respond to citizen satisfaction reviews.

### 2. 📐 Urban Planner
- **Infrastructure Planning**: Propose capital development projects, track allocated budgets against expenditures, and evaluate community impact scores.
- **Environmental Monitoring**: Air Quality Index (AQI), ambient noise levels (dB), PM2.5 pollution, and greenhouse emissions (CO2) — data is seeded or simulated.
- **Sustainability Score Engine**: Calculates the city's composite Eco-Index score out of 100 with automated mitigation advisories using threshold rules.

### 3. 🏡 Resident Citizen
- **Municipal Service Access**: File 1-click tickets for road repairs, water supply disruptions, street lighting, and waste collection.
- **Feedback & Review System**: Rate city services with 1-5 star ratings and receive official administrative responses.
- **City Information**: View weather advisories, traffic diversions, festival notifications, and emergency alerts.

---

## 🗄️ Database Architecture

The system utilizes MySQL database `smart_city_db` with 10 tables:
- `users`: Role-based authentication and user profiles (`ADMIN`, `PLANNER`, `RESIDENT`).
- `resources`: Municipal capacity and current load for energy, water, and waste.
- `traffic_data`: GPS coordinates, congestion status (`LOW`, `MODERATE`, `HIGH`, `SEVERE`), speed, and vehicle count.
- `safety_incidents`: Emergency and security incidents with severity and resolution states.
- `infrastructure_projects`: Capital projects with budgets, timelines, and impact ratings.
- `environmental_data`: AQI, noise, CO2, temperature, and humidity readings.
- `service_requests`: Citizen utility tickets with assigned department and resolution notes.
- `feedback`: Resident ratings (1-5 stars) and administrative responses.
- `city_events`: Emergency, weather, and civic bulletins.
- `iot_sensors`: Sensor deployment nodes and simulated telemetry readings.

### Connection Pooling

Database connections are managed by **HikariCP**, a high-performance JDBC connection pool.
Each servlet request obtains its own connection from the pool and returns it after use,
ensuring reliable concurrent request handling.

| Setting | Value | Purpose |
|---------|-------|---------|
| Max Pool Size | 20 | Concurrent connections for servlet threads |
| Min Idle | 5 | Pre-warmed idle connections |
| Connection Timeout | 10 s | Max wait to obtain a connection |
| Idle Timeout | 5 min | Idle connection retirement |
| Max Lifetime | 10 min | Connection recycling |
| Leak Detection | 30 s | Warns if connections are held too long |

---

## 🔑 Quick Demo Credentials

| Role | Username | Password | Direct Dashboard URL |
|------|----------|----------|----------------------|
| **City Administrator** | `admin1` | `password123` | `/admin/dashboard` |
| **Urban Planner** | `planner1` | `password123` | `/planner/dashboard` |
| **Resident Citizen** | `resident1` | `password123` | `/resident/dashboard` |

*(Note: The login page also features **1-click quick autofill buttons** for each role!)*

---

## 🚀 Setup & Execution Guide

### Prerequisites
- **JDK 11 or higher** (Tested on Java 11 through Java 26)
- **Apache Maven 3.8+**
- **MySQL Server 8.0+**
- **Apache Tomcat 9.0+** (or IntelliJ IDEA / Eclipse Enterprise Server)

### 1. Database Initialization
Open MySQL Workbench or MySQL CLI:
```sql
SOURCE sql/schema.sql;
```
*(Optionally verify database credentials in `src/main/java/com/smartcity/util/DatabaseConnection.java` - default is user `root`, password `` or `password` on `localhost:3306`).*

### 2. Build the Project
Compile the code and execute unit tests:
```powershell
mvn clean package
```
This generates `target/smart-city-management.war`.

### 3. Deploy and Run
- **In IntelliJ IDEA**:
  1. Open the project folder.
  2. Add a **Tomcat Server (Local)** Run/Debug Configuration.
  3. Under Deployment, select `smart-city-management:war exploded`.
  4. Set Application context to `/smartcity` or `/`.
  5. Click **Run**.
- **In Standalone Tomcat**:
  1. Copy `target/smart-city-management.war` to your Tomcat `webapps/` directory.
  2. Start Tomcat using `bin/startup.bat` (Windows) or `bin/startup.sh` (Linux/macOS).
  3. Navigate to: `http://localhost:8080/smart-city-management/`

---

## 🧪 Unit Tests
Execute the automated test suite:
```powershell
mvn test
```
**Test Coverage:**
- `ModelHierarchyTest`: Validates OOP inheritance, polymorphism, and interface compliance.
- `AnalyticsServiceTest`: Tests resource optimization, traffic bottleneck detection, and environmental index calculation using rule-based logic.
- `PasswordUtilTest`: Verifies SHA-256 password hashing and verification.
