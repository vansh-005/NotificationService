package com.example.notificationservice.DTOs;

import com.example.notificationservice.entities.SubscriptionAction;
import com.example.notificationservice.enums.TargetType;

import java.time.Instant;

public record SubscriptionChangedEvent(
        String eventId,
        String username,
        TargetType targetType,
        String targetId,
        SubscriptionAction action,
        Instant occuredAt
){}
