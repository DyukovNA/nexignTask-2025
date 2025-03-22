package org.example.service.CDR;

import org.example.entity.CDR;
import org.example.entity.Subscriber;
import org.example.repository.CDRRepository;
import org.example.service.subscriber.SubscriberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@DataJpaTest
class CDRServiceImplTest {

    @Mock
    private CDRRepository cdrRepository;

    @Mock
    private SubscriberServiceImpl subscriberService;

    @InjectMocks
    private CDRServiceImpl cdrService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков
    }

    @Test
    void saveCDR_ShouldReturnSavedCDR() {
        // Arrange
        CDR cdr = new CDR();
        cdr.setCallType("01");
        cdr.setCallerMsisdn("79992221122");
        cdr.setReceiverMsisdn("79993331133");
        cdr.setStartTime(LocalDateTime.now());
        cdr.setEndTime(LocalDateTime.now().plusMinutes(5));

        when(cdrRepository.save(cdr)).thenReturn(cdr);

        // Act
        CDR savedCDR = cdrService.saveCDR(cdr);

        // Assert
        assertNotNull(savedCDR);
        assertEquals("01", savedCDR.getCallType());
        verify(cdrRepository, times(1)).save(cdr);
    }

    @Test
    void fetchCDRList_ShouldReturnAllCDRs() {
        // Arrange
        CDR cdr1 = new CDR();
        cdr1.setCallType("01");
        cdr1.setCallerMsisdn("79992221122");

        CDR cdr2 = new CDR();
        cdr2.setCallType("02");
        cdr2.setCallerMsisdn("79993331133");

        when(cdrRepository.findAll()).thenReturn(Arrays.asList(cdr1, cdr2));

        // Act
        List<CDR> cdrs = cdrService.fetchCDRList();

        // Assert
        assertEquals(2, cdrs.size());
        verify(cdrRepository, times(1)).findAll();
    }

    @Test
    void deleteCDRByID_ShouldDeleteCDR() {
        // Arrange
        Long cdrId = 1L;

        // Act
        cdrService.deleteCDRByID(cdrId);

        // Assert
        verify(cdrRepository, times(1)).deleteById(cdrId);
    }

    @Test
    void fetchCDRListByMsisdn_ShouldReturnCDRsForGivenMsisdn() {
        // Arrange
        String callerMsisdn = "79992221122";
        String receiverMsisdn = "79993331133";

        CDR cdr1 = new CDR();
        cdr1.setCallerMsisdn(callerMsisdn);

        CDR cdr2 = new CDR();
        cdr2.setReceiverMsisdn(receiverMsisdn);

        when(cdrRepository.findByCallerMsisdnOrReceiverMsisdn(callerMsisdn, receiverMsisdn))
                .thenReturn(Arrays.asList(cdr1, cdr2));

        // Act
        List<CDR> cdrs = cdrService.fetchCDRListByMsisdn(callerMsisdn, receiverMsisdn);

        // Assert
        assertEquals(2, cdrs.size());
        verify(cdrRepository, times(1))
                .findByCallerMsisdnOrReceiverMsisdn(callerMsisdn, receiverMsisdn);
    }

    @Test
    void initializeData_ShouldInitializeSubscribersAndGenerateCDRs() {
        // Arrange
        Subscriber subscriber = new Subscriber();
        subscriber.setMsisdn("79992221122");

        when(subscriberService.fetchSubscriberList()).thenReturn(Collections.singletonList(subscriber));
        when(cdrRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        // Act
        cdrService.initializeData();

        // Assert
        verify(subscriberService, times(1)).fetchSubscriberList();
        verify(cdrRepository, times(1)).saveAll(anyList());
    }

    @Test
    void saveAllCDRs_ShouldSaveAllCDRs() {
        // Arrange
        CDR cdr1 = new CDR();
        CDR cdr2 = new CDR();

        when(cdrRepository.saveAll(Arrays.asList(cdr1, cdr2))).thenReturn(Arrays.asList(cdr1, cdr2));

        // Act
        List<CDR> savedCDRs = cdrService.saveAllCDRs(Arrays.asList(cdr1, cdr2));

        // Assert
        assertEquals(2, savedCDRs.size());
        verify(cdrRepository, times(1)).saveAll(Arrays.asList(cdr1, cdr2));
    }

    @Test
    void fetchCDRListByMsisdnAndTime_ShouldReturnCDRsForGivenMsisdnAndTimeRange() {
        // Arrange
        String msisdn = "79992221122";
        LocalDateTime startOfMonth = LocalDateTime.now().minusMonths(1);
        LocalDateTime endOfMonth = LocalDateTime.now();

        CDR cdr = new CDR();
        cdr.setCallerMsisdn(msisdn);

        when(cdrRepository.findByCallerMsisdnOrReceiverMsisdnAndStartTimeBetween(msisdn, startOfMonth, endOfMonth))
                .thenReturn(Collections.singletonList(cdr));

        // Act
        List<CDR> cdrs = cdrService.fetchCDRListByMsisdnAndTime(msisdn, startOfMonth, endOfMonth);

        // Assert
        assertEquals(1, cdrs.size());
        verify(cdrRepository, times(1))
                .findByCallerMsisdnOrReceiverMsisdnAndStartTimeBetween(msisdn, startOfMonth, endOfMonth);
    }
}