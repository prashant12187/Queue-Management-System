package src.main.java.com.hospital_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.main.java.com.hospital_management_system.entity.Slot;
import src.main.java.com.hospital_management_system.service.SlotService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class SlotController {
    @Autowired
    private SlotService slotService;

    @PostMapping("/book")
    public ResponseEntity<Slot> bookSlot(@RequestParam String queueName, @RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime) {
        Slot bookedSlot = slotService.bookSlot(queueName, startTime, endTime);
        return ResponseEntity.ok(bookedSlot);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Slot>> getAvailableSlots(@RequestParam String queueName) {
        List<Slot> availableSlots = slotService.getAvailableSlots(queueName);
        return ResponseEntity.ok(availableSlots);
    }
}
