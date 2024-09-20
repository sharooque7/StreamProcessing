package org.ainzson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ainzson.schema.RingFrame40StreamDTO;
import org.ainzson.streamprocessor.RingFrameStreamProcessing;
import org.ainzson.config.RedisConfig;
import org.ainzson.utils.EpochToTimestampTypeAdapter;

import java.sql.Timestamp;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {
        RingFrameStreamProcessing ringFrameStreamProcessing = new RingFrameStreamProcessing();
        ringFrameStreamProcessing.initializeRingframeStream();

//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(Timestamp.class, new EpochToTimestampTypeAdapter())  // Register adapter for long values (timestamps)
//                .create();
//
//        String prod = "[{\"totalkvar\":0.01005,\"productionefficiency\":6.437565,\"airconsumption\":26.710176,\"totalkva\":1.403091,\"overallmachineutilization\":0.808478,\"productioninhanks\":63.233685,\"counts\":1.664785,\"shiftnumber\":1,\"voltageavg\":8.900768,\"dofftimeminute\":6.528368,\"frequency\":0.690923,\"ukg\":7.021536,\"noofdoffs\":15.495927,\"machineid\":\"Asset001\",\"totalkvarh\":2.835686,\"productionkg\":6.45914,\"siteid\":\"Unit1\",\"powerfailtimehour\":9.637084,\"averagespindlespeed\":3.28787,\"weightperspindle\":69.341882,\"idletimehour\":1.743071,\"averagedelivery\":0.034149,\"departmentid\":\"RingFrame1\",\"productioninmeters\":82.898703,\"idletimeminute\":6.801292,\"powerfailtimeminute\":7.670065,\"averagetpi\":6.855748,\"shifthour\":18,\"currentavg\":2.691464,\"totalactivepower\":9.950499,\"totalkwh\":0.781656,\"tags\":{\"groupid\":-278747700,\"location\":\"Chennai\"},\"shiftyear\":2024,\"dofftimehour\":2.635459,\"powerfactor\":6.157107,\"runtimeminute\":8.389247,\"totalkvah\":9.027366,\"assetid\":\"Asset001\",\"shiftdate\":1726576896441,\"shiftmonth\":9,\"gpss\":7.451853,\"runtimehour\":3.363367,\"shiftminute\":11,\"ts\":1726576896441}]\n";
//
//        // Deserialize the JSON string into an array of RR objects
//        RingFrame40StreamDTO[] ringframe40SArray = gson.fromJson(String.valueOf(prod), RingFrame40StreamDTO[].class);
//        Arrays.stream(ringframe40SArray).forEach(value->{
//            System.out.println(value.getTs());
//            System.out.println(value.getShiftDate());
//            System.out.println(value.getAirConsumption());
//            System.out.println(value.getGpss());
//
//        });

    }
}