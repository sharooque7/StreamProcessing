package org.ainzson.oops.io.outputstream;

import java.io.PipedOutputStream;

public class PipedOutputExample {
    PipedOutputStream pos = new PipedOutputStream();

    public PipedOutputStream get() {
        return pos;
    }
    public void pipedOutputStreamInvoke() {

        Thread writer = new Thread(() -> {
            try {

                pos.write("Hello i am writing to pipedOutputStream".getBytes());
                pos.close();
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        });
        writer.start();
    }
}
