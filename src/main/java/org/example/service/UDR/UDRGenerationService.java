package org.example.service.UDR;

import org.example.dto.UDR;
import org.example.entity.CDR;
import org.example.service.CDR.CDRServiceImpl;
import org.example.service.subscriber.SubscriberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UDRGenerationService {
    @Autowired
    private final CDRServiceImpl cdrService;
    @Autowired
    private final SubscriberServiceImpl subscriberService;


    public UDRGenerationService(CDRServiceImpl cdrService, SubscriberServiceImpl subscriberService) {
        this.cdrService = cdrService;
        this.subscriberService = subscriberService;
    }

    public UDR generateUDRForSubscriber(String msisdn) {
        List<CDR> cdrs = cdrService.fetchCDRListByMsisdn(msisdn, msisdn);
        return createUDRFromCDRs(msisdn, cdrs);
    }

    public UDR generateUDRForSubscriber(String msisdn, YearMonth month) {
        LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);
        List<CDR> cdrs = cdrService.fetchCDRListByMsisdnAndTime(
                msisdn, startOfMonth, endOfMonth);
        return createUDRFromCDRs(msisdn, cdrs);
    }

    public Map<String, UDR> generateUDRForAllSubscribers(YearMonth month) {
        LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);
        Map<String, UDR> udrMap = new HashMap<>();

        List<String> msisdns = subscriberService.fetchMsisdnList();
        for (String msisdn : msisdns) {
            List<CDR> cdrs = cdrService.fetchCDRListByMsisdnAndTime(
                    msisdn, startOfMonth, endOfMonth);
            udrMap.put(msisdn, createUDRFromCDRs(msisdn, cdrs));
        }

        return udrMap;
    }

    private UDR createUDRFromCDRs(String msisdn, List<CDR> cdrs) {
        Duration incomingCallDuration = Duration.ZERO;
        Duration outcomingCallDuration = Duration.ZERO;

        for (CDR cdr : cdrs) {
            Duration callDuration = Duration.between(cdr.getStartTime(), cdr.getEndTime());
            if (cdr.getCallType().equals("02") && cdr.getReceiverMsisdn().equals(msisdn)) {
                incomingCallDuration = incomingCallDuration.plus(callDuration);
            } else if (cdr.getCallType().equals("01") && cdr.getCallerMsisdn().equals(msisdn)) {
                outcomingCallDuration = outcomingCallDuration.plus(callDuration);
            }
        }

        UDR udr = new UDR();
        udr.setMsisdn(msisdn);
        udr.setIncomingCall(new UDR.CallDetail(formatDuration(incomingCallDuration)));
        udr.setOutcomingCall(new UDR.CallDetail(formatDuration(outcomingCallDuration)));

        return udr;
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}