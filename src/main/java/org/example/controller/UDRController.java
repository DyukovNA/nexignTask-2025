package org.example.controller;


import org.example.dto.UDR;
import org.example.service.UDR.UDRGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.Map;

@RestController
@RequestMapping("/udr")
public class UDRController {

    private final UDRGenerationService udrService;

    @Autowired
    public UDRController(UDRGenerationService udrService) {
        this.udrService = udrService;
    }

    // Метод для получения UDR записи по одному абоненту
    @GetMapping("/{msisdn}")
    public UDR getUDRForSubscriber(
            @PathVariable String msisdn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) YearMonth month) {
        if (month != null) {
            return udrService.generateUDRForSubscriber(msisdn, month);
        } else {
            return udrService.generateUDRForSubscriber(msisdn);
        }
    }

    // Метод для получения UDR записей по всем абонентам за месяц
    @GetMapping("/monthly")
    public Map<String, UDR> getUDRForAllSubscribers(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) YearMonth month) {
        return udrService.generateUDRForAllSubscribers(month);
    }
}
