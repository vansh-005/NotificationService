package com.example.notificationservice.repositories;

import com.example.notificationservice.entities.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, Long> {
    public boolean existsByEventId(String eventId);
}