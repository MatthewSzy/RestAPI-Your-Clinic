package com.surgery.generator;

import java.util.ArrayList;
import java.util.List;

public class HoursGenerator {

    private HoursGenerator() { }

    public static List<String> generateAllAvailableHoursList() {
        List<String> availableHoursList = new ArrayList<>();
        for (int i = 10; i < 20; i++) {
            String hour = String.format("%d:00 - %d:00", i, i + 1);
            availableHoursList.add(hour);
        }
        return availableHoursList;
    }

    public static List<String> getListOfAvailableHours(List<String> hours) {

        List<String> allAvailableHours = generateAllAvailableHoursList();
        List<String> listOfFreeHours = new ArrayList<>();

        for (String time : allAvailableHours) {
            if (!hours.contains(time)) {
                listOfFreeHours.add(time);
            }
        }

        return listOfFreeHours;
    }
}
