package org.example.service.CDR;

import org.example.entity.CDR;
import org.example.entity.Subscriber;
import org.example.repository.CDRRepository;
import org.example.repository.SubscriberRepository;
import org.example.service.subscriber.SubscriberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class CDRGeneratorService {

    @Autowired
    private CDRServiceImpl cdrService;

    @Autowired
    private SubscriberServiceImpl subscriberService;

    private final Random random = new Random();

    public void generateCDRsForYear() {
        List<Subscriber> subscribers = subscriberService.fetchSubscriberList();
        LocalDateTime startDate = LocalDateTime.now().minusYears(1);
        LocalDateTime endDate = LocalDateTime.now();

        for (Subscriber subscriber : subscribers) {
            LocalDateTime currentDate = startDate;
            while (currentDate.isBefore(endDate)) {
                generateCDRForSubscriber(subscriber, currentDate);
                currentDate = currentDate.plusMinutes(random.nextInt(1440));
            }
        }
    }

    public void generateCDRForSubscriber(Subscriber subscriber, LocalDateTime startTime) {
        CDR cdr = new CDR();
        cdr.setCallType(random.nextBoolean() ? "01" : "02");
        cdr.setCallerMsisdn(subscriber.getMsisdn());
        cdr.setReceiverMsisdn(getRandomReceiverMsisdn(subscriber.getMsisdn()));
        cdr.setStartTime(startTime);
        cdr.setEndTime(startTime.plusSeconds(random.nextInt(3600)));

        cdrService.saveCDR(cdr);
    }

    public String getRandomReceiverMsisdn(String callerMsisdn) {
        List<Subscriber> subscribers = subscriberService.fetchSubscriberList();
        Subscriber receiver = subscribers.get(random.nextInt(subscribers.size()));

        if (!checkOtherMsisdnExists(callerMsisdn, subscribers)) throw new IllegalStateException();

        while (receiver.equals(callerMsisdn)) {
            receiver = subscribers.get(random.nextInt(subscribers.size()));
        }
        return receiver.getMsisdn();
    }

    private boolean checkOtherMsisdnExists(String callerMsisdn, List<Subscriber> subscribers) {
        return subscribers.stream().anyMatch(element -> !element.getMsisdn().equals(callerMsisdn));
    }
}