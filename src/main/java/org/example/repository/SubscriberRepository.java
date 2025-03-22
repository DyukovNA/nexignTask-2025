package org.example.repository;

import org.example.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriberRepository extends JpaRepository<Subscriber, String> {
    void deleteById(String msisdn);
    @Query("SELECT s.msisdn FROM Subscriber s")
    List<String> findAllMsisdn();
}
