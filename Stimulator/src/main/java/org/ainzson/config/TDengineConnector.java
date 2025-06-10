package org.ainzson.config;

import com.taosdata.jdbc.TSDBDriver;
import com.taosdata.jdbc.ws.TSWSPreparedStatement;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.ainzson.Sensors.Vibration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

@Slf4j
public class TDengineConnector {

//    private static final String JDBC_URL = "jdbc:TAOS-RS://localhost:6041/?batchfetch=true";
    private static final String JDBC_URL = "jdbc:TAOS://localhost:6030/?batchfetch=true";

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "taosdata";
    private static final HikariDataSource dataSource;

    HikariConfig config = new HikariConfig();

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);

        // Connection Pool Optimizations
        config.setMinimumIdle(2);            // Maintain at least 2 idle connections
        config.setMaximumPoolSize(50);        // Limit total connections to 5
        config.setConnectionTimeout(10000);  // Wait max 10 sec for a connection
        config.setMaxLifetime(300000);       // Recycle connections every 5 mins
        config.setIdleTimeout(60000);        // Close idle connections after 1 min
        config.setConnectionTestQuery("SELECT SERVER_STATUS()");

        dataSource = new HikariDataSource(config);
    }

    // Common properties for the connection
    private static Properties getConnectionProperties() {
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "Asia/Kolkata");
        return connProps;
    }

    // Method to establish and return a connection
    public static Connection getConnection() {
        try {
            return dataSource.getConnection();

//            return DriverManager.getConnection(JDBC_URL, getConnectionProperties());
        }
        catch (SQLException sqlException) {
            log.error("Failed to connect to the database: {}, Error Message: {}", JDBC_URL, sqlException.getMessage());
            throw  new RuntimeException(sqlException);
        }
    }
    public static void closePool() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}

//private void setTags(TSWSPreparedStatement preparedStatement, Vibration vibration) throws SQLException {
//    log.info("Asset"+vibration.getTags().getAssetId());
//    preparedStatement.setTagString(1, vibration.getTags().getAssetId());
//    preparedStatement.setTagString(2, vibration.getTags().getDepartmentId());
//}
//
//// Helper method to set values in the prepared statement
//private void setValues(TSWSPreparedStatement preparedStatement, Vibration vibration) throws SQLException {
//    System.out.println("sss");
//    preparedStatement.setTimestamp(3, Timestamp.valueOf(vibration.getTs())); // Timestamp
//    preparedStatement.setDouble(4, vibration.getAcceleration());                    // Temperature
//    preparedStatement.setDouble(5, vibration.getVelocity());                // Humidity
//    preparedStatement.setDouble(6, vibration.getDisplacement());               // Dew Point
//    preparedStatement.setDouble(7, vibration.getFrequency());              // Heat Index
//}
//
//private void executeInsert(Connection connection, String subTable, String superTable, Vibration vibration) throws SQLException {
//    String sql = "INSERT INTO " + subTable + " USING " + superTable + " TAGS(?, ?) VALUES (?, ?, ?, ?, ?)";
//    try (TSWSPreparedStatement preparedStatement = connection.prepareStatement(sql).unwrap(TSWSPreparedStatement.class)) {
//        // Set tags and values
//        preparedStatement.execute("USE rawdata");
//        setTags(preparedStatement, vibration);
//        setValues(preparedStatement, vibration);
//        preparedStatement.execute();
//        log.info("Data inserted successfully into sub-table: {}", subTable);
//    }
//}
//
//public void push(Vibration vibration) {
//    try (Connection connection = new TDengineConnector().getConnection()) {
////            String subTable = temperature.getTags().getAssetId().trim();
////            String superTable = "rawdata.temperature";
//        executeInsert(connection, subTable, superTable, vibration);
//    } catch (Exception ex) {
//        log.error("Failed to connect to the database: Error Message: {}", ex.getMessage());
//    }
//
//}
