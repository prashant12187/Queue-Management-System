package com.hospital_management_system.controller;

import com.hospital_management_system.entity.Patient;
import com.hospital_management_system.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.hospital_management_system.entity.Slot;
import com.hospital_management_system.service.SlotService;
import com.hospital_management_system.payload.SlotDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/slots")
public class SlotController {
    @Autowired
    private SlotService slotService;


    @Autowired
        private EmailService emailService;


    @PreAuthorize("hasRole('USER')") // Only user can book slots
    @PostMapping("/book/{patientId}")
    public ResponseEntity<Slot> bookSlot(@PathVariable Long patientId,@RequestParam String queueName, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endTime) {
        Slot bookedSlot = slotService.bookSlot(queueName, startTime, endTime,patientId);

        emailService.sendEmail(bookedSlot.getPatient().getEmail(), "Slot Booking Confirmation", "Your slot has been booked successfully.");

        return ResponseEntity.ok(bookedSlot);
    }

    @PostMapping("/allSlotsBooked")
    public ResponseEntity<List<Slot>> bookAllSlot() {
        List<Slot> slotList = slotService.bookAllSlots();
        return ResponseEntity.ok(slotList);
    }

    @PreAuthorize("hasRole('ADMIN')") // Only admin can create
    @PostMapping
    public ResponseEntity<Slot> createSlots(@RequestBody SlotDTO slotDto) {
        Slot slot = slotService.createSlots(slotDto);
        return new ResponseEntity<>(slot, HttpStatus.CREATED);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Slot>> getAvailableSlots(@RequestParam String queueName) {
        List<Slot> availableSlots = slotService.getAvailableSlots(queueName);
        return ResponseEntity.ok(availableSlots);
    }


    @GetMapping("/queue")
    public ResponseEntity<List<Slot>> getPatientsInTheQueueDetail(@RequestParam String queueName) {
        List<Slot> bookedSlots = slotService.getPatientsInTheQueue(queueName);
        List<Slot> bookedSlotsTodaysList = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (Slot slot : bookedSlots) {
            if(slot.getStartTime().toLocalDate().equals(today) ){
                bookedSlotsTodaysList.add(slot);
            }
        }

        return ResponseEntity.ok(bookedSlotsTodaysList);
    }


    @GetMapping("/queue/{patientId}")
    public ResponseEntity<Integer> getQueuePosition(@PathVariable Long patientId, @RequestParam String queueName) {
       List<Slot> bookedSlots = slotService.getPatientsInTheQueue(queueName);
        LocalDate today = LocalDate.now();
            int position = 1;
        for (Slot slot : bookedSlots) {
            if(slot.getStartTime().toLocalDate().equals(today) && slot.getPatient()!=null){
                if(slot.getPatient().getId()==patientId){
                    return ResponseEntity.ok(position);
                }
                position++;
            }
        }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(-1); // Patient not found in the queue
    }


}
