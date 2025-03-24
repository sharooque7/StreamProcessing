package org.ainzson.oops.io.inputstream;

import org.ainzson.oops.utils.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BufferedInputStreamReadLog {
    private final String logPath = "./logs/application.log";
    private final String destination = "./logs/processed/application.log";

    public void readLogs () {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(logPath),1024)) {
            int data;
            while ((data = bis.read()) != -1) {
                System.out.println((char) data);
            }
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void writeLogs() {
        if (!Files.exists(Paths.get(destination))) {
            FileUtils.createFileIfNotExist(destination);
        }
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(logPath));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destination))
             )
            {
                int data;
                while ((data = bis.read()) != -1) {
                    bos.write( data);
                }
                System.out.println("Buffered log writing completed with 1KB buffer.");
            }
        catch (Exception exception) {
                 System.out.println(exception.getMessage());
        }
    }
}
