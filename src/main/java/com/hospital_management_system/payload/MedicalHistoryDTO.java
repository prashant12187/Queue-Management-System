package com.hospital_management_system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class MedicalHistoryDTO {
    private Long patientId;
    private String allergies;
    private String previousIllnesses;
    private String currentMedications;}
