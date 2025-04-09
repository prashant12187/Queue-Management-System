package com.hospital_management_system.service.impl;

import com.hospital_management_system.entity.MedicalHistory;
import com.hospital_management_system.entity.Patient;
import com.hospital_management_system.entity.Slot;
import com.hospital_management_system.repository.MedicalHistoryRepository;
import com.hospital_management_system.repository.SlotRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
public class MedicalHistoryServiceImplTest {
    @InjectMocks
    private MedicalHistoryServiceImpl  medicalHistoryService;

    @Mock
    private MedicalHistoryRepository medicalHistoryRepository;

   /* @Mock
    private ModelMapper modelMapper;*/

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Option 2: Manually initialize mocks
    }

    @Test
    public void testGetMedicalHistoryById() {

        MedicalHistory medicalHistory = new MedicalHistory();
            medicalHistory.setId(7L);
            medicalHistory.setCurrentMedications("xyz");
            medicalHistory.setAllergies("pqr");
            medicalHistory.setPreviousIllnesses("xyz");
            medicalHistory.setPatient(new Patient());

        Mockito.when(medicalHistoryRepository.findById(7L)).thenReturn(Optional.of(medicalHistory));

        MedicalHistory result = medicalHistoryService.getMedicalHistoryById(7L);
        assertEquals(7L, result.getId());
    }

    @Test
    public void testDeleteMedicalHistoryById() {

        Mockito.when(medicalHistoryRepository.existsById(1L)).thenReturn(true);

        boolean result = medicalHistoryService.deleteMedicalHistoryById(1L);
        Mockito.verify(medicalHistoryRepository, Mockito.times(1)).deleteById(1L);
    }


    @Test
    public void testDeleteMedicalHistoryByIdNegativeScenario() {
        Mockito.when(medicalHistoryRepository.existsById(1L)).thenReturn(false);
        boolean result = medicalHistoryService.deleteMedicalHistoryById(1L);

        assertFalse(result, "Expected checkMedicalHistoryExists to return false");
    }

}
