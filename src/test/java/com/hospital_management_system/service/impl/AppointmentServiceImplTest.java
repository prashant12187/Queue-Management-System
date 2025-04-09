package com.hospital_management_system.service.impl;

import com.hospital_management_system.entity.Appointment;
import com.hospital_management_system.entity.MedicalHistory;
import com.hospital_management_system.entity.Patient;
import com.hospital_management_system.payload.AppointmentDTO;
import com.hospital_management_system.payload.AppointmentResponse;
import com.hospital_management_system.payload.MedicalHistoryDTO;
import com.hospital_management_system.payload.PatientDTO;
import com.hospital_management_system.repository.AppointmentRepository;
import com.hospital_management_system.repository.MedicalHistoryRepository;
import com.hospital_management_system.repository.PatientRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
public class AppointmentServiceImplTest {
    @InjectMocks
    private AppointmentServiceImpl  appointmentService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private MedicalHistoryRepository medicalHistoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private ConcurrentLinkedQueue<Appointment> appointmentQueue = new ConcurrentLinkedQueue<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Option 2: Manually initialize mocks
    }


    @Test
    public void testCreateAppointment() {

        Patient patient = new Patient(1L, "prashant", "1991-01-30","male" );
        AppointmentDTO appointmentDTO = new AppointmentDTO();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String  currentDateAndTime= dtf.format(now);

        appointmentDTO.setAppointmentDate(currentDateAndTime);
        appointmentDTO.setReasonForVisit("body pain");
        appointmentDTO.setPatientId(1L);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setAppointmentDate(currentDateAndTime);
        appointment.setReasonForVisit("body pain");
        appointment.setPatient(patient);
        appointment.setProcessed(false);

        Mockito.when(modelMapper.map(appointmentDTO, Appointment.class)).thenReturn(appointment);
        Mockito.when(appointmentRepository.save(appointment)).thenReturn(appointment);
        Mockito.when(patientRepository.findById(appointmentDTO.getPatientId())).thenReturn(Optional.of(patient));

        Appointment result = appointmentService.createAppointment(appointmentDTO);
        Assertions.assertEquals(result.getId(), 1L);
    }

    @Test
    public void testGetAppointmentById() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String  currentDateAndTime= dtf.format(now);

        Patient patient = new Patient(1L, "prashant", "1991-01-30","male" );

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setAppointmentDate(currentDateAndTime);
        appointment.setReasonForVisit("body pain");
        appointment.setPatient(patient);
        appointment.setProcessed(false);


        Mockito.when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Appointment result = appointmentService.getAppointmentById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetAllAppointments() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String  currentDateAndTime= dtf.format(now);

        Patient patient = new Patient(2L, "atul", "1993-01-30","male" );
        Patient patient1 = new Patient(1L, "at", "1993-01-30","male" );

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setAppointmentDate(currentDateAndTime);
        appointment.setReasonForVisit("body pain");
        appointment.setPatient(patient);
        appointment.setProcessed(false);

        Appointment appointment1 = new Appointment();
        appointment1.setId(2L);
        appointment.setAppointmentDate(currentDateAndTime);
        appointment.setReasonForVisit("body pain");
        appointment.setPatient(patient1);
        appointment.setProcessed(false);

        List<Appointment> appointmentList = new ArrayList<>();

           String sortDir="ASC";
           String sortBy="id";
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(0, 10 , sort);

        Page<Appointment> appointments = new Page<Appointment>() {
            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super Appointment, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<Appointment> getContent() {
                return appointmentList;
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<Appointment> iterator() {
                return null;
            }
        };

        Mockito.when(appointmentRepository.findAll(pageable)).thenReturn(appointments);

        AppointmentResponse result = appointmentService.getAllAppointments(0, 10, "id", "ASC");
        assertEquals(0, result.getPageNo());
    }

    @Test
    public void testAddAppointment() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String  currentDateAndTime= dtf.format(now);

        Patient patient = new Patient(1L, "prashant", "1991-01-30","male" );

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setAppointmentDate(currentDateAndTime);
        appointment.setReasonForVisit("body pain");
        appointment.setPatient(patient);
        appointment.setProcessed(false);

        appointmentService.addAppointment(appointment);
    }

    @Test
    public void testDeleteAppointment() {
        Mockito.when(appointmentRepository.existsById(1L)).thenReturn(true);

        boolean result = appointmentService.deleteAppointment(1L);
        Mockito.verify(appointmentRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAppointmentNegativeScenario() {
        Mockito.when(appointmentRepository.existsById(1L)).thenReturn(false);
        boolean result = appointmentService.deleteAppointment(1L);

        assertFalse(result, "Expected checkMedicalHistoryExists to return false");
    }

}
