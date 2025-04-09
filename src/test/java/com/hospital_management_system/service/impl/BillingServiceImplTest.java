package com.hospital_management_system.service.impl;

import com.hospital_management_system.entity.Billing;
import com.hospital_management_system.entity.Patient;
import com.hospital_management_system.repository.BillingRepository;
import com.hospital_management_system.service.BillingService;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
//@//RunWith(MockitoJUnitRunner.class)
public class BillingServiceImplTest {
    @InjectMocks
    private BillingServiceImpl billingService;

    @Mock
    private BillingRepository billingRepository;


    @Test
    public void testGetBillingById() {
        Patient patient = new Patient();
        Billing billing = new Billing(1L, patient, "tata insurance",  "abc123", 45000.00 );
        Mockito.when(billingRepository.findById(1L)).thenReturn(Optional.of(billing));

        Billing result = billingService.getBillingById(1L);
        assertEquals(1L, result.getId());
     }
    @Test
    public void testDeleteBillingById() {
        Mockito.when(billingRepository.existsById(1L)).thenReturn(true);
       // Mockito.when(billingRepository.existsById(1L)).thenReturn(false);

        boolean result = billingService.deleteBillingById(1L);
        Mockito.verify(billingRepository, Mockito.times(1)).deleteById(1L);
    }

    }
