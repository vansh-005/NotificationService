package com.example.notificationservice.DTOs;

import java.time.Instant;

public record UserUpdateEvent(
        String eventId,
        String username,
        String email,
        String firstName,
        String lastName,
        Instant occuredAt
){}
