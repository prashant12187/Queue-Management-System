package com.hospital_management_system.service;

import com.hospital_management_system.entity.Slot;
import com.hospital_management_system.payload.SlotDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface SlotService {
    Slot bookSlot(String queueName, LocalDateTime startTime, LocalDateTime endTime);
    List<Slot> getAvailableSlots(String queueName);
    Slot createSlots(SlotDTO slotDto);
}
