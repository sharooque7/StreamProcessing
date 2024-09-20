package org.ainzson.streamprocessor;

import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.ainzson.schema.RingFrame40StreamDTO;
import org.ainzson.utils.EpochToTimestampTypeAdapter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import org.apache.kafka.streams.kstream.Produced;

@Slf4j
public class RingFrameStreamProcessing {
    private final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Timestamp.class,new EpochToTimestampTypeAdapter())
            .create();
    private  final Map<String, RingFrame40StreamDTO> cached= new HashMap<>();

    public Properties setProperties() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "ringframe40");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 5000); // 5 seconds
        properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 5000);  // Commit every 5 seconds
        return properties;
    }

    public void initializeRingframeStream() {
        try {

            final StreamsBuilder builder = new StreamsBuilder();

            KStream<String, String> stream = builder.stream("tdengine-rawdata-ringframe_40");

            KStream<String, RingFrame40StreamDTO> transformedStream = stream.flatMap((key, value) -> {
                RingFrame40StreamDTO[] ringframe40SArray = gson.fromJson(value, RingFrame40StreamDTO[].class);
                return Arrays.stream(ringframe40SArray)
                        .map(ringframe -> new KeyValue<>(ringframe.getAssetId(), ringframe))
                        .collect(Collectors.toList());
            });

            transformedStream.map(this::instanceCalculation);


            transformedStream.to("pop", Produced.with(Serdes.String(), new RingFrame40Serde()));


            final Topology topology = builder.build();

            try(final KafkaStreams streams = new KafkaStreams(topology, setProperties());) {
                final CountDownLatch latch = new CountDownLatch(1);

                // attach shutdown handler to catch control-c
                Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
                    @Override
                    public void run() {
                        streams.close();
                        latch.countDown();
                    }
                });

                streams.start();
                latch.await();
            }
            catch (KafkaException ex) {
                log.info(ex.getLocalizedMessage());
            }
            catch (Throwable e) {
                System.exit(1);
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }

        System.exit(0);
    }

    public KeyValue<String, Object> instanceCalculation(String key , RingFrame40StreamDTO ringFrame40StreamDTO) {
        Map<String, Object> processed = new HashMap<>();
        if (!cached.containsKey(key)) {
            cached.put(key,ringFrame40StreamDTO);
            return new KeyValue<>(key, ringFrame40StreamDTO);
        }
        else {
            Map<String, Object> current = ringFrame40StreamDTO.toMap();
            Map<String, Object> previous = cached.get(key).toMap();


            for (Map.Entry<String, Object> entry : current.entrySet()) {
                String k = entry.getKey();
                Object v = entry.getValue();
                if (v instanceof Double current_c) {
                    Double current_p = (Double) previous.get(k);
                    Double value_c = current_c - current_p;
                    System.out.println(current_p);
                    System.out.println(current_c);

                    processed.put(k, value_c);
                } else {
                    processed.put(k, v);
                }
            }
            return new KeyValue<>(key, processed);
        }


    }

}
