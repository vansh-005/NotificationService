package com.example.notificationservice.DTOs;

public record JobExecutionResult (
    Long jobId,
    boolean success,
    String failureReason
) {}
