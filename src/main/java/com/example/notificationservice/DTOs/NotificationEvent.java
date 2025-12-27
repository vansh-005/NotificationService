package com.example.notificationservice.DTOs;

import com.example.notificationservice.enums.NotificationType;
import com.example.notificationservice.enums.TargetType;

import java.time.Instant;
import java.util.Map;

public record NotificationEvent (
        String eventId, // For idempotency
        NotificationType notificationType,
        TargetType targetType,
        String targetId,
        Map<String,Object> metaData, // move no., eval, score
        Instant occurredAt
){}

// Target -> Player, Tournament, Round, Game


