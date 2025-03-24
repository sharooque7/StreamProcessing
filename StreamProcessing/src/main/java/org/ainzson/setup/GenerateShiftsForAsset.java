package org.ainzson.setup;

import java.time.LocalDateTime;
import java.util.*;
import java.time.ZoneId;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.ainzson.utils.Mapper;
import org.ainzson.utils.RedisUtils;
import redis.clients.jedis.Jedis;
import lombok.extern.slf4j.Slf4j;
import org.ainzson.models.shift.Shift;
import org.ainzson.config.RedisConfig;
import org.ainzson.models.shift.Holiday;
import org.ainzson.config.TDengineConfig;
import org.ainzson.models.shift.Shutdown;
import org.ainzson.models.shift.PlannedDowntime;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import static io.lettuce.core.models.command.CommandDetail.Flag.RANDOM;

@Slf4j
public class GenerateShiftsForAsset {
    LocalDateTime
    private static final Random RANDOM = new Random();
    private static final String[] SHIFT_NAMES = {"Shift A", "Shift B", "Shift C"};

    public List<String> getAssetList(Connection conn) {
        List<String> assetList = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT asset FROM normalized.normalized_pms;");
            while (rs.next()) {
                assetList.add(rs.getString("asset"));
            }
        } catch (Exception e) {
            System.err.println("Error fetching asset list: " + e.getMessage());
        }
        return assetList;
    }

    public static void processShift(Jedis redis, String asset)  {
        try {
            ZoneId indiaZone = ZoneId.of("Asia/Kolkata");
            ZonedDateTime currentTime = ZonedDateTime.now(indiaZone).withZoneSameInstant(ZoneOffset.UTC);

            ZonedDateTime shiftStart = currentTime.withSecond(0).withNano(0);
            ZonedDateTime shiftEnd = shiftStart.plusMinutes(15);

            Shift shift = getShift(shiftStart, shiftEnd);

            String shiftInfo = RedisUtils.serializeToString(shift);

            redis.hset("shift", asset, shiftInfo);

            log.info("Created Shift for {}:{}" ,asset,shiftInfo);

        }
        catch (Exception exception) {
            log.error("Something went wrong in shift creation {}",exception.getLocalizedMessage());
        }
    }

    private static Shift getShift(ZonedDateTime shiftStart, ZonedDateTime shiftEnd) {

        ZonedDateTime downtime1Start = shiftStart.plusMinutes(2);
        ZonedDateTime downtime1End = downtime1Start.plusMinutes(2);

        @SuppressWarnings("unused")
        ZonedDateTime downtime2Start = downtime1End;
        ZonedDateTime downtime2End = downtime2Start.plusMinutes(2);

        ZonedDateTime shutdownStart= downtime2End.plusMinutes(2);
        ZonedDateTime shutdownEnd =  shutdownStart.plusMinutes(2);

        ZonedDateTime holidayStart = shutdownEnd.plusMinutes(1);
        ZonedDateTime holidayEnd = holidayStart.plusMinutes(2);

        Shift shift = new Shift();
        String shiftName = SHIFT_NAMES[RANDOM.nextInt(SHIFT_NAMES.length)];
        shift.setName(shiftName);

        String shiftId = "S" + (RANDOM.nextInt(900) + 100); // Random number between 100 and 999
        shift.setShiftId(shiftId);

        shift.setStartTime(shiftStart);
        shift.setEndTime(shiftEnd);
        shift.setPlannedDowntime(List.of(new PlannedDowntime("BRK1", "First Downtime", downtime1Start, downtime1End),
                new PlannedDowntime("BRK2", "Second Downtime", downtime2Start, downtime2End)
                ));
        shift.setPlannedShutdown(List.of(new Shutdown("SD1", "Shutdown", shutdownStart, shutdownEnd)));
        shift.setHolidays(List.of(new Holiday("HOL1", "Holiday", holidayStart, holidayEnd)));
        return shift;
    }

    // Helper methods to create downtime, shutdown, and holiday structures
    public static Map<String, String> createDowntime(String id, String name, ZonedDateTime start, ZonedDateTime end) {
        Map<String, String> downtime = new HashMap<>();
        downtime.put("id", id);
        downtime.put("name", name);
        downtime.put("startTime", start.toString());
        downtime.put("endTime", end.toString());
        return downtime;
    }
    public static Map<String, String> createShutdown(String id, String name, ZonedDateTime start, ZonedDateTime end) {
        Map<String, String> shutdown = new HashMap<>();
        shutdown.put("id", id);
        shutdown.put("name", name);
        shutdown.put("startTime", start.toString());
        shutdown.put("endTime", end.toString());
        return shutdown;
    }

    public static Map<String, String> createHoliday(String id, String name, ZonedDateTime start, ZonedDateTime end) {
        Map<String, String> holiday = new HashMap<>();
        holiday.put("id", id);
        holiday.put("name", name);
        holiday.put("startTime", start.toString());
        holiday.put("endTime", end.toString());
        return holiday;
    }


    public void initShit()  {
        Jedis redis_conn = new RedisConfig().getJedis();
        Connection connection = TDengineConfig.getConnection();

        if (redis_conn != null && connection != null) {
            List<String> assetList = getAssetList(connection);

            for (String asset : assetList) {
                processShift(redis_conn,asset);
            }

            redis_conn.close();

            try {
                 connection.close();
            }
            catch (Exception e) {
                log.error("Error closing TAOS connection: {}", e.getMessage());
            }
        }
    }


}
