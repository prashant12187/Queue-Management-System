package com.hospital_management_system.service.impl;

import com.hospital_management_system.entity.Patient;
import com.hospital_management_system.exception.ResourceNotFoundException;
import com.hospital_management_system.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hospital_management_system.entity.Slot;
import com.hospital_management_system.repository.SlotRepository;
import com.hospital_management_system.service.SlotService;
import com.hospital_management_system.payload.SlotDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Service
public class SlotServiceImpl implements SlotService {
    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Slot bookSlot(String queueName, LocalDateTime startTime, LocalDateTime endTime, Long patientId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String startTime1 = startTime.format(formatter);
        String endTime1 = endTime.format(formatter);

        List<Slot> availableSlots = slotRepository.findByQueueNameAndIsBookedFalse(queueName);
        Patient patient = patientRepository.findById(patientId).get();
        for (Slot slot : availableSlots) {

            String formattedStartTime = slot.getStartTime().format(formatter);
            String formattedEndTime = slot.getEndTime().format(formatter);
            if (formattedStartTime.equals(startTime1) && formattedEndTime.equals(endTime1)) {
                slot.setBooked(true);
                slot.setPatient(patient);
                return slotRepository.save(slot);
            }
        }
        throw new RuntimeException("No available slots for the specified time.");
    }

    public List<Slot> getAvailableSlots(String queueName) {
        return slotRepository.findByQueueNameAndIsBookedFalse(queueName);
    }

    @Override
    public Slot createSlots(SlotDTO slotDto) {
        Slot slot = modelMapper.map(slotDto, Slot.class);
        slot = slotRepository.save(slot);
        return slot;
    }

    @Override
    public List<Slot> bookAllSlots() {
        //List<Slot> slots = slotRepository.findAll();
        List<Slot> slotList = slotRepository.findByIsBookedFalse();
        if(slotList.isEmpty()){
            throw new ResourceNotFoundException("No slots are available");
        }
        else{
            slotList.forEach(slot -> slot.setBooked(true));
            slotRepository.saveAll(slotList);
        }
        return slotList;

    }

    @Override
    public List<Slot> getPatientsInTheQueue(String queueName) {

        List<Slot> bookedSlots = slotRepository.findByQueueNameAndIsBookedTrueOrderByStartTime(queueName);
        return bookedSlots;

    }

}
