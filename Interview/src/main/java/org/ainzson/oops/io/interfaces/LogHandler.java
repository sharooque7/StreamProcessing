package org.ainzson.oops.io.interfaces;

public interface LogHandler {
    void writeLogs(String sourcePath, String destinationPath);
    void readLogs(String sourcePath);
}
