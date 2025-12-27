package com.example.notificationservice.DTOs;

import com.example.notificationservice.enums.TargetType;

public record SubscriptionDto (
    String username,
    TargetType targetType,
    String targetId
){}
