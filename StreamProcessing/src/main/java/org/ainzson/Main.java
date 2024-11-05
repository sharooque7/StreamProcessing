package org.ainzson;

import org.ainzson.streamprocessor.PmsEventStream;
import org.ainzson.streamprocessor.XMallStatefulTransaction;

public class Main {

    public static void main(String[] args) throws Exception {

//        new RuleEngineStream().TemperatureStreamProcessor();
//        new ConditionalMonitoringProcess().conditionalMonitoringProcessor();

//        new XMallStatefulTransaction().xMallStream();

        new PmsEventStream().PmsEventStreaming();
    }
}