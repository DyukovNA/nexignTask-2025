package org.example.repository;

import org.example.entity.Subscriber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class SubscriberRepositoryTest {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Test
    void deleteById_ShouldRemoveSubscriber() {
        String msisdn = "79992221122";
        Subscriber subscriber = new Subscriber();
        subscriber.setMsisdn(msisdn);
        subscriberRepository.save(subscriber);

        subscriberRepository.deleteById(msisdn);

        assertFalse(subscriberRepository.existsById(msisdn));
    }

    @Test
    void findAllMsisdn_ShouldReturnListOfMsisdn() {
        String msisdn1 = "79992221122";
        String msisdn2 = "79993331133";

        Subscriber subscriber1 = new Subscriber();
        subscriber1.setMsisdn(msisdn1);
        subscriberRepository.save(subscriber1);

        Subscriber subscriber2 = new Subscriber();
        subscriber2.setMsisdn(msisdn2);
        subscriberRepository.save(subscriber2);

        List<String> msisdns = subscriberRepository.findAllMsisdn();

        assertEquals(2, msisdns.size());
        assertTrue(msisdns.contains(msisdn1));
        assertTrue(msisdns.contains(msisdn2));
    }

    @Test
    @Sql("/test-data.sql")
    void findAllMsisdn_ShouldReturnMsisdnFromTestData() {
        List<String> msisdns = subscriberRepository.findAllMsisdn();

        assertEquals(3, msisdns.size());
        assertTrue(msisdns.contains("79991112233"));
        assertTrue(msisdns.contains("79992223344"));
        assertTrue(msisdns.contains("79993334455"));
    }
}
