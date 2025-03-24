package org.ainzson.oops.io.inputstream;

import org.ainzson.oops.utils.FileUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class ObjectInputStreamReadLog {
    private final String logPath = "./logs/serialize/application.log";

    public void wirteLogObject(List<LogEntry> logEntry) {

        if (!Files.exists(Paths.get(logPath))) {
            FileUtils.createFileIfNotExist(logPath);
        }
        try(ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(logPath))) {
            ois.writeObject(logEntry);
            System.out.println("Log entry written successfully!");
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void readLogObject() {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(logPath))) {
            List<LogEntry> logEntry = (List<LogEntry>) ois.readObject();
            for (LogEntry log : logEntry) {
                System.out.println(log.toString());
            }
            System.out.println("Read from file: " + logEntry.toString());
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
