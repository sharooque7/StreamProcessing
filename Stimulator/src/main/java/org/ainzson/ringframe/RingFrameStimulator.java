package org.ainzson.ringframe;

import com.taosdata.jdbc.TSDBDriver;
import com.taosdata.jdbc.TSDBPreparedStatement;
import lombok.extern.slf4j.Slf4j;

import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RingFrameStimulator {
    private final List<Ringframe40> assets = new ArrayList<>();
    private static final int NUM_ASSETS = 3;

    public RingFrameStimulator() {
        // Initialize the 10 assets
        for (int i = 1; i <= NUM_ASSETS; i++) {
            assets.add(new Ringframe40("Asset00" + i, "RingFrame" + i, "Unit" + i));
        }
    }

    public void pushToTd(Ringframe40 ringframe40) {
        String jdbcUrl = "jdbc:TAOS://10.20.32.70:6030?user=root&password=taosdata";
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en-US.UTF+5:30");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "Asia/Kolkata");

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, connProps);

            String subTable = ringframe40.getAssetId().trim();
            String superTable = "test.ringframe_40";

            String tdSql = "INSERT INTO " + subTable + " USING " + superTable + " TAGS (?,?)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)";

            String query = String.format(
                    "INSERT INTO %s USING %s TAGS ('%s', %d) VALUES ('%s', '%s', '%s', '%s','%s', %d, '%s', %d, %d, %d, %d, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f)",
                    subTable,                                // Sub-table name
                    superTable,                              // Super-table name
                    ringframe40.getLocation(),               // First tag (location)
                    ringframe40.getGroupId(),                // Second tag (groupId or other integer tag)
                    ringframe40.getTs(),                     // Timestamp (ensure it's in the correct format)
                    ringframe40.getAssetId(),                // Asset ID
                    ringframe40.getDepartmentId(),           // Department ID
                    ringframe40.getSiteId(),                 // Site ID
                    ringframe40.getMachineId(),              // Machine ID
                    ringframe40.getShiftNumber(),            // Shift number
                    ringframe40.getShiftDate(),              // Shift date
                    ringframe40.getShiftMonth(),             // Shift month
                    ringframe40.getShiftYear(),              // Shift year
                    ringframe40.getShiftHour(),              // Shift hour
                    ringframe40.getShiftMinute(),            // Shift minute
                    ringframe40.getProductionInHanks(),      // Production in hanks
                    ringframe40.getProductionInMeters(),     // Production in meters
                    ringframe40.getWeightPerSpindle(),       // Weight per spindle
                    ringframe40.getNoOfDoffs(),              // Number of doffs
                    ringframe40.getAirConsumption(),         // Air consumption
                    ringframe40.getRunTimeHour(),            // Run time (hour)
                    ringframe40.getRunTimeMinute(),          // Run time (minute)
                    ringframe40.getIdleTimeHour(),           // Idle time (hour)
                    ringframe40.getIdleTimeMinute(),         // Idle time (minute)
                    ringframe40.getPowerFailTimeHour(),      // Power failure time (hour)
                    ringframe40.getPowerFailTimeMinute(),    // Power failure time (minute)
                    ringframe40.getDoffTimeHour(),           // Doff time (hour)
                    ringframe40.getDoffTimeMinute(),         // Doff time (minute)
                    ringframe40.getCounts(),                 // Counts
                    ringframe40.getAverageTpi(),             // Average TPI
                    ringframe40.getAverageSpindleSpeed(),    // Average spindle speed
                    ringframe40.getAverageDelivery(),        // Average delivery
                    ringframe40.getVoltageAvg(),             // Voltage average
                    ringframe40.getCurrentAvg(),             // Current average
                    ringframe40.getPowerFactor(),            // Power factor
                    ringframe40.getFrequency(),              // Frequency
                    ringframe40.getTotalActivePower(),       // Total active power
                    ringframe40.getTotalKva(),               // Total KVA
                    ringframe40.getTotalKvar(),              // Total KVAR
                    ringframe40.getTotalKwh(),               // Total KWH
                    ringframe40.getTotalKvah(),              // Total KVAH
                    ringframe40.getTotalKvarh(),             // Total KVARH
                    ringframe40.getOverallMachineUtilization(),  // Overall machine utilization
                    ringframe40.getProductionEfficiency(),   // Production efficiency
                    ringframe40.getProductionKg(),           // Production KG
                    ringframe40.getUkg(),                    // UKG
                    ringframe40.getGpss()                    // GPSS
            );

            try (TSDBPreparedStatement preparedStatement = connection.prepareStatement(tdSql).unwrap(TSDBPreparedStatement.class);) {
                preparedStatement.execute("USE test");
                preparedStatement.execute(query);

                log.info("Inserted Successfully");
            }


//                preparedStatement.execute("USE rawdata");
//
//                preparedStatement.setTagString(1, ringframe40.getLocation());
//                preparedStatement.setTagInt(2, 10);
//                preparedStatement.setTimestamp(3, ringframe40.getTs());
//                preparedStatement.setString(4, ringframe40.getAssetId());
//                preparedStatement.setString(5, ringframe40.getDepartmentId());
//                preparedStatement.setString(6, ringframe40.getSiteId());
//                preparedStatement.setString(7, ringframe40.getMachineId());
//                preparedStatement.setInt(8, ringframe40.getShiftNumber());
//                preparedStatement.setTimestamp(9, ringframe40.getShiftDate());
//                preparedStatement.setInt(10, String.valueOf(ringframe40.getShiftMonth()));
//                preparedStatement.setInt(11, ringframe40.getShiftYear());
//                preparedStatement.setInt(12, ringframe40.getShiftHour());
//                preparedStatement.setInt(13, ringframe40.getShiftMinute());
//                preparedStatement.setDouble(14, ringframe40.getProductionInHanks());
//                preparedStatement.setDouble(15, ringframe40.getProductionInMeters());
//                preparedStatement.setDouble(16, ringframe40.getWeightPerSpindle());
//                preparedStatement.setDouble(17, ringframe40.getNoOfDoffs());
//                preparedStatement.setDouble(18, ringframe40.getAirConsumption());
//                preparedStatement.setDouble(19, ringframe40.getRunTimeHour());
//                preparedStatement.setDouble(20, ringframe40.getRunTimeMinute());
//                preparedStatement.setDouble(21, ringframe40.getIdleTimeHour());
//                preparedStatement.setDouble(22, ringframe40.getIdleTimeMinute());
//                preparedStatement.setDouble(23, ringframe40.getPowerFailTimeHour());
//                preparedStatement.setDouble(24, ringframe40.getPowerFailTimeMinute());
//                preparedStatement.setDouble(25, ringframe40.getDoffTimeHour());
//                preparedStatement.setDouble(26, ringframe40.getDoffTimeMinute());
//                preparedStatement.setDouble(27, ringframe40.getCounts());
//                preparedStatement.setDouble(28, ringframe40.getAverageTpi());
//                preparedStatement.setDouble(29, ringframe40.getAverageSpindleSpeed());
//                preparedStatement.setDouble(30, ringframe40.getAverageDelivery());
//                preparedStatement.setDouble(31, ringframe40.getVoltageAvg());
//                preparedStatement.setDouble(32, ringframe40.getCurrentAvg());
//                preparedStatement.setDouble(33, ringframe40.getPowerFactor());
//                preparedStatement.setDouble(34, ringframe40.getFrequency());
//                preparedStatement.setDouble(35, ringframe40.getTotalActivePower());
//                preparedStatement.setDouble(36, ringframe40.getTotalKva());
//                preparedStatement.setDouble(37, ringframe40.getTotalKvar());
//                preparedStatement.setDouble(38, ringframe40.getTotalKwh());
//                preparedStatement.setDouble(39, ringframe40.getTotalKvah());
//                preparedStatement.setDouble(40, ringframe40.getTotalKvarh());
//                preparedStatement.setDouble(41, ringframe40.getOverallMachineUtilization());
//                preparedStatement.setDouble(42, ringframe40.getProductionEfficiency());
//                preparedStatement.setDouble(43, ringframe40.getProductionKg());
//                preparedStatement.setDouble(44, ringframe40.getUkg());
//                preparedStatement.setDouble(45, ringframe40.getGpss());
//
//                preparedStatement.execute();
//                System.out.println("Inserted Successfully");
//            }
            catch (SQLException e) {
                log.info(e.getLocalizedMessage());
                log.info(e.getSQLState());
                throw new RuntimeException(e);

            }
        }
        catch (Exception ex) {
            System.out.printf("Failed to connect to %s, %sErrMessage: %s%n",
                    jdbcUrl,
                    "",
                    ex.getMessage());
            log.info(ex.getLocalizedMessage());
        }
    }

    public void stimulator() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            // For each asset, generate new data and print it out
            for (Ringframe40 asset : assets) {
                asset.generateRandomDate();
                System.out.println(asset);

                pushToTd(asset);

            }
        }, 0, 1, TimeUnit.MINUTES); // Run every 1 minute
    }
}
