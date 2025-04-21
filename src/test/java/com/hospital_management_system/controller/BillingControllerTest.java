package com.hospital_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital_management_system.entity.Billing;
import com.hospital_management_system.entity.MedicalHistory;
import com.hospital_management_system.entity.Patient;
import com.hospital_management_system.service.BillingService;
import com.hospital_management_system.service.MedicalHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class BillingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BillingService billingService;

    @Test
    public void testGetBillingById() throws Exception {
        Patient patient = new Patient();
        Billing billing = new Billing(1L, patient, "tata insurance",  "abc123", 45000.00 );
        Mockito.when(billingService.getBillingById(1L)).thenReturn(billing);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/billing/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceProvider").value("tata insurance"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.policyNumber").value("abc123"));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteBillingById() throws Exception {

        Mockito.when(billingService.deleteBillingById(7L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/billing/7"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteBillingById_NotFound() throws Exception {

        Mockito.when(billingService.deleteBillingById(7L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/billing/7"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
