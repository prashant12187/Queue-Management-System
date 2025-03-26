package com.hospital_management_system.controller;

import com.hospital_management_system.entity.Queue;
import com.hospital_management_system.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/queues")
public class QueueController {

    @Autowired
    private QueueService queueService;

    @PostMapping
    public ResponseEntity<Queue> createQueue(@RequestBody Queue queue) {
        Queue createdQueue = queueService.createQueue(queue);
        return new ResponseEntity<>(createdQueue, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Queue> getQueue(@PathVariable Long id) {
        Queue queue = queueService.getQueueById(id);
        return new ResponseEntity<>(queue, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Queue>> getAllQueues() {
        List<Queue> queues = queueService.getAllQueues();
        return new ResponseEntity<>(queues, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Queue> updateQueue(@PathVariable Long id, @RequestBody Queue queueDetails) {
        Queue updatedQueue = queueService.updateQueue(id, queueDetails);
        return new ResponseEntity<>(updatedQueue, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQueue(@PathVariable Long id) {
        queueService.deleteQueue(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}