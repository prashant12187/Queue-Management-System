package com.hospital_management_system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotDTO {
    private Long id;
    private String queueName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isBooked;
    private boolean isNotified;
}
