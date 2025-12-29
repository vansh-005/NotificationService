package com.example.notificationservice.messages;

import com.example.notificationservice.DTOs.*;
import com.example.notificationservice.entities.ProcessedEvent;
import com.example.notificationservice.event_handlers.NotificationEventHandler;
import com.example.notificationservice.event_handlers.NotificationPreferenceEventHandler;
import com.example.notificationservice.event_handlers.SubscriptionEventHandler;
import com.example.notificationservice.event_handlers.UserEventHandler;
import com.example.notificationservice.repositories.ProcessedEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j

public class NotificationEventRouter {
    private final UserEventHandler userEventHandler;
    private final SubscriptionEventHandler subscriptionEventHandler;
    private final NotificationPreferenceEventHandler notificationPreferenceEventHandler;
    private final NotificationEventHandler notificationEventHandler;

    private final ObjectMapper objectMapper;
    private final ProcessedEventRepository processedEventRepository;

    @Transactional
    public void route(NotificationEnvelope envelope) {
        try {
            processedEventRepository.save(new ProcessedEvent(envelope.eventId()));
        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate event {} ignored", envelope.eventId());
            return;
        }

        switch(envelope.eventType()){
            case "USER_CREATED" -> {
                UserCreatedEvent event = objectMapper.convertValue(envelope.payload(), UserCreatedEvent.class);
                userEventHandler.handleUserCreation(event);

            }
            case "USER_UPDATED" -> {
                UserUpdateEvent event = objectMapper.convertValue(envelope.payload(), UserUpdateEvent.class);
                userEventHandler.handleUserUpdate(event);
            }
            case "NOTIFICATION_EVENT" -> {
                UpdateNotificationPreference event = objectMapper.convertValue(envelope.payload(), UpdateNotificationPreference.class);
                notificationPreferenceEventHandler.handle(event);
            }
            case "SUBSCRIPTION_CHANGED" -> {
                SubscriptionChangedEvent event = objectMapper.convertValue(envelope.payload(), SubscriptionChangedEvent.class);
                subscriptionEventHandler.handle(event);
            }
            case "NOTIFICATION_PREFERENCE_UPDATED" -> {
                NotificationEvent event = objectMapper.convertValue(envelope.payload(), NotificationEvent.class);
                notificationEventHandler.handle(event);
            }
            default -> {
                log.error("Unsupported event type {}", envelope.eventType());
                return;
            }
        }

    }
}
