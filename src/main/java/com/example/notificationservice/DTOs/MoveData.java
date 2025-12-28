package com.example.notificationservice.DTOs;

public record MoveData(
        Integer moveNumber,
        String movePlayedBy,
        String playerColor,
        Integer evalBefore,
        Integer evalAfter,
        Integer timeRemaining
){}
