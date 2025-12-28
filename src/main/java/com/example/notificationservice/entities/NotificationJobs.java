package com.example.notificationservice.entities;

import com.example.notificationservice.DTOs.NotificationEvent;
import com.example.notificationservice.enums.NotificationChannels;
import com.example.notificationservice.enums.NotificationType;
import com.example.notificationservice.enums.Status;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class NotificationJobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannels notificationChannel;

    private NotificationType notificationType;

//    @Type(JsonType.class)   // hibernate-types
//    @Column(columnDefinition = "jsonb")
//    private Map<String, Object> payload;

    @Column(nullable = false, columnDefinition = "jsonb")
    @Convert(converter = NotificationEventConverter.class)
    private NotificationEvent payload;


    @Enumerated(EnumType.STRING)
    private Status status;

    private int retryCount;

    private LocalDateTime nextAttemptAt;

    private LocalDateTime lockedAt;
    private String lockedBy;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = Status.PENDING;
        retryCount = 0;

    }
    @PostUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
