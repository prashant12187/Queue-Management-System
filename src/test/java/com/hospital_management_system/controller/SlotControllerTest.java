package com.hospital_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital_management_system.entity.Patient;
import com.hospital_management_system.entity.Slot;
import com.hospital_management_system.payload.SlotDTO;
import com.hospital_management_system.service.SlotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class SlotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SlotService slotService;

    @Test
    public void testGetAvailableSlots() throws Exception {
      String queueName = "testQueue";
        LocalDateTime startTime = LocalDateTime.parse("2025-04-09T09:30");
        LocalDateTime endTime = LocalDateTime.parse("2025-04-09T10:00");

        LocalDateTime startTime1 = LocalDateTime.parse("2025-04-09T10:00");
        LocalDateTime endTime1 = LocalDateTime.parse("2025-04-09T10:30");

        Patient patient = new Patient();
          patient.setId(1L);
          patient.setName("prashant");
          patient.setGender("male");
          patient.setDateOfBirth("2025-04-09");

        List<Slot> availableSlots = Arrays.asList(new Slot(1L, "testQueue", startTime, endTime, false, false, patient), new Slot(2L, "testQueue", startTime1, endTime1, false, false, patient));

        Mockito.when(slotService.getAvailableSlots(queueName)).thenReturn(availableSlots);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/slots/available")
                .param("queueName", queueName))
                .andExpect(MockMvcResultMatchers.status().isOk())

                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                  .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                  .andExpect(MockMvcResultMatchers.jsonPath("$[0].queueName").value("testQueue"))
                  .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                  .andExpect(MockMvcResultMatchers.jsonPath("$[1].queueName").value("testQueue"));
    }
         @Test
         @WithMockUser(roles = "ADMIN")
        public void testAdminAccessCreateSlots() throws Exception {
        LocalDateTime startTime = LocalDateTime.parse("2025-04-09T09:30");
        LocalDateTime endTime = LocalDateTime.parse("2025-04-09T10:00");
        SlotDTO slotDTO = new SlotDTO(1L, "testQueue", startTime, endTime, false, false);

           mockMvc.perform(MockMvcRequestBuilders.post("/api/slots")
                         .contentType(MediaType.APPLICATION_JSON)
           .content(objectMapper.writeValueAsString(slotDTO)))
           .andExpect(MockMvcResultMatchers.status().isCreated());
         }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testBookSlot() throws Exception {
        String queueName = "testQueue";
        LocalDateTime startTime = LocalDateTime.parse("2025-04-09T09:30");
        LocalDateTime endTime = LocalDateTime.parse("2025-04-09T10:00");
        Long patientId=1L;


        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("prashant");
        patient.setGender("male");
        patient.setDateOfBirth("2025-04-09");

        //List<Slot> availableSlots = Arrays.asList(new Slot(1L, "testQueue", startTime, endTime, false), new Slot(2L, "testQueue", startTime1, endTime1, false));
        //SlotDTO slotDTO = new SlotDTO(1L, "testQueue", startTime, endTime, false);
        Slot slot = new Slot(1L, "testQueue", startTime, endTime, false, false, patient);

        Mockito.when(slotService.bookSlot(queueName, startTime, endTime,patientId)).thenReturn(slot);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/slots/book/1")
                        .param("queueName", "testQueue")
                        .param("startTime", String.valueOf(startTime))
                        .param("endTime", String.valueOf(endTime)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.queueName").value(queueName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").value("2025-04-09T09:30:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endTime").value("2025-04-09T10:00:00"));

    }

}
