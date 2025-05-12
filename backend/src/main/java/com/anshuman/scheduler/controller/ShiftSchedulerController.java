package com.anshuman.scheduler.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anshuman.scheduler.service.ShiftScheduler;

@RestController
@RequestMapping("/api/scheduler")
public class ShiftSchedulerController {

    private static final Logger logger = LoggerFactory.getLogger(ShiftSchedulerController.class);

    @Autowired
    private ShiftScheduler shiftScheduler;

    @GetMapping("/test")
    public String testEndpoint() {
        return "Scheduler is working!";
    }

    @PostMapping("/generate")
    public Map<String, Object> generateSchedule(@RequestBody Map<String, Object> payload) {
        try {
            // Extract and validate input values
            int totalEmployees = (int) payload.get("totalEmployees");
            int generalEmployees = (int) payload.get("generalEmployees");
            int month = (int) payload.get("month");
            int year = (int) payload.get("year");

            if (totalEmployees <= 0 || generalEmployees < 0 || month < 1 || month > 12 || year < 1900) {
                logger.error("Invalid input parameters: totalEmployees={}, generalEmployees={}, month={}, year={}",
                        totalEmployees, generalEmployees, month, year);
                throw new IllegalArgumentException("Invalid input values.");
            }

            // Safely extract leaves, check if it's null or empty
            Map<String, List<List<String>>> leaves = new HashMap<>();
            Object leavesObj = payload.get("leaves");

            // If the leaves data is not provided, set leaves as an empty map
            if (leavesObj == null || !(leavesObj instanceof Map)) {
                leaves = new HashMap<>();
            } else {
                // Process the leaves data if it's present and in the correct format
                Map<?, ?> rawMap = (Map<?, ?>) leavesObj;
                for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
                    String key = String.valueOf(entry.getKey());
                    Object value = entry.getValue();

                    if (value instanceof List) {
                        List<?> rawList = (List<?>) value;
                        List<List<String>> leaveRanges = new java.util.ArrayList<>();

                        for (Object item : rawList) {
                            if (item instanceof List) {
                                List<?> inner = (List<?>) item;
                                List<String> dateRange = new java.util.ArrayList<>();
                                for (Object date : inner) {
                                    dateRange.add(String.valueOf(date));
                                }
                                leaveRanges.add(dateRange);
                            }
                        }

                        leaves.put(key, leaveRanges);
                    }
                }
            }

            // Log the received data for debugging
            logger.info("Received request with totalEmployees={}, generalEmployees={}, month={}, year={}, leaves={}",
                    totalEmployees, generalEmployees, month, year, leaves);

            // Call the service layer to generate the schedule with the validated input and leave data
            return shiftScheduler.generateSchedule(totalEmployees, generalEmployees, month, year, leaves);

        } catch (Exception e) {
            // Log any errors that occur during schedule generation
            logger.error("Error generating schedule: ", e);
            throw e;
        }
    }
}
