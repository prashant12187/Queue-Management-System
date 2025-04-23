package com.hospital_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital_management_system.entity.Appointment;
import com.hospital_management_system.entity.Billing;
import com.hospital_management_system.entity.Patient;
import com.hospital_management_system.entity.Slot;
import com.hospital_management_system.payload.AppointmentDTO;
import com.hospital_management_system.payload.AppointmentResponse;
import com.hospital_management_system.service.AppointmentService;
import com.hospital_management_system.service.BillingService;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AppointmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentService appointmentService;

    @Test
    public void testGetAppointmentById() throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String  currentDateAndTime= dtf.format(now);

        Patient patient = new Patient(1L, "prashant", "1991-01-30","male", "prashant12@gmail.com");

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setAppointmentDate(currentDateAndTime);
        appointment.setReasonForVisit("body pain");
        appointment.setPatient(patient);
        appointment.setProcessed(false);
        Mockito.when(appointmentService.getAppointmentById(1L)).thenReturn(appointment);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appointmentDate").value(currentDateAndTime))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reasonForVisit").value("body pain"));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddAppointment() throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String  currentDateAndTime= dtf.format(now);

        Patient patient = new Patient(1L, "prashant", "1991-01-30","male", "prashant12@gmail.com" );

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setAppointmentDate(currentDateAndTime);
        appointment.setReasonForVisit("body pain");
        appointment.setPatient(patient);
        appointment.setProcessed(false);

       // Mockito.when(appointmentService.addAppointment(appointment).thenReturn(slot);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((objectMapper.writeValueAsString(appointment))))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void testGetAllAppointments() throws Exception {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String  currentDateAndTime= dtf.format(now);

        Patient patient = new Patient(2L, "atul", "1993-01-30","male", "prashant12@gmail.com" );
        Patient patient1 = new Patient(1L, "at", "1993-01-30","male", "atul12@gmail.com" );

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setPatientId(1L);
        appointmentDTO.setReasonForVisit("headeche");
        appointmentDTO.setAppointmentDate("2025-04-05T09:30");

        AppointmentDTO appointmentDTO1 = new AppointmentDTO();
        appointmentDTO1.setPatientId(2L);
        appointmentDTO1.setReasonForVisit("body pain");
        appointmentDTO1.setAppointmentDate("2025-04-06T09:30");

        List<AppointmentDTO> appointmentList = new ArrayList<>();
          appointmentList.add(appointmentDTO);
          appointmentList.add(appointmentDTO1);

        AppointmentResponse appointmentResponse = new AppointmentResponse();
           appointmentResponse.setReasonForVisit(appointmentList);
           appointmentResponse.setPageNo(0);
           appointmentResponse.setLast(false);
           appointmentResponse.setTotalElements(2L);
           appointmentResponse.setPageSize(10);
           appointmentResponse.setPageNo(0);

        Mockito.when(appointmentService.getAllAppointments(0, 10, "id", "ASC")).thenReturn(appointmentResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageNo").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageSize").value(10));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteAppointment() throws Exception {

        Mockito.when(appointmentService.deleteAppointment(7L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/appointment/7"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteBillingById_NotFound() throws Exception {

        Mockito.when(appointmentService.deleteAppointment(7L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/appointment/7"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
