package org.ainzson.oops.io.inputstream;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PipedOutputStream;

public class LogProducer extends Thread {

    PipedOutputStream pos ;

    public LogProducer(PipedOutputStream pipedOutputStream)  {
        this.pos = pipedOutputStream;
    }

    @Override
    public void run() {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(pos))) {
            for(int i = 0; i < 5 ;i ++) {
                bufferedWriter.write("Log entry " + i + "\n" );
                bufferedWriter.flush();
                Thread.sleep(1000);
            }
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
