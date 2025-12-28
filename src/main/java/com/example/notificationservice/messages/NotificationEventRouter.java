package com.example.notificationservice.messages;

import com.example.notificationservice.DTOs.NotificationEnvelope;
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

    public void route(NotificationEnvelope envelope) {

        switch(envelope.eventType()){
            case "USER_CREATED" -> userEventHandler.handleUserCreation(envelope);
            case "USER_UPDATED" -> userEventHandler.handleUserUpdate(envelope);
            case "NOTIFICATION_EVENT" -> notificationPreferenceEventHandler.handle(envelope);
            case "SUBSCRIPTION_CHANGED" -> subscriptionEventHandler.handle(envelope);
            case "NOTIFICATION_PREFERENCE_UPDATED" -> notificationPreferenceEventHandler.handle(envelope);
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported notification event type: " + envelope.eventType()
                    );
        }
    }
}
