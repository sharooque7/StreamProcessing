package org.ainzson.streamprocessor;


import lombok.extern.slf4j.Slf4j;
import org.ainzson.Serdes.JsonSerdes;
import org.ainzson.models.xmall.Transaction;
import org.ainzson.models.xmall.TransactionKey;
import org.ainzson.models.xmall.TransactionPattern;
import org.ainzson.models.xmall.TransactionReward;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class XMallStatefulTransaction {

    private final static String BOOTSTRAP_SERVER = "localhost:9092";
    private final static String APP_ID = "xmall_app";
    private final static String XMALL_TRANSACTION_SOURCE_TOPIC = "xmall.transaction";
    private final static String XMALL_TRANSACTION_PATTERN_TOPIC = "xmall.pattern.transaction";
    private final static String XMALL_TRANSACTION_REWARDS_TOPIC = "xmall.rewards.transaction";
    private final static String XMALL_TRANSACTION_PURCHASES_TOPIC = "xmall.purchases.transaction";
    private final static String XMALL_TRANSACTION_COFFEE_TOPIC = "xmall.coffee.transaction";
    private final static String XMALL_TRANSACTION_ELECT_TOPIC = "xmall.elect.transaction";

    public void xMallStream() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APP_ID);

        StreamsBuilder builder = new StreamsBuilder();
        // 1. consume the raw data from source topic `transaction`
        KStream<String, Transaction> ks0 = builder.stream(XMALL_TRANSACTION_SOURCE_TOPIC, Consumed.with(Serdes.String(),JsonSerdes.Transaction())
                        .withName("transaction-source")
                        .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST));

        //2. masking the transaction record, credit number for pii
        KStream<String, Transaction> ks1 = ks0.mapValues(v -> Transaction.newBuilder(v).build(), Named.as("transaction-masking-pii"));

        //3. extract the pattern data from transaction
        ks1.mapValues(v -> TransactionPattern.builder(v).build(), Named.as("transaction-pattern"))
                .to(XMALL_TRANSACTION_PATTERN_TOPIC, Produced.with(Serdes.String(),JsonSerdes.TransactionPattern()));

        //4. process the customer reward
        ks1.mapValues(v -> TransactionReward.builder(v).build(), Named.as("transaction-reward"))
                .to(XMALL_TRANSACTION_REWARDS_TOPIC, Produced.with(Serdes.String(),JsonSerdes.TransactionReward()));

        //5. process the purchase
        ks1.filter((k,v) -> v.getPrice() > 5.0D)
                .selectKey((k,v) -> new TransactionKey(v.getCustomerId(),v.getPurchaseDate()))
                .to(XMALL_TRANSACTION_PURCHASES_TOPIC,Produced.with(JsonSerdes.TransactionKey(),JsonSerdes.Transaction()));

        //6. split the coffee and elect
        ks1.split(Named.as("transaction-split-"))
                .branch((k,v) -> v.getDepartment().equalsIgnoreCase("COFFEE"),
                        Branched.withConsumer(ks -> ks.to(XMALL_TRANSACTION_COFFEE_TOPIC, Produced.with(Serdes.String(),JsonSerdes.Transaction()))))
                .branch((k,v) -> v.getDepartment().equalsIgnoreCase("ELECT"),
                        Branched.withConsumer(ks -> ks.to(XMALL_TRANSACTION_ELECT_TOPIC, Produced.with(Serdes.String(), JsonSerdes.Transaction()))));

        //7. locate the raw data to data lake
        ks1.foreach((k, v) -> log.info("simulate located the transaction record(masked) to the data lake, the value:{}", v));

        Topology topology = builder.build();
        try (KafkaStreams kafkaStreams = new KafkaStreams(topology,properties)) {
            final CountDownLatch latch = new CountDownLatch(1);
            Runtime.getRuntime().addShutdownHook(new Thread(()->{
                kafkaStreams.close();
                latch.countDown();
                log.info("The kafka streams application is closed.");
            }));

            kafkaStreams.start();
            log.info("The kafka streams application start...");
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }


}
