package org.ainzson.stimulator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ainzson.model.Tags;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;

@Slf4j
public class MotorProducer {
    private static final String BOOTSTRAP_SERVER = "localhost:9094";
    private static final String XMOTOR_CURRENT = "motor_xaxismotorcurrent";
    private static final String YMOTOR_CURRENT = "motor_yaxismotorcurrent";
    private static final String XMOTOR_TEMPERATURE = "motor_xaxismotortemperature";
    private static final String YMOTOR_TEMPERATURE = "motor_yaxismotortemperature";


    private static final Random RANDOM  = new Random();
    private static final ObjectMapper Mapper = new ObjectMapper();

    public void produce() {
        new Thread(() -> produceData(XMOTOR_CURRENT)).start();
        new Thread(() -> produceData(YMOTOR_CURRENT)).start();
        new Thread(() -> produceData(XMOTOR_TEMPERATURE)).start();
        new Thread(() -> produceData(YMOTOR_TEMPERATURE)).start();
    }



    private static void produceData(String topic) {

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVER);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        try (Producer<String,String> producer = new KafkaProducer<>(properties)) {
                while (true) {
                    String message = generateMotorData(topic);
                    producer.send(new ProducerRecord<>(topic, message));

                    Thread.sleep(5000);
                }
        }
        catch (Exception exception) {
            log.info("Exception at producing {}", exception.getMessage());
        }
    }

    private static String generateMotorData(String topic) throws JsonProcessingException {
        long timestamp = System.currentTimeMillis();
        int motorCurrent = RANDOM .nextInt(50);

        Tags tags = new Tags();
        tags.setSite("SIT064138992b74d1035");
        tags.setSubtenant("BSN14412474302131918");
        tags.setSubSystem("Motor");
        tags.setDepartment("DEP1331149313d15c833");
        tags.setAsset("AST074801769dc0220a7");
        tags.setTenant("TNT1432069083c77a7aa");

        HashMap<String, Object> hashMap =  new HashMap<>(){{
            put("ts",timestamp);
            put("ts_act",timestamp);
            put(topic,motorCurrent);
            put("tags", tags);
        }};

//        MotorData motorData = new MotorData(timestamp,topic,motorCurrent, tags);

        return Mapper.writeValueAsString(hashMap);
    }

}
