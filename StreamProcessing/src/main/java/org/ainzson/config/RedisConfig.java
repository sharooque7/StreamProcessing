package org.ainzson.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Getter
@Slf4j
@Setter
public class RedisConfig {

    private final Jedis jedis;

    public  RedisConfig() {
        this.jedis = new Jedis("localhost", 6379); // Adjust the host and port as needed
    }

    public void close() {
        jedis.close();
    }
}
