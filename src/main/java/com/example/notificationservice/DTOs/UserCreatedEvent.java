package com.example.notificationservice.DTOs;

import com.example.notificationservice.enums.NotificationChannels;
import com.example.notificationservice.enums.NotificationType;

import java.util.List;
import java.util.Set;

public record UserCreatedEvent (
        String username,
        String firstName,
        String lastName,
        String email,
        Set<NotificationType> enabledTypes,
        List<NotificationChannels> channels

)
{}
