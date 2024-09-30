package org.ainzson.stimulator;

import com.taosdata.jdbc.TSDBDriver;
import com.taosdata.jdbc.ws.TSWSPreparedStatement;
import lombok.extern.slf4j.Slf4j;
import org.ainzson.Sensors.Temperature;
import java.text.MessageFormat;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TemperatureStimulator {
    private final List<Temperature> sensors = new ArrayList<>();
    private final String superTable = "rawdata.temperature";
    private final String subTable = "temperaturesensor";
    private final int NUMBER_OF_ASSET = 3;
    private final ScheduledExecutorService executor;

    public TemperatureStimulator() {
        for (int i = 0; i < NUMBER_OF_ASSET; i++) {
            sensors.add(new Temperature("asset00I" + i, "DEPT77777","TempS"+i));
        }
        this.executor = Executors.newScheduledThreadPool(1); // Configure as needed

    }

    public void push(Temperature temperature) {
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "Asia/Kolkata");
        String jdbcUrl = "jdbc:TAOS-RS://" + "localhost" + ":6041/?user=root&password=taosdata&batchfetch=true";

        try (Connection connection = DriverManager.getConnection(jdbcUrl,connProps)) {


            String query = getQuery(temperature);
            try (TSWSPreparedStatement preparedStatement = connection.prepareStatement("").unwrap(TSWSPreparedStatement.class)) {
                preparedStatement.execute("USE rawdata");
                preparedStatement.execute(query);
                log.info("Data inserted successfully into {} table.", subTable);

            } catch (SQLException sqlException) {
                log.error("Error executing insert statement: {} - SQL State: {}", sqlException.getLocalizedMessage(), sqlException.getSQLState());
                throw new RuntimeException(sqlException);
            }


        } catch (Exception ex) {
            log.error("Failed to connect to the database: {}, Error Message: {}", jdbcUrl, ex.getMessage());
        }
    }

    private String getQuery(Temperature temperature) {
        String pattern =    """
                                INSERT INTO {0} USING {1} TAGS(''{2}'', ''{3}'')
                                VALUES (''{4}'', ''{5}'', ''{6}'', ''{7}'', ''{8}'' , ''{9}'');
                             """;

        return MessageFormat.format(
                pattern,
                temperature.getDeviceId(),
                superTable,
                temperature.getTags().getAssetId(),
                temperature.getTags().getDepartmentId(),
                temperature.getTs(),
                temperature.getDeviceId(),
                temperature.getTemp(),
                temperature.getHumidity(),
                temperature.getDew_point(),
                temperature.getHeat_index()

        );
    }


    public void stimulator() {
        long initialDelay = 0;
        long period = 1; // Run every 1 minute

        executor.scheduleAtFixedRate(() -> {
            try {
                for (Temperature sensor : sensors) {
                    sensor.generateRandomDate();  // Generate random data
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

