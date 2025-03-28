package src.main.java.com.hospital_management_system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import src.main.java.com.hospital_management_system.entity.Slot;
import src.main.java.com.hospital_management_system.repository.SlotRepository;
import src.main.java.com.hospital_management_system.service.SlotService;

import java.time.LocalDateTime;
import java.util.List;

public class SlotServiceImpl implements SlotService {
    @Autowired
    private SlotRepository slotRepository;

    public Slot bookSlot(String queueName, LocalDateTime startTime, LocalDateTime endTime) {
        List<Slot> availableSlots = slotRepository.findByQueueNameAndIsBookedFalse(queueName);
        for (Slot slot : availableSlots) {
            if (slot.getStartTime().equals(startTime) && slot.getEndTime().equals(endTime)) {
                slot.setBooked(true);
                return slotRepository.save(slot);
            }
        }
        throw new RuntimeException("No available slots for the specified time.");
    }

    public List<Slot> getAvailableSlots(String queueName) {
        return slotRepository.findByQueueNameAndIsBookedFalse(queueName);
    }

}
