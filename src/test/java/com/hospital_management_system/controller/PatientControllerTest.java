package com.hospital_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital_management_system.entity.Patient;
import com.hospital_management_system.entity.Slot;
import com.hospital_management_system.payload.MedicalHistoryDTO;
import com.hospital_management_system.payload.PatientDTO;
import com.hospital_management_system.service.PatientService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreatePatient() throws Exception {
        String queueName = "testQueue";

        LocalDateTime startTime = LocalDateTime.parse("2025-04-09T09:30");
        LocalDateTime endTime = LocalDateTime.parse("2025-04-09T10:00");

        Patient patient = new Patient(1L, "prashant", "1991-01-30","male" );
        MedicalHistoryDTO medicalHistoryDTO = new MedicalHistoryDTO(1L, "cough and cold", "chest pain", "abcxyz");
        PatientDTO patientDTO = new PatientDTO(1L, "prashant", "1991-01-30","male" , "back pain", medicalHistoryDTO);

        Mockito.when(patientService.createPatient(patientDTO)).thenReturn(patient);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("prashant"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("male"));

    }

    @Test
    public void testGetAllPatients() throws Exception {
        LocalDateTime startTime = LocalDateTime.parse("2025-04-05T09:30");
        LocalDateTime endTime = LocalDateTime.parse("2025-04-05T10:00");

        Patient patient = new Patient(1L, "prashant", "1991-01-30","male" );
        Patient patient1 = new Patient(2L, "rakesh", "1990-05-04","male" );

        List<Patient> patients = new ArrayList<Patient>();
        patients.add(patient);
        patients.add(patient1);


        Mockito.when(patientService.getAllPatients()).thenReturn(patients);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients"))

                .andExpect(MockMvcResultMatchers.status().isOk())

                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("prashant"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("rakesh"));
    }
}
