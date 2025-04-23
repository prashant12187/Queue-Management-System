package com.hospital_management_system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
public class PatientDTO {
    private Long id;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String reasonForVisit;
    private String email;
    private MedicalHistoryDTO medicalHistory;
    // private List<AppointmentDTO> appointments;
    //private BillingDTO billing; }
}


