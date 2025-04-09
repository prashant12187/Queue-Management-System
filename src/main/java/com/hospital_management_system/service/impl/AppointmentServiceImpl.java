package com.hospital_management_system.service.impl;


import com.hospital_management_system.entity.Appointment;
import com.hospital_management_system.exception.ResourceNotFoundException;
import com.hospital_management_system.payload.AppointmentDTO;
import com.hospital_management_system.payload.AppointmentResponse;
import com.hospital_management_system.repository.AppointmentRepository;
import com.hospital_management_system.repository.PatientRepository;
import com.hospital_management_system.service.AppointmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ModelMapper modelMapper;

    private ConcurrentLinkedQueue<Appointment> appointmentQueue = new ConcurrentLinkedQueue<>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public AppointmentServiceImpl() {
        scheduler.scheduleAtFixedRate(this::processAppointments, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public Appointment createAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = modelMapper.map(appointmentDTO, Appointment.class);
        appointment.setPatient(patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", appointmentDTO.getPatientId())));
        return appointmentRepository.save(appointment);
    }


    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }


    //pagination and sorting in rest API
    @Override
    public AppointmentResponse getAllAppointments(int pageNo, int pageSize, String sortBy, String sortDir) {


        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);


        Page<Appointment> appointments = appointmentRepository.findAll(pageable);


        // get Appointment for page object
        List<Appointment> listOfAppointments = appointments.getContent();
        List<AppointmentDTO> reasonForVisit = listOfAppointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class))
                .collect(Collectors.toList());


        AppointmentResponse appointmentResponse = new AppointmentResponse();
        appointmentResponse.setReasonForVisit(reasonForVisit);
        appointmentResponse.setPageNo(appointments.getNumber());
        appointmentResponse.setPageSize(appointments.getSize());
        appointmentResponse.setTotalElements(appointments.getTotalElements());
        appointmentResponse.setTotalPages(appointments.getTotalPages());
        appointmentResponse.setLast(appointments.isLast());


        return appointmentResponse;
    }
    public void addAppointment(Appointment appointment) {
        appointmentQueue.offer(appointment);
        System.out.println("Added: " + appointment);
        //processAppointments();
    }


    @Scheduled(fixedDelay = 5000)
    private void processAppointments() {
        while (!appointmentQueue.isEmpty()) {
            Appointment appointment = appointmentQueue.poll();
            if (appointment != null) {
                processAppointment(appointment);
            }
        }
    }
    // Process single appointment
    private void processAppointment(Appointment appointment) {
        System.out.println("Processing appointment: " + appointment.getPatient());

        // Mark as processed and save to DB
        appointment.setProcessed(true);
        appointmentRepository.save(appointment);
    }
    private void completeAppointment(Appointment appointment) {
        System.out.println("Completed: " + appointment);
    }


    @Override
    public boolean deleteAppointment(Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}