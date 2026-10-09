# 🏙️ Smart City Management System (IoT, AI & Big Data)

A comprehensive enterprise-grade Java Web Application for smart urban infrastructure monitoring and municipal operations, built with **Java Servlets, JSP/JSTL, JDBC, MySQL, Multithreading, and AI/Big Data Analytics**.

## 👥 User Roles & Capabilities

### 1. 🏛️ City Administrator
- **Resource Management**: Allocate and manage municipal energy grids, water plants, and recycling facilities with AI efficiency scoring.
- **Traffic Monitoring**: Real-time traffic congestion telemetry, vehicle density tracking, and AI rerouting suggestions.
- **Public Safety & CCTV**: Monitor emergency security alerts and live CCTV sensor feeds with rapid incident response dispatch.
- **Municipal Services & Feedback**: Review resident utility requests and respond to citizen satisfaction reviews.

### 2. 📐 Urban Planner
- **Infrastructure Planning**: Propose capital development projects, track allocated budgets against expenditures, and evaluate community impact scores.
- **Environmental Monitoring**: Sensor-driven Air Quality Index (AQI), ambient noise levels (dB), PM2.5 pollution, and greenhouse emissions (CO2).
- **Sustainability Score Engine**: Calculates the city's composite Eco-Index score out of 100 with automated mitigation advisories.

### 3. 🏡 Resident Citizen
- **Municipal Service Access**: File 1-click tickets for road repairs, water supply disruptions, street lighting, and waste collection.
- **Feedback & Review System**: Rate city services with 1-5 star ratings and receive official administrative responses.
- **Real-Time City Information**: View live weather advisories, traffic diversions, festival notifications, and emergency alerts.

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
- `city_events`: Real-time emergency, weather, and civic bulletins.
- `iot_sensors`: IoT sensor hardware deployment nodes and live telemetry.

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
- `AnalyticsServiceTest`: Tests AI resource optimization, traffic bottlenecks, and environmental index calculation.
- `PasswordUtilTest`: Verifies SHA-256 password hashing and verification.
