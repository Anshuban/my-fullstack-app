package com.anshuman.scheduler.controller;

import com.anshuman.scheduler.service.ShiftScheduler;  // Import the service class
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;  // Import Spring annotations
import java.util.Map;  // Import Map class

@RestController
@RequestMapping("/api/scheduler")
public class ShiftSchedulerController {

    @Autowired
    private ShiftScheduler shiftScheduler;  // Inject the service class

    @GetMapping("/test")
    public String testEndpoint() {
        return "Scheduler is working!";
    }

    @PostMapping("/generate")
    public Map<String, Object> generateSchedule(@RequestBody Map<String, Integer> payload) {
        int totalEmployees = payload.get("totalEmployees");
        int generalEmployees = payload.get("generalEmployees");

        return shiftScheduler.generateSchedule(totalEmployees, generalEmployees);  // Call the service method
    }
}
