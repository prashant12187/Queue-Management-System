package com.hospital_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hospital_management_system.entity.Slot;

import java.util.List;
@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByQueueNameAndIsBookedFalse(String queueName);
}
