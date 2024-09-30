package org.ainzson.streamprocessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ainzson.convertor.TemperatureSerde;
import org.ainzson.schema.Pressure;
import org.ainzson.schema.RingFrame40StreamDTO;
import org.ainzson.schema.Temperature;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.internals.WindowedSerializer;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.WindowStore;

import java.io.DataInput;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Slf4j
public class RuleEngineStream {
    private  final ObjectMapper objectMapper = new ObjectMapper();
    public Properties setProperties() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "Temperature");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 5000); // 5 seconds
        properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 5000);  // Commit every 5 seconds
        properties.put(StreamsConfig.STATE_DIR_CONFIG, "/Users/sharooque/Documents/tt/Spin");

        return properties;
    }

    public void TemperatureStreamProcessor() {
        try{
            final Topology topology = getTopology(objectMapper);

            try(final KafkaStreams streams = new KafkaStreams(topology,setProperties());) {
                final CountDownLatch latch = new CountDownLatch(1);

                Runtime.getRuntime().addShutdownHook(new Thread("TemperatureStream"){
                    @Override
                    public  void run() {
                        streams.close();
                        latch.countDown();
                    }
                });

                streams.start();
                latch.await();
            }
            catch (KafkaException kafkaException) {
                log.info("kafkaException in stream {}",kafkaException.getLocalizedMessage());
            }
            catch (Throwable e) {
                System.exit(1);
            }

        }
        catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }

        System.exit(0);
    }

    private static Topology getTopology(ObjectMapper objectMapper) {
        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> stream1 = builder.stream("tdengine-rawdata-temperature",Consumed.with(Serdes.String(),Serdes.String()));
        KStream<String,String> stream2 = builder.stream("tdengine-rawdata-pressure");


        KStream<String,Temperature>  temperatureKStream = stream1.map((key,value)->{
            try {
                Temperature[] temperature = objectMapper.readValue(value,Temperature[].class);
                System.out.println(temperature[0]);
                if (key == null) {
                    key = temperature[0].getTags().getAssetId();
                }
                return new KeyValue<>(key,temperature[0]);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        TimeWindows timeWindow = TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(30));

//        KStream<String, Temperature> transformedStream = temperatureKStream.flatMap((key, value) -> {
//            Temperature[] temperatures = null;
//            try {
//                temperatures = objectMapper.readValue((DataInput) value, Temperature[].class);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            return Arrays.stream(temperatures)
//                    .map(temperature -> new KeyValue<>(temperature.getTags().getAssetId(), temperature))
//                    .collect(Collectors.toList());
//        });

//        KStream<String, Pressure>  pressureKStream = stream2.map((key, value)->{
//            try {
//                Pressure[] pressure = objectMapper.readValue(value,Pressure[].class);
//                System.out.println(pressure[0]);
//                if (key == null) {
//                    key = pressure[0].getTags().getAssetId();
//                }
//                return new KeyValue<String,Pressure>(key,pressure[0]);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        });



//        KGroupedStream<String, Temperature> groupedStream = temperatureKStream
//                        .groupBy((key,value)->value.getTags().getAssetId(),Grouped.with(Serdes.String(),new TemperatureSerde()));
        final Serde<Temperature> temperatureSerde = Serdes.serdeFrom(new TemperatureSerde.TemperatureSerializer(),new TemperatureSerde.TemperatureDeserializer());

        KGroupedStream<String, Temperature> groupedStream = temperatureKStream
                .groupByKey(Grouped.with(Serdes.String(),new TemperatureSerde()));


        KStream<Windowed<String>,Temperature> pro = groupedStream
               .windowedBy(timeWindow)
               .aggregate(
                       Temperature::new,
                       (key,value,aggregate) ->{
                           try {
                               return  aggregate.calculate(value);
                           }
                           catch (Exception e) {
                               throw new RuntimeException(e);
                           }
                       },
                       Materialized.<String, Temperature, WindowStore<Bytes, byte[]>>as("average-temperature-store")
                               .withKeySerde(Serdes.String())
                               .withValueSerde(new TemperatureSerde())               )
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream()
                .map(KeyValue::new);

        pro
                .foreach((key,value)->{
                    System.out.println("Aggregated");
                    System.out.println(key);
                    System.out.println(value);
                });



        return builder.build();
    }
}
