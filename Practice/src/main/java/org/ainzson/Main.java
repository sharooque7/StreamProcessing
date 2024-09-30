package org.ainzson;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String json = "{\"shiftdate\":1727113551,\"points\":9.8,\"name\":\"sharooque\",\"ts\":1726820147775}";

        ObjectMapper objectMapper = new ObjectMapper();
        RingFrame data = objectMapper.readValue(json, RingFrame.class);

        System.out.println("Shift Date in ISO 8601 format: " + data.getShiftDate());
        System.out.println("Shift Date in ISO 8601 format: " + data.getTs());
        System.out.println("Shift Date in ISO 8601 format: " + data.getName());
        System.out.println("Shift Date in ISO 8601 format: " + data.getPoints());

    }
}