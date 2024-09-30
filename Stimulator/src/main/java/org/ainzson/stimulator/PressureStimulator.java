package org.ainzson.stimulator;

import com.taosdata.jdbc.ws.TSWSPreparedStatement;
import lombok.extern.slf4j.Slf4j;
import org.ainzson.Sensors.Pressure;
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
public class PressureStimulator {

    private final List<Pressure> sensors = new ArrayList<>();
    private final String superTable = "rawdata.pressure";
    private final int NUMBER_OF_ASSET = 3;
    private final ScheduledExecutorService executor;

    public PressureStimulator() {
        for (int i = 0; i < NUMBER_OF_ASSET; i++) {
            sensors.add(new Pressure("asset00I" + i, "DEPT77777","PSR"+i));
        }
        this.executor = Executors.newScheduledThreadPool(1); // Configure as needed
    }

    private void setTags(TSWSPreparedStatement preparedStatement, Pressure pressure) throws SQLException {
        preparedStatement.setTagString(1, pressure.getTags().getAssetId());
        preparedStatement.setTagString(2, pressure.getTags().getDepartmentId());
    }

    private void setValues(TSWSPreparedStatement preparedStatement, Pressure pressure) throws SQLException {
        preparedStatement.setTimestamp(3, Timestamp.valueOf(pressure.getTs())); // Timestamp
        preparedStatement.setString(4, pressure.getDeviceId());                    // Temperature
        preparedStatement.setDouble(5, pressure.getPressure());                    // Temperature
        preparedStatement.setDouble(6, pressure.getAbsolute_pressure());                // Humidity
        preparedStatement.setDouble(7, pressure.getDifferential_pressure());               // Dew Point
        preparedStatement.setDouble(8, pressure.getTemperature());               // Dew Point

    }

    private void executeInsert(Connection connection, String subTable, String superTable, Pressure pressure) throws SQLException {
        String sql = "INSERT INTO " + subTable + " USING " + superTable + " TAGS(?, ?) VALUES (?, ?, ?, ?, ?, ?)";
        try (TSWSPreparedStatement preparedStatement = connection.prepareStatement(sql).unwrap(TSWSPreparedStatement.class)) {
            // Set tags and values
            preparedStatement.execute("USE rawdata");
            setTags(preparedStatement, pressure);
            setValues(preparedStatement, pressure);
            preparedStatement.execute();
            log.info("Data inserted successfully into sub-table: {}", subTable);
        }
    }

    public void push(Pressure pressure) {
        try (Connection connection = TDengineConnector.getConnection()) {
            String subTable = pressure.getDeviceId().trim();
            executeInsert(connection, subTable, superTable, pressure);
        } catch (Exception ex) {
            log.error("Failed to connect to the database: Error Message: {}", ex.getMessage());
        }
    }

    public void stimulator() {
        long initialDelay = 0;
        long period = 1; // Run every 1 minute

        executor.scheduleAtFixedRate(() -> {
            try {
                for (Pressure sensor : sensors) {
                    sensor.generateRandomData();  // Generate random data
                    log.info("Generated data: {}", sensor); // Use logger instead of System.out.println
                    push(sensor); // Push data to the database
                }
            } catch (Exception e) {
                log.error("Error during data generation and insertion: {}", e.getMessage(), e);
            }
        }, initialDelay, period, TimeUnit.MINUTES);
    }

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
