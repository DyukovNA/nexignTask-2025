package org.example.controller;

import org.example.service.report.ReportGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportGenerationService reportService;

    @Autowired
    public ReportController(ReportGenerationService reportService) {
        this.reportService = reportService;
    }

    // http://localhost:8080/report/generate?msisdn=79992221122&startDate=2025-02-01T00:00:00&endDate=2025-02-28T23:59:59
    @GetMapping("/generate")
    public ResponseEntity<String> generateReport(
            @RequestParam String msisdn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            UUID requestId = reportService.generateCDRReport(msisdn, startDate, endDate);
            return ResponseEntity.ok("Request ID: " + requestId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
