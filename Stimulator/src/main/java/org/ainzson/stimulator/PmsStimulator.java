package org.ainzson.stimulator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taosdata.jdbc.ws.TSWSPreparedStatement;
import lombok.extern.slf4j.Slf4j;
import org.ainzson.Sensors.Pressure;
import org.ainzson.Stimulator;
import org.ainzson.config.TDengineConnector;
import org.ainzson.model.MachineStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PmsStimulator {

    private final List<MachineStatus> machineStatuses = new ArrayList<>();
    private final String superTable = "normalized_pms";

    private static final String[] ListAsset ;

    static  {
        ListAsset = loadAsset();
    }
    private final int NUMBER_OF_ASSET = ListAsset.length;
    private final ScheduledExecutorService executor;

    public PmsStimulator() {
        for (int i = 0; i < NUMBER_OF_ASSET; i++) {
            machineStatuses.add(new MachineStatus(ListAsset[i], "TNT06090533644d3bb08","SIT063707182c94268b5","BSN12113925478a40f2b","DEP122338230328c31ec","CEL122717164f8a37e96"));
        }
        this.executor = Executors.newScheduledThreadPool(1); // Configure as needed
    }

    private static String[] loadAsset() {
        try(InputStream assetStream = Stimulator.class.getClassLoader().getResourceAsStream("Data/asset.json");) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(assetStream, new TypeReference<String[]>() {});
        }
        catch (IOException e) {
            e.printStackTrace();
            return new String[0];  // Return an empty array if there was an error
        }

    }


    private void setTags(TSWSPreparedStatement preparedStatement, MachineStatus machineStatus) throws SQLException {
        preparedStatement.setTagString(1, machineStatus.getTags().getAsset());
        preparedStatement.setTagString(2, machineStatus.getTags().getTenant());
        preparedStatement.setTagString(3, machineStatus.getTags().getSite());
        preparedStatement.setTagString(4, machineStatus.getTags().getSubtenant());
        preparedStatement.setTagString(5, machineStatus.getTags().getCell());
        preparedStatement.setTagString(6, machineStatus.getTags().getDepartment());
    }

    private void setValues(TSWSPreparedStatement preparedStatement, MachineStatus machineStatus) throws SQLException {
        preparedStatement.setTimestamp(7, Timestamp.valueOf(machineStatus.getTs())); // Timestamp
        preparedStatement.setTimestamp(8, Timestamp.valueOf(machineStatus.getTs_act())); // Timestamp
        preparedStatement.setInt(9, machineStatus.getStatus());
        preparedStatement.setBoolean(10, machineStatus.isProcessed());
        preparedStatement.setFloat(11, machineStatus.getFeedRate());
        preparedStatement.setFloat(12, machineStatus.getSpindleLoad());
        preparedStatement.setInt(13, machineStatus.getInterLockPMCStatus());
        preparedStatement.setLong(14, machineStatus.getCuttingTime());
        preparedStatement.setLong(15, machineStatus.getSpindleSpeed());
        preparedStatement.setLong(16, machineStatus.getPartCount());
        preparedStatement.setLong(17, machineStatus.getCycleTime());
        preparedStatement.setInt(18, machineStatus.getFeedRateOverride());
    }

    private void executeInsert(Connection connection, String subTable, MachineStatus machineStatus) throws SQLException {
        String sql = "INSERT INTO " + subTable + " USING " + "normalized.normalized_pms" + " TAGS(?, ?, ?, ?, ?, ?) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (TSWSPreparedStatement preparedStatement = connection.prepareStatement(sql).unwrap(TSWSPreparedStatement.class)) {
            // Set tags and values
            preparedStatement.execute("USE normalized");
            setTags(preparedStatement, machineStatus);
            setValues(preparedStatement, machineStatus);

            preparedStatement.execute();
            log.info("Data inserted successfully into sub-tables: {}", subTable);
        }
    }

    public void push(MachineStatus machineStatus) {
        try (Connection connection = TDengineConnector.getConnection()) {
            String subTable = machineStatus.getTags().getAsset();
            executeInsert(connection, subTable, machineStatus);
        } catch (Exception ex) {
            log.error("Failed to connect to the database: Error Message: {}", ex.getMessage());
        }
    }

    public void stimulator() {
        long initialDelay = 0;
        long period = 1; // Run every 1 minute

        executor.scheduleAtFixedRate(() -> {
            try {
                for (MachineStatus machineStatus : machineStatuses) {
                    machineStatus.generateRandomData();  // Generate random data
                    log.info("Generated data: {}", machineStatus); // Use logger instead of System.out.println
                    push(machineStatus); // Push data to the database
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
