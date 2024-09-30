package org.ainzson.ringframe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taosdata.jdbc.TSDBDriver;
import com.taosdata.jdbc.TSDBPreparedStatement;
import com.taosdata.jdbc.ws.TSWSPreparedStatement;
import lombok.extern.slf4j.Slf4j;
import org.ainzson.Sensors.Temperature;
import org.ainzson.Sensors.Vibration;
import org.ainzson.config.TDengineConnector;

import java.sql.DriverManager;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RingFrameStimulator {
    private final List<RingFrame40> ringFrame40 = new ArrayList<>();
    private static final int NUM_ASSETS = 1;
    private final String superTable = "test";
    private final String subTable = "dep0929402177c41e1ba_40";
    private final ScheduledExecutorService executor;

    public RingFrameStimulator() {
        // Initialize the 10 assets
        for (int i = 1; i <= NUM_ASSETS; i++) {
            ringFrame40.add(new RingFrame40("TNT090155315f8257f07","BSN0907214650d7844a8","UNI091953640535f1349","SHE092613357ce0a5592","DEP0929402177c41e1ba", "AST0935486169d78fdbe"));
        }
        this.executor = Executors.newScheduledThreadPool(1); // Configure as needed

    }


    private String tenant;
    @JsonProperty("business")
    private String business;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("shed")
    private String shed;
    @JsonProperty("department")
    private String department;
    @JsonProperty("asset")
    private String asset;
    private void setTags(TSWSPreparedStatement preparedStatement, RingFrame40 ringFrame40) throws SQLException {
        preparedStatement.setTagString(1, ringFrame40.getTenant());
        preparedStatement.setTagString(2, ringFrame40.getBusiness());
        preparedStatement.setTagString(3, ringFrame40.getUnit());
        preparedStatement.setTagString(4, ringFrame40.getShed());
        preparedStatement.setTagString(5, ringFrame40.getDepartment());
        preparedStatement.setTagString(6, ringFrame40.getAsset());
    }

    // Helper method to set values in the prepared statement
    private void setValues(TSWSPreparedStatement preparedStatement, RingFrame40 ringFrame40) throws SQLException {
//       ts
        preparedStatement.setTimestamp(7, Timestamp.valueOf(ringFrame40.getTs())); // Timestamp
        preparedStatement.setString(8, ringFrame40.getTsAct());                    // Temperature
//      shift
        preparedStatement.setFloat(9, ringFrame40.getShiftNumber());                    // Temperature
        preparedStatement.setFloat(10, ringFrame40.getShiftDate());                // Humidity
        preparedStatement.setFloat(11, ringFrame40.getShiftMonth());               // Dew Point
        preparedStatement.setFloat(12, ringFrame40.getShiftYear());
        preparedStatement.setFloat(13, ringFrame40.getShiftHour());
        preparedStatement.setFloat(14, ringFrame40.getShiftMinute());

        preparedStatement.setFloat(15, ringFrame40.getProductionInHanks());         // Heat Index
        preparedStatement.setFloat(16, ringFrame40.getProductionInMeters());         // Heat Index
        preparedStatement.setFloat(17, ringFrame40.getWeightPerSpindle());         // Heat Index
        preparedStatement.setFloat(18, ringFrame40.getNoOfDoffs());         // Heat Index
        preparedStatement.setFloat(19, ringFrame40.getAirConsumption());         // Heat Index

        preparedStatement.setFloat(20, ringFrame40.getRunTimeHour());         // Heat Index
        preparedStatement.setFloat(21, ringFrame40.getRunTimeMinute());         // Heat Index
        preparedStatement.setFloat(22, ringFrame40.getIdleTimeHour());         // Heat Index
        preparedStatement.setFloat(23, ringFrame40.getIdleTimeMinute());         // Heat Index
        preparedStatement.setFloat(24, ringFrame40.getPowerFailTimeHour());         // Heat Index
        preparedStatement.setFloat(25, ringFrame40.getPowerFailTimeMinute());         // Heat Index
        preparedStatement.setFloat(26, ringFrame40.getDoffTimeHour());         // Heat Index
        preparedStatement.setFloat(27, ringFrame40.getDoffTimeMinute());         // Heat Index

        preparedStatement.setFloat(28, ringFrame40.getCounts());         // Heat Index
        preparedStatement.setFloat(29, ringFrame40.getAverageTpi());         // Heat Index
        preparedStatement.setFloat(30, ringFrame40.getAverageSpindleSpeed());         // Heat Index
        preparedStatement.setFloat(31, ringFrame40.getAverageDelivery());         // Heat Index

        preparedStatement.setFloat(32, ringFrame40.getVoltageAvg());         // Heat Index
        preparedStatement.setFloat(33, ringFrame40.getCurrentAvg());         // Heat Index
        preparedStatement.setFloat(34, ringFrame40.getPowerFactor());         // Heat Index
        preparedStatement.setFloat(35, ringFrame40.getFrequency());         // Heat Index
        preparedStatement.setFloat(36, ringFrame40.getTotalActivePower());         // Heat Index
        preparedStatement.setFloat(37, ringFrame40.getTotalKva());         // Heat Index
        preparedStatement.setFloat(38, ringFrame40.getTotalKvarh());         // Heat Index
        preparedStatement.setFloat(39, ringFrame40.getTotalKwh());         // Heat Index
        preparedStatement.setFloat(40, ringFrame40.getTotalKvah());         // Heat Index
        preparedStatement.setFloat(41, ringFrame40.getTotalKvarh());         // Heat Index

        preparedStatement.setFloat(42, ringFrame40.getOverallMachineUtilization());         // Heat Index
        preparedStatement.setFloat(43, ringFrame40.getProductionEfficiency());         // Heat Index
        preparedStatement.setFloat(44, ringFrame40.getProductionKg());         // Heat Index
        preparedStatement.setFloat(45, ringFrame40.getUkg());         // Heat Index
        preparedStatement.setFloat(46, ringFrame40.getGpss());         // Heat Index

    }

    private void executeInsert(Connection connection, String subTable, String superTable, RingFrame40 ringFrame40) throws SQLException {
        String sql = "INSERT INTO "
                + subTable + " USING "
                + ringFrame40.getDepartment() +"_40" +
                " TAGS(?, ?, ?, ?, ?, ?) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?)";
        try (TSWSPreparedStatement preparedStatement = connection.prepareStatement(sql).unwrap(TSWSPreparedStatement.class)) {
            // Set tags and values
            preparedStatement.execute("USE test");
            setTags(preparedStatement, ringFrame40);
            setValues(preparedStatement, ringFrame40);
            preparedStatement.execute();
            log.info("Data inserted successfully into sub-table: {}", subTable);
        }
    }

    public void push(RingFrame40 ringFrame40) {
        try (Connection connection = TDengineConnector.getConnection()) {
            String subTable = ringFrame40.getAsset().trim();
            executeInsert(connection, subTable, superTable, ringFrame40);
        } catch (Exception ex) {
            log.error("Failed to connect to the database: Error Message: {}", ex.getMessage());
        }
    }

    public void stimulator() {
        long initialDelay = 0;
        long period = 1; // Run every 1 minute

        executor.scheduleAtFixedRate(() -> {
            try {
                for (RingFrame40 ringFrame40 : ringFrame40) {
                    ringFrame40.generateRandomDate();  // Generate random data
                    log.info("Generated data: {}", ringFrame40); // Use logger instead of System.out.println
                    push(ringFrame40); // Push data to the database
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
