package com.example.notificationservice.DTOs;

import com.example.notificationservice.enums.NotificationChannels;
import com.example.notificationservice.enums.NotificationType;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public record UpdateNotificationPreferenceEvent(
        String username,
        Set<NotificationType> notificationTypes,
        List<NotificationChannels> notificationChannels,
        Instant updatedAt
){}
