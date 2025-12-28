package com.example.notificationservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ProcessedEvent {
    @Id
    @Column(nullable = false, unique = true)
    private String eventId;

    public ProcessedEvent(String eventId) {
        this.eventId = eventId;
    }
}
