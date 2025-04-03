package com.hospital_management_system.payload;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SlotDTO {
    private Long id;
    private String queueName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isBooked;
}
