package org.ainzson;

import lombok.extern.slf4j.Slf4j;
import org.ainzson.config.TDengineConfig;
import org.ainzson.setup.GenerateShiftsForAsset;
import org.ainzson.streamprocessor.PmsEventStream;
import org.ainzson.streamprocessor.XMallStatefulTransaction;

import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {

//        new RuleEngineStream().TemperatureStreamProcessor();
//        new ConditionalMonitoringProcess().conditionalMonitoringProcessor();
//        new XMallStatefulTransaction().xMallStream();
        new PmsEventStream().PmsEventStreaming();

//        new GenerateShiftsForAsset().initShit();
    }
}


