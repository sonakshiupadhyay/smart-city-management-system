package com.smartcity.util;

import com.smartcity.service.IoTSimulationService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Lifecycle listener that cleanly stops background telemetry simulation and shuts down
 * the HikariCP connection pool when the web application is stopped or undeployed.
 *
 * @author Smart City Management System
 */
@WebListener
public class DatabaseConnectionPoolListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Pool is lazily initialized on first use via DatabaseConnection.getInstance()
        System.out.println("[DatabaseConnectionPoolListener] Application context initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            IoTSimulationService.shutdownIfRunning();
            DatabaseConnection.shutdownIfInitialized();
            System.out.println("[DatabaseConnectionPoolListener] Telemetry simulation and connection pool shut down cleanly.");
        } catch (Exception e) {
            System.err.println("[DatabaseConnectionPoolListener] Error during shutdown: " + e.getMessage());
        }
    }
}
