package com.example.notificationservice;

import com.example.notificationservice.DTOs.NotificationEnvelope;
import com.example.notificationservice.event_handlers.NotificationEventHandler;
import com.example.notificationservice.event_handlers.NotificationPreferenceEventHandler;
import com.example.notificationservice.event_handlers.SubscriptionEventHandler;
import com.example.notificationservice.event_handlers.UserEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventRouter {
    private final UserEventHandler userEventHandler;
    private final SubscriptionEventHandler subscriptionEventHandler;
    private final NotificationPreferenceEventHandler notificationPreferenceEventHandler;

    public void route(NotificationEnvelope envelop) {

        switch(envelop.eventType()){
            case "USER_CREATED" -> userEventHandler.handleUserCreation(envelop);
            case "USER_UPDATED" -> userEventHandler.handleUserUpdate(envelop);
            case "NOTIFICATION_EVENT" -> {}
            case "SUBSCRIPTION_CHANGED" -> subscriptionEventHandler.handle(envelop);
            case "NOTIFICATION_PREFERENCE_UPDATED" -> notificationPreferenceEventHandler.handle(envelop);
        }
    }
}
