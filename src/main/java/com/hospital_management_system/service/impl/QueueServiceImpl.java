package com.hospital_management_system.service.impl;

import com.hospital_management_system.entity.Queue;
import com.hospital_management_system.exception.ResourceNotFoundException;
import com.hospital_management_system.repository.QueueRepository;
import com.hospital_management_system.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueueServiceImpl implements QueueService {
    @Autowired
    private QueueRepository queueRepository;
    @Override
    public Queue createQueue(Queue queue) {
        return queueRepository.save(queue);
    }

    @Override
    public Queue getQueueById(Long id) {
        return queueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Queue not found with id " + id));
    }

    @Override
    public List<Queue> getAllQueues() {
        return queueRepository.findAll();
    }


    @Override
    public Queue updateQueue(Long id, Queue queueDetails) {
        Queue queue = getQueueById(id);
        queue.setName(queueDetails.getName());
        queue.setDescription(queueDetails.getDescription());
        queue.setSlot_details(queueDetails.getSlot_details());
        // Update other fields as necessary
        return queueRepository.save(queue);
    }

    @Override
    public void deleteQueue(Long id) {
        Queue queue = getQueueById(id);
        queueRepository.delete(queue);
    }
}
