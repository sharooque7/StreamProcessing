package org.ainzson;

import org.ainzson.ringframe.RingFrameStimulator;
import org.ainzson.stimulator.PressureStimulator;
import org.ainzson.stimulator.TemperatureStimulator;
import org.ainzson.stimulator.VibrationStimulator;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Stimulator {
    public static void main(String[] args) {
//        System.out.println(System.getProperty("java.library.path"));
//
//        RingFrameStimulator simulator = new RingFrameStimulator();
//        simulator.stimulator();

        new TemperatureStimulator().stimulator();
        new VibrationStimulator().stimulator();
        new PressureStimulator().stimulator();
    }
}