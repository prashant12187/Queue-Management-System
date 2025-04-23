package com.hospital_management_system.service.impl;

import com.hospital_management_system.entity.Appointment;
import com.hospital_management_system.entity.MedicalHistory;
import com.hospital_management_system.entity.Patient;
import com.hospital_management_system.entity.Slot;
import com.hospital_management_system.payload.MedicalHistoryDTO;
import com.hospital_management_system.payload.PatientDTO;
import com.hospital_management_system.payload.SlotDTO;
import com.hospital_management_system.repository.AppointmentRepository;
import com.hospital_management_system.repository.MedicalHistoryRepository;
import com.hospital_management_system.repository.PatientRepository;
import com.hospital_management_system.service.AppointmentService;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class PatientServiceImplTest {
    @InjectMocks
    private PatientServiceImpl  patientServiceImpl;

    @Mock
    private AppointmentServiceImpl appointmentService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private MedicalHistoryRepository medicalHistoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Option 2: Manually initialize mocks
    }

    @Test
    public void testGetAllPatients() {
        LocalDateTime startTime = LocalDateTime.parse("2025-04-05T09:30");
        LocalDateTime endTime = LocalDateTime.parse("2025-04-05T10:00");

        Patient patient = new Patient(1L, "prashant", "1991-01-30","male", "prashant12@gmail.com" );
        Patient patient1 = new Patient(2L, "rakesh", "1990-05-04","male", "rakesh42@gmail.com" );

        List<Patient> patientList = new ArrayList<Patient>();
        patientList.add(patient);
        patientList.add(patient1);


        Mockito.when(patientRepository.findAll()).thenReturn(patientList);

        List<Patient> result = patientServiceImpl.getAllPatients();
        assertEquals(2L, result.size());
    }

    @Test
    public void testCreatePatient() {

        LocalDateTime startTime = LocalDateTime.parse("2025-04-05T09:30");
        LocalDateTime endTime = LocalDateTime.parse("2025-04-05T10:00");

        Patient patient = new Patient(1L, "prashant", "1991-01-30","male", "prashant12@gmail.com" );
        MedicalHistory medicalHistory = new MedicalHistory(1L, patient,  "cough and cold", "chest pain", "abcxyz");
        MedicalHistoryDTO medicalHistoryDTO = new MedicalHistoryDTO(1L, "cough and cold", "chest pain", "abcxyz");
        PatientDTO patientDTO = new PatientDTO(1L, "prashant", "1991-01-30","male" , "back pain", "prashant12@gmail.com", medicalHistoryDTO);



        Mockito.when(patientRepository.save(patient)).thenReturn(patient);
        Mockito.when(modelMapper.map(patientDTO, Patient.class)).thenReturn(patient);


        Mockito.when(medicalHistoryRepository.save(medicalHistory)).thenReturn(medicalHistory);
        Mockito.when(modelMapper.map(medicalHistoryDTO, MedicalHistory.class)).thenReturn(medicalHistory);

        Appointment appointment = new Appointment();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String  currentDateAndTime= dtf.format(now);
        appointment.setAppointmentDate(currentDateAndTime);
        appointment.setReasonForVisit(patientDTO.getReasonForVisit());
        appointment.setPatient(patient);
        appointment.setProcessed(false);

        Mockito.when(appointmentRepository.save(appointment)).thenReturn(appointment);

        appointmentService.addAppointment(appointment);
        Patient result = patientServiceImpl.createPatient(patientDTO);
        Assertions.assertEquals(result.getName(), "prashant");
    }

}
