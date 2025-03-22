package org.example.service.CDR;

import org.example.entity.CDR;
import org.example.entity.Subscriber;
import org.example.repository.CDRRepository;
import org.example.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Service
public class CDRGeneratorService {

    @Autowired
    private CDRRepository cdrRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    private final Random random = new Random();

    public void generateCDRsForYear() {
        List<Subscriber> subscribers = subscriberRepository.findAll();
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

    private void generateCDRForSubscriber(Subscriber subscriber, LocalDateTime startTime) {
        CDR cdr = new CDR();
        cdr.setCallType(random.nextBoolean() ? "01" : "02");
        cdr.setCallerMsisdn(subscriber.getMsisdn());
        cdr.setReceiverMsisdn(getRandomReceiverMsisdn(subscriber.getMsisdn()));
        cdr.setStartTime(startTime);
        cdr.setEndTime(startTime.plusSeconds(random.nextInt(3600)));

        cdrRepository.save(cdr);
    }

    private String getRandomReceiverMsisdn(String callerMsisdn) {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        Subscriber receiver = subscribers.get(random.nextInt(subscribers.size()));
        while (receiver.getMsisdn().equals(callerMsisdn)) {
            receiver = subscribers.get(random.nextInt(subscribers.size()));
        }
        return receiver.getMsisdn();
    }
}