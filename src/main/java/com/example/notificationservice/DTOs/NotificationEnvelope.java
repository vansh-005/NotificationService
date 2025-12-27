package com.example.notificationservice.DTOs;

import java.time.Instant;

public record NotificationEnvelope(
        String eventId,
        String eventType,
        Object payload,
        Instant occurredAt
) {
}
