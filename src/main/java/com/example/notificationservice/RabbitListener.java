package com.example.notificationservice;


import com.example.notificationservice.DTOs.NotificationEnvelope;
import lombok.RequiredArgsConstructor;

// @RabbitListener
@RequiredArgsConstructor
public class RabbitListener {
    private final NotificationEventRouter router;
    public void onMessage(NotificationEnvelope envelope){
        router.route(envelope);
    }
}
