package org.example.service.CDR;

import jakarta.annotation.PostConstruct;
import org.example.entity.CDR;
import org.example.entity.Subscriber;
import org.example.repository.CDRRepository;
import org.example.service.subscriber.SubscriberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Component
public class CDRServiceImpl implements CDRService {
    private final CDRRepository cdrRepository;
    private final SubscriberServiceImpl subscriberService;

    @Autowired
    public CDRServiceImpl (CDRRepository cdrRepository, SubscriberServiceImpl subscriberService) {
        this.cdrRepository = cdrRepository;
        this.subscriberService = subscriberService;
    }

    @Override
    public CDR saveCDR(CDR cdr) {
        return cdrRepository.save(cdr);
    }

    @Override
    public List<CDR> fetchCDRList() {
        return cdrRepository.findAll();
    }

    @Override
    public void deleteCDRByID(Long CDRId) {
        cdrRepository.deleteById(CDRId);
    }

    @Override
    public List<CDR> fetchCDRListByMsisdn(String callerMsisdn, String receiverMsisdn) {
        return cdrRepository.findByCallerMsisdnOrReceiverMsisdn(callerMsisdn, receiverMsisdn);
    }

    @Override
    public void initializeData() {
        initializeSubscribers();
        generateCDRsForYear();
    }

    @Override
    public <S extends CDR> List<S> saveAllCDRs(Iterable<S> entities) {
        return cdrRepository.saveAll(entities);
    }

    @Override
    public List<CDR> fetchCDRListByMsisdnAndTime(String msisdn, LocalDateTime startOfMonth, LocalDateTime endOfMonth) {
        return cdrRepository.findByCallerMsisdnOrReceiverMsisdnAndStartTimeBetween(
                msisdn, startOfMonth, endOfMonth
        );
    }

    @PostConstruct
    public void init() {
        initializeData();
    }

    private void initializeSubscribers() {
        List<String> msisdns = Arrays.asList(
                "79992221122", "79993331133", "79994441144", "79995551155",
                "79996661166", "79997771177", "79998881188", "79999991199",
                "79991112233", "79992223344"
        );

        for (String msisdn : msisdns) {
            Subscriber subscriber = new Subscriber();
            subscriber.setMsisdn(msisdn);
            subscriberService.saveSubscriber(subscriber);
        }
    }

    private void generateCDRsForYear() {
        List<Subscriber> subscribers = subscriberService.fetchSubscriberList();
        LocalDateTime startDate = LocalDateTime.now().minusYears(1);
        LocalDateTime endDate = LocalDateTime.now();
        Random random = new Random();

        List<CDR> allCDRs = new ArrayList<>();

        for (Subscriber subscriber : subscribers) {
            LocalDateTime currentDate = startDate;
            while (currentDate.isBefore(endDate)) {
                CDR cdr = generateCDRForSubscriber(subscriber, currentDate);
                allCDRs.add(cdr);
                currentDate = currentDate.plusMinutes(random.nextInt(1440));
            }
        }

        allCDRs.sort(Comparator.comparing(CDR::getStartTime));

        saveAllCDRs(allCDRs);
    }

    private CDR generateCDRForSubscriber(Subscriber subscriber, LocalDateTime startTime) {
        CDR cdr = new CDR();
        Random random = new Random();
        cdr.setCallType(random.nextBoolean() ? "01" : "02");
        cdr.setCallerMsisdn(subscriber.getMsisdn());
        cdr.setReceiverMsisdn(getRandomReceiverMsisdn(subscriber.getMsisdn()));
        cdr.setStartTime(startTime);
        cdr.setEndTime(startTime.plusSeconds(random.nextInt(3600)));

        return cdr;
    }

    private String getRandomReceiverMsisdn(String callerMsisdn) {
        Random random = new Random();
        List<Subscriber> subscribers = subscriberService.fetchSubscriberList();
        Subscriber receiver = subscribers.get(random.nextInt(subscribers.size()));
        while (receiver.getMsisdn().equals(callerMsisdn)) {
            receiver = subscribers.get(random.nextInt(subscribers.size()));
        }
        return receiver.getMsisdn();
    }
}
