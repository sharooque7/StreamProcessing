package org.ainzson.Serdes;

import org.ainzson.models.pms.MachineStatus;
import org.ainzson.models.xmall.Transaction;
import org.ainzson.models.xmall.TransactionKey;
import org.ainzson.models.xmall.TransactionPattern;
import org.ainzson.models.xmall.TransactionReward;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerdes {

    public static TransactionWrapSerde Transaction() {
        return new TransactionWrapSerde(new JsonSerialization<>(),new JsonDeserialization<>(Transaction.class));
    }
    public final static class TransactionWrapSerde extends WrapSerde<Transaction> {
        private TransactionWrapSerde(Serializer<Transaction> serializer,Deserializer<Transaction> deserializer) {
            super(serializer,deserializer);
        }
    }

    public static TransactionPatternWrapSerde TransactionPattern() {
        return new TransactionPatternWrapSerde(new JsonSerialization<>(),new JsonDeserialization<>(TransactionPattern.class));
    }
    public final static class TransactionPatternWrapSerde extends WrapSerde<TransactionPattern> {
        private TransactionPatternWrapSerde(Serializer<TransactionPattern> serializer,Deserializer<TransactionPattern> deserializer) {
            super(serializer,deserializer);
        }
    }

    public static TransactionRewardWrapSerde TransactionReward() {
        return new TransactionRewardWrapSerde(new JsonSerialization<>(),new JsonDeserialization<>(TransactionReward.class));
    }
    public final static class TransactionRewardWrapSerde extends WrapSerde<TransactionReward> {
        private TransactionRewardWrapSerde(Serializer<TransactionReward> serializer,Deserializer<TransactionReward> deserializer) {
            super(serializer,deserializer);
        }
    }


    public static TransactionKeyWrapSerde TransactionKey() {
        return new TransactionKeyWrapSerde(new JsonSerialization<>(),new JsonDeserialization<>(TransactionKey.class));
    }
    public final static class TransactionKeyWrapSerde extends WrapSerde<TransactionKey> {
        private TransactionKeyWrapSerde(Serializer<TransactionKey> serializer,Deserializer<TransactionKey> deserializer) {
            super(serializer,deserializer);
        }
    }
    public static MachineStatusWrapSerde MachineStatusSerde() {
        return new MachineStatusWrapSerde(new JsonSerialization<>(),new JsonDeserialization<>(MachineStatus.class));
    }
    public final static class MachineStatusWrapSerde extends WrapSerde<MachineStatus> {
        private MachineStatusWrapSerde(Serializer<MachineStatus> serializer,Deserializer<MachineStatus> deserializer) {
            super(serializer,deserializer);
        }
    }

    private static class WrapSerde<T> implements Serde<T> {
        private final Serializer<T> serializer;
        private final Deserializer<T> deserializer;

        public WrapSerde(Serializer<T> serializer,Deserializer<T> deserializer) {
            this.serializer = serializer;
            this.deserializer = deserializer;
        }
        @Override
        public Serializer<T> serializer() {
            return serializer;
        }

        @Override
        public Deserializer<T> deserializer() {
            return deserializer;
        }
    }
}
