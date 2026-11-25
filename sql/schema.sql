-- ============================================================
-- Smart City Management System - Database Schema
-- ============================================================
-- This schema supports rule-based analytics and simulated telemetry
-- for managing urban infrastructure and services.
-- ============================================================

CREATE DATABASE IF NOT EXISTS smart_city_db;
USE smart_city_db;

-- ============================================================
-- 1. USERS TABLE - Stores all user types with role-based access
-- ============================================================
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    role ENUM('ADMIN', 'PLANNER', 'RESIDENT') NOT NULL,
    department VARCHAR(100),
    address VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- 2. RESOURCES TABLE - City resource management (energy, water, waste)
-- ============================================================
CREATE TABLE resources (
    resource_id INT AUTO_INCREMENT PRIMARY KEY,
    resource_type ENUM('ENERGY', 'WATER', 'WASTE') NOT NULL,
    resource_name VARCHAR(100) NOT NULL,
    location VARCHAR(255),
    total_capacity DOUBLE NOT NULL,
    current_usage DOUBLE DEFAULT 0,
    unit VARCHAR(20) NOT NULL,
    status ENUM('OPTIMAL', 'WARNING', 'CRITICAL') DEFAULT 'OPTIMAL',
    efficiency_score DOUBLE DEFAULT 100.0,
    managed_by INT,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (managed_by) REFERENCES users(user_id) ON DELETE SET NULL
);

-- ============================================================
-- 3. TRAFFIC DATA TABLE - Traffic corridor monitoring & analytics
-- ============================================================
CREATE TABLE traffic_data (
    traffic_id INT AUTO_INCREMENT PRIMARY KEY,
    location VARCHAR(255) NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    congestion_level ENUM('LOW', 'MODERATE', 'HIGH', 'SEVERE') DEFAULT 'LOW',
    average_speed DOUBLE,
    vehicle_count INT DEFAULT 0,
    alert_type VARCHAR(100),
    suggestion TEXT,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 4. SAFETY INCIDENTS TABLE - Public safety management
-- ============================================================
CREATE TABLE safety_incidents (
    incident_id INT AUTO_INCREMENT PRIMARY KEY,
    incident_type ENUM('SECURITY', 'EMERGENCY', 'FIRE', 'ACCIDENT', 'OTHER') NOT NULL,
    location VARCHAR(255) NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    description TEXT NOT NULL,
    severity ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') DEFAULT 'MEDIUM',
    status ENUM('REPORTED', 'IN_PROGRESS', 'RESOLVED', 'CLOSED') DEFAULT 'REPORTED',
    reported_by INT,
    assigned_to INT,
    reported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    FOREIGN KEY (reported_by) REFERENCES users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (assigned_to) REFERENCES users(user_id) ON DELETE SET NULL
);

-- ============================================================
-- 5. INFRASTRUCTURE PROJECTS TABLE - Urban planning
-- ============================================================
CREATE TABLE infrastructure_projects (
    project_id INT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(200) NOT NULL,
    description TEXT,
    category ENUM('TRANSPORTATION', 'HOUSING', 'UTILITIES', 'PUBLIC_SPACES', 'TECHNOLOGY') NOT NULL,
    location VARCHAR(255),
    planned_by INT,
    status ENUM('PROPOSED', 'APPROVED', 'IN_PROGRESS', 'COMPLETED', 'ON_HOLD') DEFAULT 'PROPOSED',
    budget DOUBLE,
    spent_amount DOUBLE DEFAULT 0,
    start_date DATE,
    end_date DATE,
    impact_score DOUBLE DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (planned_by) REFERENCES users(user_id) ON DELETE SET NULL
);

-- ============================================================
-- 6. ENVIRONMENTAL DATA TABLE - Environmental monitoring
-- ============================================================
CREATE TABLE environmental_data (
    env_id INT AUTO_INCREMENT PRIMARY KEY,
    location VARCHAR(255) NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    air_quality_index INT DEFAULT 0,
    noise_level DOUBLE DEFAULT 0,
    pollution_level DOUBLE DEFAULT 0,
    temperature DOUBLE,
    humidity DOUBLE,
    co2_level DOUBLE DEFAULT 0,
    status ENUM('GOOD', 'MODERATE', 'UNHEALTHY', 'HAZARDOUS') DEFAULT 'GOOD',
    recommendation TEXT,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 7. SERVICE REQUESTS TABLE - Resident service management
-- ============================================================
CREATE TABLE service_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    resident_id INT NOT NULL,
    service_type ENUM('WATER_SUPPLY', 'ELECTRICITY', 'WASTE_COLLECTION', 'ROAD_REPAIR', 
                      'STREET_LIGHTING', 'DRAINAGE', 'PARK_MAINTENANCE', 'OTHER') NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(255),
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') DEFAULT 'MEDIUM',
    status ENUM('SUBMITTED', 'IN_REVIEW', 'IN_PROGRESS', 'RESOLVED', 'REJECTED') DEFAULT 'SUBMITTED',
    assigned_to INT,
    resolution_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    FOREIGN KEY (resident_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_to) REFERENCES users(user_id) ON DELETE SET NULL
);

-- ============================================================
-- 8. FEEDBACK TABLE - Resident feedback system
-- ============================================================
CREATE TABLE feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    resident_id INT NOT NULL,
    category ENUM('INFRASTRUCTURE', 'SERVICES', 'SAFETY', 'ENVIRONMENT', 'TRANSPORT', 'GENERAL') NOT NULL,
    subject VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    status ENUM('SUBMITTED', 'ACKNOWLEDGED', 'IN_REVIEW', 'ADDRESSED') DEFAULT 'SUBMITTED',
    admin_response TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP NULL,
    FOREIGN KEY (resident_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ============================================================
-- 9. CITY EVENTS TABLE - City events and active civic alerts
-- ============================================================
CREATE TABLE city_events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    event_type ENUM('TRAFFIC', 'WEATHER', 'MAINTENANCE', 'FESTIVAL', 'EMERGENCY', 'GENERAL') NOT NULL,
    location VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE,
    severity ENUM('INFO', 'WARNING', 'ALERT', 'CRITICAL') DEFAULT 'INFO',
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL
);

-- ============================================================
-- 10. IOT SENSORS TABLE - Simulated sensor node registry
-- ============================================================
CREATE TABLE iot_sensors (
    sensor_id INT AUTO_INCREMENT PRIMARY KEY,
    sensor_name VARCHAR(100) NOT NULL,
    sensor_type ENUM('TRAFFIC', 'ENVIRONMENT', 'ENERGY', 'WATER', 'SAFETY') NOT NULL,
    location VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE,
    status ENUM('ACTIVE', 'INACTIVE', 'MAINTENANCE') DEFAULT 'ACTIVE',
    last_reading DOUBLE,
    last_reading_time TIMESTAMP,
    installed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- INSERT SAMPLE DATA
-- ============================================================

-- Sample Users (password is 'password123' - in production, use hashed passwords)
INSERT INTO users (username, password, email, full_name, phone, role, department) VALUES
('admin1', 'password123', 'admin@smartcity.gov', 'Rajesh Kumar', '9876543210', 'ADMIN', 'City Administration'),
('admin2', 'password123', 'admin2@smartcity.gov', 'Priya Sharma', '9876543211', 'ADMIN', 'Traffic Management'),
('planner1', 'password123', 'planner@smartcity.gov', 'Amit Verma', '9876543212', 'PLANNER', 'Urban Development'),
('planner2', 'password123', 'planner2@smartcity.gov', 'Sneha Patel', '9876543213', 'PLANNER', 'Environmental Division'),
('resident1', 'password123', 'resident1@email.com', 'Vikram Singh', '9876543214', 'RESIDENT', NULL),
('resident2', 'password123', 'resident2@email.com', 'Anita Desai', '9876543215', 'RESIDENT', NULL),
('resident3', 'password123', 'resident3@email.com', 'Rahul Gupta', '9876543216', 'RESIDENT', NULL);

-- Sample Resources
INSERT INTO resources (resource_type, resource_name, location, total_capacity, current_usage, unit, status, efficiency_score, managed_by) VALUES
('ENERGY', 'Solar Grid - Sector 12', 'Sector 12, Main Road', 5000, 3200, 'kWh', 'OPTIMAL', 92.5, 1),
('ENERGY', 'Wind Farm - East Zone', 'East Industrial Zone', 8000, 6500, 'kWh', 'WARNING', 78.3, 1),
('WATER', 'Central Water Plant', 'Central District', 100000, 65000, 'Liters', 'OPTIMAL', 88.0, 1),
('WATER', 'North Zone Reservoir', 'North Zone', 50000, 42000, 'Liters', 'WARNING', 72.5, 1),
('WASTE', 'Recycling Center - West', 'West Industrial Area', 2000, 1400, 'Tons', 'OPTIMAL', 85.0, 2),
('WASTE', 'Landfill - South Zone', 'South Zone', 5000, 4200, 'Tons', 'CRITICAL', 45.0, 2),
('ENERGY', 'Thermal Plant - North', 'North Industrial Zone', 12000, 9800, 'kWh', 'WARNING', 68.5, 1);

-- Sample Traffic Data
INSERT INTO traffic_data (location, latitude, longitude, congestion_level, average_speed, vehicle_count, alert_type, suggestion) VALUES
('MG Road Junction', 12.9716, 77.5946, 'HIGH', 15.5, 450, 'Congestion Alert', 'Consider alternate route via Ring Road'),
('Brigade Road', 12.9719, 77.6072, 'MODERATE', 25.0, 280, NULL, 'Normal flow expected after 7 PM'),
('Outer Ring Road - Marathahalli', 12.9568, 77.7011, 'SEVERE', 8.2, 620, 'Heavy Traffic', 'Major congestion due to construction work'),
('Whitefield Main Road', 12.9698, 77.7500, 'LOW', 45.0, 120, NULL, 'Clear roads'),
('Electronic City Flyover', 12.8399, 77.6770, 'MODERATE', 30.5, 310, NULL, 'Moderate traffic expected during peak hours'),
('Hebbal Flyover', 13.0358, 77.5970, 'HIGH', 12.0, 520, 'Accident Alert', 'Minor accident reported, expect delays');

-- Sample Safety Incidents
INSERT INTO safety_incidents (incident_type, location, latitude, longitude, description, severity, status, reported_by) VALUES
('SECURITY', 'Central Park Area', 12.9716, 77.5946, 'Suspicious activity reported near Central Park entrance', 'MEDIUM', 'IN_PROGRESS', 5),
('EMERGENCY', 'Sector 5 Residential', 12.9750, 77.6000, 'Medical emergency - elderly person collapsed', 'HIGH', 'RESOLVED', 6),
('FIRE', 'Industrial Zone B', 12.9600, 77.7100, 'Small fire reported in abandoned warehouse', 'HIGH', 'IN_PROGRESS', 5),
('ACCIDENT', 'Highway NH-44 Junction', 12.9500, 77.5800, 'Two-vehicle collision, minor injuries reported', 'MEDIUM', 'REPORTED', 7),
('SECURITY', 'Shopping Mall Complex', 12.9800, 77.6200, 'Theft reported at electronics store', 'LOW', 'RESOLVED', 6);

-- Sample Infrastructure Projects
INSERT INTO infrastructure_projects (project_name, description, category, location, planned_by, status, budget, spent_amount, start_date, end_date, impact_score) VALUES
('Smart Traffic Signal System', 'Rule-based adaptive traffic signals using corridor congestion metrics', 'TECHNOLOGY', 'City-wide', 3, 'IN_PROGRESS', 5000000, 2100000, '2026-01-15', '2026-12-31', 8.5),
('Green Belt Extension', 'Extension of city green belt by 15 km along the river', 'PUBLIC_SPACES', 'River Front Area', 3, 'APPROVED', 8000000, 500000, '2026-03-01', '2027-06-30', 9.2),
('Metro Line Phase 3', 'Construction of metro line connecting East and West zones', 'TRANSPORTATION', 'East-West Corridor', 4, 'PROPOSED', 150000000, 0, '2027-01-01', '2030-12-31', 9.8),
('Solar Rooftop Initiative', 'Installing solar panels on all government buildings', 'UTILITIES', 'Government Buildings', 4, 'IN_PROGRESS', 12000000, 4500000, '2026-02-01', '2026-11-30', 8.0),
('Smart Water Grid', 'Automated water distribution monitoring with threshold-based leak alerts', 'UTILITIES', 'Central & North Zones', 3, 'APPROVED', 25000000, 1000000, '2026-06-01', '2027-12-31', 8.8);

-- Sample Environmental Data
INSERT INTO environmental_data (location, latitude, longitude, air_quality_index, noise_level, pollution_level, temperature, humidity, co2_level, status, recommendation) VALUES
('Central Business District', 12.9716, 77.5946, 120, 72.5, 45.0, 28.5, 65.0, 420.0, 'MODERATE', 'Consider using public transport to reduce emissions'),
('Industrial Zone', 12.9600, 77.7100, 185, 85.0, 72.0, 30.0, 55.0, 580.0, 'UNHEALTHY', 'Industrial emission controls need enforcement'),
('Residential Sector 7', 12.9800, 77.6200, 65, 45.0, 20.0, 27.0, 70.0, 380.0, 'GOOD', 'Air quality is good. Enjoy outdoor activities'),
('Highway Corridor', 12.9500, 77.5800, 150, 78.0, 55.0, 29.5, 60.0, 490.0, 'UNHEALTHY', 'High vehicular emissions detected. Speed restrictions recommended'),
('City Park Area', 13.0000, 77.5900, 42, 35.0, 12.0, 26.0, 72.0, 350.0, 'GOOD', 'Excellent air quality. Green cover is effective'),
('Suburb North', 13.0358, 77.5970, 95, 55.0, 35.0, 28.0, 68.0, 410.0, 'MODERATE', 'Monitor construction dust levels');

-- Sample Service Requests
INSERT INTO service_requests (resident_id, service_type, title, description, location, priority, status) VALUES
(5, 'ROAD_REPAIR', 'Pothole on Main Road', 'Large pothole causing traffic disruption near Sector 5 junction', 'Sector 5 Main Road', 'HIGH', 'IN_PROGRESS'),
(5, 'STREET_LIGHTING', 'Broken Street Light', 'Street light not working for past 3 days at Lane 4', 'Sector 5, Lane 4', 'MEDIUM', 'SUBMITTED'),
(6, 'WATER_SUPPLY', 'Low Water Pressure', 'Very low water pressure during morning hours 6-8 AM', 'Sector 8 Residential', 'HIGH', 'IN_REVIEW'),
(6, 'WASTE_COLLECTION', 'Missed Garbage Collection', 'Garbage not collected for 2 consecutive days', 'Sector 8, Block C', 'MEDIUM', 'RESOLVED'),
(7, 'DRAINAGE', 'Blocked Drainage', 'Storm drain blocked causing waterlogging during rains', 'Market Area, Sector 3', 'URGENT', 'IN_PROGRESS'),
(7, 'PARK_MAINTENANCE', 'Park Equipment Damaged', 'Swing set and slide in community park are broken', 'Community Park, Sector 3', 'LOW', 'SUBMITTED');

-- Sample Feedback
INSERT INTO feedback (resident_id, category, subject, message, rating, status, admin_response) VALUES
(5, 'TRANSPORT', 'Improved Bus Service', 'The new bus routes have really improved my daily commute. Great work!', 5, 'ACKNOWLEDGED', 'Thank you for your positive feedback!'),
(5, 'INFRASTRUCTURE', 'Need Better Roads', 'Many roads in Sector 5 have potholes. Please prioritize road repair.', 2, 'IN_REVIEW', NULL),
(6, 'SERVICES', 'Water Supply Timing', 'Water supply timing should be extended during summer months', 3, 'SUBMITTED', NULL),
(6, 'SAFETY', 'More Street Lights Needed', 'Several lanes in Sector 8 are poorly lit. Safety concern for residents.', 2, 'ADDRESSED', 'Street lighting improvement project approved for Sector 8'),
(7, 'ENVIRONMENT', 'Tree Plantation Drive', 'Please organize more tree plantation drives in our area', 4, 'ACKNOWLEDGED', 'Community plantation drive scheduled for next month');

-- Sample City Events
INSERT INTO city_events (title, description, event_type, location, latitude, longitude, severity, start_time, end_time, is_active, created_by) VALUES
('Road Construction - NH44', 'Major road repair work on NH44. Expect diversions.', 'MAINTENANCE', 'NH44 Junction', 12.9500, 77.5800, 'WARNING', '2026-10-09 06:00:00', '2026-10-15 18:00:00', TRUE, 1),
('Dussehra Festival Celebrations', 'City-wide Dussehra celebrations with fireworks and cultural events', 'FESTIVAL', 'Central Park', 12.9716, 77.5946, 'INFO', '2026-10-10 17:00:00', '2026-10-10 22:00:00', TRUE, 1),
('Heavy Rain Warning', 'IMD has issued heavy rainfall warning for the next 48 hours', 'WEATHER', 'City-wide', 12.9716, 77.5946, 'ALERT', '2026-10-09 00:00:00', '2026-10-11 23:59:59', TRUE, 1),
('Water Supply Disruption', 'Scheduled maintenance of water treatment plant', 'MAINTENANCE', 'North Zone', 13.0358, 77.5970, 'WARNING', '2026-10-12 08:00:00', '2026-10-12 16:00:00', TRUE, 2),
('Traffic Diversion - Marathon', 'City marathon event. Roads closed in Central District.', 'TRAFFIC', 'Central District', 12.9750, 77.6000, 'WARNING', '2026-10-15 05:00:00', '2026-10-15 12:00:00', TRUE, 1);

-- Sample Simulated Sensor Nodes
INSERT INTO iot_sensors (sensor_name, sensor_type, location, latitude, longitude, status, last_reading, last_reading_time) VALUES
('TRF-001', 'TRAFFIC', 'MG Road Junction', 12.9716, 77.5946, 'ACTIVE', 450, NOW()),
('TRF-002', 'TRAFFIC', 'Brigade Road', 12.9719, 77.6072, 'ACTIVE', 280, NOW()),
('ENV-001', 'ENVIRONMENT', 'Central Business District', 12.9716, 77.5946, 'ACTIVE', 120, NOW()),
('ENV-002', 'ENVIRONMENT', 'Industrial Zone', 12.9600, 77.7100, 'ACTIVE', 185, NOW()),
('ENG-001', 'ENERGY', 'Solar Grid - Sector 12', 12.9800, 77.5900, 'ACTIVE', 3200, NOW()),
('WTR-001', 'WATER', 'Central Water Plant', 12.9700, 77.5950, 'ACTIVE', 65000, NOW()),
('SAF-001', 'SAFETY', 'Central Park CCTV', 12.9716, 77.5946, 'ACTIVE', 1, NOW()),
('SAF-002', 'SAFETY', 'Market Area CCTV', 12.9750, 77.6000, 'MAINTENANCE', 0, NOW());
