package org.ainzson.streamprocessor;

import ch.qos.logback.core.joran.conditional.ThenAction;
import lombok.extern.slf4j.Slf4j;
import org.ainzson.Serdes.JsonSerdes;
import org.ainzson.models.pms.MachineStatus;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Printed;


import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import static org.apache.kafka.streams.kstream.Printed.toSysOut;

@Slf4j
public class PmsEventStream {
    private final static String BOOTSTRAP_SERVER = "localhost:9092";
    private final static String APP_ID = "PMS_EVENT";
    private final static String SOURCE_TOPIC = "tdengine-normalized-normalized_pms";
    
    public Properties properties() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,APP_ID);
        properties.put(StreamsConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 5000); // 5 seconds
        properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 5000);  // Commit every 5 seconds

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
                log.error("kafkaException in stream {}",kafkaException.getLocalizedMessage());
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

        KStream<String,MachineStatus> ks0 = builder.stream("tdengine-normalized-normalized_pms", Consumed.with(Serdes.String(), JsonSerdes.MachineStatusSerde())
                .withName("Event_PMS_Stream")
                .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST));





//        ks0.print(Printed.<String,MachineStatus>toSysOut().withLabel("Printing"));
//        ks0.foreach((key,value) -> System.out.println(value.toString()));

        ks0.foreach((key,value) -> log.info(value.toString()));

        return builder.build();
    }


}
