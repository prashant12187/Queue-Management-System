package src.main.java.com.hospital_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import src.main.java.com.hospital_management_system.entity.Slot;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByQueueNameAndIsBookedFalse(String queueName);
}
