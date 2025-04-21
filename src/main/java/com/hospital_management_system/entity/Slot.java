package com.hospital_management_system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "slot")
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String queueName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isBooked;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    // Getters and setters
}
