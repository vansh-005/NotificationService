package com.example.notificationservice.DTOs;
import com.example.notificationservice.enums.NotificationChannels;
import com.example.notificationservice.enums.NotificationType;

import java.util.Map;

public record CreateNotificationJobCommand(
        Long userId,
        NotificationType type,
        NotificationChannels channel,
        Map<String, Object> payload
) {}

