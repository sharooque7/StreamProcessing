package org.ainzson.oops.utils;
import java.nio.file.*;

public class FileUtils {
    public static void createFileIfNotExist(String fileName) {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                System.out.println("File Created "+ fileName);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
