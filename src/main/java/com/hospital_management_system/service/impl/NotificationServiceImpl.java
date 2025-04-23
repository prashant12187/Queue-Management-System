package com.hospital_management_system.service.impl;

import com.hospital_management_system.entity.Slot;
import com.hospital_management_system.repository.SlotRepository;
import com.hospital_management_system.service.EmailService;
import com.hospital_management_system.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationServiceImpl implements NotificationService {


    @Autowired
    private EmailService emailService;
    @Autowired
    private SlotRepository slotRepository;

    @Override

    @Scheduled(fixedRate = 60000) // Check every minute
    public void sendNotification() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime notificationTime = now.plusMinutes(30);

        List<Slot> slots = slotRepository.findByStartTime(notificationTime);
        for (Slot slot : slots) {
            if (!slot.isNotified()) {
                emailService.sendEmail(slot.getPatient().getEmail(), "Appointment Reminder", "Your turn is approaching, please be ready.");
                slot.setNotified(true);
                slotRepository.save(slot);
            }
        }

     }
}
