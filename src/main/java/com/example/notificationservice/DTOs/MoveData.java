package com.example.notificationservice.DTOs;

import java.time.Instant;

public record MoveData(
        Integer moveNumber,
        String movePlayedBy,
        String playerColor,
        Double evalBefore,
        Double evalAfter,
        Integer timeRemaining
){}
