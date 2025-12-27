package com.example.notificationservice.DTOs;

import com.example.notificationservice.enums.NotificationChannels;
import com.example.notificationservice.enums.NotificationType;

import java.time.Instant;
import java.util.Set;

public record UpdateNotificationPreference(
        String username,
        Set<NotificationType> notificationTypes,
        Set<NotificationChannels> notificationChannels,
        Instant updatedAt
){}
