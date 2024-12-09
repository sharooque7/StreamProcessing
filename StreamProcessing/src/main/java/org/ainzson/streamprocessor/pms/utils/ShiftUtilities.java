package org.ainzson.streamprocessor.pms.utils;

import lombok.extern.slf4j.Slf4j;
import org.ainzson.config.RedisConfig;
import org.ainzson.models.pms.Downtime;
import org.ainzson.models.pms.MachineStatus;
import org.ainzson.models.shift.PlannedDowntime;
import org.ainzson.models.shift.PlannedEvent;
import org.ainzson.models.shift.Shift;
import org.ainzson.models.shift.Shutdown;
import org.ainzson.setup.GenerateShiftsForAsset;
import org.ainzson.utils.RedisUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
public class ShiftUtilities {

    private boolean isShiftExpired(Instant currentEventTime, Instant shiftEndTime) {
        return currentEventTime.equals(shiftEndTime) || currentEventTime.isAfter(shiftEndTime);
    }

    private void processExpiredShift(Jedis jedis, String asset, Map<String, Downtime> downtimes) {
        // Process and clean up expired shifts
        GenerateShiftsForAsset.processShift(jedis, asset);
        jedis.del(asset);
        downtimes.remove(asset);
        log.info("Processed expired shift for asset: {}", asset);
    }

    public void checkIsEventFallsWithinShift(
            MachineStatus machineStatus,
            RedisConfig redisConfig,
            Map<String,Downtime> downtimes
    ) {
        try(Jedis jedis = redisConfig.getJedis()) {
            String asset = machineStatus.getTags().getAsset();
            String shiftInfoJson = jedis.hget("shift", asset);
            if (shiftInfoJson == null) {
                log.info("No shift information found in Redis for asset: {}", asset);
                return;
            }
            Shift shiftInfo = RedisUtils.deserializeFromString(shiftInfoJson, Shift.class);
            if (shiftInfo == null) {
                log.warn("Failed to deserialize shift information for asset: {}", asset);
                return;
            }
            Instant currentEventTime = machineStatus.getTs();
            Instant shiftEndTime = shiftInfo.getEndTime().toInstant();

            if (isShiftExpired(currentEventTime, shiftEndTime)) {
                processExpiredShift(jedis, asset, downtimes);
                jedis.del(asset);
                jedis.hdel("asset",asset);
                downtimes.remove(asset);
            }

        }
        catch (JedisException e) {
            log.error("Redis error while processing asset: {} - {}",
                    machineStatus.getTags().getAsset(), e.getMessage(), e);
        } catch (IOException e) {
            log.error("Deserialization error for asset: {} - {}",
                    machineStatus.getTags().getAsset(), e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error in checkIsEventFallsWithinShift for asset: {} - {}",
                    machineStatus.getTags().getAsset(), e.getMessage(), e);
        }
    }

    public org.ainzson.models.shift.Shift decodeShift(
            MachineStatus machineStatus,
            RedisConfig redisConfig,
            Map<String, Downtime> downtimes
    ){
        String asset = machineStatus.getTags().getAsset();
        try(Jedis jedis = redisConfig.getJedis()) {
            String shiftInfo = redisConfig.getJedis().hget("shift", asset);
            if (shiftInfo == null) {
                log.info("Shift info not found for asset: {}. Generating shift.", asset);
                GenerateShiftsForAsset.processShift(redisConfig.getJedis(),asset);
                shiftInfo = jedis.hget("shift", asset);
                if (shiftInfo == null) {
                    log.warn("Failed to generate shift info for asset: {}", asset);
                    return null;
                }
            }
            return RedisUtils.deserializeFromString(shiftInfo, org.ainzson.models.shift.Shift.class);
        }
        catch (JedisException e) {
            log.error("Redis error while fetching/generating shift for asset: {} - {}", asset, e.getMessage(), e);
        } catch (IOException e) {
            log.error("Deserialization error for shift info of asset: {} - {}", asset, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error in decodeShift for asset: {} - {}", asset, e.getMessage(), e);
        }
        return null;
    }

//    public boolean isInDowntimeOrShutdown(Instant currentTime, List<? extends PlannedEvent> plannedEvents) {
//        if (plannedEvents == null || plannedEvents.isEmpty()) {
//            return false;
//        }
//        return plannedEvents.stream()
//                .anyMatch(event ->
//                        currentTime.isAfter(event.getStartTime().toInstant()) &&
//                                currentTime.isBefore(event.getEndTime().toInstant())
//                );
//    }

//    public void checkForDownTime(
//    MachineStatus machineStatus,
//    Shift shiftInfo,
//    Map<String,Downtime> downtimes
//    ){
//        String asset = machineStatus.getTags().getAsset();
//        try {
//            List<PlannedDowntime> plannedDowntime = shiftInfo.getPlannedDowntime();
//            Downtime downtime = downtimes.get(asset);
//            ZoneId zoneId = ZoneId.of("UTC");
//            ZonedDateTime currentTime = ZonedDateTime.ofInstant(machineStatus.getTs(),zoneId);
//
//            if (plannedDowntime != null) {
//                if (isInDowntimeOrShutdown(machineStatus.getTs(),plannedDowntime)) {
//                    downtime.setPlannedDowntime(true);
//                    if (downtime.getDowntimeCount() == 0) {
//                        downtime.setStartDatetime(currentTime);
//                        downtime.setDowntimeCount(1);
//                    }
//                    else {
//                        if (downtime.getDowntimeCount() == 1) {
//                            downtime.setStartDatetime(currentTime);
//                        }
//                        downtime.setDowntimeCount(0);
//                        downtime.setPlannedDowntime(false);
//                    }
//                }
//                downtimes.put(asset,downtime);
//            }
//        }
//        catch (Exception ex) {
//            log.error("Error in checkForDowntime for asset: {} - {}", asset, ex.getMessage(), ex);
//        }
//    }
//
//    public void checkForShutDown(
//            MachineStatus machineStatus,
//            Shift shiftInfo,
//            Map<String,Downtime> downtimes)
//    {
//        String asset = machineStatus.getTags().getAsset();
//        try {
//            List<Shutdown> plannedShutdown = shiftInfo.getPlannedShutdown();
//            Downtime downtime = downtimes.get(asset);
//            ZoneId zoneId = ZoneId.of("UTC");
//            ZonedDateTime currentTime = ZonedDateTime.ofInstant(machineStatus.getTs(),zoneId);
//            if (plannedShutdown != null) {
//                if (isInDowntimeOrShutdown(machineStatus.getTs(),plannedShutdown)) {
//                    downtime.setPlannedShutdown(true);
//                    if (downtime.getShutDownCount() == 0) {
//                        downtime.setStartDatetime(currentTime);
//                        downtime.setShutDownCount(1);
//
//                    }
//                    else {
//                        if (downtime.getShutDownCount() == 1) {
//                            downtime.setStartDatetime(currentTime);
//                        }
//                        downtime.setShutDownCount(0);
//                        downtime.setPlannedShutdown(false);
//                    }
//                }
//            }
//            downtimes.put(asset,downtime);
//        }
//        catch (Exception ex) {
//            log.error("Error in checkForDowntime for asset: {} - {}", asset, ex.getMessage(), ex);
//        }
//    }


    public void checkForDownTime(
            MachineStatus machineStatus,
            Shift shiftInfo,
            Map<String, Downtime> downtimes
    ) {

        String asset = machineStatus.getTags().getAsset();
        List<PlannedDowntime> plannedDowntime = shiftInfo.getPlannedDowntime();
        checkForPlannedEvent(asset, machineStatus, plannedDowntime, downtimes, false);
    }

    public void checkForShutDown(
            MachineStatus machineStatus,
            Shift shiftInfo,
            Map<String, Downtime> downtimes
    ) {
        String asset = machineStatus.getTags().getAsset();
        List<Shutdown> plannedShutdown = shiftInfo.getPlannedShutdown();
        checkForPlannedEvent(asset, machineStatus, plannedShutdown, downtimes, true);
    }

    private <T extends PlannedEvent> void checkForPlannedEvent(
            String asset,
            MachineStatus machineStatus,
            List<T> plannedEvents,
            Map<String, Downtime> downtimes,
            boolean isShutdown) {
        if (plannedEvents == null || plannedEvents.isEmpty()) {
            return;
        }
        try {
            Downtime downtime = downtimes.get(asset);
            Instant currentTime = machineStatus.getTs();
            if (isInDowntimeOrShutdown(currentTime, plannedEvents)) {
                if (isShutdown) {
                    downtime.setPlannedShutdown(true);
                    handleCountAndTime(machineStatus, downtime, true);
                } else {
                    downtime.setPlannedDowntime(true);
                    handleCountAndTime(machineStatus, downtime, false);
                }
            } else {
                ZoneId zoneId = ZoneId.of("UTC");
                ZonedDateTime currentDateTime = ZonedDateTime.ofInstant(machineStatus.getTs(),zoneId);
                if (isShutdown) {
                    if (downtime.getShutDownCount() == 1 ){
                        downtime.setStartDatetime(currentDateTime);
                    }
                    downtime.setPlannedShutdown(false);
                    downtime.setShutDownCount(0);
                } else {
                    if (downtime.getDowntimeCount() == 1 ){
                        downtime.setStartDatetime(currentDateTime);
                    }
                    downtime.setPlannedDowntime(false);
                    downtime.setDowntimeCount(0);
                }
                downtimes.put(asset, downtime);
            }
        }
        catch(Exception ex){
            log.error("Error in checking planned event for asset: {} - {}", asset, ex.getMessage(), ex);
        }
    }



    public boolean isInDowntimeOrShutdown(Instant currentTime, List<? extends PlannedEvent> plannedEvents) {
        if (plannedEvents == null || plannedEvents.isEmpty()) {
            return false;
        }
        return plannedEvents.stream()
                .anyMatch(event ->
                        ( currentTime.isAfter(event.getStartTime().toInstant()) ||
                                currentTime.equals(event.getStartTime().toInstant())
                        ) &&
                                currentTime.isBefore(event.getEndTime().toInstant())
                );
    }

    private void handleCountAndTime(MachineStatus machineStatus, Downtime downtime, boolean isShutdown) {
        ZoneId zoneId = ZoneId.of("UTC");
        ZonedDateTime currentDateTime = ZonedDateTime.ofInstant(machineStatus.getTs(),zoneId);
        if (isShutdown) {
            if (downtime.getShutDownCount() == 0) {
                downtime.setStartDatetime(currentDateTime);
                downtime.setShutDownCount(1);
            }
        } else {
            if (downtime.getDowntimeCount() == 0) {
                downtime.setStartDatetime(currentDateTime);
                downtime.setDowntimeCount(1);
            }
        }
    }

}


