package com.hospital_management_system.service;

import com.hospital_management_system.entity.Queue;

import java.util.List;

public interface QueueService {

    Queue createQueue(Queue queue);
    Queue getQueueById(Long id);
    List<Queue> getAllQueues();
    Queue updateQueue(Long id, Queue queueDetails);
    void deleteQueue(Long id);
}
