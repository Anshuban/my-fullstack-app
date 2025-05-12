package com.anshuman.scheduler.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
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

    public Map<String, Object> generateSchedule(int totalEmployees, int numGeneral, int month, int year, Map<String, List<List<String>>> leaves) {
        if (numGeneral > totalEmployees) {
            throw new IllegalArgumentException("Number of general employees cannot exceed total employees.");
        }

        int numNormal = totalEmployees - numGeneral;

        // Calculate the number of days in the given month and year
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int daysInMonth = firstDayOfMonth.lengthOfMonth();

        // Generate the calendar for the given month and year
        List<Day> monthDays = new ArrayList<>();
        for (int i = 1; i <= daysInMonth; i++) {
            LocalDate date = LocalDate.of(year, month, i);
            boolean isSunday = date.getDayOfWeek() == DayOfWeek.SUNDAY;
            boolean isGovtHoliday = (i == 15 || i == 26); // Example fixed govt holidays
            monthDays.add(new Day(date.toString(), isSunday, isGovtHoliday));
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
        for (Day day : monthDays) {
            Map<String, String> daily = new HashMap<>();
            daily.put("date", day.date);
            for (String emp : generalEmployees) {
                String status = (day.isSunday || day.isGovtHoliday) ? "OFF" : "GEN";
                daily.put(emp, status);
            }
            generalSchedule.add(daily);
        }

        // Build normal shift schedule with leave logic
        List<Map<String, String>> transposedNormalSchedule = new ArrayList<>();
        for (String emp : normalEmployees) {
            Map<String, String> empSchedule = new LinkedHashMap<>();
            empSchedule.put("employee", emp);
            int idx = shiftCycleIndex.get(emp);

            List<List<String>> empLeaveRanges = leaves.getOrDefault(emp, new ArrayList<>());

            for (Day day : monthDays) {
                boolean isOnLeave = false;
                LocalDate current = LocalDate.parse(day.date);
                for (List<String> range : empLeaveRanges) {
                    if (range.size() == 2) {
                        LocalDate start = LocalDate.parse(range.get(0));
                        LocalDate end = LocalDate.parse(range.get(1));
                        if ((current.isEqual(start) || current.isAfter(start)) &&
                            (current.isEqual(end) || current.isBefore(end))) {
                            isOnLeave = true;
                            break;
                        }
                    }
                }

                if (isOnLeave) {
                    empSchedule.put(day.date, "LEAVE");
                    idx = (idx + 1) % shiftPattern.size();
                } else {
                    String shift = shiftPattern.get(idx);
                    empSchedule.put(day.date, shift);
                    idx = (idx + 1) % shiftPattern.size();
                }
            }

            transposedNormalSchedule.add(empSchedule);
        }

        // Combine schedules into result
        Map<String, Object> result = new HashMap<>();
        result.put("generalSchedule", generalSchedule);
        result.put("normalSchedule", transposedNormalSchedule);
        return result;
    }
}
