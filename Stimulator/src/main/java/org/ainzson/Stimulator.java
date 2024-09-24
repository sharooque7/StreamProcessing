package org.ainzson;

import org.ainzson.ringframe.RingFrameStimulator;

public class Stimulator {
    public static void main(String[] args) {
//        System.out.println(System.getProperty("java.library.path"));

        RingFrameStimulator simulator = new RingFrameStimulator();
        simulator.stimulator();
    }
}