package com.example.notificationservice.DTOs;

import com.example.notificationservice.enums.NotificationType;
import com.example.notificationservice.enums.TargetType;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record NotificationEvent (
        String eventId, // For idempotency
        NotificationType notificationType,
        List<TargetRef> targets,
        Map<String,Object> metaData, // move no., eval, score
        Instant occurredAt
){}

// TargetType -> Player, Tournament, Round, Game


