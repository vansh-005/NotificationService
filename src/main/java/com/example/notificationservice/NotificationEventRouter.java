package com.example.notificationservice;

import com.example.notificationservice.DTOs.NotificationEnvelope;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventRouter {
    public void route(NotificationEnvelope envelop) {
        switch(envelop.eventType()){
            case "USER_CREATED" -> {}
            case "USER_UPDATED" -> {}
            case "NOTIFICATION_EVENT" -> {}
            case "SUBSCRIPTION_CHANGED" -> {}
            case "NOTIFICATION_PREFERENCE_UPDATED" -> {}
        }
    }
}
