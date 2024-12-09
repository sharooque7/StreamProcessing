package org.ainzson.streamprocessor.pms.downtime;

import lombok.extern.slf4j.Slf4j;
import org.ainzson.Serdes.JsonSerdes;
import org.ainzson.config.RedisConfig;
import org.ainzson.models.pms.Downtime;
import org.ainzson.models.pms.MachineStatus;
import org.ainzson.streamprocessor.pms.utils.ShiftUtilities;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class EventStream {
    private final static String BOOTSTRAP_SERVER = "127.0.0.1:9092";
    private final static String APP_ID = "PMS_EVENT";
    private final static String SOURCE_TOPIC = "_normalized_normalized_pms";
    
    public Properties properties() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,APP_ID);
        properties.put(StreamsConfig.RETRY_BACKOFF_MS_CONFIG, 20000);
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 20000); // 5 seconds
        properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 20000);  // Commit every 5 seconds

        return  properties;
    }


    public void PmsEventStreaming() {
        try {
            final Topology topology = getTopology();

            try(final KafkaStreams kafkaStreams = new KafkaStreams(topology, properties());) {
                final CountDownLatch latch = new CountDownLatch(1);

                Runtime.getRuntime().addShutdownHook(new Thread("Event Stream shutdown thread") {
                    @Override
                    public void run() {
                        kafkaStreams.close();
                        latch.countDown();
                    }
                });
                kafkaStreams.start();
                latch.await();
            }
            catch (KafkaException kafkaException) {
                log.error("kafkaException in pms event stream {}",kafkaException.getLocalizedMessage());
            }
            catch (Throwable e) {
                System.exit(1);
            }

        }
        catch(Exception ex) {
            log.error(ex.getLocalizedMessage());
        }
        System.exit(0);

    }
    private Topology getTopology() {
        final StreamsBuilder builder = new StreamsBuilder();

        ShiftUtilities shiftUtilities = new ShiftUtilities();
        RedisConfig redisConfig = new RedisConfig();
        Map<String,Downtime> downtimes = new HashMap<>();
        EventChangeCapture eventChangeCapture = new EventChangeCapture();

        KStream<String,MachineStatus> ks0 = builder.stream("_normalized_normalized_pms", Consumed.with(Serdes.String(), JsonSerdes.MachineStatusSerde())
                .withName("Event_PMS_Stream")
                .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST));

        ks0.print(Printed.<String, MachineStatus>toSysOut().withLabel("attack"));


        EventChangeProcess eventChangeProcess = new EventChangeProcess(eventChangeCapture, shiftUtilities, redisConfig, downtimes);

        KStream<String, Downtime> processedStream = ks0.mapValues(eventChangeProcess::process);

        processedStream.to("events", Produced.with(Serdes.String(), JsonSerdes.DowntimeStatusSerde()));

        return builder.build();
    }

}
