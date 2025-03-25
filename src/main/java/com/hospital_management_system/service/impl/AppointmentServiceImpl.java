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
import org.springframework.stereotype.Service;
import java.util.List;
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

    /*@Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, PatientRepository patientRepository, ModelMapper modelMapper) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.modelMapper = modelMapper;
    }*/


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
        appointmentQueue.add(appointment);
        System.out.println("Added: " + appointment);
    }

    private void processAppointments() {
        Appointment appointment = appointmentQueue.poll();
        if (appointment != null) {
            System.out.println("Processing: " + appointment);
            // Simulate appointment completion
            completeAppointment(appointment);
        }
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