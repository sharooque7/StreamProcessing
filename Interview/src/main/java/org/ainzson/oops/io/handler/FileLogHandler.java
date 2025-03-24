package org.ainzson.oops.io.handler;

import org.ainzson.oops.io.interfaces.LogHandler;
import org.ainzson.oops.utils.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileLogHandler implements LogHandler {
    @Override
    public void writeLogs(String sourcePath, String destinationPath) {
        try (FileInputStream fis = new FileInputStream(sourcePath);
             FileOutputStream fos = new FileOutputStream(destinationPath)) {

            int data;
            while ((data = fis.read()) != -1) {
                fos.write(data);
            }

            System.out.println("Logs written successfully from " + sourcePath + " to " + destinationPath);
        } catch (IOException e) {
            System.err.println("Error writing logs: " + e.getMessage());
        }
    }

    @Override
    public void readLogs(String sourcePath) {
        try (FileInputStream fis = new FileInputStream(sourcePath)) {
            int data;
            while ((data = fis.read()) != -1) {
                System.out.print((char) data);
            }
            System.out.println("\nLogs read successfully from " + sourcePath);
        } catch (IOException e) {
            System.err.println("Error reading logs: " + e.getMessage());
        }
    }

    public void processLogsWithBuffering(String sourcePath, String destinationPath) {
        if (!Files.exists(Paths.get(sourcePath))) {
            System.out.println("Source file does not exist: " + sourcePath);
            return;
        }

        if (!Files.exists(Paths.get(destinationPath))) {
            FileUtils.createFileIfNotExist(destinationPath);
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourcePath), 1024);
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destinationPath), 1024)) {

            byte[] buffer = new byte[1024];  // 1KB buffer size
            int bytesRead;

            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            System.out.println("Buffered log processing completed with 1KB buffer.");

        } catch (IOException e) {
            System.out.println("Error processing logs: " + e.getMessage());
        }
    }

}
