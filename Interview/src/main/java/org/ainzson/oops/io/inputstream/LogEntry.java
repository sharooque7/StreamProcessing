package org.ainzson.oops.io.inputstream;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LogEntry implements Serializable {
    private static final long serialVersionUID = 1L; // Ensures compatibility
    private String logLevel;
    private String message;
    private LocalDateTime timestamp;

    public LogEntry(String logLevel, String message) {
        this.logLevel = logLevel;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + logLevel + ": " + message;
    }
}
