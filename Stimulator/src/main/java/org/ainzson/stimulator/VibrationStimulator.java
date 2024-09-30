package org.ainzson.stimulator;

import com.taosdata.jdbc.ws.TSWSPreparedStatement;
import lombok.extern.slf4j.Slf4j;
import org.ainzson.Sensors.Vibration;
import org.ainzson.config.TDengineConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class VibrationStimulator {
    private final List<Vibration> sensors = new ArrayList<>();
    private final String superTable = "rawdata.vibration";
    private final String subTable ="vibrationsensor";
    private final int NUMBER_OF_ASSET = 3;
    private final ScheduledExecutorService executor;

    public VibrationStimulator() {
        for (int i = 0; i < NUMBER_OF_ASSET; i++) {
            sensors.add(new Vibration("asset00I" + i, "DEPT77777","VSen"+i));
        }
        this.executor = Executors.newScheduledThreadPool(1); // Configure as needed

    }
    private void setTags(TSWSPreparedStatement preparedStatement, Vibration vibration) throws SQLException {
        preparedStatement.setTagString(1, vibration.getTags().getAssetId());
        preparedStatement.setTagString(2, vibration.getTags().getDepartmentId());
    }

    // Helper method to set values in the prepared statement
    private void setValues(TSWSPreparedStatement preparedStatement, Vibration vibration) throws SQLException {
        preparedStatement.setTimestamp(3, Timestamp.valueOf(vibration.getTs())); // Timestamp
        preparedStatement.setString(4, vibration.getDeviceid());                    // Temperature
        preparedStatement.setDouble(5, vibration.getAcceleration());                    // Temperature
        preparedStatement.setDouble(6, vibration.getVelocity());                // Humidity
        preparedStatement.setDouble(7, vibration.getDisplacement());               // Dew Point
        preparedStatement.setDouble(8, vibration.getFrequency());              // Heat Index
    }

    private void executeInsert(Connection connection, String subTable, String superTable, Vibration vibration) throws SQLException {
        String sql = "INSERT INTO " + subTable + " USING " + superTable + " TAGS(?, ?) VALUES (?, ?, ?, ?, ?, ?)";
        try (TSWSPreparedStatement preparedStatement = connection.prepareStatement(sql).unwrap(TSWSPreparedStatement.class)) {
            // Set tags and values
            preparedStatement.execute("USE rawdata");
            setTags(preparedStatement, vibration);
            setValues(preparedStatement, vibration);
            preparedStatement.execute();
            log.info("Data inserted successfully into sub-table: {}", subTable);
        }
    }

    public void push(Vibration vibration) {
        try (Connection connection = TDengineConnector.getConnection()) {
            String subTable = vibration.getDeviceid().trim();
            executeInsert(connection, subTable, superTable, vibration);
        } catch (Exception ex) {
            log.error("Failed to connect to the database: Error Message: {}", ex.getMessage());
        }
    }

    public void stimulator() {
        long initialDelay = 0;
        long period = 1; // Run every 1 minute

        executor.scheduleAtFixedRate(() -> {
            try {
                for (Vibration sensor : sensors) {
                    sensor.generateRandomData();  // Generate random data
                    log.info("Generated data: {}", sensor); // Use logger instead of System.out.println
                    push(sensor); // Push data to the database
                }
            } catch (Exception e) {
                log.error("Error during data generation and insertion: {}", e.getMessage(), e);
            }
        }, initialDelay, period, TimeUnit.MINUTES);
    }

    // Method to gracefully shut down the executor
    public void shutdown() {
        try {
            log.info("Shutting down the executor...");
            executor.shutdown();
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
            log.info("Executor shut down successfully.");
        } catch (InterruptedException ex) {
            log.error("Executor shutdown interrupted: {}", ex.getMessage(), ex);
            Thread.currentThread().interrupt();
        }
    }

}
