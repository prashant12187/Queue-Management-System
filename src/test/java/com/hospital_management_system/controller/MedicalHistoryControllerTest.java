package com.hospital_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital_management_system.entity.MedicalHistory;
import com.hospital_management_system.entity.Patient;
import com.hospital_management_system.entity.Slot;
import com.hospital_management_system.service.MedicalHistoryService;
import com.hospital_management_system.service.SlotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class MedicalHistoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MedicalHistoryService medicalHistoryService;

    @Test
    public void testGetMedicalHistoryById() throws Exception {
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setId(7L);
        medicalHistory.setCurrentMedications("xyz");
        medicalHistory.setAllergies("pqr");
        medicalHistory.setPreviousIllnesses("xyz");
        medicalHistory.setPatient(new Patient());

        Mockito.when(medicalHistoryService.getMedicalHistoryById(7L)).thenReturn(medicalHistory);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/medical-history/7"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(7L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentMedications").value("xyz"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.allergies").value("pqr"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteMedicalHistoryById() throws Exception {

        Mockito.when(medicalHistoryService.deleteMedicalHistoryById(7L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/medical-history/7"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteMedicalHistoryById_NotFound() throws Exception {

        Mockito.when(medicalHistoryService.deleteMedicalHistoryById(7L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/medical-history/7"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
