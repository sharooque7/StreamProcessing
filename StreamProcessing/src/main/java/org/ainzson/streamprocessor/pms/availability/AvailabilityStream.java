package org.ainzson.streamprocessor.pms.availability;


import lombok.extern.slf4j.Slf4j;
import org.ainzson.Serdes.JsonSerdes;
import org.ainzson.config.RedisConfig;
import org.ainzson.models.pms.Availability;
import org.ainzson.models.pms.MachineStatus;
import org.ainzson.streamprocessor.pms.utils.ShiftUtilities;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;


import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;



@Slf4j
public class AvailabilityStream {
    private final static String BOOTSTRAP_SERVER = "127.0.0.1:9092";
    private final static String APP_ID = "PMS_AVAILABILITY";
    private final static String SOURCE_TOPIC = "_normalized_normalized_pms";

    public Properties properties() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APP_ID);
        properties.put(StreamsConfig.RETRY_BACKOFF_MS_CONFIG, 20000);
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 20000); // 5 seconds
        properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 20000);  // Commit every 5 seconds

        return properties;
    }

    public void AvailabilityStreamProcess() {
        try {
            final Topology topology = getTopology();

            try (final KafkaStreams kafkaStreams = new KafkaStreams(topology, properties());) {
                final CountDownLatch latch = new CountDownLatch(1);

                Runtime.getRuntime().addShutdownHook(new Thread("Availability Stream") {
                    @Override
                    public void run() {
                        kafkaStreams.close();
                        latch.countDown();
                    }
                });
                kafkaStreams.start();
                latch.await();
            } catch (KafkaException kafkaException) {
                log.error("kafkaException in availability stream {}", kafkaException.getLocalizedMessage());
            } catch (Throwable e) {
                System.exit(1);
            }
        } catch (Exception exception) {
            log.error(exception.getLocalizedMessage());
        }
        System.exit(0);
    }

    private Topology getTopology() {
        final StreamsBuilder builder = new StreamsBuilder();
        ShiftUtilities shiftUtilities = new ShiftUtilities();
        RedisConfig redisConfig = new RedisConfig();

        KStream<String,MachineStatus> ks0 = builder.stream("_normalized_normalized_pms", Consumed.with(Serdes.String(), JsonSerdes.MachineStatusSerde())
                .withName("Availability_Stream")
                .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST))
                .map((k, v) -> KeyValue.pair(v.getTags().getAsset(),v));

        ks0.print(Printed.<String, MachineStatus>toSysOut().withLabel("attack"));

        KStream<Windowed<String>,Availability> ks1 = ks0
                .groupByKey(Grouped.with(Serdes.String(), JsonSerdes.MachineStatusSerde()))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1)))
                .aggregate(Availability::new,
                        (key,value,aggregation) -> aggregation.process(value),
                        Materialized.<String, Availability, WindowStore<Bytes,byte[]>>as("availability_stream")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(JsonSerdes.AvailabilitySerde()))
                .suppress(Suppressed.untilTimeLimit(Duration.ofSeconds(30), Suppressed.BufferConfig.unbounded()))
                .toStream();

        KStream<String,Availability> ks2 = ks1.map((windowedKey, value) -> {
                    String key = windowedKey.key() + "@" + windowedKey.window().start() + "-" + windowedKey.window().end();
                    Availability processed = Availability.processShift(windowedKey.window().start(),windowedKey.window().end(),value,shiftUtilities,redisConfig);
                    return KeyValue.pair(key, processed);
                });

        ks2.to("availability",Produced.with(Serdes.String(),JsonSerdes.AvailabilitySerde()));


        return builder.build();
    }
}
