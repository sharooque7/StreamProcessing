package org.ainzson.oops.io.inputstream;


import org.ainzson.oops.utils.FileUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileInputStreamReadLog {
    private final String logPath = "./logs/application.log";
    private final String destination = "./logs/processed/application.log";

    public void ReadLogs() {
        try(FileInputStream fis = new FileInputStream(logPath)) {
            int data;
            while ((data = fis.read()) != -1) {
                System.out.println((char) data);
            }
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void writeLogs() {
        // Ensure the destination file exists
        if (!Files.exists(Paths.get(destination))) {
            FileUtils.createFileIfNotExist(destination);
        }

        // Use try-with-resources to automatically close streams
        try (FileInputStream fis = new FileInputStream(logPath);
             FileOutputStream fos = new FileOutputStream(destination)) {

            // Copy data from source to destination
            int data;
            while ((data = fis.read()) != -1) {
                fos.write(data);
            }

            System.out.println("Log file copied successfully.");
        } catch (IOException e) {
            System.err.println("Error copying log file: " + e.getMessage());
        }
    }
}