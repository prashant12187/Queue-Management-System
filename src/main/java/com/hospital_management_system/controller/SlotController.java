package com.hospital_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.hospital_management_system.entity.Slot;
import com.hospital_management_system.service.SlotService;
import com.hospital_management_system.payload.SlotDTO;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class SlotController {
    @Autowired
    private SlotService slotService;

    @PostMapping("/book")
    public ResponseEntity<Slot> bookSlot(@RequestParam String queueName, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endTime) {
        Slot bookedSlot = slotService.bookSlot(queueName, startTime, endTime);
        return ResponseEntity.ok(bookedSlot);
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
}
