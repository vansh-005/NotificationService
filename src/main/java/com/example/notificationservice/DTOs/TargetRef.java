    package com.example.notificationservice.DTOs;

    import com.example.notificationservice.enums.TargetType;

    public record TargetRef(
            TargetType targetType,
            String targetId
    ){}
