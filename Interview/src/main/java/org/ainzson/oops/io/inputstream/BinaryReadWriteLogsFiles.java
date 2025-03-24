package org.ainzson.oops.io.inputstream;

import org.ainzson.oops.utils.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BinaryReadWriteLogsFiles {
    private final String logPath = "./logs/application.log";
    private final String destination = "./logs/binary/application.log";

    public void writeLogsToBinary() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(destination))) {
            dos.writeLong(System.currentTimeMillis()); // Timestamp
            dos.writeUTF("ERROR");  // Log level
            dos.writeUTF("Disk space low!"); // Log message
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void readLogsFromBinary() {
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(destination)))) {

            while (dis.available() > 0) {
                long timestamp = dis.readLong();
                String logLevel = dis.readUTF();
                String message = dis.readUTF();

                System.out.println("Read Log: [" + timestamp + "] [" + logLevel + "] " + message);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
