package org.ainzson.oops.io.inputstream;

import org.ainzson.oops.utils.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SequentialLogMerger {
    private final String log1 = "./logs/application.log";
    private final String log2 = "./logs/security.log";
    private final String log3 = "./logs/system.log";
    private static final String DESTINATION = "./logs/merged.log";



    public void mergeLogs() throws FileNotFoundException {

        if(Files.exists(Paths.get(DESTINATION))) {
            FileUtils.createFileIfNotExist(DESTINATION);
        }

        try(FileInputStream application = new FileInputStream(log1);
            FileInputStream security = new FileInputStream(log2);
            FileInputStream system = new FileInputStream(log3);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(DESTINATION))) {

            List<FileInputStream> streams = List.of(application,security,system);

            SequenceInputStream sequenceInputStream = new SequenceInputStream(Collections.enumeration(streams));

            int data;
            while ((data =  sequenceInputStream.read()) != -1) {
                bos.write(data);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
