package org.ainzson;

//import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ainzson.ringframe.RingFrameStimulator;
import org.ainzson.stimulator.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Stimulator {


    public static void main(String[] args) {

//        new PressureStimulator().stimulator();
//        new TemperatureStimulator().stimulator();
//        new VibrationStimulator().stimulator();
        new PmsStimulator().stimulator();
//        new MotorProducer().produce();
    }
}