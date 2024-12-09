package org.ainzson.streamprocessor.pms.downtime;

import lombok.extern.slf4j.Slf4j;
import org.ainzson.config.RedisConfig;
import org.ainzson.models.pms.Downtime;
import org.ainzson.models.pms.MachineStatus;
import org.ainzson.models.shift.Shift;
import org.ainzson.utils.RedisUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;


@Slf4j
public class EventChangeCapture {
    public void updateLocalVariable(
            MachineStatus machineStatus,
            RedisConfig redisConfig,
            Map<String, Downtime> downtimes
    ) {
        String assetId = machineStatus.getTags().getAsset();
        try (Jedis jedis = redisConfig.getJedis()) {
            // Fetch the last status of the asset from Redis
            String assetLastStatus = jedis.hget("asset", assetId);
            if (assetLastStatus == null) {
                log.info("No previous status found in Redis for asset: {}", assetId);
                return;
            }

            // Deserialize the last status into Downtime object
            Downtime downtime = RedisUtils.deserializeFromString(assetLastStatus, Downtime.class);
            if (downtime == null) {
                log.warn("Deserialization returned null for asset: {}", assetId);
                return;
            }

            // Update local downtimes map
            downtimes.put(assetId, downtime);
            log.info("Updated local variable for asset: {} with downtime: {}", assetId, downtime);
        } catch (JedisException e) {
            log.error("Redis error while updating local variable for asset: {} - {}", assetId, e.getMessage(), e);
        } catch (IOException e) {
            log.error("Deserialization error while updating local variable for asset: {} - {}", assetId, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error in updateLocalVariable for asset: {} - {}", assetId, e.getMessage(), e);
        }
    }

    public void checkStatusChanged(
            MachineStatus machineStatus,
            Shift shiftInfo,
            Map<String,Downtime> downtimes
    ) {
        String asset = machineStatus.getTags().getAsset();

        try {
            Downtime downtime = downtimes.computeIfAbsent(asset, k -> new Downtime(machineStatus, shiftInfo));
            ZoneId zoneId = ZoneId.of("UTC");
            ZonedDateTime currentDateTime = ZonedDateTime.ofInstant(machineStatus.getTs(), zoneId);
            if (machineStatus.getStatus() != downtime.getStatusCode()) {
                downtime.setStatusCode(machineStatus.getStatus());
                downtime.setStartDatetime(currentDateTime);
                downtime.setEndDatetime(currentDateTime);
                log.info("Status change detected for asset: {}, updated status to: {}", asset, machineStatus.getStatus());
            }
            else {
                    downtime.setEndDatetime(currentDateTime);
            }
        }
        catch (Exception ex) {
            log.error("Error in checkStatusForChange for asset: {} - {}", asset, ex.getMessage(), ex);
        }
    }


    public void updateCurrentStateToRedis(
            MachineStatus machineStatus,
            RedisConfig redisConfig,
            Map<String, Downtime> downtimes)  {
        String asset = machineStatus.getTags().getAsset();

        try(Jedis jedis = redisConfig.getJedis()) {
            Downtime downtime = downtimes.get(asset);
            String downtimeSerialized = RedisUtils.serializeToString(downtime);

            jedis.hset("asset",asset,downtimeSerialized);
        }
        catch (JedisException e) {
            log.error("Redis error while updating local variable for asset: {} - {}", asset, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error in updateLocalVariable for asset: {} - {}", asset, e.getMessage(), e);
        }
    }



}
