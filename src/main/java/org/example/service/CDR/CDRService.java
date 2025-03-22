package org.example.service.CDR;

import org.example.entity.CDR;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CDRService {
    CDR saveCDR(CDR cdr);
    List<CDR> fetchCDRList();
    void deleteCDRByID(Long CDRId);
    List<CDR> fetchCDRListByMsisdn(String callerMsisdn, String receiverMsisdn);
    void initializeData();
    <S extends CDR> List<S> saveAllCDRs(Iterable<S> entities);
    @Query("SELECT c FROM CDR c WHERE (c.callerMsisdn = :msisdn OR c.receiverMsisdn = :msisdn) " +
            "AND c.startTime BETWEEN :startTime AND :endTime")
    List<CDR> fetchCDRListByMsisdnAndTime(String msisdn, LocalDateTime startOfMonth, LocalDateTime endOfMonth);
}
