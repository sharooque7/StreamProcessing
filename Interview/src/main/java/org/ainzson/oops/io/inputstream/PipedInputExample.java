package org.ainzson.oops.io.inputstream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class PipedInputExample {
    PipedInputStream pis = null;

    public PipedInputExample(PipedOutputStream pipedOutputStream) throws IOException {
        this.pis = new PipedInputStream(pipedOutputStream);
    }


    public  void pipedInputStreamInvoke() {
        Thread reader = new Thread(()-> {
            try {
                int data;
                while ((data = pis.read()) != 1) {
                    System.out.println((char) data);
                }
                pis.close();
            }
            catch (Exception exception) {
                System.out.println(exception.getLocalizedMessage());
            }
        });
        reader.start();
    }
}
