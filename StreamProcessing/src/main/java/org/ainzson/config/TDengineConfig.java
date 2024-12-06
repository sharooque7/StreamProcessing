package org.ainzson.config;

import com.taosdata.jdbc.TSDBDriver;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class TDengineConfig {
    private static final String JDBC_URL = "jdbc:TAOS-RS://10.20.30.128:6041/?user=root&password=taosdata&batchfetch=true";
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
            return DriverManager.getConnection(JDBC_URL, getConnectionProperties());
        }
        catch (SQLException sqlException) {
            log.error("Failed to connect to the database: {}, Error Message: {}", JDBC_URL, sqlException.getMessage());
            throw  new RuntimeException(sqlException);
        }
    }
}



