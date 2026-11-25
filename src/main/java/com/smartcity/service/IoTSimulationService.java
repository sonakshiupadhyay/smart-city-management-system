package com.smartcity.service;

import com.smartcity.dao.EnvironmentalDAO;
import com.smartcity.dao.IoTSensorDAO;
import com.smartcity.dao.TrafficDAO;
import com.smartcity.model.IoTSensor;
import java.util.*;
import java.util.concurrent.*;

/**
 * IoTSimulationService - Generates simulated sensor telemetry in background threads.
 * Demonstrates Multithreading, Synchronization, and Thread-Safe Collections.
 * 
 * <p><strong>Important:</strong> This service does NOT connect to real IoT hardware
 * or external sensor APIs. All telemetry values are randomly generated for
 * demonstration purposes. In a production system, this would be replaced by
 * real device integrations (e.g., MQTT brokers, sensor gateways).</p>
 * 
 * @author Smart City Management System
 */
public class IoTSimulationService {

    private static volatile IoTSimulationService instance;
    private final ScheduledExecutorService scheduler;
    private final Map<Integer, Double> latestReadingsCache;
    private final List<String> simulatedAlerts;
    private final Object lock = new Object();
    private boolean isRunning = false;

    private final IoTSensorDAO sensorDAO;
    private final TrafficDAO trafficDAO;
    private final EnvironmentalDAO environmentalDAO;
    private final Random random = new Random();

    private IoTSimulationService() {
        this.scheduler = Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r, "IoTSensorSimulatorThread");
            t.setDaemon(true);
            return t;
        });
        this.latestReadingsCache = new ConcurrentHashMap<>();
        this.simulatedAlerts = Collections.synchronizedList(new ArrayList<>());
        this.sensorDAO = new IoTSensorDAO();
        this.trafficDAO = new TrafficDAO();
        this.environmentalDAO = new EnvironmentalDAO();
    }

    public static IoTSimulationService getInstance() {
        if (instance == null) {
            synchronized (IoTSimulationService.class) {
                if (instance == null) {
                    instance = new IoTSimulationService();
                }
            }
        }
        return instance;
    }

    /**
     * Starts the periodic simulation background thread with synchronization.
     */
    public synchronized void startSimulation() {
        if (isRunning) return;
        isRunning = true;

        // Schedule periodic sensor readings generation every 15 seconds
        scheduler.scheduleAtFixedRate(this::simulateTelemetry, 2, 15, TimeUnit.SECONDS);
    }

    /**
     * Generates randomized telemetry values and updates database and thread-safe caches.
     * All readings are simulated — no real sensor hardware is involved.
     */
    private void simulateTelemetry() {
        try {
            List<IoTSensor> sensors = sensorDAO.findActive();
            for (IoTSensor sensor : sensors) {
                double delta = (random.nextDouble() * 10.0) - 5.0; // slight variance
                double newReading = Math.max(0, sensor.getLastReading() + delta);
                
                // Synchronized cache update
                synchronized (lock) {
                    latestReadingsCache.put(sensor.getSensorId(), newReading);
                }

                // Persist new reading
                sensorDAO.updateReading(sensor.getSensorId(), Math.round(newReading * 10.0) / 10.0);

                // High threshold trigger
                if ("ENVIRONMENT".equalsIgnoreCase(sensor.getSensorType()) && newReading > 160) {
                    addAlert("Critical AQI spike detected at " + sensor.getLocation() + ": " + Math.round(newReading));
                } else if ("TRAFFIC".equalsIgnoreCase(sensor.getSensorType()) && newReading > 550) {
                    addAlert("Traffic bottleneck warning near " + sensor.getLocation() + " (" + (int)newReading + " vehicles/hr)");
                }
            }
        } catch (Exception e) {
            // Avoid terminating the scheduled background executor
            System.err.println("Error in IoT Simulation background thread: " + e.getMessage());
        }
    }

    /**
     * Thread-safe alert recording.
     */
    public void addAlert(String alertMessage) {
        synchronized (simulatedAlerts) {
            simulatedAlerts.add(0, "[" + new Date() + "] " + alertMessage);
            if (simulatedAlerts.size() > 20) {
                simulatedAlerts.remove(simulatedAlerts.size() - 1);
            }
        }
    }

    /**
     * Thread-safe retrieval of recent simulated alerts.
     */
    public List<String> getRecentAlerts() {
        synchronized (simulatedAlerts) {
            return new ArrayList<>(simulatedAlerts);
        }
    }

    public Double getCachedReading(int sensorId) {
        return latestReadingsCache.get(sensorId);
    }

    /**
     * Stops the background simulation only if an instance was already initialized.
     */
    public static void shutdownIfRunning() {
        IoTSimulationService current = instance;
        if (current != null) {
            current.stopSimulation();
        }
    }

    public synchronized void stopSimulation() {
        if (isRunning) {
            scheduler.shutdownNow();
            isRunning = false;
        }
        synchronized (IoTSimulationService.class) {
            if (instance == this) {
                instance = null;
            }
        }
    }
}
