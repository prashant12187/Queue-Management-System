package src.main.java.com.hospital_management_system.service;

import src.main.java.com.hospital_management_system.entity.Slot;

import java.time.LocalDateTime;
import java.util.List;

public interface SlotService {
    Slot bookSlot(String queueName, LocalDateTime startTime, LocalDateTime endTime);
    List<Slot> getAvailableSlots(String queueName);
}
