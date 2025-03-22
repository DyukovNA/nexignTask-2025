package org.example.repository;

import org.example.entity.CDR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CDRRepository extends JpaRepository<CDR, Long> {
    void deleteById(Long CDRId);
    List<CDR> findByCallerMsisdnOrReceiverMsisdn(String callerMsisdn, String receiverMsisdn);
    @Override
    <S extends CDR> List<S> saveAll(Iterable<S> entities);
    @Query("SELECT c FROM CDR c WHERE (c.callerMsisdn = :msisdn OR c.receiverMsisdn = :msisdn) " +
            "AND c.startTime BETWEEN :startTime AND :endTime")
    List<CDR> findByCallerMsisdnOrReceiverMsisdnAndStartTimeBetween(
            @Param("msisdn") String receiverMsisdn,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
