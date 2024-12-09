package org.ainzson.streamprocessor.pms.downtime;

import org.ainzson.config.RedisConfig;
import org.ainzson.models.pms.Downtime;
import org.ainzson.models.pms.MachineStatus;
import org.ainzson.models.shift.Shift;
import org.ainzson.streamprocessor.pms.utils.ShiftUtilities;

import java.util.Map;

public class EventChangeProcess {

    private  final ShiftUtilities shiftUtilities;
    private final RedisConfig redisConfig;
    private final Map<String, Downtime> downtimes;
    private final EventChangeCapture eventChangeCapture;

    public EventChangeProcess(EventChangeCapture eventChangeCapture, ShiftUtilities shiftUtilities, RedisConfig redisConfig, Map<String, Downtime> downtimes) {
        this.downtimes = downtimes;
        this.shiftUtilities = shiftUtilities;
        this.redisConfig = redisConfig;
        this.eventChangeCapture = eventChangeCapture;
    }

    public Downtime process(MachineStatus machineStatus) {
        shiftUtilities.checkIsEventFallsWithinShift(machineStatus, redisConfig, downtimes);
        Shift shiftInfo = shiftUtilities.decodeShift(machineStatus, redisConfig, downtimes);
        eventChangeCapture.updateLocalVariable(machineStatus, redisConfig, downtimes);
        eventChangeCapture.checkStatusChanged(machineStatus, shiftInfo, downtimes);
        shiftUtilities.checkForDownTime(machineStatus, shiftInfo, downtimes);
        shiftUtilities.checkForShutDown(machineStatus, shiftInfo, downtimes);
        eventChangeCapture.updateCurrentStateToRedis(machineStatus, redisConfig, downtimes);
        return downtimes.get(machineStatus.getTags().getAsset());
    }
}
