package org.example.service.subscriber;

import org.example.entity.Subscriber;

import java.util.List;


public interface SubscriberService {
    Subscriber saveSubscriber(Subscriber subscriber);
    List<Subscriber> fetchSubscriberList();
    void deleteSubscriberByID(String msisdn);
//    List<String> fetchMsisdnList();
}
