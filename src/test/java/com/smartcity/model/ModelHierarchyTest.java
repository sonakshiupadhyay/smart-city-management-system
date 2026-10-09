package com.smartcity.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ModelHierarchyTest - Verifies OOP Inheritance, Polymorphism, and Interfaces.
 */
public class ModelHierarchyTest {

    @Test
    public void testUserPolymorphism() {
        User admin = new CityAdministrator("admin_test", "pass", "admin@test.com", "Admin Test");
        User planner = new UrbanPlanner("plan_test", "pass", "planner@test.com", "Planner Test");
        User resident = new Resident("res_test", "pass", "res@test.com", "Resident Test");

        // Polymorphic invocation of getDashboardURL()
        assertEquals("/admin/dashboard", admin.getDashboardURL());
        assertEquals("/planner/dashboard", planner.getDashboardURL());
        assertEquals("/resident/dashboard", resident.getDashboardURL());

        assertEquals("ADMIN", admin.getRole());
        assertEquals("PLANNER", planner.getRole());
        assertEquals("RESIDENT", resident.getRole());
    }

    @Test
    public void testMonitorableInterfaceImplementation() {
        Monitorable sensor = new IoTSensor("TRF-99", "TRAFFIC", "West Gate", 12.9, 77.5, "ACTIVE");
        assertEquals("ACTIVE", sensor.getStatus());
        assertFalse(sensor.hasAlerts());

        TrafficData severeTraffic = new TrafficData("Expressway", "SEVERE", 5.0, 700);
        assertTrue(severeTraffic.hasAlerts());
        assertEquals(5, severeTraffic.getSeverityLevel());

        SafetyIncident fireIncident = new SafetyIncident("FIRE", "District 4", "Warehouse blaze", "CRITICAL");
        assertTrue(fireIncident.hasAlerts());
        assertEquals(5, fireIncident.getSeverityLevel());
    }

    @Test
    public void testResourceHierarchy() {
        Resource energy = new EnergyResource("Solar Grid East", "East Sector", 5000, 3500, "SOLAR");
        assertEquals("ENERGY", energy.getResourceType());
        assertEquals("SOLAR", ((EnergyResource) energy).getEnergySource());

        Resource water = new WaterResource("North Reservoir", "North Gate", 80000, 50000, "POTABLE");
        assertEquals("WATER", water.getResourceType());

        Resource waste = new WasteResource("West Plant", "West District", 3000, 2100, "RECYCLABLE");
        assertEquals("WASTE", waste.getResourceType());
    }
}
