package com.anshuman.scheduler.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;  // For ordered map
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service  // This makes the ShiftScheduler a Spring Bean
public class ShiftScheduler {

    static class Day {
        String date;
        boolean isSunday;
        boolean isGovtHoliday;

        Day(String date, boolean isSunday, boolean isGovtHoliday) {
            this.date = date;
            this.isSunday = isSunday;
            this.isGovtHoliday = isGovtHoliday;
        }
    }

    public Map<String, Object> generateSchedule(int totalEmployees, int numGeneral) {
        if (numGeneral > totalEmployees) {
            throw new IllegalArgumentException("Number of general employees cannot exceed total employees.");
        }

        int numNormal = totalEmployees - numGeneral;

        // Generate the month (30 days)
        List<Day> month = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            boolean isSunday = (i % 7 == 0);
            boolean isGovtHoliday = (i == 15 || i == 26);
            month.add(new Day("Day " + i, isSunday, isGovtHoliday));
        }

        // Create employee lists
        List<String> generalEmployees = new ArrayList<>();
        List<String> normalEmployees = new ArrayList<>();
        for (int i = 1; i <= numGeneral; i++) generalEmployees.add("G" + i);
        for (int i = 1; i <= numNormal; i++) normalEmployees.add("E" + i);

        // Shift pattern
        List<String> shiftPattern = Arrays.asList("B", "B", "A", "A", "C", "C", "Off");

        // Assign initial shift indices
        Map<String, Integer> shiftCycleIndex = new HashMap<>();
        for (int i = 0; i < normalEmployees.size(); i++) {
            String emp = normalEmployees.get(i);
            shiftCycleIndex.put(emp, i % shiftPattern.size());
        }

        // Build general shift schedule
        List<Map<String, String>> generalSchedule = new ArrayList<>();
        for (Day day : month) {
            Map<String, String> daily = new HashMap<>();
            daily.put("date", day.date);
            for (String emp : generalEmployees) {
                String status = (day.isSunday || day.isGovtHoliday) ? "OFF" : "GEN";
                daily.put(emp, status);
            }
            generalSchedule.add(daily);
        }

        // Build normal shift schedule in transposed format with ordered days
        List<Map<String, String>> transposedNormalSchedule = new ArrayList<>();
        for (String emp : normalEmployees) {
            Map<String, String> empSchedule = new LinkedHashMap<>();
            empSchedule.put("employee", emp);
            int idx = shiftCycleIndex.get(emp);
            for (Day day : month) {
                String shift = shiftPattern.get(idx);
                empSchedule.put(day.date, shift);
                idx = (idx + 1) % shiftPattern.size();
            }
            transposedNormalSchedule.add(empSchedule);
        }

        // Combine schedules into result
        Map<String, Object> result = new HashMap<>();
        result.put("generalSchedule", generalSchedule);
        result.put("normalSchedule", transposedNormalSchedule);  // transposed + ordered
        return result;
    }
}
