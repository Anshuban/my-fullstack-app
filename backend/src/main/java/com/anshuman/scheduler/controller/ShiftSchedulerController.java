package com.anshuman.scheduler.controller;

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
    public Map<String, Object> generateSchedule(@RequestBody Map<String, Integer> payload) {
        try {
            // Log the incoming request
            logger.info("Received request: totalEmployees={}, generalEmployees={}, month={}, year={}",
                    payload.get("totalEmployees"), payload.get("generalEmployees"), payload.get("month"), payload.get("year"));

            int totalEmployees = payload.get("totalEmployees");
            int generalEmployees = payload.get("generalEmployees");
            int month = payload.get("month");
            int year = payload.get("year");

            // Check if the values are valid
            if (totalEmployees <= 0 || generalEmployees < 0 || month <= 0 || month > 12 || year < 1900) {
                logger.error("Invalid input parameters. Please check the values.");
                throw new IllegalArgumentException("Invalid input values.");
            }

            return shiftScheduler.generateSchedule(totalEmployees, generalEmployees, month, year);
        } catch (Exception e) {
            // Log the error
            logger.error("Error generating schedule: ", e);
            throw e;  // Re-throw the exception so that Spring handles it and returns a 500 error
        }
    }
}
