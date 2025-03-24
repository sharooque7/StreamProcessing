package org.ainzson.oops.io.inputstream;

import java.io.*;

public class LogConsumer extends Thread{
    private PipedInputStream pis;

    public LogConsumer(PipedInputStream pis) {
        this.pis = pis;
    }

    @Override
    public  void run() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pis))) {
            String logEntry;
            while ((logEntry = bufferedReader.readLine()) != null) {
                System.out.println("Consumer log"+ logEntry);
            }
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}

