//package org.ainzson.streamprocessor;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.ainzson.models.cms.Pressure;
//import org.ainzson.models.cms.Temperature;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.KafkaException;
//import org.apache.kafka.common.serialization.*;
//import org.apache.kafka.common.utils.Bytes;
//import org.apache.kafka.streams.*;
//import org.apache.kafka.streams.kstream.*;
//import org.apache.kafka.streams.state.WindowStore;
//
//import java.time.Duration;
//import java.util.Properties;
//import java.util.concurrent.CountDownLatch;
//
//@Slf4j
//public class RuleEngineStream {
//    private  final ObjectMapper objectMapper = new ObjectMapper();
//    public Properties setProperties() {
//        Properties properties = new Properties();
//        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "Temperature");
//        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
//        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
//        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
//        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 5000); // 5 seconds
//        properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 5000);  // Commit every 5 seconds
//        properties.put(StreamsConfig.STATE_DIR_CONFIG, "/Users/sharooque/Documents/tt/Spin");
//
//        return properties;
//    }
//
//    public void TemperatureStreamProcessor() {
//        try{
//            final Topology topology = getTopology(objectMapper);
//
//            try(final KafkaStreams streams = new KafkaStreams(topology,setProperties());) {
//                final CountDownLatch latch = new CountDownLatch(1);
//
//                Runtime.getRuntime().addShutdownHook(new Thread("TemperatureStream"){
//                    @Override
//                    public  void run() {
//                        streams.close();
//                        latch.countDown();
//                    }
//                });
//
//                streams.start();
//                latch.await();
//            }
//            catch (KafkaException kafkaException) {
//                log.info("kafkaException in stream {}",kafkaException.getLocalizedMessage());
//            }
//            catch (Throwable e) {
//                System.exit(1);
//            }
//
//        }
//        catch (Exception ex) {
//            System.out.println(ex.getLocalizedMessage());
//        }
//
//        System.exit(0);
//    }
//
//    private static Topology getTopology(ObjectMapper objectMapper) {
//        final StreamsBuilder builder = new StreamsBuilder();
//
//        final Serde<Temperature> temperatureSerde = Serdes.serdeFrom(new TemperatureSerde.TemperatureSerializer(),new TemperatureSerde.TemperatureDeserializer());
//        final Serde<Pressure> pressureSerde = Serdes.serdeFrom(new PressureSerde.PressureSerializer(), new PressureSerde.PressureDerializer());
//        final Serde<String> stringSerde = Serdes.String();
//
//        TimeWindows timeWindow = TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(60));
//
//        KStream<String, String> stream1 = builder.stream("tdengine-rawdata-temperature",Consumed.with(Serdes.String(),Serdes.String()));
//        KStream<String,String> stream2 = builder.stream("tdengine-rawdata-pressure");
//
//
//        KStream<String,Temperature>  temperatureKStream = stream1.map((key,value)->{
//            try {
//                Temperature[] temperature = objectMapper.readValue(value,Temperature[].class);
////                System.out.println(temperature[0]);
//                if (key == null) {
//                    key = temperature[0].getTags().getAssetId();
//                }
//                return new KeyValue<>(key,temperature[0]);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//
//
//        KStream<String, Pressure>  pressureKStream = stream2.map((key, value)->{
//            try {
//                Pressure[] pressure = objectMapper.readValue(value,Pressure[].class);
//                System.out.println(pressure[0]);
//                if (key == null) {
//                    key = pressure[0].getTags().getAssetId();
//                }
//                return new KeyValue<>(key,pressure[0]);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//
//
//        KGroupedStream<String, Temperature> temperatureGroupedStream = temperatureKStream
//                .groupByKey(Grouped.with(Serdes.String(),new TemperatureSerde()));
//
//
//        KGroupedStream<String, Pressure> pressureGroupedStream = pressureKStream
//                .groupByKey(Grouped.with(Serdes.String(),new PressureSerde()));
//
//
//
//        KStream<Windowed<String>,Temperature> temperatureAggregated = temperatureGroupedStream
//               .windowedBy(timeWindow)
//               .aggregate(
//                       Temperature::new,
//                       (key,value,aggregate) ->{
//                           try {
//                               return  aggregate.calculate(value);
//                           }
//                           catch (Exception e) {
//                               throw new RuntimeException(e);
//                           }
//                       },
//                       Materialized.<String, Temperature, WindowStore<Bytes, byte[]>>as("average-temperature-store")
//                               .withKeySerde(Serdes.String())
//                               .withValueSerde(new TemperatureSerde())               )
//                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
//                .toStream();
//
//
//        KStream<Windowed<String>,Pressure> pressureAggregated = pressureGroupedStream
//                .windowedBy(timeWindow)
//                .aggregate(
//                        Pressure::new,
//                        (key,value,aggregate) ->{
//                            try {
//                                return aggregate.calculate(value);
//                            }
//                            catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
//                        },
//                        Materialized.<String, Pressure, WindowStore<Bytes, byte[]>>as("average-pressure-store")
//                                .withKeySerde(Serdes.String())
//                                .withValueSerde(new PressureSerde())               )
//                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
//                .toStream();
//
//        KStream<Windowed<String>, Cumulative> joinedStream = temperatureAggregated
//                .join(pressureAggregated,
//                        (temperature, pressure) -> {
//                    Temperature temperature1 = Temperature.TemperatureBuilder().build();
//                    Pressure pressure1 = Pressure.PressureBuilder().build();
//                            return new Cumulative(pressure1,temperature1) ;
//                        },
//                        JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(5))  // Window duration for joining
//                );
//
//
//        temperatureAggregated.foreach((key,value)->{
//            System.out.println("temperature");
//            System.out.println(key);
//            System.out.println(value);
//        });
//
//        pressureAggregated.foreach((key,value)->{
//            System.out.println("pressure");
//            System.out.println(key);
//            System.out.println(value);
//        });
//
//
//        KStream<Windowed<String>, Object> joined = temperatureAggregated
//                .join(
//                        pressureAggregated,
//                ( temperature,pressure) -> "left=" + temperature + ", right=" + pressure, /* ValueJoiner */
//                JoinWindows.of(Duration.ofMinutes(1))
//                        /* right value */
//        );
//        joined.to("pop");
//
////        KStream<Windowed<String>, Cumulative> combinedStream = temperatureAggregated
////                .join(
////                        pressureAggregated,
////                        (temperature, pressure) -> new Cumulative(pressure, temperature), // ValueJoiner
////                        JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(5)), // Adjust window as needed
////                        StreamJoined.<Windowed<String>, Temperature, Pressure>with(
////                                stringSerde),
////                                new TemperatureSerde(),                           // Value Serde for Temperature
////                                new PressureSerde()                               // Value Serde for Pressure
////                        )
////                );
//
//
//        return builder.build();
//    }
//}
