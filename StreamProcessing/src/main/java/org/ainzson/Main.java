package org.ainzson;

import org.ainzson.streamprocessor.RingFrameStreamProcessing;

public class Main {
    public static void main(String[] args) throws Exception {
        RingFrameStreamProcessing ringFrameStreamProcessing = new RingFrameStreamProcessing();
        ringFrameStreamProcessing.initializeRingFrameStream();
    }
}


